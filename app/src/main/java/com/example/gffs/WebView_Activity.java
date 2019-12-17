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
