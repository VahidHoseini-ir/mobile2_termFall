package com.example.musicplayer_termpfall;

import android.content.Context;
import android.graphics.PostProcessor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class musicAdapter extends RecyclerView.Adapter<musicAdapter.musciViewHolder> {

    List<Mp3Finder.Mp3File> musicList;

    public musicAdapter(List<Mp3Finder.Mp3File> value) {
        musicList = value;
    }

    @NonNull
    @Override
    public musciViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);

        return new musciViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull musciViewHolder holder, int position) {
        holder.setItems(musicList.get(position).name);

        holder.tv_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.showMusicDialog(musicList.get(holder.getAdapterPosition()).path);
            }
        });

    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }


    public class musciViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_music;
        private SeekBar loader;
        private Context context;
        private Boolean isPlaying = false;


        public musciViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            tv_music = itemView.findViewById(R.id.tv_music);

        }

        public void setItems(String name) {
            tv_music.setText(name);
        }

        private void showMusicDialog(String musicPath) {
            loadMusic(musicPath);
            AlertDialog.Builder musicDialog = new AlertDialog.Builder(MainActivity.currentActivity);
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_music, null, false);
            musicDialog.setView(view);

            ImageView play_music = view.findViewById(R.id.play_music);
            loader = view.findViewById(R.id.loader);


            play_music.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView view = (ImageView) v;
                    if (isPlaying) {
                        pauseMusic();
                        view.setImageResource(R.drawable.ic_play_music);

                    } else {
                        playMusic();
                        view.setImageResource(R.drawable.ic_pause);

                    }

                }
            });
            musicDialog.show();
//
            initializeLoader();

        }

        private void loadMusic(String musicPath) {
//        String url = "https://irsv.upmusics.com/AliBZ/Ali%20Ahmadiani%20-%20Ariyayee%20(320).mp3";
//        String muci_path = "/storage/emulated/0/Music/homayoon_shajarian.mp3";
            Uri uri = Uri.parse("file://" + musicPath);

            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(MainActivity.currentActivity.getApplicationContext(), uri);
                mediaPlayer.prepare();

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("TAG", "loadMusic: " + e.toString());
            }

        }

        private void initializeLoader() {

            loader.setMax(mediaPlayer.getDuration());
            loader.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int value, boolean userChanded) {
                    if (userChanded) {
                        mediaPlayer.seekTo(value);
                    }

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (mediaPlayer != null) {
                        try {
                            final int currentPosition = mediaPlayer.getCurrentPosition();
                            MainActivity.currentActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    loader.setProgress(currentPosition);
                                }
                            });
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }

        private void pauseMusic() {
            mediaPlayer.pause();
        }

        MediaPlayer mediaPlayer;


        private void playMusic() {
            initializeLoader();
            mediaPlayer.start();
            isPlaying = true;
        }


    }

}
