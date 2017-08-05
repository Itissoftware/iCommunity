package software.is.com.myapplication;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.squareup.okhttp.OkHttpClient;

import io.fabric.sdk.android.Fabric;
import java.io.File;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import software.is.com.myapplication.event.ApiBus;
import software.is.com.myapplication.service.ApiHandler;
import software.is.com.myapplication.service.ApiService;

/**
 * Created by madhur on 3/1/15.
 */
public class IcrmApp extends Application implements Application.ActivityLifecycleCallbacks {


    public static final String API_ENDPOINT = "http://todayissoftware.com";
   //public static final String API_ENDPOINT = "http://192.168.1.141";
    public static Activity currentActivity;

    private static IcrmApp Instance;
    public static volatile Handler applicationHandler = null;
    private ApiHandler someApiHandler;
    private static PrefManager prefManager;

    public static final String APP_PERMISSIONS = "email,public_profile,user_friends";
    private static OkHttpClient sHttpClient;
    private static Activity mFbHandleActivity;
    private static Context sContext = null;


    public static Typeface CustomFontTypeFace() {
        return Typeface.createFromAsset(getAppContext().getAssets(), "fonts/SWZ721BR.ttf");
    }

    String applicationID = "5UDvYSr2ngfrUVKo5G3cQUaaiTGakrIngAlXNhqC";
    String clientKey = "f0RqCB5EYYuTVoGghacM2ITIxWHST5iUipg5y6vs";

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        sContext = this;
        Instance = this;
        applicationHandler = new Handler(getInstance().getMainLooper());
        prefManager = new PrefManager(getSharedPreferences("App", MODE_PRIVATE));

        saveInstallation(0);




        someApiHandler = new ApiHandler(this, buildApi(), ApiBus.getInstance());
        someApiHandler.registerForEvents();
    }


    public static Context getAppContext() {
        return sContext;
    }


    public static OkHttpClient getHttpClient() {
        if (sHttpClient == null) {
            sHttpClient = new OkHttpClient();
            int cacheSize = 10 * 1024 * 1024;
            File cacheLocation = new File(StorageUtils.getIdealCacheDirectory(IcrmApp.getAppContext()).toString());
            cacheLocation.mkdirs();
            com.squareup.okhttp.Cache cache = new com.squareup.okhttp.Cache(cacheLocation, cacheSize);
            sHttpClient.setCache(cache);
        }
        return sHttpClient;
    }






    ApiService buildApi() {

        return new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(API_ENDPOINT)

                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        //request.addQueryParam("p1", "var1");
                        //request.addQueryParam("p2", "");
                    }
                })

                .build()
                .create(ApiService.class);
    }

    public static void saveInstallation(int userId) {
    }
    public static void removeInstallation(int userId) {

    }

    public static void logout(Context context) {

    }

    public static IcrmApp get(Context context) {
        return (IcrmApp) context.getApplicationContext();
    }

    public static IcrmApp getInstance() {
        return Instance;
    }

    public static boolean applicationOnPause = false;
    public static PrefManager getPrefManager() {
        return prefManager;
    }
    @Override
    public void onActivityCreated(Activity arg0, Bundle arg1) {
        currentActivity = arg0;
        Log.e("VMVMVM", "onActivityCreated");
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Log.e("VMVMVM", "onActivityDestroyed ");
    }

    @Override
    public void onActivityResumed(Activity activity) {
        currentActivity = activity;
        applicationOnPause = false;
        Log.e("VMVMVM", "onActivityResumed " + activity.getClass());
    }

    @Override
    public void onActivityPaused(Activity activity) {
        applicationOnPause = true;
        Log.e("VMVMVM", "onActivityPaused " + activity.getClass());
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Log.e("VMVMVM", "onActivityStopped");
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        Log.e("VMVMVM", "onActivitySaveInstanceState");
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Log.e("VMVMVM", "onActivityDestroyed ");
    }

}
