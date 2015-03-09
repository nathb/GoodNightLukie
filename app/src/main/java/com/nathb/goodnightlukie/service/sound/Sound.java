package com.nathb.goodnightlukie.service.sound;

import android.graphics.drawable.Drawable;

public class Sound {

    private final int mSoundId;
    private final Drawable mOutlineDrawable;
    private final Drawable mFilledDrawable;
    private int mVolumePercentage;

    public Sound(int soundId,  int volumePercentage, Drawable outlineDrawable, Drawable filledDrawable) {
        mSoundId = soundId;
        mVolumePercentage = volumePercentage;
        mOutlineDrawable = outlineDrawable;
        mFilledDrawable = filledDrawable;
    }

    public int getSoundId() {
        return mSoundId;
    }

    public int getVolumePercentage() {
        return mVolumePercentage;
    }

    public void setVolumePercentage(int volumePercentage) {
        mVolumePercentage = volumePercentage;
    }

    public Drawable getOutlineDrawable() {
        return mOutlineDrawable;
    }

    public Drawable getFilledDrawable() {
        return mFilledDrawable;
    }
}
