package com.sdl.dxa.modules.experienceoptimization;

import com.sdl.dxa.modules.smarttarget.mapping.SmartTargetPageBuilder;
import com.sdl.dxa.modules.smarttarget.model.entity.SmartTargetPromotion;
import com.sdl.dxa.modules.smarttarget.model.entity.SmartTargetRegion;
import com.sdl.webapp.common.api.content.ContentProvider;
import com.sdl.webapp.common.api.content.ContentProviderException;
import com.sdl.webapp.common.api.localization.Localization;
import com.sdl.webapp.common.api.model.EntityModel;
import com.sdl.webapp.common.api.model.PageModel;
import com.sdl.webapp.common.api.model.RegionModel;
import com.sdl.webapp.common.exceptions.DxaException;
import lombok.extern.slf4j.Slf4j;
import org.dd4t.contentmodel.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Experience Optimization Page Builder
 *
 * @author nic
 */
@Component
@Slf4j
public class XOPageBuilder extends SmartTargetPageBuilder {

    @Override
    public PageModel createPage(Page page, PageModel pageModel, Localization localization, ContentProvider contentProvider) throws ContentProviderException {

        if ( pageModel != null && !pageModel.getRegions().containsClass(SmartTargetRegion.class) ) {

            // Go through page template metadata and do override on the regions that are marked as XO regions
            //
            List<XORegionConfig> regionConfigs = this.getXORegionConfiguration(pageModel);
            if ( regionConfigs != null ) {
                for (XORegionConfig regionConfig : regionConfigs) {
                    RegionModel region = pageModel.getRegions().get(regionConfig.getName());
                    try {
                        // Create XO region based on the page metadata
                        //
                        SmartTargetRegion xoRegion = new SmartTargetRegion(regionConfig.getName(), regionConfig.getView());
                        if (region != null) {
                            xoRegion.getEntities().addAll(region.getEntities()); // Transfer fallback content
                            pageModel.getRegions().remove(region);
                        }
                        pageModel.getRegions().add(xoRegion);
                    } catch (DxaException e) {
                        log.error("Could not create XO region: " + regionConfig.getName(), e);
                    }
                }

                pageModel = super.createPage(page, pageModel, localization, contentProvider);

                // Process page model and extract out the promotions and add them as entities directly.
                // This to avoid having specific XO region views, then XO can be applied on any region.
                //
                for (XORegionConfig regionConfig : regionConfigs) {
                    RegionModel region = pageModel.getRegions().get(regionConfig.getName());
                    if ( region != null && region instanceof SmartTargetRegion ) {
                        List<EntityModel> entities = new ArrayList<>();
                        for ( EntityModel xoEntity : region.getEntities() ) {
                            if ( xoEntity instanceof SmartTargetPromotion ) {
                                SmartTargetPromotion promotion = (SmartTargetPromotion) xoEntity;
                                for ( EntityModel entity : promotion.getEntityModels() ) {
                                    // A tweak using MVC metadata to be able to piggy back the promotion
                                    //
                                    entity.getMvcData().addMetadataValue("Promotion", promotion);
                                    entities.add(entity);
                                }
                            }
                            else {
                                entities.add(xoEntity);
                            }
                        }
                        ((SmartTargetRegion) region).setEntities(entities);
                    }
                }
            }
        }

        return pageModel;


    }

    private List<XORegionConfig> getXORegionConfiguration(PageModel page) {
        List<Map<String,String>> xoRegionConfig = (List<Map<String,String>>) page.getMvcData().getMetadata().get("regions");
        if ( xoRegionConfig == null ) return null;

        List<XORegionConfig> configList = new ArrayList<>();
        for ( Map<String,String> regionConfig : xoRegionConfig ) {
            if ( regionConfig.get("maxItems") == null  )
            {
                // Temporary workaround. We should have better way of detecting if this is a XO template or not.
                //
                return null;
            }
            configList.add(new XORegionConfig(regionConfig));
        }
        return configList;
    }

    @Override
    public int getOrder() {
        return 1001;
    }
}
