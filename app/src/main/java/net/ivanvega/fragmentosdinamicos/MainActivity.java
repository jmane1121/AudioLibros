package net.ivanvega.fragmentosdinamicos;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.contenedor_pequeno) != null && getSupportFragmentManager().findFragmentById(R.id.contenedor_pequeno) == null
        ) {
            getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).add(R.id.contenedor_pequeno, SelectorFragment.class, null).commit();
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
               irUltimoVisitado();
            }
        });

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        System.out.println("Todos");
                        break;
                    case 1:
                        System.out.println("Nuevos");
                        break;
                    case 2:
                        System.out.println("Leidos");
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }
            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

    }

    public void mostrarDetalle(int id) {
        DetalleFragment detalleFragment = (DetalleFragment) getSupportFragmentManager().findFragmentById(R.id.detalle_fragment);
        if (detalleFragment != null) {
            detalleFragment.setInfoLibro(id);
        } else {
            detalleFragment = new DetalleFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(DetalleFragment.ARG_INDEX_LIBRO, id);
            detalleFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.contenedor_pequeno, detalleFragment).addToBackStack(null).commit();
        }
        SharedPreferences pref = getSharedPreferences("com.example.audiolibros_internal", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("ultimo", id);
        editor.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_preferencias:
                Toast.makeText(this,"Preferencias", Toast.LENGTH_LONG).show();
                break;
            case R.id.menu_acerca:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Acerca De");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.create().show();
                break;
            case R.id.menu_ultimo:
                irUltimoVisitado();
                break;
        }
        return false;
    }

    public void irUltimoVisitado() {
        SharedPreferences pref = getSharedPreferences("com.example.audiolibros_internal", MODE_PRIVATE);
        int id = pref.getInt("ultimo", -1);
        if (id >= 0) {
            mostrarDetalle(id);
        } else {
            Toast.makeText(this,"Sin última vista",Toast.LENGTH_LONG).show();
        }
    }
}