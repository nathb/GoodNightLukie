package com.nathb.goodnightlukie.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.nathb.goodnightlukie.GoodNightApp;
import com.nathb.goodnightlukie.R;
import com.nathb.goodnightlukie.service.sound.Sound;
import com.nathb.goodnightlukie.service.sound.SoundManager;
import com.nathb.goodnightlukie.service.sound.SoundStorage;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SoundFragment extends Fragment {

    @Inject SoundManager mSoundManager;
    @Inject SoundStorage mSoundStorage;
    @InjectView(R.id.SoundGrid) GridView mSoundGrid;

    private SoundAdapter mAdapter;

    public SoundFragment() {
        GoodNightApp.inject(this);
    }

    public static SoundFragment newInstance() {
        return new SoundFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_sound, container, false);
        ButterKnife.inject(this, view);
        mAdapter = new SoundAdapter(getActivity(), R.layout.griditem_sound, mSoundStorage.getSounds(),
                new SoundAdapter.SoundVolumeChangedListener() {
                    @Override
                    public void onVolumeChanged(Sound sound) {
                        mSoundManager.update(sound);
                    }

                    @Override
                    public void onVolumeChangeCompleted(Sound sound) {
                        mSoundStorage.save(sound);
                    }
                });
        mSoundGrid.setAdapter(mAdapter);
        return view;
    }
}
