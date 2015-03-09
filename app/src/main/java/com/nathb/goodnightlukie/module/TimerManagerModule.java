package com.nathb.goodnightlukie.module;

import com.nathb.goodnightlukie.service.sound.SoundManager;
import com.nathb.goodnightlukie.service.timer.TimerManager;
import com.nathb.goodnightlukie.ui.MainActivity;
import com.nathb.goodnightlukie.ui.TimerFragment;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = {
                MainActivity.class,
                SoundManager.class,
                TimerFragment.class
        },
        complete = false
)
public class TimerManagerModule {

    @Provides @Singleton TimerManager providesTimerManager() {
        return new TimerManager();
    }
}
