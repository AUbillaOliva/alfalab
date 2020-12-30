package cl.alfa.alfalab;

import android.app.Application;
import android.content.Context;

import org.acra.ACRA;
import org.acra.config.CoreConfigurationBuilder;
import org.acra.config.SchedulerConfigurationBuilder;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import cl.alfa.alfalab.utils.SharedPreferences;

/*
    @Author: Álvaro Felipe Ubilla Oliva
    @Year: 2020
    @Version: v1.0.0
    @License: GPL-3.0
    This app is under the GPL-3.0 license, as well of the Alfa-lab Api
    for more information, see README.md of this repo on Github.
    (https://github.com/AUbillaOliva/LSCH/blob/v1.0.1/README.md)
*/

public class MainApplication extends Application {
    private static Application application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        try {
            // Google Play will install latest OpenSSL
            SSLContext sslContext;
            sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, null, null);
            sslContext.createSSLEngine();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
    }

    public static Application getApplication() {
        return application;
    }

    public static Context getContext() {
        return getApplication().getApplicationContext();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        final CoreConfigurationBuilder builder = new CoreConfigurationBuilder(this)
                .setBuildConfigClass(BuildConfig.class)
                .setResReportSendSuccessToast(R.string.report_sended)
                .setResReportSendFailureToast(R.string.report_dont_sended)
                .setEnabled(true);
        builder.getPluginConfigurationBuilder(SchedulerConfigurationBuilder.class)
                .setEnabled(true)
                .setRestartAfterCrash(false);
        final SharedPreferences mSharedPreferences = new SharedPreferences(this);
        if(mSharedPreferences.sendReport()) {
            ACRA.init(this, builder);
        }
    }

}
