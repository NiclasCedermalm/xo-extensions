package com.sdl.dxa.modules.experienceoptimization;

import com.sdl.dxa.modules.smarttarget.model.entity.SmartTargetRegion;
import com.sdl.webapp.common.api.model.RegionModel;
import com.sdl.webapp.common.api.model.RegionModelSet;
import com.sdl.webapp.common.api.model.region.RegionModelSetImpl;

import java.util.Set;

/**
 * XO Region Model Set
 * A XO specific region model set to take care of mapping SmartTargetRegion to XORegion if needed.
 * This to make sure generic logic in the OOTB XO module works with extensions in this module.
 *
 * @author nic
 */
public class XORegionModelSet extends RegionModelSetImpl {

    public XORegionModelSet() {
    }

    public XORegionModelSet(RegionModelSet regionModelSet) {
        for ( RegionModel region : regionModelSet ) {
            this.add(region);
        }
    }

    @Override
    public boolean containsClass(Class<? extends RegionModel> clazz) {
        boolean contains = super.containsClass(clazz);
        if ( !contains && clazz == SmartTargetRegion.class ) {
            contains = super.containsClass(XORegion.class);
        }
        return contains;
    }

    @Override
    public <T extends RegionModel> Set<T> get(Class<T> clazz) {
        Set<T> regions = super.get(clazz);
        if ( regions == null && clazz == SmartTargetRegion.class ) {
            regions = (Set<T>) super.get(XORegion.class);

        }
        return regions;
    }
}
