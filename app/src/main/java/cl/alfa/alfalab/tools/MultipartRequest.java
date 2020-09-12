package cl.alfa.alfalab.tools;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import cl.alfa.alfalab.MainActivity;
import cl.alfa.alfalab.R;
import cl.alfa.alfalab.activities.ReportActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.Objects;


public class MultipartRequest {

    private Context caller;
    public MultipartBody.Builder builder;
    private OkHttpClient client;
    private ProgressDialog dialog;
    private int responseCode = 0;

    public MultipartRequest(Context caller, ProgressDialog dialog){
        this.caller = caller;
        this.dialog = dialog;
        this.builder = new MultipartBody.Builder();
        this.builder.setType(MultipartBody.FORM);
        this.client = new OkHttpClient();
    }

    public void addString(String name, String value){
        this.builder.addFormDataPart(name, value);
    }

    public void addFile(String name, RequestBody file, String fileName){
        this.builder.addFormDataPart(name, fileName, file);
    }

    public void onCode(String message, int code){
        if(responseCode == code)
            backgroundThreadShortToast(caller, message);
    }

    public void execute(String url){
        RequestBody requestBody;
        Request request;

        try {
            requestBody = this.builder.build();
            request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(new Callback(){
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e){
                    dialog.dismiss();
                    Log.e(MainActivity.REP, "Error failure: " + Objects.requireNonNull(e.getMessage()));
                    if(e.getMessage().equals("timeout")) {
                        responseCode = 552;
                        onCode(caller.getResources().getString(R.string.report_max_size), responseCode);
                    } else
                        onCode(caller.getResources().getString(R.string.report_dont_sended), responseCode);
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) {
                    dialog.dismiss();
                    if(response.isSuccessful()){
                        backgroundThreadShortToast(caller, caller.getResources().getString(R.string.report_sended));
                        responseCode = response.code();
                        Log.d(MainActivity.REP, "STATUS CODE: " + responseCode);
                        ((ReportActivity)caller).finish();
                    } else {
                        backgroundThreadShortToast(caller, caller.getResources().getString(R.string.report_dont_sended));
                        responseCode = response.code();
                        Log.e(MainActivity.REP, "STATUS CODE ERROR: " + responseCode);
                    }
                }
            });

        } catch (Exception e){
            e.printStackTrace();
            Log.e(MainActivity.REP, "" + e.getMessage());
        } finally {
            builder = null;
            if (client != null)
                client = null;
            System.gc();
        }
    }

    private static void backgroundThreadShortToast(final Context context, final String msg){
        if (context != null && msg != null)
            new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(context, msg, Toast.LENGTH_SHORT).show());
    }

}