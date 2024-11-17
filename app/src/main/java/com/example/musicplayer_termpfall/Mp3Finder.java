package com.example.musicplayer_termpfall;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Mp3Finder {

    public static class Mp3File {

        String name;
        String path;

        public Mp3File(String name, String path) {
            this.name = name;
            this.path = path;

        }
    }

    // این متد فایل‌های MP3 را پیدا کرده و در لیستی برمی‌گرداند
    public List<Mp3File> findMp3Files(Context context) {
        List<Mp3File> mp3Files = new ArrayList<>();

        // ابتدا به بررسی حافظه داخلی و خارجی می‌پردازیم
        File storageDir = Environment.getExternalStorageDirectory();
        if (storageDir.exists()) {
            findMp3InDirectory(storageDir, mp3Files);
        }

        // سپس از MediaStore برای پیدا کردن MP3 ها استفاده می‌کنیم
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DATA
        };

        Cursor cursor = context.getContentResolver().query(uri, projection,
                MediaStore.Audio.Media.MIME_TYPE + " = ?", new String[]{"audio/mpeg"}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME);
            int dataColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);

            do {
                String name = cursor.getString(nameColumnIndex);
                String path = cursor.getString(dataColumnIndex);

                mp3Files.add(new Mp3File(name, path));
            } while (cursor.moveToNext());

            cursor.close();
        }

        return mp3Files;
    }

    // این متد برای جستجوی فایل‌های MP3 در دایرکتوری خاص استفاده می‌شود
    private void findMp3InDirectory(File directory, List<Mp3File> mp3Files) {
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        findMp3InDirectory(file, mp3Files); // جستجو در دایرکتوری‌های فرعی
                    } else if (file.getName().endsWith(".mp3")) {
                        mp3Files.add(new Mp3File(file.getName(), file.getAbsolutePath()));
                    }
                }
            }
        }
    }
}
