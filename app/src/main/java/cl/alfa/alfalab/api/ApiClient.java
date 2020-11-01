package cl.alfa.alfalab.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    /**
     * MORE INFO ABOUT THE API ON https://github.com/AUbillaOliva/Alfalab/blob/master/README.md
     * CREATED BY {@author}: ÁLVARO UBILLA OLIVA
     * @version: v1.0.0
     * @license GPL-3.0
     **/

    private static final String BASE_URL = "https://alfa-lab.herokuapp.com/api/";
    public final static String REPORT_URL = "https://alfa-lab.herokuapp.com/support/report";

    public static Retrofit getClient() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}