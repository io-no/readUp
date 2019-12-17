package com.example.gffs;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import java.io.ByteArrayOutputStream;
import java.util.Locale;

public class WriteUtilities {
    public int writeTag(Tag tag, NdefMessage message) {
        if (tag != null) {
            try {
                Ndef ndefTag = Ndef.get(tag);
                    if (ndefTag == null) {
                        NdefFormatable nForm = NdefFormatable.get(tag);
                        if (nForm != null) {
                            nForm.connect();
                            nForm.format(message);
                            nForm.close();
                            return 0;
                        }
                    } else if (ndefTag.getMaxSize()>=message.getByteArrayLength()) {
                        ndefTag.connect();
                        ndefTag.writeNdefMessage(message);
                        ndefTag.close();
                        return 0;
                    }
               else return 1;
            } catch (Exception e) {
                e.printStackTrace();
                return 2;
            }
        }
        return 2;
    }


    public NdefMessage createUriMessage(String content, String type) {
        NdefRecord record = NdefRecord.createUri(type + content);
        NdefMessage msg = new NdefMessage(new NdefRecord[]{record});
        return msg;
    }


    public NdefMessage createTextMessage(String content) {
        try {
            byte[] lang = Locale.getDefault().getLanguage().getBytes("UTF-8");
            byte[] text = content.getBytes("UTF-8");
            int langSize = lang.length;
            int textLength = text.length;
            ByteArrayOutputStream payload = new ByteArrayOutputStream(1 + langSize + textLength);
            payload.write((byte) (langSize & 0x1F));
            payload.write(lang, 0, langSize);
            payload.write(text, 0, textLength);
            NdefRecord record = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payload.toByteArray());
            return new NdefMessage(new NdefRecord[]{record});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
