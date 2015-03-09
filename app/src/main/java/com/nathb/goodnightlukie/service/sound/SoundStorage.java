package com.nathb.goodnightlukie.service.sound;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import com.nathb.goodnightlukie.R;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class SoundStorage {
    
    private List mSounds;
    private Context mContext;

    @Inject
    public SoundStorage(Context context) {
        mContext = context;
        final Resources resources = mContext.getResources();
        mSounds = new ArrayList<>();
        mSounds.add(new Sound(R.raw.birds, getVolumePercentage(R.raw.birds),
                resources.getDrawable(R.drawable.birds_outline),
                resources.getDrawable(R.drawable.birds_fill)));
        mSounds.add(new Sound(R.raw.fire, getVolumePercentage(R.raw.fire),
                resources.getDrawable(R.drawable.fire_outline),
                resources.getDrawable(R.drawable.fire_fill)));
        mSounds.add(new Sound(R.raw.people, getVolumePercentage(R.raw.people),
                resources.getDrawable(R.drawable.people_outline),
                resources.getDrawable(R.drawable.people_fill)));
        mSounds.add(new Sound(R.raw.rain, getVolumePercentage(R.raw.rain),
                resources.getDrawable(R.drawable.rain_outline),
                resources.getDrawable(R.drawable.rain_fill)));
        mSounds.add(new Sound(R.raw.waves, getVolumePercentage(R.raw.waves),
                resources.getDrawable(R.drawable.waves_outline),
                resources.getDrawable(R.drawable.waves_fill)));
        mSounds.add(new Sound(R.raw.whitenoise, getVolumePercentage(R.raw.whitenoise),
                resources.getDrawable(R.drawable.whitenoise_outline),
                resources.getDrawable(R.drawable.whitenoise_fill)));
    }

    public List<Sound> getSounds() {
        return mSounds;
    }

    private SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    public int getVolumePercentage(int soundId) {
        return getSharedPreferences().getInt(String.valueOf(soundId), 0);
    }

    public void save(Sound sound) {
        getSharedPreferences().edit()
                .putInt(String.valueOf(sound.getSoundId()), sound.getVolumePercentage())
                .apply();
    }
}
