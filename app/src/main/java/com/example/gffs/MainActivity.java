package com.example.gffs;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private TextView TextMessage;
    private TextInputLayout testo;
    private String str;
    private int checked;
    private CharSequence element[] = new CharSequence[]{"Link Web", "Simple Text"};
    private Bundle bundle;



    /*
     *  Ad on creazione vengono inizialzzate ed associate una serie
     *  di variabili alle rispettive componenti grafiche e funzionali.
     *  Di default viene avviato il fragment principale.
     *
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        TextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(this);
        loadFragments(new HomeFragment());
    }


    /*
     * Gestisco il passaggio da un fragment ad un altro attraverso
     * la selezione di un elemento del botton navigation.
     */

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment=null;
        switch(item.getItemId()){
            case(R.id.navigation_home):
                fragment=new HomeFragment();
                TextMessage.setText(R.string.title_home);
                break;
            case(R.id.navigation_dashboard):
                checked=-1; //Utile alla creazione del dialog di scelta del data type
                fragment=new WriteFragment();
                TextMessage.setText(R.string.title_dashboard);
                break;
            case(R.id.navigation_notifications):
                fragment=new LastFragment();
                TextMessage.setText(R.string.title_notifications);
                break;
        }
        return loadFragments(fragment);
    }


    /*
     * Carico il fragment passato come argomento
     */

    private boolean loadFragments (Fragment fragment){
        if (fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();

            return true;
        }
        return false;
    }


    /*
     * Metodo on click per la clickable TextView di selezione
     * del data type. Sono offerte due scelte: link web o
     * semplice testo. La posssiblità di scelta avviene per
     * mezzo di un AlertDialog.
     */

    public void Select(View v) {
        androidx.appcompat.app.AlertDialog.Builder buildino = new AlertDialog.Builder(MainActivity.this);
        buildino.setTitle("Choose the NFC data type:");
        buildino.setSingleChoiceItems(element, checked, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                TextView result = findViewById(R.id.selected);
                result.setText(element[i]);
                checked=i;
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialoghino = buildino.create();
        dialoghino.show();
    }


    /*
     * Implemento il metodo onClick relativo al pulsante
     * di scrittura nel fragment Write. Viene avviata
     * l'activity di scrittura passando il testo scritto
     * dall'utente e il data l'info sul data type selezionato.
     * Todo
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++
     * if(Write_Activity.RESULT_OK==1){
            Snackbar.make(w,"Tag Written",Snackbar.LENGTH_SHORT).show();
        }
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++
     * Todo disabilitazione pulsante quando non ho info sul data type oppure sul dato stesso
     */

    public void writenow (View w){
        testo = findViewById(R.id.text);
        Intent i = new Intent(w.getContext(), Write_Activity.class);
            str=testo.getEditText().getText().toString();
            i.putExtra("data",str);
            i.putExtra("type", checked);
        startActivityForResult(i,1);
    }


    /*
     * Gestisco l'arrivo di un nuovo intent richiamando
     * la classe che gestisce la lettura del tag. In base
     * all'informazione ottenuta da quella classe sul tipo
     * del tag, decido se avviare l'activity di visualizzazione
     * testi opppure la WebView.
     */

    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        bundle= new ReadUtility().newRead(intent);
        if(bundle!=null){
            if(bundle.getString("Uri")!=null){
                Intent i = new Intent(this,WebView_Activity.class);
                i.putExtra("tagUriRead",bundle.getString("Uri"));
                startActivity(i);
            }else if(bundle.getString("Text")!=null) {
                Intent i = new Intent(this, ReadText_Activity.class);
                i.putExtra("tagTextRead", bundle.getString("Text"));
                startActivity(i);

            }
        }
    }
}








