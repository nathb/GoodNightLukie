package com.nathb.goodnightlukie.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.nathb.goodnightlukie.R;
import com.nathb.goodnightlukie.service.sound.Sound;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SoundAdapter extends ArrayAdapter<Sound> {

    public interface SoundVolumeChangedListener {
        void onVolumeChanged(Sound sound);
        void onVolumeChangeCompleted(Sound sound);
    }

    private LayoutInflater mInflater;
    private SoundVolumeChangedListener mListener;

    public SoundAdapter(Context context, int resource, List<Sound> objects,
                        SoundVolumeChangedListener listener) {
        super(context, resource, objects);
        mInflater = LayoutInflater.from(context);
        mListener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.griditem_sound, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Sound item = getItem(position);
        viewHolder.mOutlineImage.setImageDrawable(item.getOutlineDrawable());
        viewHolder.mFilledImage.setImageDrawable(item.getFilledDrawable());
        viewHolder.mFilledImage.setAlpha(item.getVolumePercentage() / 100f);
        viewHolder.mSeekBar.setOnSeekBarChangeListener(null);
        viewHolder.mSeekBar.setProgress(item.getVolumePercentage());
        viewHolder.mSeekBar.setOnSeekBarChangeListener(new SeekBarListener(item));

        return convertView;
    }

    static class ViewHolder {
        @InjectView(R.id.OutlineImage) ImageView mOutlineImage;
        @InjectView(R.id.FilledImage) ImageView mFilledImage;
        @InjectView(R.id.SeekBar) SeekBar mSeekBar;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    private class SeekBarListener implements SeekBar.OnSeekBarChangeListener {

        private Sound mItem;

        public SeekBarListener(Sound item) {
            mItem = item;
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            mItem.setVolumePercentage(i);
            mListener.onVolumeChanged(mItem);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mListener.onVolumeChangeCompleted(mItem);
            notifyDataSetChanged();
        }
    }
}
