/**
 * @Author Esther Adjei Mensah
 * @Leerlijn Praktijk 1
 * @Date 25/07/21
 */
package com.esther.noviMedewerkerMaandApp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity;
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants;
import com.esther.noviMedewerkerMaandApp.databinding.ActivityMainBinding;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    int IMAGE_REQUEST_CODE = 45;
    int CAMERA_REQUEST_CODE = 14;
    int RESULT_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//      hides actionbar
        getSupportActionBar().hide();

//      Opens gallery
        binding.openGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, IMAGE_REQUEST_CODE);
            }
        });

//      Opens camera
        binding.cameraButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

//              Asks for Camera permission
                if(ActivityCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.CAMERA}, 32);
                } else {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST_CODE){
            if (data.getData() != null) {
                Uri filepath = data.getData();

                Intent dsPhotoEditorIntent = new Intent(this, DsPhotoEditorActivity.class);
                dsPhotoEditorIntent.setData(filepath);

                dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, "Novi Medewerker vd maand gallerij");

                int[] toolsToHide = {DsPhotoEditorActivity.TOOL_ORIENTATION, DsPhotoEditorActivity.TOOL_CROP};
                dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE, toolsToHide);

                startActivityForResult(dsPhotoEditorIntent, RESULT_CODE);
            }
        }
        if (requestCode == RESULT_CODE){
            Intent intent = new Intent(MainActivity.this, ResultActivity.class);
            intent.setData(data.getData());
            startActivity(intent);
        }
        if (requestCode == CAMERA_REQUEST_CODE){
            Bitmap photo = (Bitmap) data.getExtras().get("data");

            Uri uri = getImageUri(photo);

            Intent dsPhotoEditorIntent = new Intent(this, DsPhotoEditorActivity.class);
            dsPhotoEditorIntent.setData(uri);

            dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, "Novi Medewerker vd maand gallerij");

            int[] toolsToHide = {DsPhotoEditorActivity.TOOL_ORIENTATION, DsPhotoEditorActivity.TOOL_CROP};
            dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE, toolsToHide);

            startActivityForResult(dsPhotoEditorIntent, RESULT_CODE);
        }
    }

    public Uri getImageUri(Bitmap bitmap){
        Date date = Calendar.getInstance().getTime();
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG , 100, arrayOutputStream);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "MedewerkerNoviCam " + date,null);
        return Uri.parse(path);
    }
}