package com.example.gffs;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;



public class WebView_Activity extends AppCompatActivity {
    private WebView webView;
    private String address;
    private WebSettings webSettings;
    private Bundle bundle;



    /*
     * Alla creazione, estrapolo il link web da caricare.
     * Gestisco le due possibili cause dell'avvio di questa
     * activity:
     * 1. Se lanciata dalla MainActivity, il dato inserito
     *    all'interno dell'intent avrà chiave "tagUriRead".
     * 2. Se lanciata dal sistema mentre il fragment principale
     *    non è in foreground, mi servo dei metodi implementati
     *    nella classe ReadUtility e gestisco la possibilità in
     *    cui sia restituito un messaggio di errore.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        address = getIntent().getExtras().getString("tagUriRead");
        if(address==null){
            bundle = new ReadUtility().newRead(getIntent());
            address = bundle.getString("Uri");
            if(address==null){
                Toast.makeText(getApplicationContext(),bundle.getString("err"),Toast.LENGTH_SHORT).show();
            }
        }
        setContentView(R.layout.activity_web_view_);
        webView = findViewById(R.id.Web);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(address);
        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }


    @Override
    public void onBackPressed(){
        if(webView.canGoBack()){
            webView.goBack();
        } else{
            super.onBackPressed();
        }
    }

}
