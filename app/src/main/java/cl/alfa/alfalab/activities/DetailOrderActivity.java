package cl.alfa.alfalab.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import cl.alfa.alfalab.MainActivity;
import cl.alfa.alfalab.R;
import cl.alfa.alfalab.adapters.GenericAdapter;
import cl.alfa.alfalab.api.ApiClient;
import cl.alfa.alfalab.api.ApiService;
import cl.alfa.alfalab.interfaces.RecyclerViewOnClickListenerHack;
import cl.alfa.alfalab.models.Client;
import cl.alfa.alfalab.models.Order;
import cl.alfa.alfalab.models.Orders;
import cl.alfa.alfalab.utils.GenericViewHolder;
import cl.alfa.alfalab.utils.SharedPreferences;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailOrderActivity extends AppCompatActivity {

    private final Context context = this;
    private GenericAdapter<Order> adapter;
    private Client client;
    private Orders order;
    private ArrayList<Order> orderArrayList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent intent = getIntent();
        final Bundle data = intent.getExtras();
        order = data != null ? (Orders) data.getSerializable("data") : null;

        assert order != null;
        if(order.getOrderList() != null) {
            orderArrayList = order.getOrderList();
        }
        client = order.getClient();

        final SharedPreferences mSharedPreferences = new SharedPreferences(context);
        if(mSharedPreferences.loadNightModeState()) {
            setTheme(R.style.AppThemeDark);
        } else {
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.detail_order_activity_layout);

        final Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        final TextView clientFirstname = findViewById(R.id.client_firstname_text),
                clientLastname = findViewById(R.id.client_lastname_text),
                clientEmail = findViewById(R.id.client_email_text),
                clientInstagram = findViewById(R.id.client_instagram_text),
                totalPriceText = findViewById(R.id.total_price_text);
        final ExtendedFloatingActionButton button = findViewById(R.id.button);
        final RecyclerView recyclerView = findViewById(R.id.recycler_view);

        if(!order.getStatus()) {
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(view -> setDelivered(order));
        } else {
            button.setVisibility(View.GONE);
        }

        recyclerView.setHasFixedSize(true);
        adapter = new GenericAdapter<Order>() {
            @Override
            public RecyclerView.ViewHolder setViewHolder(ViewGroup parent, RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack) {
                return new GenericViewHolder(LayoutInflater.from(context).inflate(R.layout.item_order_card, parent, false), recyclerViewOnClickListenerHack);
            }

            @Override
            public void onBindData(RecyclerView.ViewHolder holder, Order val, int position) {
                final GenericViewHolder viewHolder = (GenericViewHolder) holder;
                final TextView orderType = viewHolder.get(R.id.order_type),
                        orderDigitalized = viewHolder.get(R.id.order_digitalized),
                        orderPrice = viewHolder.get(R.id.order_price);

                final TypedValue typedValue = new TypedValue();
                final Resources.Theme theme = context.getTheme();
                theme.resolveAttribute(R.attr.textColor, typedValue, true);
                @ColorInt final int textColor = typedValue.data;

                orderType.setText(Html.fromHtml(String.format(getResources().getString(R.string.order_type), textColor, capitalizeFirstLetter(val.getOrder_type()))));
                orderDigitalized.setText(Html.fromHtml(String.format(getResources().getString(R.string.order_quantity), textColor, val.isdigitized() ? "Si" : "No")));
                orderPrice.setText(Html.fromHtml(String.format(getResources().getString(R.string.order_price), textColor, val.getPrice())));
            }

            // TODO: SHOW ITEM INFO
            @Override
            public RecyclerViewOnClickListenerHack onGetRecyclerViewOnClickListenerHack() {
                return null;
            }

        };

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        adapter.addItems(orderArrayList);

        clientFirstname.setText(Html.fromHtml(capitalizeFirstLetter(order.getClient().getFirstname())));
        clientLastname.setText(capitalizeFirstLetter(order.getClient().getLastname()));
        clientEmail.setText(order.getClient().getEmail());
        clientInstagram.setText(order.getClient().getInstagram());
        totalPriceText.setText(String.valueOf(totalPrice()));

    }

    private int totalPrice() {
        int total = 0;
        for(int i = 0; i < adapter.getItemCount(); i++) {
            total += adapter.getItem(i).getPrice();
        }
        return total;
    }

    private void setDelivered(cl.alfa.alfalab.models.Orders order) {
        final Date date = new Date();
        @SuppressLint("SimpleDateFormat") String formattedDate = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(date);
        order.setDelivered_date(formattedDate);
        order.setStatus(true);
        final ApiService.SetDeliveredService service = ApiClient.getClient().create(ApiService.SetDeliveredService.class);
        final Call<Orders> responseCall = service.setDelivered(order);

        responseCall.enqueue(new Callback<Orders>() {
            @Override
            public void onResponse(@NonNull Call<cl.alfa.alfalab.models.Orders> call, @NonNull Response<Orders> response) {
                if(response.isSuccessful()) {
                    deleteOrder(order.getOrders_number());
                    Toast.makeText(context, "Pedido entregado", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(context, MainActivity.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                } else {
                    Log.e(MainActivity.API, "onResponse (errorBody): " + response.errorBody());
                    Log.e(MainActivity.API, "onResponse (message): " + response.message());
                    Toast.makeText(context, context.getResources().getString(R.string.check_internet_connection), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<cl.alfa.alfalab.models.Orders> call, @NonNull Throwable t) {
                Toast.makeText(context, context.getResources().getString(R.string.feed_update_error), Toast.LENGTH_SHORT).show();
                Log.d(MainActivity.API, "onFailure: " + t.getMessage());
            }
        });
    }

    private void deleteOrder(int number) {
        try {
            final ApiService.DeleteOrderService service = ApiClient.getClient().create(ApiService.DeleteOrderService.class);
            Call<ResponseBody> deleteRequest = service.deleteOrder(number);
            deleteRequest.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    if(response.isSuccessful()) {
                        Toast.makeText(context, "Pedido eliminado", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(context, MainActivity.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                    } else {
                        Log.e(MainActivity.API, "onResponse (errorBody): " + response.errorBody());
                        Log.e(MainActivity.API, "onResponse (message): " + response.message());
                        Toast.makeText(context, context.getResources().getString(R.string.check_internet_connection), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    Toast.makeText(context, "Error al finalizar pedido.", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Throwable err) {
            Log.e(MainActivity.API, Objects.requireNonNull(err.getMessage()));
        }
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(true);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        @SuppressLint("InflateParams") final View view  = getLayoutInflater().inflate(R.layout.confirmation_dialog_layout, null);
        dialog.setContentView(view);

        final ExtendedFloatingActionButton acceptButton;
        final TextView rejectButton, dialogTitle, dialogSubtitle;

        dialogTitle = view.findViewById(R.id.dialog_title);
        dialogTitle.setText(getResources().getString(R.string.delete_order_question));
        dialogSubtitle = view.findViewById(R.id.dialog_subtitle);
        dialogSubtitle.setText(getResources().getString(R.string.delete_order_warning));

        acceptButton = view.findViewById(R.id.confirm_button);
        acceptButton.setText(getResources().getString(R.string.positive_delete_dialog_button));
        acceptButton.setBackgroundColor(context.getResources().getColor(R.color.design_default_color_error));
        acceptButton.setOnClickListener(view1 -> {
            deleteOrder(order.getOrders_number());
            dialog.dismiss();
        });

        rejectButton = view.findViewById(R.id.negative_button);
        rejectButton.setText(getResources().getString(R.string.cancel_dialog_buttton));
        rejectButton.setOnClickListener(view12 -> dialog.dismiss());

        dialog.show();
    }

    @NonNull
    private String capitalizeFirstLetter(@NonNull String s) {
        final String[] in = s.split("^/([ ]?\\.?[a-zA-Z]+)+/$");
        final StringBuilder out = new StringBuilder();
        for (String ss : in) {
            String upperString = ss.substring(0, 1).toUpperCase() + ss.substring(1).toLowerCase();
            out.append(upperString);
            out.append(" ");
        }
        return out.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_order_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                startActivity(new Intent(context, MainActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                break;
            case R.id.action_edit_order:
                Intent intent = new Intent(context, CreateClientActivity.class);
                intent.putExtra("client", client);
                intent.putExtra("zone", order.getZone());
                intent.putExtra("data", order);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                return true;
            case R.id.action_delete_order:
                showDialog();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(context, MainActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
}
