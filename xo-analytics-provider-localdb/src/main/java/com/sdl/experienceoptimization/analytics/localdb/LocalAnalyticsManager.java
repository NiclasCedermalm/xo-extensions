package com.sdl.experienceoptimization.analytics.localdb;

import com.sdl.experienceoptimization.analytics.*;
import com.sdl.experienceoptimization.analytics.dummy.DummyDataProvider;
import com.tridion.smarttarget.SmartTargetException;
import com.tridion.smarttarget.analytics.results.AnalyticsResults;
import com.tridion.smarttarget.analytics.statistics.StatisticsExperimentDimensions;
import com.tridion.smarttarget.analytics.statistics.StatisticsFilters;
import com.tridion.smarttarget.analytics.statistics.StatisticsTimeDimensions;
import com.tridion.smarttarget.analytics.tracking.ExperimentDimensions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Local Analytics Manager
 *
 * @author nic
 */
public class LocalAnalyticsManager extends AnalyticsManagerBase {

    static private Logger log = LoggerFactory.getLogger(LocalAnalyticsManager.class);

    // Static members to minimize the creation of new worker threads+repositories for each time
    //
    private static AnalyticsResultWorker resultWorker;
    private static AnalyticsResultRepository resultRepository;

    private static DummyDataProvider dummyDataProvider;

    public LocalAnalyticsManager() throws SmartTargetException {

        synchronized ( LocalAnalyticsManager.class ) {
            if (resultRepository == null) {
                resultRepository = new AnalyticsResultRepository(this.getConfiguration());
                resultWorker = new AnalyticsResultWorker(this, resultRepository, this.getConfiguration());
                String useDummyData = this.getConfiguration().getAnalyticsProperty("UseDummyData");
                if ( useDummyData != null && Boolean.parseBoolean(useDummyData) == true ) {
                    dummyDataProvider = new DummyDataProvider();
                }
            }
        }
    }

    /**
     * Submit view tracking data to worker that asynchronously write analytics data into the database
     * @param experimentDimensions
     * @param metadata
     */
    @Override
    public void trackView(ExperimentDimensions experimentDimensions, Map<String, String> metadata) {
        log.debug("Track view: " + experimentDimensions);
        // TODO: Stop tracking after a winner has been selected????
        this.resultWorker.submitTracking(new TrackedExperiment(experimentDimensions, ExperimentType.VIEW));
    }

    /**
     * Submit conversion tracking data to worker that asynchronously write analytics data into the database
     * @param experimentDimensions
     * @param metadata
     */
    @Override
    public void trackConversion(ExperimentDimensions experimentDimensions, Map<String, String> metadata) {
        log.debug("Track conversion: " + experimentDimensions);
        this.resultWorker.submitTracking(new TrackedExperiment(experimentDimensions, ExperimentType.CONVERSION));
    }

    /**
     * Get analytics results from the tracking database
     * @param startDate
     * @param endDate
     * @param experimentDimensions
     * @param timeDimensions
     * @param extraDimensions
     * @param statisticsFilters
     * @param startIndex
     * @param maxResults
     * @return
     * @throws Exception
     */
    @Override
    protected AnalyticsResults getStatisticsResults(Date startDate,
                                                    Date endDate,
                                                    StatisticsExperimentDimensions experimentDimensions,
                                                    StatisticsTimeDimensions timeDimensions,
                                                    List<String> extraDimensions,
                                                    StatisticsFilters statisticsFilters,
                                                    int startIndex,
                                                    int maxResults) throws Exception {

        log.debug("Getting analytics results...");
        log.debug("Statistics Filters: " + statisticsFilters);

        String experimentId = this.getExperimentId(statisticsFilters);
        if ( dummyDataProvider != null && experimentId != null && experimentId.equals(dummyDataProvider.getExperimentId()) ) {
            return dummyDataProvider.getDummyResults(startDate, endDate, experimentDimensions, timeDimensions, extraDimensions, statisticsFilters, startIndex, maxResults);
        }
        // TODO: Check winner here instead???

        List<AggregatedTracking> trackings = this.resultRepository.getTrackingResults(statisticsFilters);

        SimpleAnalyticsResults results = new SimpleAnalyticsResults();

        if ( trackings != null ) {
            for (AggregatedTracking tracking : trackings) {
                this.addAnalyticsResultsRow(results, tracking, experimentDimensions, timeDimensions);
            }
        }

        return results;
    }

}
