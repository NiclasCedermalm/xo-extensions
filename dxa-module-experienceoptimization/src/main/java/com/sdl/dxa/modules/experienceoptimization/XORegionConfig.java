package com.sdl.dxa.modules.experienceoptimization;

import java.util.Map;

/**
 * SmartTargetRegionConfig
 *
 * @author nic
 */
public class XORegionConfig {

    private String name;
    private String view;
    private int maxItems;
    private AllowDuplicatesValue allowDuplicates;

    public enum AllowDuplicatesValue {
        USE_CONFIGURATION,
        ALLOW,
        DISALLOW;

        public static AllowDuplicatesValue fromString(String value) {
            if ( value == null ) {
                return DISALLOW;
            }
            if ( value.equalsIgnoreCase("Use configuration") ) {
                return USE_CONFIGURATION;
            }
            else if ( value.equalsIgnoreCase("Yes") ) {
                return ALLOW;
            }
            else {
                return DISALLOW;
            }
        }
    }

    public XORegionConfig(Map<String,String> configData) {
        this.view = configData.get("view");
        this.name = configData.get("name");
        if ( this.name == null ) {
            if ( this.view.contains(":") ) {
                this.name = this.view.split(":")[1];
            }
            else {
                this.name = this.view;
            }
        }
        this.maxItems = (int) Float.parseFloat(configData.get("maxItems"));
        this.allowDuplicates = AllowDuplicatesValue.fromString(configData.get("allowDuplicates"));
    }

    public String getName() {
        return name;
    }

    public String getView() {
        return view;
    }

    public int getMaxItems() {
        return maxItems;
    }

    public AllowDuplicatesValue getAllowDuplicates() {
        return allowDuplicates;
    }
}
