package com.sdl.experienceoptimization.analytics.dummy;


import com.sdl.experienceoptimization.analytics.AnalyticsManagerBase;
import com.tridion.smarttarget.SmartTargetException;
import com.tridion.smarttarget.analytics.results.AnalyticsResults;
import com.tridion.smarttarget.analytics.results.AnalyticsResultsRow;
import com.tridion.smarttarget.analytics.statistics.StatisticsExperimentDimensions;
import com.tridion.smarttarget.analytics.statistics.StatisticsFilter;
import com.tridion.smarttarget.analytics.statistics.StatisticsFilters;
import com.tridion.smarttarget.analytics.statistics.StatisticsTimeDimensions;
import com.tridion.smarttarget.analytics.tracking.ExperimentDimensions;
import com.tridion.smarttarget.experiments.Experiment;
import com.tridion.smarttarget.experiments.ExperimentCalendar;
import com.tridion.smarttarget.experiments.Experiments;
import com.tridion.smarttarget.promotions.ActionItem;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Dummy Data Analytics Provider
 */
public class DummyDataProvider extends AnalyticsManagerBase
{
    private DummyDataConfiguration configuration;
    private static Map<String, CachedAnalyticsResults> cachedAnalyticsResults = new HashMap();

    public DummyDataProvider() throws SmartTargetException {
        this.configuration = getConfiguration();
    }

    public String getExperimentId() {
        return this.configuration.getExperimentId();
    }

    public DummyDataConfiguration getConfiguration()
            throws SmartTargetException
    {
        if (this.configuration == null) this.configuration = new DummyDataConfiguration();
        return this.configuration;
    }

    public AnalyticsResults getDummyResults(Date startDate, Date endDate, StatisticsExperimentDimensions experimentDimensions, StatisticsTimeDimensions timeDimensions, List<String> extraDimensions, StatisticsFilters filters, int startIndex, int maxResults)
            throws Exception {
        return this.getStatisticsResults(startDate, endDate, experimentDimensions, timeDimensions, extraDimensions, filters, startIndex, maxResults);
    }

    @Override
    public void trackView(ExperimentDimensions experimentDimensions, Map<String, String> stringStringMap)
    {
    }

    @Override
    public void trackConversion(ExperimentDimensions experimentDimensions, Map<String, String> stringStringMap)
    {
    }

    @Override
    protected AnalyticsResults getStatisticsResults(Date startDate, Date endDate, StatisticsExperimentDimensions experimentDimensions, StatisticsTimeDimensions timeDimensions, List<String> extraDimensions, StatisticsFilters filters, int startIndex, int maxResults)
            throws Exception
    {
        String experimentId = getFilterValue(filters, getCustomDimensions().getExperimentId());
        String publicationId = getFilterValue(filters, getCustomDimensions().getPublicationId());

        CachedAnalyticsResults cachedResultForExperiment = cachedAnalyticsResults.get(experimentId);

        if (cachedResultForExperiment == null)
        {
            cachedResultForExperiment = calculateAnalyticsResults(experimentId);
            cachedAnalyticsResults.put(experimentId, cachedResultForExperiment);
        }
        AnalyticsResults results;
        if (timeDimensions.isPerDay())
        {
            results = getAnalyticsPerDay(publicationId, cachedResultForExperiment);
        }
        else
        {
            results = getAnalyticsSummary(cachedResultForExperiment);
        }
        return results;
    }

    private AnalyticsResults getAnalyticsPerDay(String publicationId, CachedAnalyticsResults cachedResultForExperiment)
    {
        AnalyticsResults results = new AnalyticsResults();
        for (AnalyticsResultsRow row : cachedResultForExperiment.getPerDay().getRows())
        {
            if (row.getExperimentDimensions().getPublicationId().equalsIgnoreCase(publicationId))
            {
                AnalyticsResultsRow resultRow = new AnalyticsResultsRow();
                resultRow.getExperimentDimensions().setChosenVariant(row.getExperimentDimensions().getChosenVariant());
                resultRow.getTimeDimensions().setDay(row.getTimeDimensions().getDay());
                resultRow.setVariantViews(row.getVariantViews());
                resultRow.setVariantConversions(row.getVariantConversions());

                results.getRows().add(resultRow);
            }
        }
        return results;
    }

