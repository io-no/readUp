package com.example.gffs;

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
import static com.example.gffs.ReadUtility.foregroundDispatch;
import static com.example.gffs.ReadUtility.stopForegroundDispatch;

public class HomeFragment extends Fragment {
    private NFCManager nfcManager;
    private NfcAdapter nfcAdpt;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        nfcManager = new NFCManager(this.getActivity());
        return inflater.inflate(R.layout.home_fragment, null);
    }


    @Override
    public void onResume(){
        super.onResume();
        try {
            nfcManager.verifyNFC();
            Intent nfcIntent = new Intent(this.getActivity(), getClass());
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


    @Override
    public void onPause() {
        super.onPause();
        nfcAdpt = NfcAdapter.getDefaultAdapter(this.getActivity());
        nfcManager.disableDispatch();
        stopForegroundDispatch(this.getActivity(),nfcAdpt);
    }
}



