package com.esther.noviMedewerkerMaandApp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.esther.noviMedewerkerMaandApp.databinding.ActivityResultBinding;

public class ResultActivity extends AppCompatActivity {

    ActivityResultBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imageView.setImageURI(getIntent().getData());

    }
}
