package com.example.musicplayer_termpfall;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static Activity currentActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currentActivity = this;
        checkStoragePermission();

        RecyclerView recyclerView = findViewById(R.id.rv_music);

        Mp3Finder mp3Finder = new Mp3Finder();
        List<Mp3Finder.Mp3File> musicList = mp3Finder.findMp3Files(this);


        musicAdapter adapterM = new musicAdapter(musicList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterM);

    }


    private void checkStoragePermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_AUDIO);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_MEDIA_AUDIO},
                    REQUEST_EXTERNAL_STORAGE);
        } else {
            // Permission already granted, proceed with accessing the file
        }
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with accessing the file
            } else {
                // Permission denied, handle accordingly (e.g., show a message)
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


}


