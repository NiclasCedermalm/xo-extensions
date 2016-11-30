package com.sdl.dxa.modules.experienceoptimization.markup;

import com.sdl.dxa.modules.smarttarget.markup.AbstractTrackingMarkupDecorator;
import com.sdl.dxa.modules.smarttarget.model.entity.SmartTargetExperiment;
import com.sdl.dxa.modules.smarttarget.model.entity.SmartTargetPromotion;
import com.sdl.webapp.common.api.model.ViewModel;
import com.sdl.webapp.common.markup.html.HtmlNode;
import com.tridion.smarttarget.SmartTargetException;
import com.tridion.smarttarget.analytics.AnalyticsManager;
import com.tridion.smarttarget.analytics.tracking.AnalyticsMetaData;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;

/**
 * XO Tracking Markup Decorator
 *
 * @author nic
 */
@Slf4j
public class XOTrackingMarkupDecorator extends AbstractTrackingMarkupDecorator {

    @Setter
    private AnalyticsManager analyticsManager;

    @Override
    protected boolean isAnalyticsManagerNotInitialized() {
        return this.analyticsManager == null;
    }

    @Override
    protected boolean isNotExperiment(ViewModel model) {
        SmartTargetPromotion promotion = (SmartTargetPromotion) model.getMvcData().getMetadata().get("Promotion");
        return promotion == null || !(promotion instanceof SmartTargetExperiment);
    }

    @Override
    protected String processMarkupByAnalytics(HtmlNode markup, ViewModel model) {
        SmartTargetExperiment experiment = (SmartTargetExperiment) model.getMvcData().getMetadata().get("Promotion");
        try {

            this.analyticsManager.trackView(experiment.getExperimentDimensions(), Collections.emptyMap()
                    /*AnalyticsMetaData.fromRequest(this.httpRequest,
                            this.httpRequest.getSession())*/);


            return this.analyticsManager.addTrackingToLinks(markup.toHtml(),
                    experiment.getExperimentDimensions(), Collections.emptyMap());
        } catch (SmartTargetException e) {
            log.warn("Exception while adding tracking to experiment links", e);
            return markup.toHtml();
        }
    }
}
