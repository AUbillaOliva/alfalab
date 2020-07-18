package cl.alfa.alfalab.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Date;
import java.util.Objects;

import androidx.annotation.LongDef;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import cl.alfa.alfalab.MainActivity;
import cl.alfa.alfalab.MainApplication;
import cl.alfa.alfalab.R;
import cl.alfa.alfalab.api.ApiClient;
import cl.alfa.alfalab.api.ApiService;
import cl.alfa.alfalab.models.Client;
import cl.alfa.alfalab.models.Orders;
import cl.alfa.alfalab.utils.SharedPreferences;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateOrderActivity extends AppCompatActivity {

    private Context context = MainApplication.getContext();

    private TextInputLayout firstnameInputLayout, lastnameInputLayout, emailInputLayout, typeInputLayout, instagramLayout, commentariesLayout, priceLayout, responsibleLayout;
    private TextInputEditText firstnameInputEditText, lastnameInputEditText, emailInputEditText, instagramInputEditText, commentariesInputEditText, priceInputEditText, responsibleEditText;

    private ProgressDialog progressDialog;

    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent orderIntent = getIntent();
        Bundle order = orderIntent.getExtras();
        assert order != null;
        cl.alfa.alfalab.models.Orders ord = (Orders) order.get("order");
        Client client = ord.getClient();

        mSharedPreferences = new SharedPreferences(context);
        if(mSharedPreferences.loadNightModeState())
            setTheme(R.style.AppThemeDark);
        else
            setTheme(R.style.AppTheme);
        setContentView(R.layout.update_order_activity_layout);

        final Toolbar mToolbar = findViewById(R.id.toolbar);
        final TextView toolbarTitle = mToolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if(mSharedPreferences.loadNightModeState())
            mToolbar.setTitleTextAppearance(context, R.style.ToolbarTypefaceDark);
        else
            mToolbar.setTitleTextAppearance(context, R.style.ToolbarTypefaceLight);
        toolbarTitle.setText(R.string.update_order);

        firstnameInputLayout = findViewById(R.id.client_firstname_input_layout);
        lastnameInputLayout = findViewById(R.id.client_lastname_input_layout);
        emailInputLayout = findViewById(R.id.client_order_email);
        instagramLayout = findViewById(R.id.client_instagram_input_layout);
        typeInputLayout = findViewById(R.id.client_order_type);
        responsibleLayout = findViewById(R.id.order_responsible_input_layout);
        commentariesLayout = findViewById(R.id.order_commentaries_input_layout);
        priceLayout = findViewById(R.id.order_price_input_layout);

        final ExtendedFloatingActionButton button = findViewById(R.id.button);

        firstnameInputEditText = firstnameInputLayout.findViewById(R.id.client_firstname_input_edit_text);
        firstnameInputEditText.setHint(Html.fromHtml(getString(R.string.client_firstname)));
        firstnameInputEditText.setText(client.getFirstname());
        lastnameInputEditText = lastnameInputLayout.findViewById(R.id.client_lastname_input_edit_text);
        lastnameInputEditText.setHint(Html.fromHtml(getString(R.string.client_lastname)));
        lastnameInputEditText.setText(client.getLastname());
        emailInputEditText = emailInputLayout.findViewById(R.id.client_email_input_edit_text);
        emailInputEditText.setHint(Html.fromHtml(getString(R.string.client_email)));
        emailInputEditText.setText(client.getEmail());
        instagramInputEditText = instagramLayout.findViewById(R.id.client_instagram_input_edit_text);
        instagramInputEditText.setText(client.getInstagram());
        responsibleEditText = findViewById(R.id.order_responsible_input_edit_text);
        responsibleEditText.setText(mSharedPreferences.getResponsible());
        commentariesInputEditText = commentariesLayout.findViewById(R.id.order_commentaries_input_edit_text);
        commentariesInputEditText.setText(ord.getCommentaries());
        priceInputEditText = priceLayout.findViewById(R.id.order_price_input_edit_text);
        priceInputEditText.setHint(Html.fromHtml(getString(R.string.order_price)));
        priceInputEditText.setText(String.valueOf(ord.getPrice()));

        firstnameInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() < 1) firstnameInputLayout.setError(getResources().getString(R.string.required));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() < 1 ) firstnameInputLayout.setError(getResources().getString(R.string.required));
            }
        });
        lastnameInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() < 1) lastnameInputLayout.setError(getResources().getString(R.string.required));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() < 1) lastnameInputLayout.setError(getResources().getString(R.string.required));
            }
        });
        emailInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() < 1) emailInputLayout.setError(getResources().getString(R.string.required));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() < 1) emailInputLayout.setError(getResources().getString(R.string.required));
            }
        });
        priceInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() < 1) priceLayout.setError(getResources().getString(R.string.required));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() < 1) priceLayout.setError(getResources().getString(R.string.required));
            }
        });

        button.setOnClickListener(view -> {
            progressDialog = ProgressDialog.show(this, "Creando orden...",
                    "Creando. Porfavor espera...", true);
            updateOrder(ord, client);
        });

    }

    private void updateOrder(Orders order, Client client){
        if(priceInputEditText.getText() != null)
            order.setPrice(Integer.parseInt(String.valueOf(priceInputEditText.getEditableText())));
        if(firstnameInputEditText.getEditableText().length() != 0 || firstnameInputEditText.getEditableText().toString().equals(""))
            client.setFirstname(firstnameInputEditText.getEditableText().toString());
        if(Objects.requireNonNull(lastnameInputEditText.getEditableText()).toString().length() != 0 || lastnameInputEditText.getEditableText().toString().equals(""))
            client.setLastname(lastnameInputEditText.getEditableText().toString());
        if(Objects.requireNonNull(emailInputEditText.getEditableText()).toString().length() != 0 || emailInputEditText.getEditableText().toString().equals(""))
            client.setEmail(emailInputEditText.getEditableText().toString());
        client.setInstagram(Objects.requireNonNull(instagramInputEditText.getEditableText()).toString());
        order.setClient(client);
        order.setCommentaries(Objects.requireNonNull(commentariesInputEditText.getEditableText()).toString());
        order.setStatus(order.getStatus());
        order.setResponsible(mSharedPreferences.getResponsible());
        order.setNumber(order.getNumber());
        order.setType(order.getType());
        final Date date = new Date();
        long time = date.getTime();
        java.sql.Timestamp timestamp = new java.sql.Timestamp(time);
        order.setCheckout(timestamp.toString());
        order.setCheckin(order.getCheckin());
        try {
            final ApiService.UpdateOrderService service = ApiClient.getClient().create(ApiService.UpdateOrderService.class);
            Call<ResponseBody> deleteRequest = service.updateOrder(order.getNumber(), order);
            deleteRequest.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Toast.makeText(context, "onResponse", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Log.d(MainActivity.TAG, response.toString());
                    Log.d(MainActivity.TAG, response.message());
                    if(response.code() == 200){
                        Toast.makeText(context, "Pedido actualizado", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(context, MainActivity.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(context, "onFailure", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        } catch (Throwable err){
            Log.e(MainActivity.TAG, Objects.requireNonNull(err.getMessage()));
        }
    }

}
