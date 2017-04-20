package com.sdl.dxa.modules.experienceoptimization;

import com.sdl.dxa.modules.smarttarget.model.entity.SmartTargetRegion;
import com.sdl.webapp.common.api.localization.Localization;
import com.sdl.webapp.common.api.model.MvcData;
import com.sdl.webapp.common.api.model.region.RegionModelImpl;
import com.sdl.webapp.common.exceptions.DxaException;
import lombok.extern.slf4j.Slf4j;

/**
 * XO Region
 *
 * @author nic
 */
@Slf4j
public class XORegion extends SmartTargetRegion {

    public XORegion(String name) throws DxaException {
        super(name);
    }

    public XORegion(String name, String qualifiedViewName) throws DxaException {
        super(name, qualifiedViewName);
    }

    public XORegion(MvcData mvcData) throws DxaException {
        super(mvcData);
    }

    @Override
    public String getXpmMarkup(Localization localization) {

        try {
            RegionModelImpl baseRegionModel = new RegionModelImpl(this.getName());
            return baseRegionModel.getXpmMarkup(localization);

        }
        catch ( DxaException e ) {
            log.error("Could not get XPM markup XO region: " + this.getName());
        }
        return super.getXpmMarkup(localization);
    }
}
