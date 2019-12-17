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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private TextView TextMessage;
    private TextInputLayout testo;
    private String str;
    private int checked;
    private CharSequence element[] = new CharSequence[]{"Link Web", "Simple Text"};
    private Bundle bundle;
    private Button writeNow;
    private BottomNavigationView navView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navView = findViewById(R.id.nav_view);
        TextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(this);
        loadFragments(new HomeFragment());
    }


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
        }
        return loadFragments(fragment);
    }


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


    public void Select(View v) {
        androidx.appcompat.app.AlertDialog.Builder buildino = new AlertDialog.Builder(MainActivity.this);
        buildino.setTitle("Choose the NFC data type:");
        buildino.setSingleChoiceItems(element, checked, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                TextView result = findViewById(R.id.selected);
                result.setText(element[i]);
                checked = i;
                writeNow = findViewById(R.id.writeNow);
                writeNow.setEnabled(true);
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialoghino = buildino.create();
        dialoghino.show();
    }


    public void writenow (View w){
        testo = findViewById(R.id.text);
        Intent i = new Intent(w.getContext(), Write_Activity.class);
            str=testo.getEditText().getText().toString();
            i.putExtra("data",str);
            i.putExtra("type", checked);
        startActivityForResult(i,1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == 1 && resultCode == Write_Activity.RESULT_OK) {
            Snackbar.make(findViewById(R.id.fragment_container) , "Written!",Snackbar.LENGTH_SHORT)
                    .setAnchorView(navView).show();
            }
        else if(requestCode == 1 && resultCode == Write_Activity.RESULT_CANCELED){
            Toast.makeText(getApplicationContext(),"Ops! Data Dimension Excedeed",Toast.LENGTH_SHORT).show();
        } else if(requestCode == 1 && resultCode == Write_Activity.RESULT_FIRST_USER){
            Toast.makeText(getApplicationContext(),"Ops! Something Goes Wrong",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        bundle = new ReadUtility().newRead(intent);
        if (bundle != null) {
            if (bundle.getString("Uri") != null) {
                Intent i = new Intent(this, WebView_Activity.class);
                i.putExtra("tagUriRead", bundle.getString("Uri"));
                startActivity(i);
            } else if (bundle.getString("Text") != null) {
                Intent i = new Intent(this, ReadText_Activity.class);
                i.putExtra("tagTextRead", bundle.getString("Text"));
                startActivity(i);
            } else {
                Toast.makeText(getApplicationContext(), bundle.getString("err"), Toast.LENGTH_SHORT).show();
            }
        }
    }
}








