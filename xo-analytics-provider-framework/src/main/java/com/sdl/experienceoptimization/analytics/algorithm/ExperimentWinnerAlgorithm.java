package com.sdl.experienceoptimization.analytics.algorithm;

import com.tridion.smarttarget.experiments.statistics.Variants;

/**
 * ExperimentWinnerAlgorithm
 *
 * @author nic
 */
public interface ExperimentWinnerAlgorithm {

    public void process(Variants variants);
}
