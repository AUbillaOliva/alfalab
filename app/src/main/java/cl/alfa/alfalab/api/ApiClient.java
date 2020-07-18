package cl.alfa.alfalab.api;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    /**
     * THIS APP USES LSCH-API, AN OPEN SOURCE API FOR THE CHILEAN SIGN LANGUAGE
     * MORE INFO ABOUT THE API ON https://github.com/AUbillaOliva/Alfalab/blob/master/README.md
     * CREATED BY {@author}: ÁLVARO UBILLA OLIVA
     * @version: v1.0.0
     * @license GPL-3.0
     */

    //private static final String BASE_URL = "https://alfalab-api.herokuapp.com/api/";
    private static final String BASE_URL = "https://alfa-lab.herokuapp.com/api/";
    //public final static String REPORT_URL = "https://alfalab-api.herokuapp.com/support/report";
    //public final static String REPORT_URL = "http://192.168.0.16:5000/support/report";

    /*public static Retrofit getClient(OkHttpClient client){
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

    }*/

    public static Retrofit getClient(){
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }
}