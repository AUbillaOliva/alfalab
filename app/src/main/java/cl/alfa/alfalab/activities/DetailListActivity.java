package cl.alfa.alfalab.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import cl.alfa.alfalab.MainApplication;
import cl.alfa.alfalab.R;
import cl.alfa.alfalab.models.Orders;
import cl.alfa.alfalab.utils.SharedPreferences;

public class DetailListActivity extends AppCompatActivity {

    private Context context = MainApplication.getContext();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent intent = getIntent();
        final Bundle data = intent.getExtras();
        final Orders order = data != null ? (Orders) data.getSerializable("data") : null;

        final SharedPreferences mSharedPreferences = new SharedPreferences(context);
        if(mSharedPreferences.loadNightModeState())
            setTheme(R.style.AppThemeDark);
        else
            setTheme(R.style.AppTheme);
        setContentView(R.layout.detail_order_activity_layout);


    }
}
