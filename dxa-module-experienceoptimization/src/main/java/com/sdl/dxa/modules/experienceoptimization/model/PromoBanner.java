package com.sdl.dxa.modules.experienceoptimization.model;

import com.sdl.webapp.common.api.mapping.semantic.annotations.SemanticEntity;
import com.sdl.webapp.common.api.mapping.semantic.annotations.SemanticProperty;
import com.sdl.webapp.common.api.model.RichText;
import com.sdl.webapp.common.api.model.entity.AbstractEntityModel;
import com.sdl.webapp.common.api.model.entity.Link;
import com.sdl.webapp.common.api.model.entity.MediaItem;

import static com.sdl.webapp.common.api.mapping.semantic.config.SemanticVocabulary.SDL_CORE;

/**
 * Promo Banner
 * - Simple banner which can be used in XO promotions.
 *
 * @author nic
 */

@SemanticEntity(entityName = "PromoBanner", vocabulary = SDL_CORE, prefix = "pb", public_ = true)
public class PromoBanner extends AbstractEntityModel {

    @SemanticProperty("pb:headline")
    private String headline;

    @SemanticProperty("pb:content")
    private RichText content;

    @SemanticProperty("pb:media")
    private MediaItem media;

    @SemanticProperty("pb:link")
    private Link link;

    public String getHeadline() {
        return headline;
    }


    public RichText getContent() {
        return content;
    }

    public MediaItem getMedia() {
        return media;
    }

    public Link getLink() {
        return link;
    }

    @Override
    public String toString() {
        return "PromoBanner{" +
                "headline='" + headline + '\'' +
                ", content='" + content + '\'' +
                ", media=" + media +
                ", link=" + link +
                '}';
    }
}
