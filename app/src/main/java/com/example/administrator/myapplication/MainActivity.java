package com.example.administrator.myapplication;

import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String uri = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath()
                + "/" ;

        MediaScannerConnection.scanFile(this,
                new String[]{"/storage/sdcard1/Android/data/com.androidesk/ex_portrait/"},new String[]{"image/jpeg","image/png"}, null);
        Log.i("uriuriuri", uri);
        Toast.makeText(this, uri, Toast.LENGTH_SHORT).show();

//        while (true) {
//            new Thread(){
//                @Override
//                public void run() {
//                    super.run();
//                    try {
//                        URL url = new URL("http://www.baidu.com");
//                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//
//                    } catch (MalformedURLException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }.start();
//        }

    }
}
