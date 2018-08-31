package edepa.app;

import android.app.Application;
import com.cloudinary.android.MediaManager;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;
import java.util.HashMap;
import edepa.settings.SettingsLanguage;


public class EdepaApplication extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SettingsLanguage.applyLanguage(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        initCloudinary();
    }

    public void initCloudinary(){
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dgifcffo9");
        MediaManager.init(this, config);
    }

}