package com.sdl.dxa.modules.experienceoptimization.markup;

import com.sdl.dxa.modules.smarttarget.model.entity.SmartTargetRegion;
import com.sdl.webapp.common.api.WebRequestContext;
import com.sdl.webapp.common.api.model.ViewModel;
import com.sdl.webapp.common.markup.MarkupDecorator;
import com.sdl.webapp.common.markup.html.HtmlCommentNode;
import com.sdl.webapp.common.markup.html.HtmlNode;
import com.sdl.webapp.common.markup.html.builders.HtmlBuilders;

/**
 * SmartTarget Region XPM Markup
 *
 * @author nic
 */
public class XORegionXpmMarkup implements MarkupDecorator {

    @Override
    public HtmlNode process(HtmlNode markup, ViewModel model, WebRequestContext webRequestContext) {

        if ( webRequestContext.isPreview() ) {
            if (model instanceof SmartTargetRegion) {
                SmartTargetRegion stRegion = (SmartTargetRegion) model;

                // If SmartTarget is disabled or down -> Ignore to generate targeting XPM markup
                //
                if ( stRegion.getStartQueryXpmMarkup() == null ) {
                    return markup;
                }

                // Surround with the SmartTarget XPM region markup
                //
                markup =
                        HtmlBuilders.div()
                                .withNode(new HtmlCommentNode(stRegion.getStartQueryXpmMarkup()))
                                .withNode(markup).build();
                
                markup = HtmlBuilders.span()
                        .withNode(new HtmlCommentNode("<!-- Start Promotion Region: {\"RegionID\": \"" + stRegion.getName() + "\"} -->"))
                        .withNode(markup).build();

            }
        }
        return markup;
    }

    @Override
    public int getPriority() {
        return 0;
    }
}
