package com.sdl.web.experience.management.experiments;

import com.tridion.smarttarget.SmartTargetException;
import com.tridion.smarttarget.experiments.Experiment;
import com.tridion.smarttarget.promotions.Trigger;
import com.tridion.storage.BaseEntity;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ExperimentEntity
 *
 * @author nic
 */

public class ExperimentEntity extends BaseEntity
{
    private static final Logger LOG = LoggerFactory.getLogger(ExperimentEntity.class);
    private Experiment experiment;
    private static final String ERROR_FALLBACK = "Unknown";

    public ExperimentEntity(Experiment experiment)
    {
        this.experiment = experiment;
    }

    public Object getPK()
    {
        return getId();
    }

    public int getObjectSize() {
        return 0;
    }

    public String getId() {
        return this.experiment.getId();
    }

    public String getPublicationTargetId() {
        return this.experiment.getPublicationTargetId();
    }

    public String getName() {
        return this.experiment.getName();
    }

    public String getPromotionType() {
        return this.experiment.getPromotionType().name();
    }

    public String getTitle() {
        try {
            return this.experiment.getTitle();
        } catch (SmartTargetException ex) {
            LOG.error("Unable to get the title of an Experiment.", ex);
        }return "Unknown";
    }

    public String getSlogan()
    {
        try {
            return this.experiment.getSlogan();
        } catch (SmartTargetException ex) {
            LOG.error("Unable to get the Slogan of an Experiment.", ex);
        }return "Unknown";
    }

    public Date getLastModified()
    {
        return this.experiment.getLastModifiedDate();
    }

    public boolean isActive() {
        return this.experiment.isActive().booleanValue();
    }

    public String getState() {
        return this.experiment.getState().name();
    }

    public String getTriggers() {
        return this.experiment.getTriggers().serialize();
    }

    public String getAction() {
        return this.experiment.getAction().serialize();
    }

    public int getMaxItems() {
        return this.experiment.getMaxItems();
    }

    public String getScopeTriggers() {

        // Modify the region name (set it to only lower case) to trick the
        // validation in the XO GUI. This makes it possible to define several
        // experiments on the same page and same region. Which is needed if you
        // want to have several contextual experiments on the same page
        //
        for (Trigger trigger : this.experiment.getScopeTriggers() ) {
            if ( trigger.getName().equals("SmartTarget Region") ) {
                for ( int i=0; i < trigger.getValues().size(); i++ ) {
                    String regionName = trigger.getValues().get(i);
                    regionName = regionName.toLowerCase();
                    trigger.getValues().set(i, regionName);
                }
            }
        }
        return this.experiment.getScopeTriggers().serialize();
    }

    public String getScopePublication()
    {
        try {
            return this.experiment.getScopePublication();
        } catch (SmartTargetException ex) {
            LOG.error("Unable to get the ScopePublication of an Experiment.", ex);
        }return "Unknown";
    }

    public boolean isIncludeChildPublications()
    {
        return this.experiment.isIncludeChildPublications();
    }

    public String getInsertAfter() {
        return this.experiment.getInsertAfter();
    }

    public Date getStartDate() {
        return this.experiment.getStartDate();
    }

    public Date getEndDate() {
        return this.experiment.getEndDate();
    }

    public String getStatistics() {
        return this.experiment.getStatistics().serialize();
    }
}