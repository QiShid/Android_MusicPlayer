package com.example.musicplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class MusicAdapter extends ArrayAdapter<Music> {
    private int resourceId;
    public MusicAdapter(Context context, int textViewResourceId, List<Music> objects) {
        super(context, textViewResourceId, objects);
        this.resourceId = textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Music music = getItem(position);
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        } else {
            view = convertView;
        }
        TextView city_name = (TextView) view.findViewById(R.id.music_name);
        city_name.setText((position+1)+"."+music.getName());
        return view;
    }
}
