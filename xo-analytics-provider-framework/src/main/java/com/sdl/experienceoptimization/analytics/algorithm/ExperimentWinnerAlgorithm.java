package com.sdl.experienceoptimization.analytics.algorithm;

import com.tridion.smarttarget.experiments.statistics.Variants;

/**
 * Interface for Experiment winner algorithms
 *
 * @author nic
 */
public interface ExperimentWinnerAlgorithm {

    /**
     * Process the different experiment variants.
     * @param variants
     */
    void process(Variants variants);
}
