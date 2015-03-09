package com.nathb.goodnightlukie.service.sound;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.nathb.goodnightlukie.GoodNightApp;
import com.nathb.goodnightlukie.service.timer.TimerManager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.inject.Inject;

public class SoundManager {

    public interface PlaybackStateChangedListener {
        void onPlaybackStateChanged();
    }

    @Inject Context mContext;
    @Inject TimerManager mTimerManager;
    private final CopyOnWriteArraySet<PlaybackStateChangedListener> mListeners;
    private Map<Integer, MediaPlayer> mMediaMap = new HashMap<>();
    private boolean mIsPlaying;

    public SoundManager() {
        GoodNightApp.inject(this);
        mListeners = new CopyOnWriteArraySet<>();
        mTimerManager.addListener(new TimerManager.TimerFinishedListener() {
            @Override
            public void onTimerFinished() {
                stop();
            }
        });
    }

    public void addListener(PlaybackStateChangedListener listener) {
        mListeners.add(listener);
    }

    public void removeListener(PlaybackStateChangedListener listener) {
        mListeners.remove(listener);
    }

    public void add(Sound sound) {
        final MediaPlayer mediaPlayer = MediaPlayer.create(mContext, sound.getSoundId());
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setLooping(true);
        mMediaMap.put(sound.getSoundId(), mediaPlayer);
        update(sound);
    }

    public void update(Sound sound) {
        final MediaPlayer mediaPlayer = mMediaMap.get(sound.getSoundId());
        if (mediaPlayer != null) {
            float volume = sound.getVolumePercentage() / 100f;
            mediaPlayer.setVolume(volume, volume);
        }
    }

    public void play() {
        if (!isPlaying()) {
            mIsPlaying = true;
            for (MediaPlayer mediaPlayer : mMediaMap.values()) {
                mediaPlayer.start();
            }
            notifyListeners();
        }
    }

    public void stop() {
        if (mIsPlaying) {
            mIsPlaying = false;
            for (MediaPlayer mediaPlayer : mMediaMap.values()) {
                mediaPlayer.pause();
            }
            notifyListeners();
        }
    }

    public boolean isPlaying() {
        return mIsPlaying;
    }

    public void notifyListeners() {
        for (PlaybackStateChangedListener listener : mListeners) {
            listener.onPlaybackStateChanged();
        }
    }

}
