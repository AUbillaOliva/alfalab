package cl.alfa.alfalab.fragments.bottom_menu_fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cl.alfa.alfalab.MainActivity;
import cl.alfa.alfalab.MainApplication;
import cl.alfa.alfalab.R;
import cl.alfa.alfalab.api.ApiClient;
import cl.alfa.alfalab.api.ApiService;
import cl.alfa.alfalab.models.Orders;
import cl.alfa.alfalab.utils.SharedPreferences;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private final Context context = MainApplication.getContext();
    private SharedPreferences mSharedPreferences;
    private ArrayList<Orders> mData = new ArrayList<>();
    private BarChart chart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.home_fragment_layout, container, false);

        mSharedPreferences = new SharedPreferences(context);

        getData();

        MainActivity.getToolbar().setElevation(4);

        //final TextView hello = view.findViewById(R.id.hello);
        chart = view.findViewById(R.id.chart);

        //hello.setText(String.format(getResources().getString(R.string.hello), capitalizeFirstLetter(mSharedPreferences.getResponsible().getFirstname())));
        return view;
    }

    private void getData() {
        final ApiService.OrderService service = ApiClient.getClient().create(ApiService.OrderService.class);
        final Call<ArrayList<Orders>> responseCall = service.getOrders();

        responseCall.enqueue(new Callback<ArrayList<Orders>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<cl.alfa.alfalab.models.Orders>> call, @NonNull Response<ArrayList<Orders>> response) {
                //mSwipeRefreshLayout.setRefreshing(false);
                //mProgressBar.setVisibility(View.GONE);
                //mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    try {
                        setDataSet(response.body());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.check_internet_connection), Toast.LENGTH_SHORT).show();
                    Log.e(MainActivity.API, "getData - onResponse (errorBody): " + response.errorBody());
                    Log.e(MainActivity.API, "getData - onResponse (response.raw): " + response.raw().toString());
                    Log.e(MainActivity.API, "getData - onResponse (message):" + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<cl.alfa.alfalab.models.Orders>> call, @NonNull Throwable t) {
                //mSwipeRefreshLayout.setRefreshing(false);
                //mProgressBar.setVisibility(View.GONE);
                //mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                Log.d(MainActivity.API, "getData - onFailure: " + t.getMessage());
                Log.e(MainActivity.API, "getData - onFailure: " + t.getLocalizedMessage());
                Toast.makeText(context, context.getResources().getString(R.string.feed_update_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private ArrayList<BarEntry> setValues(ArrayList<Orders> mList) throws ParseException {
        final ArrayList<BarEntry> entriesYAxis = new ArrayList<>();
        final ArrayList<Integer> dayList = new ArrayList<>();
        int count = 0;
        for (Orders orders : mList) {
            @SuppressLint("SimpleDateFormat") DateFormat sourceFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
            String dateAsString = orders.getCreated_at();
            Date date = null;
            try {
                date = sourceFormat.parse(dateAsString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            assert date != null;
            @SuppressLint("SimpleDateFormat") String outputFormat = new SimpleDateFormat("dd").format(date);

            for(int i = 0; i < mList.size(); i++) {
                String dateString = mList.get(i).getCreated_at();
                Date dateC = null;
                try {
                    dateC = sourceFormat.parse(dateString);
                    assert dateC != null;
                    @SuppressLint("SimpleDateFormat") String outputFormatC = new SimpleDateFormat("dd").format(dateC);
                    if(outputFormat.equals(outputFormatC)){
                        count++;
                        Log.d(MainActivity.API, "Count: " + count + " outputFormatC: " + outputFormatC);
                        dayList.add(Integer.parseInt(outputFormat));
                        BarEntry entry = new BarEntry(Float.parseFloat(outputFormat), count);
                        entriesYAxis.add(entry);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.d(MainActivity.API, mList.get(1).getCreated_at());
        return entriesYAxis;
    }

    private int howManyDays(ArrayList<Integer> dayList) {

        return 0;
    }

    private void setDataSet(ArrayList<Orders> mList) throws ParseException {
        ArrayList<BarEntry> values = setValues(mList);
        BarData barData = new BarData();
        BarDataSet dataSetY = new BarDataSet(values, "Pedidos"); // add entries to dataset
        //BarDataSet dataSetX = new BarDataSet(valuesXAxis, "Numero");

        //barData.addDataSet(dataSetX);
        barData.addDataSet(dataSetY);
        dataSetY.setColor(getResources().getColor(R.color.colorAccent));
        dataSetY.setValueTextSize(16);
        dataSetY.setValueTextColor(mSharedPreferences.loadNightModeState() ? getResources().getColor(android.R.color.white) : getResources().getColor(android.R.color.black));

        //chart.getXAxis().setEnabled(false);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        chart.getXAxis().setLabelCount(4); // CUANTOS DIAS HAY
        chart.setData(barData);
        chart.invalidate();
    }

    private int findMaxYValue(BarData data) {
        return 0;
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

}
