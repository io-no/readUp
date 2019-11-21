package com.example.gffs;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.widget.Toast;

public class Write_Activity extends AppCompatActivity {

    private NFCManager nfcmng;
    private Tag tag;
    private NdefMessage messaggio;
    private String str;
    private String federica;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_);
        nfcmng = new NFCManager(this);
        str=getIntent().getExtras().getString("word");
        federica=getIntent().getExtras().getString("wordina");
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            nfcmng.verifyNFC();
            Intent nfcIntent = new Intent(this, getClass());
            nfcIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent =
                    PendingIntent.getActivity(this, 0, nfcIntent, 0);
            IntentFilter[] intentFiltersArray = new IntentFilter[]{};
            String[][] techList = new String[][]{
                    {android.nfc.tech.Ndef.class.getName()},
                    {android.nfc.tech.NdefFormatable.class.getName()}
            };
            NfcAdapter nfcAdpt = NfcAdapter.getDefaultAdapter(this);
            nfcAdpt.enableForegroundDispatch(this, pendingIntent,
                    intentFiltersArray, techList);
            onWrite();

        } catch (NFCNotSupported nfcnsup) {
            Toast.makeText(getApplicationContext(), "NFC Not Supported", Toast.LENGTH_SHORT).show();
            finish();
        } catch (NFCNotEnabled nfcnEn) {
            Toast.makeText(getApplicationContext(), "NFC Not Enabled", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        nfcmng.disableDispatch();
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            nfcmng.writeTag(tag, messaggio);
            setResult(1);
            finish();
        }


    public void onWrite() {
        if(str!=null){
            switch (federica) {
                case "Link Web":
                    Toast.makeText(getApplicationContext(),federica,Toast.LENGTH_SHORT).show();
                    messaggio = nfcmng.createTextMessage(str);

                    break;
                case "Simple Text":
                    messaggio = nfcmng.createUriMessage(str, "https://");
                    Toast.makeText(getApplicationContext(),federica,Toast.LENGTH_SHORT).show();
                    break;
            }}
        else Toast.makeText(getApplicationContext(),"ho",Toast.LENGTH_SHORT).show();

    }











}
