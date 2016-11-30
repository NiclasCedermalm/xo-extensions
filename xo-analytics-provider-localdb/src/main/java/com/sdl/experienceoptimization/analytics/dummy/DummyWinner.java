package com.sdl.experienceoptimization.analytics.dummy;

public class DummyWinner
{
    private String publicationId;
    private int variantIndex;
    private int minConversion;
    private int maxConversion;

    public String getPublicationId()
    {
        return this.publicationId;
    }

    public void setPublicationId(String publicationId) {
        this.publicationId = publicationId;
    }

    public int getVariantIndex() {
        return this.variantIndex;
    }

    public void setVariantIndex(int variantIndex) {
        this.variantIndex = variantIndex;
    }

    public int getMinConversion() {
        return this.minConversion;
    }

    public void setMinConversion(int minConversion) {
        this.minConversion = minConversion;
    }

    public int getMaxConversion() {
        return this.maxConversion;
    }

    public void setMaxConversion(int maxConversion) {
        this.maxConversion = maxConversion;
    }
}