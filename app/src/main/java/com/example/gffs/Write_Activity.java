package com.example.gffs;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
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
    private String data;
    private int checked;
    private WriteUtilities writeUt;
    private int result;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_);
        nfcmng = new NFCManager(this);
        writeUt = new WriteUtilities();
        data=getIntent().getExtras().getString("data");
        checked=getIntent().getExtras().getInt("type");
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
            setResult(2);
            finish();
        } catch (NFCNotEnabled nfcnEn) {
            Toast.makeText(getApplicationContext(), "NFC Not Enabled", Toast.LENGTH_SHORT).show();
            setResult(2);
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
            result = writeUt.writeTag(tag, messaggio);
        if (result == 0) {
            setResult(Activity.RESULT_OK);
            finish();
        }else if (result == 1){
            setResult(Activity.RESULT_CANCELED);
            finish();
        }else
            setResult(Activity.RESULT_FIRST_USER);
            finish();
    }


    public void onWrite() {
        if (data.isEmpty()) {
            Toast.makeText(getApplicationContext(), "No Text Inserted", Toast.LENGTH_SHORT).show();
            setResult(2);
            finish();
        } else {
            switch (checked) {
                case 1:
                    messaggio = writeUt.createTextMessage(data);
                    break;
                case 0:
                    messaggio = writeUt.createUriMessage(data, "https://");
                    break;
            }
        }
    }
}
