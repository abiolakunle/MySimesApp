package com.software.abiol.simesapp;

import com.google.firebase.database.Exclude;

import io.reactivex.annotations.NonNull;

public class extUploadDetails {

    @Exclude
    public String name;

    public <T extends extUploadDetails> T withId(@NonNull final String id) {
        this.name = id;
        return (T) this;
    }

}
