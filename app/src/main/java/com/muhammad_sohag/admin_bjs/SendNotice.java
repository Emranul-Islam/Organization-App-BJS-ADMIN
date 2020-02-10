package com.muhammad_sohag.admin_bjs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SendNotice extends AppCompatActivity {

    private EditText names;
    private EditText massage;
    private Button sBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notice);

        names = findViewById(R.id.s_name);
        massage =findViewById(R.id.s_massage);
        final Map<String,Object> values=new HashMap<>();

        sBtn = findViewById(R.id.sBtn);
        sBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sBtn.setClickable(false);
                String nameV = names.getText().toString();
                String mValue= massage.getText().toString();
                //todo: I should add Date on there
                //KEY: S_NAME , S_MASSAGE , S_TIME
                values.put("S_NAME",nameV);
                values.put("S_MASSAGE",mValue);
               // values.put("S_TIME",)

                //todo: Ekhane database setup korte hobe :)

            }
        });




    }
}
