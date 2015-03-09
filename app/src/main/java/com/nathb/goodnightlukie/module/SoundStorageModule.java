package com.nathb.goodnightlukie.module;

import android.content.Context;

import com.nathb.goodnightlukie.GoodNightApp;
import com.nathb.goodnightlukie.service.sound.SoundStorage;
import com.nathb.goodnightlukie.ui.SoundFragment;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = {
                GoodNightApp.class,
                SoundFragment.class
        },
        includes = AndroidModule.class,
        complete = false
)
public class SoundStorageModule {

    @Provides @Singleton
    SoundStorage providesSoundStorage(Context context) {
        return new SoundStorage(context);
    }
}
