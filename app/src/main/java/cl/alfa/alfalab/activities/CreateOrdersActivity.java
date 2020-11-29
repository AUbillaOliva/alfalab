package cl.alfa.alfalab.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import cl.alfa.alfalab.MainActivity;
import cl.alfa.alfalab.R;
import cl.alfa.alfalab.adapters.GenericAdapter;
import cl.alfa.alfalab.api.ApiClient;
import cl.alfa.alfalab.api.ApiService;
import cl.alfa.alfalab.fragments.CustomBottomSheetDialogFragment;
import cl.alfa.alfalab.interfaces.RecyclerViewOnClickListenerHack;
import cl.alfa.alfalab.models.Client;
import cl.alfa.alfalab.models.Order;
import cl.alfa.alfalab.models.Orders;
import cl.alfa.alfalab.utils.GenericViewHolder;
import cl.alfa.alfalab.utils.SharedPreferences;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateOrdersActivity extends AppCompatActivity implements CustomBottomSheetDialogFragment.OnCreateOrderListener, CustomBottomSheetDialogFragment.OnEditOrderListener {

    private final Context context = this;
    private SharedPreferences mSharedPreferences;
    private final ArrayList<Order> orders = new ArrayList<>();
    private Client client;
    private Orders data;
    private String orderZone;
    private TextView totalPriceText;
    private CustomBottomSheetDialogFragment fragObj;
    private ProgressDialog progressDialog;
    private GenericAdapter<Order> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = (Client) getIntent().getSerializableExtra("client");
        orderZone = getIntent().getStringExtra("zone");
        data = (Orders) getIntent().getSerializableExtra("data");
        if(data != null) {
            orders.addAll(data.getOrderList());
        }

        final Bundle bundle = new Bundle();
        fragObj = new CustomBottomSheetDialogFragment();

        mSharedPreferences = new SharedPreferences(context);
        if(mSharedPreferences.loadNightModeState()) {
            setTheme(R.style.AppThemeDark);
        } else {
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.create_order_activity_layout);

        final Toolbar mToolbar = findViewById(R.id.toolbar);
        final TextView toolbarTitle = mToolbar.findViewById(R.id.toolbar_title);
        final TextView firstname = findViewById(R.id.client_firstname_text),
                lastname = findViewById(R.id.client_lastname_text),
                email = findViewById(R.id.client_email_text),
                instagram = findViewById(R.id.client_instagram_text);
                totalPriceText = findViewById(R.id.total_price_text);
        final ExtendedFloatingActionButton createExtendedFloatinButton = findViewById(R.id.button);
        final RecyclerView mRecyclerView = findViewById(R.id.recycler_view);

        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if(mSharedPreferences.loadNightModeState()) {
            mToolbar.setTitleTextAppearance(context, R.style.ToolbarTypefaceDark);
        } else {
            mToolbar.setTitleTextAppearance(context, R.style.ToolbarTypefaceLight);
        }
        if(data != null) {
            toolbarTitle.setText(context.getResources().getString(R.string.update_order));
        } else {
            toolbarTitle.setText(R.string.create_order);
        }

        mRecyclerView.setHasFixedSize(true);
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
                final ImageView menu = viewHolder.get(R.id.more_icon);

                final TypedValue typedValue = new TypedValue();
                final Resources.Theme theme = context.getTheme();
                theme.resolveAttribute(R.attr.textColor, typedValue, true);
                @ColorInt final int textColor = typedValue.data;

                orderType.setText(Html.fromHtml(String.format(getResources().getString(R.string.order_type), textColor, val.getOrder_type())));
                orderDigitalized.setText(Html.fromHtml(String.format(getResources().getString(R.string.order_quantity), textColor, val.isdigitized() ? "Si" : "No")));
                orderPrice.setText(Html.fromHtml(String.format(getResources().getString(R.string.order_price), textColor, val.getPrice())));
                try {
                    menu.setOnClickListener(v -> {
                        final PopupMenu popup = new PopupMenu(context,v);
                        popup.getMenuInflater().inflate(R.menu.order_menu,popup.getMenu());
                        popup.show();
                        popup.setOnMenuItemClickListener(item -> {
                            switch (item.getItemId()) {
                                case R.id.edit:
                                    bundle.putSerializable("order", val);
                                    bundle.putBoolean("update", true);
                                    fragObj.setArguments(bundle);
                                    fragObj.show(getSupportFragmentManager(), "Dialog");
                                    break;
                                case R.id.delete:
                                    adapter.deleteItem(val);
                                    adapter.notifyDataSetChanged();
                                    break;
                                default:
                                    break;
                            }
                            return true;
                        });
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public RecyclerViewOnClickListenerHack onGetRecyclerViewOnClickListenerHack() {
                return new RecyclerViewOnClickListenerHack() {
                    @Override
                    public void onClickListener(View view, int position) {}

                    @Override
                    public void onLongPressClickListener(View view, int position) {
                        try {
                            final PopupMenu popup = new PopupMenu(context, view);
                            popup.getMenuInflater().inflate(R.menu.order_menu, popup.getMenu());
                            popup.show();
                            popup.setOnMenuItemClickListener(item -> {
                                switch (item.getItemId()) {
                                    case R.id.edit:
                                        bundle.putSerializable("order", adapter.getItem(position));
                                        bundle.putBoolean("update", true);
                                        fragObj.setArguments(bundle);
                                        fragObj.show(getSupportFragmentManager(), "Dialog");
                                        break;
                                    case R.id.delete:
                                        adapter.deleteItem(adapter.getItem(position));
                                        adapter.notifyDataSetChanged();
                                        break;
                                    default:
                                        break;
                                }
                                return true;
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
            }
        };

        if(data != null) {
            Log.e(MainActivity.API, "order number: " + data.getOrders_number());
            adapter.addItems(data.getOrderList());
            adapter.notifyDataSetChanged();
        }

        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setNestedScrollingEnabled(false);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(layoutManager);

        assert client != null;
        firstname.setText(client.getFirstname());
        lastname.setText(client.getLastname());
        email.setText(client.getEmail());
        instagram.setText(client.getInstagram());
        totalPriceText.setText(String.valueOf(totalPrice()));

        createExtendedFloatinButton.setOnClickListener(view1 -> new CustomBottomSheetDialogFragment().show(getSupportFragmentManager(), "Dialog"));
    }

    private void updateOrder(Orders order, ArrayList<Order> orderList) {
        progressDialog = ProgressDialog.show(context, "🙌 Actualizando pedido...",  "El pedido se está actualizando, espera.", true);

        order.setOrderList(orderList);
        order.setOrders_number(order.getOrders_number());
        final ApiService.UpdateOrderService service = ApiClient.getClient().create(ApiService.UpdateOrderService.class);
        final Call<Orders> responseCall = service.updateOrder(order.getOrders_number(), order);

        responseCall.enqueue(new Callback<Orders>() {
            @Override
            public void onResponse(@NonNull Call<cl.alfa.alfalab.models.Orders> call, @NonNull Response<Orders> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()) {
                    Toast.makeText(context, "Pedido actualizado!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(context, MainActivity.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                } else {
                    Log.e(MainActivity.API, "onResponse (errorBody): " + response.errorBody());
                    Log.e(MainActivity.API, "onResponse (response.raw): " + response.raw().toString());
                    Log.e(MainActivity.API, "onResponse (message): " + response.message());
                    Toast.makeText(context, context.getResources().getString(R.string.check_internet_connection), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<cl.alfa.alfalab.models.Orders> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, context.getResources().getString(R.string.feed_update_error), Toast.LENGTH_SHORT).show();
                Log.d(MainActivity.API, "onFailure: " + t.getMessage());
            }
        });
    }

    private void createOrder(ArrayList<Order> orderList) {
        progressDialog = ProgressDialog.show(context, "🙌 Creando pedido...", "El pedido se está creando, espera.", true);

        final Orders order = new Orders();
        order.setOrderList(orderList);
        assert client != null;
        order.setClient(client);

        final Date date = new Date();
        @SuppressLint("SimpleDateFormat") final String formattedDate = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(date);
        order.setCreated_at(formattedDate);
        order.setZone(orderZone);
        order.setStatus(false);
        final ApiService.PostOrderService service = ApiClient.getClient().create(ApiService.PostOrderService.class);
        final Call<Orders> responseCall = service.postOrder(order, mSharedPreferences.getToken());


        responseCall.enqueue(new Callback<Orders>() {
            @Override
            public void onResponse(@NonNull Call<cl.alfa.alfalab.models.Orders> call, @NonNull Response<Orders> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()) {
                    Toast.makeText(context, "Pedido creado", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(context, MainActivity.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                } else {
                    Log.e(MainActivity.API, "createOrder - onResponse (errorBody): " + response.errorBody());
                    Log.e(MainActivity.API, "createOrder - onResponse (response.raw): " + response.raw().toString());
                    Log.e(MainActivity.API, "createOrder - onResponse (message): " + response.message());
                    Toast.makeText(context, context.getResources().getString(R.string.check_internet_connection), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<cl.alfa.alfalab.models.Orders> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, context.getResources().getString(R.string.feed_update_error), Toast.LENGTH_SHORT).show();
                Log.d(MainActivity.API, "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_orders_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
            case R.id.finalize:
                if(data != null) {
                    data.setClient(client);
                    updateOrder(data, adapter.getItems());
                } else {
                    if (!orders.isEmpty()) {
                        createOrder(orders);
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    private int totalPrice() {
        int total = 0;
        for(int i = 0; i < adapter.getItemCount(); i++) {
            total += adapter.getItem(i).getPrice();
        }
        return total;
    }

    @Override
    public void saveOrder(Order order) {
        orders.add(order);
        adapter.addItem(order);
        adapter.notifyDataSetChanged();
        totalPriceText.setText(String.valueOf(totalPrice()));
    }

    @Override
    public void editOrder(Order newOrder, Order oldOrder) {
        ArrayList<Order> tmpList = new ArrayList<>();
        if(adapter.getItemCount()  <= 1) {
            adapter.clear();
            adapter.notifyDataSetChanged();
            adapter.addItem(newOrder);
        } else {
            for (int i = 0; i < adapter.getItemCount(); i++) {
                if(adapter.getItem(i) != oldOrder) {
                    tmpList.add(adapter.getItem(i));
                } else {
                    tmpList.add(newOrder);
                }
            }
            adapter.clear();
            adapter.addItems(tmpList);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        fragObj.onDetach();
        fragObj = null;
        super.onDestroy();
    }
}