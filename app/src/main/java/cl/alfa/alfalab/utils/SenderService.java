package cl.alfa.alfalab.utils;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import org.acra.data.CrashReportData;
import org.acra.sender.ReportSender;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import cl.alfa.alfalab.MainActivity;
import cl.alfa.alfalab.api.ApiClient;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SenderService implements ReportSender {

    private Context context;

    @Override
    public void send(@NonNull Context context, @NonNull CrashReportData report){

        this.context = context;

        final OkHttpClient client = new OkHttpClient();
        RequestBody requestBody;

        try {
            final JSONObject obj = new JSONObject(Objects.requireNonNull(loadJSONFromAsset()));
            final JSONObject acraSettings = obj.getJSONObject("acra-settings");
            final String user = acraSettings.getString("acra-user");
            final String pass = acraSettings.getString("acra-pass");

            requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", "ACRA-report-stacktrace.json", RequestBody.create(MultipartBody.FORM, report.toJSON()))
                    .addFormDataPart("user", user)
                    .addFormDataPart("pass", pass)
                    .build();

            final Request request = new Request.Builder()
                    .url(ApiClient.REPORT_URL)
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e){
                    e.printStackTrace();
                    Log.e(MainActivity.REP, Objects.requireNonNull(e.getMessage()));
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response){
                    if(!response.isSuccessful())
                        Log.e(MainActivity.REP, response.message());
                    Log.d(MainActivity.REP, String.valueOf(call.isExecuted()));
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(MainActivity.REP, Objects.requireNonNull(e.getMessage()));
        }
    }

    private String loadJSONFromAsset() {
        String json;
        try {
            final InputStream is = context.getAssets().open("ACRA-SETTINGS.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            //noinspection ResultOfMethodCallIgnored
            is.read(buffer);
            json = new String(buffer, StandardCharsets.UTF_8);
            is.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

}
