package com.example.gffs;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.util.Log;
import com.google.common.primitives.Bytes;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class ReadUtility  {
    private Bundle bundle;



    public Bundle newRead(Intent intent) {
        bundle = new Bundle();
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                return startRead(tag);
        }
        bundle.putString("err","Unsupported Type");
        return bundle;
    }


    protected Bundle startRead(Tag... params) {
        Tag tag = params[0];
        Ndef ndef = Ndef.get(tag);
        if (ndef == null) {
            bundle.putString("err","NDEF not supported");
        }
        NdefMessage ndefMessage = ndef.getCachedNdefMessage();
        NdefRecord[] records = ndefMessage.getRecords();
        for (NdefRecord ndefRecord : records) {
            if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                try {
                    bundle.putString("Text",readText(ndefRecord));
                    return bundle;

                } catch (UnsupportedEncodingException e) {
                    Log.e(TAG, "Unsupported Encoding", e);
                    bundle.putString("err","Unsupported Encoding");
                }
            } else if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_URI)) {
                try {
                    bundle.putString("Uri", readUri(ndefRecord));
                   return bundle;
                } catch (UnknownError e) {
                    bundle.putString("err","Unsupported Encoding");
                    Log.e(TAG, "Unsupported Encoding", e);
                }
            }
        }
        bundle.putString("err","Unsupported Type");
        return bundle;
    }


    private String readText(NdefRecord record) throws UnsupportedEncodingException {
        byte[] loadino = record.getPayload();
        String textEncoding = ((loadino[0] & 128) == 0) ? "UTF-8" : "UTF-16";
        int languageCodeLength = loadino[0] & 0063;
        return new String(loadino, languageCodeLength + 1, loadino.length - languageCodeLength - 1, textEncoding);
    }


    private String readUri(NdefRecord record) throws UnknownError {
        byte[] payload = record.getPayload();
        String prefix = "https://";
        byte[] fullUri =
                Bytes.concat(prefix.getBytes(Charset.forName("UTF-8")), Arrays.copyOfRange(payload, 1,
                        payload.length));
        return new String(fullUri, Charset.forName("UTF-8"));
    }


    public static void foregroundDispatch(Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);
        IntentFilter[] filtrello = new IntentFilter[2];
        String[][] techList = new String[][]{};
        filtrello[0] = new IntentFilter();
        filtrello[1] = new IntentFilter();
        filtrello[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filtrello[0].addCategory(Intent.CATEGORY_DEFAULT);
        filtrello[1].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filtrello[1].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filtrello[0].addDataType("text/plain");
            filtrello[1].addDataScheme("https");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("Wrong data type");
        }
        adapter.enableForegroundDispatch(activity, pendingIntent, filtrello, techList);
    }


    public static void stopForegroundDispatch(Activity activity, NfcAdapter adapter) {
        if(adapter!=null){
            adapter.disableForegroundDispatch(activity);
        }
    }
}
