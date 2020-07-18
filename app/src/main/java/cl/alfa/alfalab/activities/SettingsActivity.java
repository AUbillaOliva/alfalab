package cl.alfa.alfalab.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cl.alfa.alfalab.MainActivity;
import cl.alfa.alfalab.R;
import cl.alfa.alfalab.adapters.GenericAdapter;
import cl.alfa.alfalab.interfaces.RecyclerViewOnClickListenerHack;
import cl.alfa.alfalab.models.ListItem;
import cl.alfa.alfalab.utils.GenericViewHolder;
import cl.alfa.alfalab.utils.SharedPreferences;

public class SettingsActivity extends AppCompatActivity {
    private final Context context = this;
    private SharedPreferences mSharedPreferences;

    private RecyclerView storageList, searchList, aboutList;
    private GenericAdapter<ListItem> storageAdapter, searchAdapter, aboutAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        mSharedPreferences = new SharedPreferences(context);
        if(mSharedPreferences.loadNightModeState())
            setTheme(R.style.AppThemeDark);
        else
            setTheme(R.style.AppTheme);
        setContentView(R.layout.settings_activity);

        final Toolbar mToolbar = findViewById(R.id.toolbar);
        final TextView toolbarTitle = mToolbar.findViewById(R.id.toolbar_title);
        final Switch mDarkSwitch = findViewById(R.id.settings_dark_theme_switch);

        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if(mSharedPreferences.loadNightModeState())
            mToolbar.setTitleTextAppearance(context, R.style.ToolbarTypefaceDark);
        else
            mToolbar.setTitleTextAppearance(context, R.style.ToolbarTypefaceLight);

        toolbarTitle.setText(R.string.action_settings);

        if(mSharedPreferences.loadNightModeState())
            mDarkSwitch.setChecked(true);
        mDarkSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(!isChecked)
                mSharedPreferences.setNightMode(false);
            else
                mSharedPreferences.setNightMode(true);

            startActivity(new Intent(context, SettingsActivity.class));
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //replace with switch if has more items
        if (item.getItemId() == android.R.id.home){
            startActivity(new Intent(context, MainActivity.class));
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        startActivity(new Intent(context, MainActivity.class));
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void attachBaseContext(Context newBase){
        super.attachBaseContext(newBase);
        final Configuration override = new Configuration(newBase.getResources().getConfiguration());
        override.fontScale = 1.0f;
        applyOverrideConfiguration(override);
    }
}
