package com.sdl.experienceoptimization.analytics.dummy;

import com.tridion.smarttarget.analytics.CustomDimensionNames;

public class DummyDataCustomDimensionNames extends CustomDimensionNames
{
    protected static final String EXPERIMENT_ID = "ExperimentId";
    protected static final String PUBLICATION_TARGET_ID = "PublicationTargetId";
    protected static final String PUBLICATION_ID = "PublicationId";
    protected static final String PAGE_ID = "PageId";
    protected static final String REGION = "Region";
    protected static final String COMPONENT_ID = "ComponentId";
    protected static final String COMPONENT_TEMPLATE_ID = "ComponentTemplateId";
    protected static final String CHOSEN_VARIANT = "ChosenVariant";

    public DummyDataCustomDimensionNames() {
        this.setExperimentId(EXPERIMENT_ID);
        this.setPublicationTargetId(PUBLICATION_TARGET_ID);
        this.setPublicationId(PUBLICATION_ID);
        this.setPageId(PAGE_ID);
        this.setRegion(REGION);
        this.setComponentId(COMPONENT_ID);
        this.setComponentTemplateId(COMPONENT_TEMPLATE_ID);
        this.setChosenVariant(CHOSEN_VARIANT);
    }

}