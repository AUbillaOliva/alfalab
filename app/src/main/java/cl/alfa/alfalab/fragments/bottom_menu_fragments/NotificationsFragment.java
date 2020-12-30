package cl.alfa.alfalab.fragments.bottom_menu_fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
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

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import cl.alfa.alfalab.MainActivity;
import cl.alfa.alfalab.MainApplication;
import cl.alfa.alfalab.R;
import cl.alfa.alfalab.adapters.GenericAdapter;
import cl.alfa.alfalab.api.ApiClient;
import cl.alfa.alfalab.api.ApiService;
import cl.alfa.alfalab.interfaces.RecyclerViewOnClickListenerHack;
import cl.alfa.alfalab.models.Message;
import cl.alfa.alfalab.utils.GenericViewHolder;
import cl.alfa.alfalab.utils.SharedPreferences;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsFragment extends Fragment {

    private final Context context = MainApplication.getContext();
    private SharedPreferences mSharedPreferences;
    private View view;
    private GenericAdapter<Message> adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar mProgressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.notifications_fragment_layout, container, false);

        mSharedPreferences = new SharedPreferences(context);
        getData();
        final RecyclerView mRecyclerView = view.findViewById(R.id.recycler_view);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_layout);
        mProgressBar = view.findViewById(R.id.progress_circular);

        mSwipeRefreshLayout.setOnRefreshListener(this::getData);

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            nestedScrollView.setOnScrollChangeListener((View.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                int initialscrollY = 0;
                if (scrollY > initialscrollY) {
                    OrdersFragment.appBarLayout.setElevation(8);
                } else if(scrollY < oldScrollY - scrollY) {
                    OrdersFragment.appBarLayout.setElevation(2);
                }
            });
        }*/

        mRecyclerView.setHasFixedSize(false);
        adapter = new GenericAdapter<Message>() {
            @Override
            public RecyclerView.ViewHolder setViewHolder(ViewGroup parent, RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack) {
                return new GenericViewHolder(LayoutInflater.from(requireContext()).inflate(R.layout.message_item_layout, parent, false), recyclerViewOnClickListenerHack);
            }

            @SuppressLint("DefaultLocale")
            @Override
            public void onBindData(RecyclerView.ViewHolder holder, Message val, int position) {
                final GenericViewHolder viewHolder = (GenericViewHolder) holder;
                final TextView messageTitle = viewHolder.get(R.id.message_title),
                        messageContent = viewHolder.get(R.id.message_content);
                final ImageView profilePicture = viewHolder.get(R.id.profile_image);
                Glide.with(requireContext()).load(val.getAuthor().getProfileImage()).into(profilePicture);
                Log.d(MainActivity.API, "URL: " + val.getAuthor().getFirstname());

                final TypedValue typedValueNormal = new TypedValue();
                final Resources.Theme theme = requireContext().getTheme();
                theme.resolveAttribute(R.attr.textColor, typedValueNormal, true);
                @ColorInt final int textColor = typedValueNormal.data;

                messageTitle.setText(Html.fromHtml(String.format("<font color='%1$d'>%2$s", textColor, capitalizeFirstLetter(val.getTitle()))));
                messageContent.setText(val.getMessage());
            }

            @Override
            public RecyclerViewOnClickListenerHack onGetRecyclerViewOnClickListenerHack() {
                return new RecyclerViewOnClickListenerHack() {
                    @Override
                    public void onClickListener(View view, int position) {
                        Toast.makeText(context, adapter.getItem(position).getTitle(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onLongPressClickListener(View view, int position) {

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

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        final ApiService.MessagesService service = ApiClient.getClient().create(ApiService.MessagesService.class);
        final Call<ArrayList<Message>> responseCall = service.getMessages(mSharedPreferences.getToken());

        responseCall.enqueue(new Callback<ArrayList<Message>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Message>> call, @NonNull Response<ArrayList<Message>> response) {
                mSwipeRefreshLayout.setRefreshing(false);
                mProgressBar.setVisibility(View.GONE);
                mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                if (response.isSuccessful()) {
                    final ArrayList<Message> apiResponse = response.body();
                    assert apiResponse != null;
                    adapter.addItems(apiResponse);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        if (adapter.getItemCount() < 1) {
                            View notItemsView = view.findViewById(R.id.no_items_layout);
                            ImageView notItemsIcon = notItemsView.findViewById(R.id.no_items_icon);
                            TextView noItemsText = notItemsView.findViewById(R.id.no_items_text);
                            notItemsIcon.setBackgroundResource(R.drawable.ic_message);
                            noItemsText.setText(context.getResources().getString(R.string.no_messages));
                            view.findViewById(R.id.no_items_layout).setVisibility(View.VISIBLE);
                        } else {
                            view.findViewById(R.id.no_items_layout).setVisibility(View.GONE);
                        }
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.check_internet_connection), Toast.LENGTH_SHORT).show();
                    Log.e(MainActivity.API, "getData - onResponse (errorBody): " + response.errorBody());
                    Log.e(MainActivity.API, "getData - onResponse (response.raw): " + response.raw().toString());
                    Log.e(MainActivity.API, "getData - onResponse (message):" + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Message>> call, @NonNull Throwable t) {
                mSwipeRefreshLayout.setRefreshing(false);
                mProgressBar.setVisibility(View.GONE);
                mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                Log.d(MainActivity.API, "getData - onFailure: " + t.getMessage());
                Log.e(MainActivity.API, "getData - onFailure: " + t.getLocalizedMessage());
                Toast.makeText(context, context.getResources().getString(R.string.feed_update_error), Toast.LENGTH_SHORT).show();
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