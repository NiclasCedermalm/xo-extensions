package com.sdl.experienceoptimization.analytics;

import com.sdl.experienceoptimization.analytics.algorithm.ExperimentWinnerAlgorithm;
import com.tridion.smarttarget.SmartTargetException;
import com.tridion.smarttarget.analytics.AnalyticsConfiguration;
import com.tridion.smarttarget.analytics.AnalyticsManager;
import com.tridion.smarttarget.analytics.CustomDimensionNames;
import com.tridion.smarttarget.analytics.results.AnalyticsResults;
import com.tridion.smarttarget.analytics.results.AnalyticsResultsRow;
import com.tridion.smarttarget.analytics.statistics.StatisticsExperimentDimensions;
import com.tridion.smarttarget.analytics.statistics.StatisticsFilter;
import com.tridion.smarttarget.analytics.statistics.StatisticsFilters;
import com.tridion.smarttarget.analytics.statistics.StatisticsTimeDimensions;
import com.tridion.smarttarget.experiments.statistics.Variants;

import java.util.Calendar;

/**
 * AnalyticsManagerBase
 *
 * @author nic
 */
public abstract class AnalyticsManagerBase extends AnalyticsManager {

    protected static Class<?> experimentWinnerAlgorithm;

    protected AnalyticsManagerBase() throws SmartTargetException {

        synchronized ( AnalyticsManagerBase.class ) {
            if (experimentWinnerAlgorithm == null) {
                String algorithmClassName = this.getConfiguration().getAnalyticsProperty("ExperimentWinnerAlgorithmClassName");

                try {
                    experimentWinnerAlgorithm = Class.forName(algorithmClassName);
                } catch (ClassNotFoundException e) {
                    throw new SmartTargetException("Could not load experiment winner algorithm with class name: " + algorithmClassName, e);
                }
            }
        }
    }

    @Override
    public CustomDimensionNames getCustomDimensions() {
        return new SimpleCustomDimensionNames();
    }

    @Override
    public StatisticsFilter getStatisticsFilter() {
        return new SimpleStatisticsFilter();
    }

    @Override
    public void calculateWinner(Variants variants) throws SmartTargetException {

        try {
            ExperimentWinnerAlgorithm algorithm = (ExperimentWinnerAlgorithm)
                    this.experimentWinnerAlgorithm.getConstructor(AnalyticsConfiguration.class).
                            newInstance(this.getConfiguration());
            algorithm.process(variants);
        }
        catch ( SmartTargetException e ) {
            throw e;
        }
        catch ( Exception e ) {
            throw new SmartTargetException("Error while calculating winner", e);
        }
    }

    protected String getExperimentId(StatisticsFilters statisticsFilters) {
        for ( StatisticsFilter filter : statisticsFilters ) {
            if ( filter.getName().equals("ExperimentId") ) {
                return filter.getOperand();
            }
        }
        return null;
    }

    protected void addAnalyticsResultsRow(AnalyticsResults results,
                                        AggregatedTracking tracking,
                                        StatisticsExperimentDimensions experimentDimensions,
                                        StatisticsTimeDimensions timeDimensions)
            throws SmartTargetException
    {

        AnalyticsResultsRow row = new AnalyticsResultsRow();
        if ( experimentDimensions.isPerExperimentId() ) {
            row.getExperimentDimensions().setExperimentId(tracking.getExperimentId());
        }
        if ( experimentDimensions.isPerPublicationTargetId() ) {
            String publicationTargetTcmUri = TcmUtils.buildPublicationTargetTcmUri(tracking.getPublicationTargetId());
            row.getExperimentDimensions().setPublicationTargetId(publicationTargetTcmUri);
        }
        if ( experimentDimensions.isPerPublicationId() ) {
            String publicationTcmUri = TcmUtils.buildPublicationTcmUri(tracking.getPublicationId());
            row.getExperimentDimensions().setPublicationId(publicationTcmUri);
        }
        if (experimentDimensions.isPerChosenVariant()) {
            row.getExperimentDimensions().setChosenVariant(Integer.toString(tracking.getChosenVariant()));
        }
        if (experimentDimensions.isPerComponentId()) {
            String componentTcmUri = TcmUtils.buildComponentTcmUri(tracking.getPublicationId(), tracking.getComponentId());
            row.getExperimentDimensions().setComponentId(componentTcmUri);
        }
        if (experimentDimensions.isPerComponentTemplateId()) {
            String componentTemplateTcmUri = TcmUtils.buildComponentTemplateTcmUri(tracking.getPublicationId(), tracking.getComponentTemplateId());
            row.getExperimentDimensions().setComponentTemplateId(componentTemplateTcmUri);
        }
        if (experimentDimensions.isPerPageId()) {
            String pageTcmUri = TcmUtils.buildPageTcmUri(tracking.getPublicationId(), tracking.getPageId());
            row.getExperimentDimensions().setPageId(pageTcmUri);
        }
        if (experimentDimensions.isPerRegion()) {
            row.getExperimentDimensions().setRegion(tracking.getRegion());
        }

        if (!row.getExperimentDimensions().isValid()) return;


        Calendar cal = Calendar.getInstance();
        cal.setTime(tracking.getDate());

        if (timeDimensions.isPerYear()) row.getTimeDimensions().setYear(cal.get(Calendar.YEAR));
        if (timeDimensions.isPerMonth()) row.getTimeDimensions().setMonth(cal.get(Calendar.MONTH));
        if (timeDimensions.isPerWeek()) row.getTimeDimensions().setWeek(cal.get(Calendar.WEEK_OF_YEAR));
        if (timeDimensions.isPerHour()) row.getTimeDimensions().setHour(cal.get(Calendar.HOUR));
        if (timeDimensions.isPerDay()) {
            Calendar dayCal = Calendar.getInstance();
            dayCal.clear();
            dayCal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            row.getTimeDimensions().setDay(dayCal.getTime());
        }
        AnalyticsResultsRow existingRow = results.getExistingRowWithSameDimensions(row);
        if (existingRow != null) {
            row = existingRow;
        }
        else {
            results.getRows().add(row);
        }

        if ( tracking.getType() == ExperimentType.CONVERSION ) {
            row.setVariantConversions(row.getVariantConversions()+1);
        }
        else {
            row.setVariantViews(row.getVariantViews()+1);
        }
    }

}
