package cl.alfa.alfalab.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import cl.alfa.alfalab.MainActivity;
import cl.alfa.alfalab.MainApplication;
import cl.alfa.alfalab.R;
import cl.alfa.alfalab.activities.CreateOrderActivity;
import cl.alfa.alfalab.activities.DetailListActivity;
import cl.alfa.alfalab.activities.UpdateOrderActivity;
import cl.alfa.alfalab.adapters.GenericAdapter;
import cl.alfa.alfalab.api.ApiClient;
import cl.alfa.alfalab.api.ApiService;
import cl.alfa.alfalab.interfaces.RecyclerViewOnClickListenerHack;
import cl.alfa.alfalab.models.Orders;
import cl.alfa.alfalab.utils.GenericViewHolder;
import cl.alfa.alfalab.utils.databases.OrdersDatabaseHelper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Delivered extends Fragment {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar mProgressBar;
    private GenericAdapter<Orders> adapter;
    private final Context context = MainApplication.getContext();

    private OrdersDatabaseHelper ordersDatabaseHelper = new OrdersDatabaseHelper(context);

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true);
        final View view = inflater.inflate(R.layout.fragment_layout, container, false);

        view.findViewById(R.id.fab).setVisibility(View.GONE);

        final RecyclerView mRecyclerView = view.findViewById(R.id.recycler_view);
        final NestedScrollView nestedScrollView = view.findViewById(R.id.nested);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_layout);
        mProgressBar = view.findViewById(R.id.progress_circular);

        mSwipeRefreshLayout.setOnRefreshListener(this::getData);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(fabView -> startActivity(new Intent(context, CreateOrderActivity.class)));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            nestedScrollView.setOnScrollChangeListener((View.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                int initialscrollY = 0;
                if (scrollY > initialscrollY){
                    ((MainActivity) Objects.requireNonNull(getActivity())).getAppBarLayout().setElevation(8);
                    ((MainActivity) getActivity()).getAppBarLayoutShadow().setVisibility(View.GONE);
                } else if(scrollY < oldScrollY - scrollY){
                    ((MainActivity) Objects.requireNonNull(getActivity())).getAppBarLayout().setElevation(2);
                    ((MainActivity) getActivity()).getAppBarLayoutShadow().setVisibility(View.VISIBLE);
                }
            });


        mRecyclerView.setHasFixedSize(true);
        adapter = new GenericAdapter<cl.alfa.alfalab.models.Orders>(){
            @Override
            public RecyclerView.ViewHolder setViewHolder(ViewGroup parent, RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack){
                return new GenericViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_card, parent, false), recyclerViewOnClickListenerHack);
            }

            @Override
            public void onBindData(RecyclerView.ViewHolder holder, cl.alfa.alfalab.models.Orders val, int position){
                final GenericViewHolder viewHolder = (GenericViewHolder) holder;
                final CheckBox checkBox = viewHolder.get(R.id.facets_img_checkbox);
                checkBox.setVisibility(View.GONE);
                final TextView orderNumber = viewHolder.get(R.id.order_number), orderClient = viewHolder.get(R.id.order_client), orderType = viewHolder.get(R.id.order_type);
                String text = String.format(getResources().getString(R.string.order_number_text), String.valueOf(val.getNumber()));
                orderNumber.setText(text);
                String orderClientText = String.format(getResources().getString(R.string.order_client_name), val.getClient().getFirstname(), val.getClient().getLastname());
                orderClient.setText(orderClientText);
                orderType.setText(val.getType());
                final ImageView menu = viewHolder.get(R.id.more_icon);
                try {
                    menu.setOnClickListener(v -> {
                        PopupMenu popup = new PopupMenu(Objects.requireNonNull(getContext()),v);
                        popup.getMenuInflater().inflate(R.menu.order_menu,popup.getMenu());
                        popup.show();
                        popup.setOnMenuItemClickListener(item -> {
                            switch (item.getItemId()) {
                                case R.id.edit:
                                    Intent intent = new Intent(context, UpdateOrderActivity.class);
                                    intent.putExtra("order", val);
                                    startActivity(intent);
                                    break;
                                case R.id.delete:
                                    showDialog(val);
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
            public RecyclerViewOnClickListenerHack onGetRecyclerViewOnClickListenerHack(){
                return new RecyclerViewOnClickListenerHack(){
                    @Override
                    public void onClickListener(View view, int position){
                        final Intent intent = new Intent(getContext(), DetailListActivity.class);
                        intent.putExtra("data", (Parcelable) getItem(position));
                        startActivity(intent);
                    }

                    @Override
                    public void onLongPressClickListener(View view, int position){}
                };
            }
        };
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setNestedScrollingEnabled(false);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        return view;
    }

    private void showDialog(Orders order){
        final boolean[] status = new boolean[1];
        final Dialog dialog = new Dialog(Objects.requireNonNull(getActivity()));
        dialog.setCancelable(true);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        @SuppressLint("InflateParams") final View view  = getActivity().getLayoutInflater().inflate(R.layout.confirmation_dialog_layout, null);
        dialog.setContentView(view);

        final ExtendedFloatingActionButton acceptButton;
        final TextView rejectButton, dialogTitle, dialogSubtitle;

        acceptButton = view.findViewById(R.id.confirm_button);
        acceptButton.setText(getResources().getString(R.string.positive_delete_dialog_button));
        acceptButton.setOnClickListener(view1 -> {
            dialog.dismiss();
            status[0] = true;
            deleteDelivery(order.getNumber());
            getData();
        });

        rejectButton = view.findViewById(R.id.negative_button);
        rejectButton.setText(getResources().getString(R.string.cancel_dialog_buttton));
        rejectButton.setOnClickListener(view12 -> {
            dialog.dismiss();
            status[0] = false;
        });

        dialogTitle = view.findViewById(R.id.dialog_title);
        dialogTitle.setText("Eliminar orden?");
        dialogSubtitle = view.findViewById(R.id.dialog_subtitle);
        dialogSubtitle.setText("Esta accion no tiene vuelta atrás!");

        dialog.show();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        mSwipeRefreshLayout.setOnRefreshListener(this::getData);
        if(isNetworkAvailable()) getData();
        else adapter.addItems(ordersDatabaseHelper.getAllOrders());
    }

    private boolean isNetworkAvailable() {
        boolean isConnected = false;
        if(getActivity() != null){
            final ConnectivityManager connectivityManager = (ConnectivityManager) Objects.requireNonNull(getContext()).getSystemService(Context.CONNECTIVITY_SERVICE);
            assert connectivityManager != null;
            final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if(networkInfo != null && networkInfo.isConnected())
                isConnected = true;
            }
        return isConnected;
    }

    private void deleteDelivery(int number){
        try {
            final ApiService.DeleteDelivery service = ApiClient.getClient().create(ApiService.DeleteDelivery.class);
            Call<ResponseBody> deleteRequest = service.deleteDelivery(number);
            deleteRequest.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Toast.makeText(context, "Pedido eliminado", Toast.LENGTH_SHORT).show();
                    ordersDatabaseHelper.deleteOrder(String.valueOf(number));
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(context, "Error al eliminar el pedido", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Throwable err){
            Log.e(MainActivity.TAG, Objects.requireNonNull(err.getMessage()));
        } finally {
            getData();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    private void getData(){
        final ApiService.DeliveredService service = ApiClient.getClient().create(ApiService.DeliveredService.class);
        final Call<ArrayList<Orders>> responseCall = service.getDeliveries();

        responseCall.enqueue(new Callback<ArrayList<Orders>>(){
            @Override
            public void onResponse(@NonNull Call<ArrayList<cl.alfa.alfalab.models.Orders>> call, @NonNull Response<ArrayList<Orders>> response){
                mSwipeRefreshLayout.setRefreshing(false);
                mProgressBar.setVisibility(View.GONE);
                mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                if(response.isSuccessful()){
                    final ArrayList<cl.alfa.alfalab.models.Orders> apiResponse = response.body();
                    assert apiResponse != null;
                    adapter.addItems(apiResponse);
                    adapter.notifyDataSetChanged();
                } else {
                    Log.e(MainActivity.TAG, "onResponse: " + response.errorBody());
                    Toast.makeText(MainApplication.getContext(), context.getResources().getString(R.string.check_internet_connection), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Orders>> call, @NonNull Throwable t){
                mSwipeRefreshLayout.setRefreshing(false);
                mProgressBar.setVisibility(View.GONE);
                mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                Log.d(MainActivity.TAG, "onFailure: " + t);
                Toast.makeText(MainApplication.getContext(), context.getResources().getString(R.string.feed_update_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.swipe_layout)
            mSwipeRefreshLayout.setRefreshing(true);
        return super.onOptionsItemSelected(item);
    }

}
