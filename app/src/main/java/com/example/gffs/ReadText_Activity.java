package com.example.gffs;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class ReadText_Activity extends AppCompatActivity {
    private TextView showHere;
    private String tagTextRead;
    private Bundle bundle;



    @Override
    @TargetApi(26)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tagTextRead = getIntent().getExtras().getString("tagTextRead");
        if(tagTextRead == null){
            bundle = new ReadUtility().newRead(getIntent());
            tagTextRead = bundle.getString("Text");
            if (tagTextRead == null){
                Toast.makeText(getApplicationContext(),bundle.getString("err"),Toast.LENGTH_SHORT).show();
            }
        }
        setContentView(R.layout.activity_read_text_);
        showHere = findViewById(R.id.showHere);
        showHere.setText(tagTextRead);
        if (Build.VERSION.SDK_INT >= 26){
            showHere.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
        }
    }
}
