package com.sdl.dxa.modules.experienceoptimization;

import com.sdl.dxa.modules.experienceoptimization.markup.XOPromotionXpmMarkup;
import com.sdl.dxa.modules.experienceoptimization.markup.XORegionXpmMarkup;
import com.sdl.dxa.modules.experienceoptimization.markup.XOTrackingMarkupDecorator;
import com.sdl.dxa.modules.experienceoptimization.model.PromoBanner;
import com.sdl.dxa.modules.smarttarget.markup.TrackingMarkupDecorator;
import com.sdl.webapp.common.api.mapping.views.AbstractInitializer;
import com.sdl.webapp.common.api.mapping.views.ModuleInfo;
import com.sdl.webapp.common.api.mapping.views.RegisteredViewModel;
import com.sdl.webapp.common.api.mapping.views.RegisteredViewModels;
import com.sdl.webapp.common.markup.MarkupDecoratorRegistry;
import com.tridion.smarttarget.SmartTargetException;
import com.tridion.smarttarget.analytics.AnalyticsManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * XO Module
 *
 * @author nic
 */
@Component
@RegisteredViewModels({
        @RegisteredViewModel(viewName = "PromoBanner", modelClass = PromoBanner.class)
})
@ModuleInfo(name = "Experience Optimization Module", areaName = "XO", description = "Module for SDL Web 8 Experience Optimization")
@Slf4j
public class XOModule extends AbstractInitializer {

    @Autowired
    private MarkupDecoratorRegistry markupDecoratorRegistry;

    @Override
    protected String getAreaName() {
        return "XO";
    }

    @PostConstruct
    public void initialize() throws Exception {

        XORegionXpmMarkup regionXpmMarkup = new XORegionXpmMarkup();
        XOPromotionXpmMarkup promotionXpmMarkup = new XOPromotionXpmMarkup();
        XOTrackingMarkupDecorator trackingMarkup = new XOTrackingMarkupDecorator();

        try {
            trackingMarkup.setAnalyticsManager(AnalyticsManager.getConfiguredAnalyticsManager());
        } catch (SmartTargetException e) {
            log.warn("Analytics manager for XO markup decorator can't be initialized. Do you have a proper configuration?", e);
        }

        //this.markupDecoratorRegistry.registerDecorator("Region", regionXpmMarkup);
        //this.markupDecoratorRegistry.registerDecorator("Regions", regionXpmMarkup);
        this.markupDecoratorRegistry.registerDecorator("Entity", promotionXpmMarkup);
        //this.markupDecoratorRegistry.registerDecorator("Entities", promotionXpmMarkup);
        this.markupDecoratorRegistry.registerDecorator("Entity", trackingMarkup);
        //this.markupDecoratorRegistry.registerDecorator("Entities", trackingMarkup);
    }

}
