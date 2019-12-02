package com.example.gffs;

import android.app.Activity;
import android.nfc.NfcAdapter;

public class NFCManager {
    private Activity activity;
    private NfcAdapter nfcAdpt;


    public NFCManager(Activity activity) {

        this.activity = activity;
    }


    /*
     * Permette la verifica della presenza e/o della
     * corretta abiliazione dell'Nfc sul dispostivo.
     */

    public void verifyNFC() throws NFCNotSupported, NFCNotEnabled {

        nfcAdpt = NfcAdapter.getDefaultAdapter(activity);

        if (nfcAdpt == null)
            throw new NFCNotSupported();

        if (!nfcAdpt.isEnabled())
            throw new NFCNotEnabled();

    }


    /*
     * Se lanciato, disabilita il dispatch.
     */
    public void disableDispatch(){
        if(nfcAdpt != null) //null point exception solution
            nfcAdpt.disableForegroundDispatch(activity);
    }

}
