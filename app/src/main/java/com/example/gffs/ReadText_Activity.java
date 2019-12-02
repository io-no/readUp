package com.example.gffs;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ReadText_Activity extends AppCompatActivity {
    private TextView showHere;
    private String tagTextRead;


    /*
     * Alla creazione, estrapolo il dato testuale da caricare.
     * Gestisco le due possibili cause dell'avvio di questa
     * activity:
     * 1. Se lanciata dalla MainActivity, il dato inserito
     *    all'interno dell'intent avrà chiave "tagTextRead".
     * 2. Se lanciata dal sistema mentre il fragment principale
     *    non è in  foreground, mi servo dei metodi implementati
     *    nella classe ReadUtility.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tagTextRead = getIntent().getExtras().getString("tagTextRead");
        if(tagTextRead==null){
            tagTextRead = new ReadUtility().newRead(getIntent()).getString("Text");
        }
        setContentView(R.layout.activity_read_text_);
        showHere = findViewById(R.id.showHere);
        showHere.setText(tagTextRead);
    }

}
