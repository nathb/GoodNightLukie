package com.nathb.goodnightlukie;

import android.app.Application;

import com.nathb.goodnightlukie.module.AndroidModule;
import com.nathb.goodnightlukie.module.SoundManagerModule;
import com.nathb.goodnightlukie.module.SoundStorageModule;
import com.nathb.goodnightlukie.module.TimerManagerModule;
import com.nathb.goodnightlukie.service.sound.Sound;
import com.nathb.goodnightlukie.service.sound.SoundManager;
import com.nathb.goodnightlukie.service.sound.SoundStorage;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import dagger.ObjectGraph;

public class GoodNightApp extends Application {

    @Inject SoundManager mSoundManager;
    @Inject SoundStorage mSoundStorage;
    private static ObjectGraph sObjectGraph;

    @Override
    public void onCreate() {
        super.onCreate();
        sObjectGraph = ObjectGraph.create(getModules().toArray());
        inject(this);
        initSoundManager();
    }

    public static void inject(Object object) {
        sObjectGraph.inject(object);
    }

    private List<? extends Object> getModules() {
        return Arrays.asList(
                new AndroidModule(this),
                new SoundManagerModule(),
                new SoundStorageModule(),
                new TimerManagerModule());
    }

    private void initSoundManager() {
        for (Sound sound : mSoundStorage.getSounds()) {
            mSoundManager.add(sound);
        }
    }
}
