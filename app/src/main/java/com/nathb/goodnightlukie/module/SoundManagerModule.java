package com.nathb.goodnightlukie.module;

import com.nathb.goodnightlukie.GoodNightApp;
import com.nathb.goodnightlukie.service.sound.SoundManager;
import com.nathb.goodnightlukie.ui.MainActivity;
import com.nathb.goodnightlukie.ui.SoundFragment;
import com.nathb.goodnightlukie.ui.TimerFragment;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = {
                GoodNightApp.class,
                MainActivity.class,
                SoundFragment.class,
                TimerFragment.class
        },
        includes = {
                AndroidModule.class
        },
        complete = false
)
public class SoundManagerModule {

    @Provides @Singleton SoundManager providesSoundManager() {
        return new SoundManager();
    }
}
