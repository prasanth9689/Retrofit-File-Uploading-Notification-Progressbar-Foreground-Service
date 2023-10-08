package com.skyblue.retrofitnotificationprogressbarforegroundservice;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.skyblue.retrofitnotificationprogressbarforegroundservice.databinding.ActivityMainBinding;
import com.skyblue.retrofitnotificationprogressbarforegroundservice.service.UploadService;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private Context context = this;
    private static final int MY_STORAGE_REQUEST_CODE = 2;
    private String realPath;
    private Uri realUriPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        permission();
        onClick();
    }

    private void onClick() {
        binding.select.setOnClickListener(v -> chooserVideoFile());
        binding.upload.setOnClickListener(v -> uploadNow());
    }

    private void uploadNow() {
        startService();
    }

    private void startService() {
        Intent serviceIntent = new Intent(this, UploadService.class);
        serviceIntent.putExtra("video_uri", String.valueOf(realUriPath));
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    private void permission() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_STORAGE_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case MY_STORAGE_REQUEST_CODE:
            {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                    {
                        // Storage Permission granted
                        chooserVideoFile();
                    }
                }
                else
                {
                    Toast.makeText(this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void chooserVideoFile() {
        // Single screen multiple image view or list the view refer below link
        // https://github.com/prasanth9689/Interview_Task_Get_Current_Location/blob/master/app/src/main/java/com/skyblue/machinetask/activity/ChooseCameraActivity.java
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityIntent.launch(intent);
    }

    ActivityResultLauncher<Intent> startActivityIntent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Uri uri = data.getData();
                        realPath = RealPathUtil.getRealPath(context, uri);
                        realUriPath = Uri.fromFile(new File(realPath));
                        playVideo(uri);
                    }
                }
            }
    );

    private void playVideo(Uri uri) {
       if (uri != null){
           MediaController mediaController= new MediaController(this);
           mediaController.setAnchorView(binding.videoView);

           binding.videoView.setMediaController(mediaController);
           binding.videoView.setVideoURI(uri);
           binding.videoView.requestFocus();
           binding.videoView.start();
       }
    }
}