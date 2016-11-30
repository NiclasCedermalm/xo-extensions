package com.sdl.experienceoptimization.analytics.dummy;

import com.tridion.smarttarget.analytics.results.AnalyticsResults;

public class CachedAnalyticsResults
{
    private AnalyticsResults summary = new AnalyticsResults();
    private AnalyticsResults perDay = new AnalyticsResults();

    public AnalyticsResults getSummary() {
        return this.summary;
    }

    public AnalyticsResults getPerDay() {
        return this.perDay;
    }
}