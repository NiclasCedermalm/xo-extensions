package com.sdl.dxa.modules.experienceoptimization.markup;

import com.sdl.dxa.modules.smarttarget.model.entity.SmartTargetPromotion;
import com.sdl.webapp.common.api.WebRequestContext;
import com.sdl.webapp.common.api.model.EntityModel;
import com.sdl.webapp.common.api.model.ViewModel;
import com.sdl.webapp.common.markup.MarkupDecorator;
import com.sdl.webapp.common.markup.html.HtmlCommentNode;
import com.sdl.webapp.common.markup.html.HtmlNode;
import com.sdl.webapp.common.markup.html.builders.HtmlBuilders;

/**
 * XO Promotion XPM Markup
 *
 * @author nic
 */
public class XOPromotionXpmMarkup implements MarkupDecorator {

    private static final String PROMOTION_PATTERN = "Start Promotion: " +
            "{\"PromotionID\": \"%s\", \"RegionID\": \"%s\"}";


    public XOPromotionXpmMarkup() {
    }

    @Override
    public HtmlNode process(HtmlNode markup, ViewModel model, WebRequestContext webRequestContext) {

        if ( webRequestContext.isPreview() ) {

            if ( model instanceof EntityModel && model.getXpmMetadata() != null ) {

                SmartTargetPromotion promotion = (SmartTargetPromotion) model.getMvcData().getMetadata().get("Promotion");
                if ( promotion != null ) {
                    final String promotionId = (String) promotion.getXpmMetadata().get("PromotionID");
                    final String regionName = (String) promotion.getXpmMetadata().get("RegionID");

                    if (promotionId != null) {
                        markup = HtmlBuilders.span()
                                .withNode(this.buildXpmMarkup(promotionId, regionName))
                                .withNode(markup).build();
                    }
                }
            }
        }
        return markup;
    }

    @Override
    public int getPriority() {
        return 2;
    }

    private HtmlNode buildXpmMarkup(String promotionId, String regionId) {
        return new HtmlCommentNode(String.format(PROMOTION_PATTERN, promotionId, regionId));
    }
}