    private AnalyticsResults getAnalyticsSummary(CachedAnalyticsResults cachedResultForExperiment)
    {
        AnalyticsResults results = new AnalyticsResults();
        for (AnalyticsResultsRow row : cachedResultForExperiment.getSummary().getRows())
        {
            AnalyticsResultsRow resultRow = new AnalyticsResultsRow();
            resultRow.getExperimentDimensions().setPublicationId(row.getExperimentDimensions().getPublicationId());
            resultRow.getExperimentDimensions().setChosenVariant(row.getExperimentDimensions().getChosenVariant());
            resultRow.setVariantViews(row.getVariantViews());
            resultRow.setVariantConversions(row.getVariantConversions());

            results.getRows().add(resultRow);
        }
        return results;
    }

    private CachedAnalyticsResults calculateAnalyticsResults(String experimentId) throws SmartTargetException
    {
        CachedAnalyticsResults cachedAnalyticsResults = new CachedAnalyticsResults();

        Experiment experiment = Experiments.getExperiment(experimentId);
        if (experiment == null) throw new SmartTargetException("Unable to find Experiment");

        Date startDate = this.configuration.getStartDate();
        Date endDate = this.configuration.getEndDate();
        if (startDate == null) startDate = experiment.getStartDate();
        if (endDate == null) endDate = experiment.getEndDate();
        ExperimentCalendar startCalendar = new ExperimentCalendar();
        startCalendar.setTime(startDate);
        startCalendar = startCalendar.getCalendarForMidnight();
        ExperimentCalendar endCalendar = new ExperimentCalendar();
        endCalendar.setTime(endDate);
        endCalendar = endCalendar.getCalendarForMidnight();

        for (Iterator i$ = experiment.getPublicationScopeTrigger().getValues().iterator(); i$.hasNext(); )
        {
            String publicationId = (String)i$.next();
            int variantIndex = 0;
            for (ActionItem item : experiment.getAction().getItems())
            {
                addAnalyticsRowsForVariant(cachedAnalyticsResults, publicationId, variantIndex++, startCalendar, endCalendar);
            }
        }
        return cachedAnalyticsResults;
    }

    private void addAnalyticsRowsForVariant(CachedAnalyticsResults cachedAnalyticsResults, String publicationId, int variantIndex, ExperimentCalendar startCalendar, ExperimentCalendar endCalendar)
    {
        int minViews = this.configuration.getMinViews();
        int maxViews = this.configuration.getMaxViews();
        int minConversions = this.configuration.getMinConversions();
        int maxConversions = this.configuration.getMaxConversions();

        String chosenVariant = Integer.toString(variantIndex);

        DummyWinner winner = this.configuration.getWinner(publicationId, variantIndex);
        if (winner != null)
        {
            minConversions = winner.getMinConversion();
            maxConversions = winner.getMaxConversion();
        }

        int totalViews = 0;
        int totalConversions = 0;
        ExperimentCalendar currentDate = startCalendar.getCalendarForMidnight();
        while (currentDate.beforeOrEquals(endCalendar))
        {
            AnalyticsResultsRow rowPerDay = new AnalyticsResultsRow();
            int views = getRandomNumber(minViews, maxViews);
            int conversions = getRandomNumber(minConversions, maxConversions);
            totalViews += views;
            totalConversions += conversions;

            rowPerDay.setVariantViews(views);
            rowPerDay.setVariantConversions(conversions);
            rowPerDay.getExperimentDimensions().setPublicationId(publicationId);
            rowPerDay.getExperimentDimensions().setChosenVariant(chosenVariant);
            rowPerDay.getTimeDimensions().setDay(currentDate.getTime());

            cachedAnalyticsResults.getPerDay().getRows().add(rowPerDay);

            currentDate.add(6, 1);
        }

        AnalyticsResultsRow summaryRow = new AnalyticsResultsRow();
        summaryRow.getExperimentDimensions().setPublicationId(publicationId);
        summaryRow.getExperimentDimensions().setChosenVariant(chosenVariant);
        summaryRow.setVariantViews(totalViews);
        summaryRow.setVariantConversions(totalConversions);

        cachedAnalyticsResults.getSummary().getRows().add(summaryRow);
    }

    private int getRandomNumber(int minValue, int maxValue)
    {
        Random r = new Random();
        return r.nextInt(maxValue - minValue) + minValue;
    }

    private String getFilterValue(StatisticsFilters filters, String customDimensionName) throws SmartTargetException {
        for (StatisticsFilter filter : filters)
        {
            if (filter.getName().equalsIgnoreCase(customDimensionName)) return filter.getOperand();
        }
        return null;
    }
}