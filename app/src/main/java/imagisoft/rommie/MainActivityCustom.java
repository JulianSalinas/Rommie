package imagisoft.rommie;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.arch.lifecycle.Lifecycle;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.aesthetic.Aesthetic;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import java.util.Locale;
import java.util.Stack;

import imagisoft.edepa.FavoriteList;
import imagisoft.edepa.Preferences;

import static imagisoft.rommie.CustomColor.APP_ACCENT_DARK;
import static imagisoft.rommie.CustomColor.APP_PRIMARY_DARK;

/**
 * Clase análoga al masterpage de un página web
 */
public abstract class MainActivityCustom extends MainActivityClassic
        implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * Variables usadas para correr el servicio de notificaciones
     */
    private FirebaseJobDispatcher dispatcher;
    private final String CURRENT_RESOURCE_KEY = "CURRENT_RESOURCE_KEY";

    private final String REMAINDER_KEY = "Recordatorios";
    private final String CHANNEL_KEY = "Canal de notificaciones";

    protected int currentResource = R.id.nav_schedule;

    /**
     * Se inician todos los componentes principales de la aplicación
     */
    @Override
    protected void onCreate (Bundle bundle) {

        Aesthetic.attach(this);

        setTheme();
        setLanguage();
        setupDispatcher();
        FavoriteList.getInstance().loadFavorites(this);

        super.onCreate(bundle);

    }

    @Override
    public void onSaveInstanceState(Bundle bundel) {
        bundel.putInt(CURRENT_RESOURCE_KEY, currentResource);
        super.onSaveInstanceState(bundel);
    }

    @Override
    public void onRestoreInstanceState(Bundle bundle) {

        super.onRestoreInstanceState(bundle);

        if(bundle != null) {
            currentResource = bundle.getInt(CURRENT_RESOURCE_KEY);
            navigateById(currentResource);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!profilePendingList.isEmpty())
            switchFragment(profilePendingList.pop());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FavoriteList.getInstance().saveFavorites(this);
    }

    private void setTheme(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        getWindow().setStatusBarColor(prefs.getInt(APP_PRIMARY_DARK.toString(), APP_PRIMARY_DARK.getColor()));
        getWindow().setNavigationBarColor(prefs.getInt(APP_ACCENT_DARK.toString(), APP_ACCENT_DARK.getColor()));
    }

    public void setLanguage(){

        Preferences prefs = Preferences.getInstance();
        String lang = prefs.getStringPreference(this, Preferences.LANG_KEY_VALUE);

        if(lang == null) {
            lang = Locale.getDefault().getLanguage();
            prefs.setPreference(this, Preferences.LANG_KEY_VALUE, lang);
        }

        updatetLanguage(lang);

    }

    private void updatetLanguage(String lang){
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = new Locale(lang);
        res.updateConfiguration(conf, dm);
    }

    /**
     * Pone a correr el sercicio de notificaciones
     */
    private void setupDispatcher(){
        GooglePlayDriver driver = new GooglePlayDriver(this);
        dispatcher = new FirebaseJobDispatcher(driver);
    }

    /**
     * Al presionar un item del menu lateral se llama a la función para
     * colocar en la pantalla el fragment relacionado a ese item
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawer.closeDrawer(GravityCompat.START);
        navigateById(item.getItemId());
        return true;
    }

    /**
     * Ayuda a la función navigateToItem. La clase MainActivityNavigation
     * es la que implementa esta función
     * @param id: Identificación de botón presionado
     */
    protected abstract void navigateById(int id);


    private void scheduleJob() {
        Job myJob = dispatcher.newJobBuilder()
                .setService(AlarmService.class)
                .setTag(CHANNEL_KEY)
                .setRecurring(true)
                .setLifetime(Lifetime.FOREVER)
                .setTrigger(Trigger.executionWindow(0, 20))
                .setReplaceCurrent(false)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
                .build();
        dispatcher.mustSchedule(myJob);
        showStatusMessage(getString(R.string.text_turned_on_notifications));
    }

    public Notification createNotification(String title, String content){
        return new NotificationCompat.Builder(this, CHANNEL_KEY)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true).build();
    }

    public void showNotification(Notification notification){

        Object service = getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationManager manager = (NotificationManager) service;

        assert manager != null;
        createNotificationChannel(manager);
        manager.notify(0, notification);

    }

    public void createNotificationChannel(NotificationManager manager){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_KEY, REMAINDER_KEY,
                    NotificationManager.IMPORTANCE_DEFAULT);

            channel.setLightColor(Color.RED);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            manager.createNotificationChannel(channel);

        }

    }

}