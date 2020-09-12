package cl.alfa.alfalab.activities;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import cl.alfa.alfalab.R;
import cl.alfa.alfalab.adapters.GenericAdapter;
import cl.alfa.alfalab.interfaces.RecyclerViewOnClickListenerHack;
import cl.alfa.alfalab.models.ListItem;
import cl.alfa.alfalab.utils.GenericViewHolder;
import cl.alfa.alfalab.utils.SharedPreferences;

public class HelpActivity extends AppCompatActivity {

    private final Context context = this;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        final SharedPreferences mSharedPreferences = new SharedPreferences(context);
        if(mSharedPreferences.loadNightModeState())
            setTheme(R.style.AppThemeDark);
        else
            setTheme(R.style.AppTheme);
        setContentView(R.layout.help_activity_layout);

        final Toolbar mToolbar = findViewById(R.id.toolbar);
        final TextView toolbarTitle = mToolbar.findViewById(R.id.toolbar_title);
        final RecyclerView mRecyclerView = findViewById(R.id.recycler_view);

        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if(mSharedPreferences.loadNightModeState())
            mToolbar.setTitleTextAppearance(context, R.style.ToolbarTypefaceDark);
        else
            mToolbar.setTitleTextAppearance(context, R.style.ToolbarTypefaceLight);
        toolbarTitle.setText(R.string.help);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(true);
        GenericAdapter<ListItem> adapter = new GenericAdapter<ListItem>(helpListItems()) {
            @Override
            public RecyclerView.ViewHolder setViewHolder(ViewGroup parent, RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack) {
                return new GenericViewHolder(LayoutInflater.from(context).inflate(R.layout.expandable_list_item, parent, false), recyclerViewOnClickListenerHack);
            }

            @Override
            public void onBindData(RecyclerView.ViewHolder holder, ListItem val, int position) {
                final GenericViewHolder viewHolder = (GenericViewHolder) holder;
                final TextView title = viewHolder.get(R.id.item_title);
                title.setText(val.getTitle());
                final TextView subtitle = viewHolder.get(R.id.item_subtitle);
                subtitle.setText(val.getSubtitle());
                final ImageView anchor = viewHolder.get(R.id.anchor_dropdown);
                final boolean expanded = val.isExpanded();
                anchor.setImageDrawable(!expanded ? context.getDrawable(R.drawable.ic_arrow_down_24) : context.getDrawable(R.drawable.ic_arrow_up_24));
                final LinearLayout layout = viewHolder.get(R.id.sub_item);
                layout.setVisibility(expanded ? View.VISIBLE : View.GONE);
                final View divider = viewHolder.get(R.id.vw_divider);
                if (position == getItemCount() - 1)
                    divider.setVisibility(View.GONE);
                holder.itemView.setOnClickListener(v -> {
                    val.setExpanded(!expanded);
                    layout.setVisibility(expanded ? View.GONE : View.VISIBLE);
                    notifyItemChanged(position);
                });
            }

            @Override
            public RecyclerViewOnClickListenerHack onGetRecyclerViewOnClickListenerHack() {
                return null;
            }
        };
        final LinearLayoutManager aboutLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(aboutLayoutManager);
        mRecyclerView.setAdapter(adapter);

    }

    private ArrayList<ListItem> helpListItems(){
        final ArrayList<ListItem> list = new ArrayList<>();
        ListItem item = new ListItem();

        item.setTitle(context.getResources().getString(R.string.help_list_item_1_title));
        item.setSubtitle(context.getResources().getString(R.string.help_list_item_1_subtitle));
        list.add(item);

        item = new ListItem();
        item.setTitle(context.getResources().getString(R.string.help_list_item_2_title));
        item.setSubtitle(context.getResources().getString(R.string.help_list_item_2_subtitle));
        list.add(item);

        item = new ListItem();
        item.setTitle(context.getResources().getString(R.string.help_list_item_3_title));
        item.setSubtitle(context.getResources().getString(R.string.help_list_item_3_subtitle));
        list.add(item);

        item = new ListItem();
        item.setTitle(getResources().getString(R.string.help_list_item_4_title));
        item.setSubtitle(getResources().getString(R.string.help_list_item_4_subtitle));
        list.add(item);

        return list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if (item.getItemId() == android.R.id.home){
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
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