package cl.alfa.alfalab.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

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

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        mSharedPreferences = new SharedPreferences(context);
        if(mSharedPreferences.loadNightModeState()) {
            setTheme(R.style.AppThemeDark);
        } else {
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.settings_activity_layout);

        final Toolbar mToolbar = findViewById(R.id.toolbar);
        final TextView toolbarTitle = mToolbar.findViewById(R.id.toolbar_title),
                profileName = findViewById(R.id.profile_text);
        final Switch mDarkSwitch = findViewById(R.id.settings_dark_theme_switch),
                mReportSwitch = findViewById(R.id.send_report_switch);
        final RecyclerView helpRecyclerView = findViewById(R.id.help_recycler_view);

        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if(mSharedPreferences.loadNightModeState()) {
            mToolbar.setTitleTextAppearance(context, R.style.ToolbarTypefaceDark);
        } else {
            mToolbar.setTitleTextAppearance(context, R.style.ToolbarTypefaceLight);
        }
        toolbarTitle.setText(R.string.action_settings);

        profileName.setText(mSharedPreferences.getResponsible());

        if(mSharedPreferences.loadNightModeState()) {
            mDarkSwitch.setChecked(true);
        }
        mDarkSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(!isChecked) {
                mSharedPreferences.setNightMode(false);
            } else {
                mSharedPreferences.setNightMode(true);
            }
            startActivity(new Intent(context, SettingsActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });

        if(mSharedPreferences.sendReport()) {
            mReportSwitch.setChecked(true);
        }
        mReportSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(!isChecked){
                mSharedPreferences.sendReports(false);
            }
            else {
                mSharedPreferences.sendReports(true);
            }
            startActivity(new Intent(context, SettingsActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });

        final LinearLayout profileLayout = findViewById(R.id.profile_layout);
        profileLayout.setOnClickListener(view -> {
            startActivity(new Intent(context, ProfileDetailActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });

        helpRecyclerView.setHasFixedSize(false);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        helpRecyclerView.setLayoutManager(linearLayoutManager);
        helpRecyclerView.setNestedScrollingEnabled(false);
        final GenericAdapter<ListItem> helpAdapter = new GenericAdapter<ListItem>(getHelpItems()) {
            @Override
            public RecyclerView.ViewHolder setViewHolder(ViewGroup parent, RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack) {
                return new GenericViewHolder(LayoutInflater.from(context).inflate(R.layout.settings_list_item, parent, false), recyclerViewOnClickListenerHack);
            }

            @Override
            public void onBindData(RecyclerView.ViewHolder holder, ListItem val, int position) {
                final GenericViewHolder myViewHolder = (GenericViewHolder) holder;
                final TextView title = myViewHolder.get(R.id.list_item_title);
                title.setText(val.getTitle());
                final TextView subtitle = myViewHolder.get(R.id.list_item_subtitle);
                if(val.getSubtitle() != null) {
                    subtitle.setText(val.getSubtitle());
                } else {
                    subtitle.setVisibility(View.GONE);
                }
            }

            @Override
            public RecyclerViewOnClickListenerHack onGetRecyclerViewOnClickListenerHack() {
                return new RecyclerViewOnClickListenerHack() {
                    @Override
                    public void onClickListener(View view, int position) {
                        switch (position) {
                            case 0:
                                startActivity(new Intent(context, ReportActivity.class));
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                break;
                            case 1:
                                startActivity(new Intent(context, ThirdPartyLibrariesActivity.class));
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                break;
                            case 2:
                                startActivity(new Intent(context, HelpActivity.class));
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                break;
                            case 3:
                                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://github.com/AUbillaOliva/Alfalab")));
                                break;
                        }
                    }
                    @Override
                    public void onLongPressClickListener(View view, int position) {}
                };
            }
        };
        helpRecyclerView.setAdapter(helpAdapter);

    }

    private ArrayList<ListItem> getHelpItems() {
        final ArrayList<ListItem> mList = new ArrayList<>();

        ListItem item = new ListItem();
        item.setTitle("Reportar un error");
        item.setSubtitle("Informanos sobre algun problema que tengas con la App.");
        mList.add(item);

        item = new ListItem();
        item.setTitle("Libreria de terceros");
        item.setSubtitle("Con la ayuda de este genial software!");
        mList.add(item);

        item = new ListItem();
        item.setTitle("Ayuda");
        item.setSubtitle("Estamos aqui para ayudarte.");
        mList.add(item);

        item = new ListItem();
        item.setTitle("Más información");
        item.setSubtitle("Visita el repositorio del proyecto.");
        item.setExpanded(true);
        mList.add(item);

        return mList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(context, MainActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(context, MainActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        final Configuration override = new Configuration(newBase.getResources().getConfiguration());
        override.fontScale = 1.0f;
        applyOverrideConfiguration(override);
    }
    
}
