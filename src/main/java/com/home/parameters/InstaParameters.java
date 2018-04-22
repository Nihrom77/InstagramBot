package com.home.parameters;

import org.brunocvcunha.instagram4j.Instagram4j;

public class InstaParameters {
    private Instagram4j instagram;

    public InstaParameters(Instagram4j instagram) {
        this.instagram = instagram;
    }

    public Instagram4j getInstagram() {
        return instagram;
    }

    public void setInstagram(Instagram4j instagram) {
        this.instagram = instagram;
    }
}
