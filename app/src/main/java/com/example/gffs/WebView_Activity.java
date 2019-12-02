package com.example.gffs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.common.primitives.Bytes;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class WebView_Activity extends AppCompatActivity {
    private WebView webView;
    private String address;



    /*
     * Alla creazione, estrapolo il link web da caricare.
     * Gestisco le due possibili cause dell'avvio di questa
     * activity:
     * 1. Se lanciata dalla MainActivity, il dato inserito
     *    all'interno dell'intent avrà chiave "tagUriRead".
     * 2. Se lanciata dal sistema mentre il fragment principale
     *    non è in foreground, mi servo dei metodi implementati
     *    nella classe ReadUtility.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        address = getIntent().getExtras().getString("tagUriRead");
        if(address==null){
            address = new ReadUtility().newRead(getIntent()).getString("Uri");
        }
        setContentView(R.layout.activity_web_view_);
        webView = findViewById(R.id.Web);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(address);
    }

}
