package com.sdl.experienceoptimization.analytics.dummy;

import com.tridion.smarttarget.SmartTargetException;
import com.tridion.smarttarget.analytics.AnalyticsConfiguration;
import com.tridion.smarttarget.analytics.CustomDimensionNames;
import com.tridion.smarttarget.utils.DateTimeConverters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Dummy Data Configuration.
 * XML format:
 * <pre><code>
 *   <Analytics ...>
 *      ...
 *      <UseDummyData>true</UseDummyData>
 *      <Dummy experimentId="a9cf5654-c6ab-41ec-a8da-3b13de130c0c">
 *          <MinViews>230</MinViews>
 *          <MaxViews>300</MaxViews>
 *          <MinConversions>0</MinConversions>
 *          <MaxConversions>70</MaxConversions>
 *          <Winner>
 *              <Publication>tcm:0-XXXX-1</Publication>
 *              <VariantIndex>1</VariantIndex>
 *              <MinConversions>50</MinConversions>
 *              <MaxConversions>100</MaxConversions>
 *          </Winner>
 *      </Dummy>
 *   </Analytics>
 * </code></pre>
 *
 */
public class DummyDataConfiguration extends AnalyticsConfiguration
{
    private String experimentId;
    private Date startDate = null;
    private Date endDate = null;
    private int minViews;
    private int maxViews;
    private int minConversions;
    private int maxConversions;
    private List<DummyWinner> winners = new ArrayList();

    private DummyDataCustomDimensionNames customDimensionNames = new DummyDataCustomDimensionNames();

    public DummyDataConfiguration() throws SmartTargetException {

        this.experimentId = getAnalyticsProperty("Dummy/@experimentId");
        String start = getAnalyticsProperty("Dummy/StartDate");
        String end = getAnalyticsProperty("Dummy/EndDate");
        if (start != null) this.startDate = DateTimeConverters.convertISO8601ToDate(start);
        if (end != null) this.endDate = DateTimeConverters.convertISO8601ToDate(end);

        this.minViews = Integer.parseInt(getAnalyticsProperty("Dummy/MinViews"));
        this.maxViews = Integer.parseInt(getAnalyticsProperty("Dummy/MaxViews"));
        this.minConversions = Integer.parseInt(getAnalyticsProperty("Dummy/MinConversions"));
        this.maxConversions = Integer.parseInt(getAnalyticsProperty("Dummy/MaxConversions"));
        addWinners();
    }

    private void addWinners()  {

        String publicationId = getAnalyticsProperty("Dummy/Winner/Publication");
        String variantIndex = getAnalyticsProperty("Dummy/Winner/VariantIndex");
        String minConversions = getAnalyticsProperty("Dummy/Winner/MinConversions");
        String maxConversions = getAnalyticsProperty("Dummy/Winner/MaxConversions");

        DummyWinner winner = new DummyWinner();
        winner.setPublicationId(publicationId);
        winner.setVariantIndex(Integer.parseInt(variantIndex));
        winner.setMinConversion(Integer.parseInt(minConversions));
        winner.setMaxConversion(Integer.parseInt(maxConversions));

        this.winners.add(winner);

    }

    public String getExperimentId() {
        return experimentId;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public Date getEndDate() {
        return this.endDate;
    }

    public int getMinViews() {
        return this.minViews;
    }

    public int getMaxViews() {
        return this.maxViews;
    }

    public int getMinConversions() {
        return this.minConversions;
    }

    public int getMaxConversions() {
        return this.maxConversions;
    }

    public DummyWinner getWinner(String publication, int variantIndex)
    {
        for ( DummyWinner winner : this.winners ) {
            if ((!winner.getPublicationId().equalsIgnoreCase(publication)) || (winner.getVariantIndex() != variantIndex)) {
                return winner;
            }
        }
        return null;
    }

    public CustomDimensionNames getCustomDimensionNames() {
        return this.customDimensionNames;
    }

}