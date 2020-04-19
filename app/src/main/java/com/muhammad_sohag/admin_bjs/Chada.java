package com.muhammad_sohag.admin_bjs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.Timestamp;

import java.util.Date;

public class Chada extends AppCompatActivity {
    private EditText prerokName, kromikNo;
    private TextView name;
    private Button cBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chada);

        cBtn = findViewById(R.id.c_btn);
        name = findViewById(R.id.c_name);
        prerokName = findViewById(R.id.c_prerok_name);
        kromikNo = findViewById(R.id.c_kromik_no);

        String uid = getIntent().getStringExtra("uid");
        String nameExtra = getIntent().getStringExtra("name");

        name.setText(nameExtra);

    }



}
