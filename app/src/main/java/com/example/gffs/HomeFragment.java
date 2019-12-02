package com.example.gffs;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class HomeFragment extends Fragment {
    private NFCManager nfcManager;
    private NfcAdapter nfcAdpt;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        nfcManager = new NFCManager(this.getActivity());
        return inflater.inflate(R.layout.home_fragment, null);
    }


    /*
     * Ad ogni ripresa dell' esecuzione del fragment, si verifica
     * la presenza e l'attivazione dell'NFC, viene inizializzato
     * l'intent filter e abilitato il dispatch (dettagli nel
     * commento relativo)
    */

    @Override
    public void onResume(){
        super.onResume();
        try {
            nfcManager.verifyNFC();
            Intent nfcIntent = new Intent(this.getActivity(), getClass());
            //Non deve essere lanciata se già in esecuzione
            nfcIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent =
                    PendingIntent.getActivity(this.getContext(), 0, nfcIntent, 0);
            IntentFilter[] intentFiltersArray = new IntentFilter[]{};
            String[][] techList = new String[][]{
                    {android.nfc.tech.Ndef.class.getName()},
                    {android.nfc.tech.NdefFormatable.class.getName()}
            };
            nfcAdpt = NfcAdapter.getDefaultAdapter(this.getActivity());
            nfcAdpt.enableForegroundDispatch(this.getActivity(), pendingIntent,
                    intentFiltersArray, techList);
            foregroundDispatch(this.getActivity(),nfcAdpt);
        } catch (NFCNotEnabled nope){
            Toast.makeText(this.getContext(), "NFC Not Enabled", Toast.LENGTH_SHORT).show();
        } catch(NFCNotSupported nopino){
            Toast.makeText(this.getContext(), "NFC Not Supported", Toast.LENGTH_SHORT).show();
        }
    }


    /*
     * Ad ogni interruzione dell' esecuzione del fragment,
     * viene disabilitato il dispatch (dettagli nel commento
     * relativo).
     */

    @Override
    public void onPause() {
        super.onPause();
        nfcAdpt = NfcAdapter.getDefaultAdapter(this.getActivity());
        nfcManager.disableDispatch();
        stopForegroundDispatch(this.getActivity(), nfcAdpt);
    }


    /*
     * Implemento e gestisco l'abilitazione del dispatch.
     * Specifico gli Itent che voglio siano gestiti in foreground.
     * Nel caso specifico, saranno previste due tipologie di Intent
     * entrambe associate ad  un'azione di tipo NDEF_DISCOVERED,
     * appartententi alla categoria CATEGORY_DEFAULT e che gestiscono
     * il tipo "text/plain" oppure lo schema "https". Coincidono
     * esattamente con le specifiche indicate nel Manifest.
     */

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


    /*
     * Implemento la disabilitazione del dispatch in foreground
     */

    public static void stopForegroundDispatch(Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }

    }



