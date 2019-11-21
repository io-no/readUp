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
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private TextView TextMessage;
    private TextInputLayout testo;
    private String str;
    private String federica;
    private TextView select;


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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
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

    private CharSequence element[] = new CharSequence[]{"Link Web", "Simple Text"};

    public void Select(View v) {
        androidx.appcompat.app.AlertDialog.Builder buildino = new AlertDialog.Builder(MainActivity.this);
        buildino.setTitle("Choose the NFC data type:");
        buildino.setSingleChoiceItems(element, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                TextView result = findViewById(R.id.selected);
                result.setText(element[i]);
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialoghino = buildino.create();
        dialoghino.show();
    }

    public void writenow (View w){
        testo = findViewById(R.id.text);
        select = findViewById(R.id.selected);
        Intent i = new Intent(w.getContext(), Write_Activity.class);
            str=testo.getEditText().toString();
            i.putExtra("word",str);
            federica=select.getText().toString();
            i.putExtra("wordina", federica);
        startActivityForResult(i,1);
        if(Write_Activity.RESULT_OK==1){
            Snackbar.make(w,"Tag Written",Snackbar.LENGTH_SHORT).show();
        }
    }
}





