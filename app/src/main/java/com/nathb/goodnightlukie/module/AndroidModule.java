package com.nathb.goodnightlukie.module;

import android.content.Context;
import android.os.Handler;

import com.nathb.goodnightlukie.service.sound.SoundStorage;
import com.nathb.goodnightlukie.service.timer.TimerManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = {
                SoundStorage.class,
                TimerManager.class
        },
        complete = false
)
public class AndroidModule {

    private Context mContext;

    public AndroidModule(Context context) {
        mContext = context;
    }

    @Provides @Singleton Context providesContext() {
        return mContext;
    }

    @Provides Handler providesHandler() {
        return new Handler();
    }
}
