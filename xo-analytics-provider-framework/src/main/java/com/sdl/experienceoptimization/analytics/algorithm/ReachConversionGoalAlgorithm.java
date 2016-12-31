package com.sdl.experienceoptimization.analytics.algorithm;

import com.sdl.experienceoptimization.analytics.algorithm.ExperimentWinnerAlgorithm;
import com.tridion.smarttarget.analytics.AnalyticsConfiguration;
import com.tridion.smarttarget.experiments.statistics.Variant;
import com.tridion.smarttarget.experiments.statistics.Variants;

/**
 * Reach Conversion Goal Algorithm
 * Simple algorithm that just takes the first variant reaching a set conversion goal. Can be useful in demos etc.
 *
 * @author nic
 */
public class ReachConversionGoalAlgorithm implements ExperimentWinnerAlgorithm {

    private int goal;

    public ReachConversionGoalAlgorithm(AnalyticsConfiguration configuration) {

        // TODO: Use reach a certain % level instead????
        this.goal = Integer.parseInt(configuration.getAnalyticsProperty("ConversionGoal", "1000"));
    }

    @Override
    public void process(Variants variants) {

        for ( Variant variant : variants ) {
            if ( variant.getConversions() >= this.goal ) {
                variant.setWinner(true);
                break;
            }
        }
    }
}
