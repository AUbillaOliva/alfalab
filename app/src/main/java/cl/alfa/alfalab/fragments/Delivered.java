package cl.alfa.alfalab.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import androidx.annotation.ColorInt;
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
import cl.alfa.alfalab.activities.DetailOrderActivity;
import cl.alfa.alfalab.adapters.GenericAdapter;
import cl.alfa.alfalab.api.ApiClient;
import cl.alfa.alfalab.api.ApiService;
import cl.alfa.alfalab.fragments.bottom_menu_fragments.OrdersFragment;
import cl.alfa.alfalab.interfaces.RecyclerViewOnClickListenerHack;
import cl.alfa.alfalab.models.Orders;
import cl.alfa.alfalab.utils.GenericViewHolder;
import cl.alfa.alfalab.utils.SharedPreferences;
import cl.alfa.alfalab.utils.databases.OrdersDatabaseHelper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Delivered extends Fragment {

    private final Context context = MainApplication.getContext();
    private SharedPreferences mSharedPreferences;
    private View view;
    private OrdersDatabaseHelper ordersDatabaseHelper = new OrdersDatabaseHelper(context);
    private GenericAdapter<Orders> adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar mProgressBar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_layout, container, false);

        mSharedPreferences = new SharedPreferences(context);

        final RecyclerView mRecyclerView = view.findViewById(R.id.recycler_view);
        final NestedScrollView nestedScrollView = view.findViewById(R.id.nested);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_layout);
        mProgressBar = view.findViewById(R.id.progress_circular);

        mSwipeRefreshLayout.setOnRefreshListener(this::getData);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            nestedScrollView.setOnScrollChangeListener((View.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                int initialscrollY = 0;
                if (scrollY > initialscrollY) {
                    OrdersFragment.appBarLayout.setElevation(8);
                } else if(scrollY < oldScrollY - scrollY) {
                    OrdersFragment.appBarLayout.setElevation(2);
                }
            });
        }

        mRecyclerView.setHasFixedSize(true);
        adapter = new GenericAdapter<cl.alfa.alfalab.models.Orders>() {
            @Override
            public RecyclerView.ViewHolder setViewHolder(ViewGroup parent, RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack) {
                return new GenericViewHolder(LayoutInflater.from(requireContext()).inflate(R.layout.item_card, parent, false), recyclerViewOnClickListenerHack);
            }

            @Override
            public void onBindData(RecyclerView.ViewHolder holder, cl.alfa.alfalab.models.Orders val, int position) {
                final GenericViewHolder viewHolder = (GenericViewHolder) holder;
                final TextView orderNumber = viewHolder.get(R.id.order_number),
                        orderClient = viewHolder.get(R.id.order_client),
                        orderZone = viewHolder.get(R.id.order_zone),
                        itemDate = viewHolder.get(R.id.item_date),
                        orderCheckOut = viewHolder.get(R.id.order_date);

                final TypedValue typedValueNormal = new TypedValue();
                final Resources.Theme theme = requireContext().getTheme();
                theme.resolveAttribute(R.attr.textColor, typedValueNormal, true);
                @ColorInt final int textColor = typedValueNormal.data;

                orderClient.setText(Html.fromHtml(String.format(getResources().getString(R.string.order_client_name), textColor, capitalizeFirstLetter(val.getClient().getFirstname()), capitalizeFirstLetter(val.getClient().getLastname()))));
                orderNumber.setText(Html.fromHtml(String.format(getResources().getString(R.string.order_number_text), val.getOrders_number())));
                @SuppressLint("SimpleDateFormat") DateFormat sourceFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                final String dateAsString = val.getDelivered_date();
                Date date = null;
                try {
                    date = sourceFormat.parse(dateAsString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                assert date != null;
                @SuppressLint("SimpleDateFormat") String outputFormat = new SimpleDateFormat("dd/MM/yy").format(date);
                itemDate.setText(getResources().getString(R.string.delivered_date_text));
                orderCheckOut.setText(outputFormat);
                orderZone.setText(capitalizeFirstLetter(val.getZone()));
            }

            @Override
            public RecyclerViewOnClickListenerHack onGetRecyclerViewOnClickListenerHack() {
                return new RecyclerViewOnClickListenerHack() {
                    @Override
                    public void onClickListener(View view, int position) {
                        final Intent intent = new Intent(context, DetailOrderActivity.class);
                        intent.putExtra("data", getItem(position));
                        intent.putExtra("delivered", true);
                        startActivity(intent);
                        requireActivity().finish();
                    }

                    @Override
                    public void onLongPressClickListener(View view, int position) {
                        final PopupMenu popup = new PopupMenu(requireContext(),view);
                        popup.getMenuInflater().inflate(R.menu.order_menu,popup.getMenu());
                        popup.getMenu().getItem(0).setVisible(false);
                        popup.show();
                        popup.setOnMenuItemClickListener(item -> {
                            if (item.getItemId() == R.id.delete) {
                                showDialog(adapter.getItem(position));
                            }
                            return true;
                        });
                    }
                };
            }
        };
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setNestedScrollingEnabled(false);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        mRecyclerView.setLayoutManager(layoutManager);

        return view;
    }

    private String capitalizeFirstLetter(String s) {
        final String[] in = s.split("^/([ ]?\\.?[a-zA-Z]+)+/$");
        final StringBuilder out = new StringBuilder();
        for (String ss : in) {
            final String upperString = ss.substring(0, 1).toUpperCase() + ss.substring(1).toLowerCase();
            out.append(upperString);
            out.append(" ");
        }
        return out.toString();
    }

    private void showDialog(Orders order) {
        final Dialog dialog = new Dialog(requireActivity());
        dialog.setCancelable(true);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        @SuppressLint("InflateParams") final View view  = requireActivity().getLayoutInflater().inflate(R.layout.confirmation_dialog_layout, null);
        dialog.setContentView(view);

        final ExtendedFloatingActionButton acceptButton;
        final TextView rejectButton, dialogTitle, dialogSubtitle;

        dialogTitle = view.findViewById(R.id.dialog_title);
        dialogTitle.setText(getResources().getString(R.string.delete_order_question));
        dialogSubtitle = view.findViewById(R.id.dialog_subtitle);
        dialogSubtitle.setText(getResources().getString(R.string.delete_order_warning));

        acceptButton = view.findViewById(R.id.confirm_button);
        acceptButton.setText(getResources().getString(R.string.positive_delete_dialog_button));
        acceptButton.setBackgroundColor(requireContext().getResources().getColor(R.color.design_default_color_error));
        acceptButton.setOnClickListener(view1 -> {
            deleteDelivery(order.getOrders_number());
            getData();
            dialog.dismiss();
        });

        rejectButton = view.findViewById(R.id.negative_button);
        rejectButton.setText(getResources().getString(R.string.cancel_dialog_buttton));
        rejectButton.setOnClickListener(view12 -> dialog.dismiss());

        dialog.show();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSwipeRefreshLayout.setOnRefreshListener(this::getData);
        if(isNetworkAvailable()) getData();
        else adapter.addItems(ordersDatabaseHelper.getAllOrders());
    }

    @SuppressWarnings("deprecation")
    private boolean isNetworkAvailable() {
        boolean isConnected = false;
        if(getActivity() != null) {
            final ConnectivityManager connectivityManager = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            assert connectivityManager != null;
            final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if(networkInfo != null && networkInfo.isConnected())
                isConnected = true;
            }
        return isConnected;
    }

    private void deleteDelivery(int number) {
        try {
            final ApiService.DeleteDelivery service = ApiClient.getClient().create(ApiService.DeleteDelivery.class);
            Call<ResponseBody> deleteRequest = service.deleteDelivery(number, mSharedPreferences.getToken());
            deleteRequest.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    if(response.isSuccessful()) {
                        Toast.makeText(context, "Pedido eliminado", Toast.LENGTH_SHORT).show();
                        ordersDatabaseHelper.deleteOrder(String.valueOf(number));
                        getData();
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e(MainActivity.API, "onResponse (errorBody): "+ response.errorBody());
                        Log.e(MainActivity.API, "onResponse (response.raw): " + response.raw().toString());
                        Log.e(MainActivity.API, "onResponse (message): " + response.message());
                    }
               }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    Toast.makeText(context, "Error al eliminar el pedido", Toast.LENGTH_SHORT).show();
                    Log.e(MainActivity.API, "onFailure: " + t.getMessage());
                }
            });
        } catch (Throwable err) {
            Log.e(MainActivity.API, Objects.requireNonNull(err.getMessage()));
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

    private void getData() {
        final ApiService.DeliveredService service = ApiClient.getClient().create(ApiService.DeliveredService.class);
        final Call<ArrayList<Orders>> responseCall = service.getDeliveries();

        responseCall.enqueue(new Callback<ArrayList<Orders>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<cl.alfa.alfalab.models.Orders>> call, @NonNull Response<ArrayList<Orders>> response) {
                mSwipeRefreshLayout.setRefreshing(false);
                mProgressBar.setVisibility(View.GONE);
                mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                if(response.isSuccessful()) {
                    final ArrayList<cl.alfa.alfalab.models.Orders> apiResponse = response.body();
                    assert apiResponse != null;
                    adapter.addItems(apiResponse);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        Log.d(MainActivity.API, String.valueOf(adapter.getItemCount()));
                        if(adapter.getItemCount() < 1) {
                            View notItemsView = view.findViewById(R.id.no_items_layout);
                            ImageView notItemsIcon = notItemsView.findViewById(R.id.no_items_icon);
                            notItemsIcon.setBackgroundResource(R.drawable.ic_move_to_inbox_24dp);
                            TextView noItemsText = notItemsView.findViewById(R.id.no_items_text);
                            noItemsText.setText(String.format(getResources().getString(R.string.no_items), "entregados"));
                            view.findViewById(R.id.no_items_layout).setVisibility(View.VISIBLE);
                        } else {
                            view.findViewById(R.id.no_items_layout).setVisibility(View.GONE);
                        }
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainApplication.getContext(), context.getResources().getString(R.string.check_internet_connection), Toast.LENGTH_SHORT).show();
                    Log.e(MainActivity.API, "onResponse: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Orders>> call, @NonNull Throwable t) {
                mSwipeRefreshLayout.setRefreshing(false);
                mProgressBar.setVisibility(View.GONE);
                mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                Log.d(MainActivity.API, "onFailure: " + t);
                Toast.makeText(MainApplication.getContext(), context.getResources().getString(R.string.feed_update_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.swipe_layout) {
            mSwipeRefreshLayout.setRefreshing(true);
        }
        return super.onOptionsItemSelected(item);
    }

}
