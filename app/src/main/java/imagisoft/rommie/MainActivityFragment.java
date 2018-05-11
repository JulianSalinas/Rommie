package imagisoft.rommie;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

import butterknife.ButterKnife;
import imagisoft.edepa.Preferences;


public abstract class MainActivityFragment extends Fragment {

    /**
     * Cuando se cambian los fragmentos es necesario conservar el
     * nombre del anterior para poder colocarlo al presionar atrás
     */
    protected CharSequence lastUsedToolbarText;

    protected static String TAG = "MainActivityFragment";

    protected final Preferences prefs = Preferences.getInstance();

    /**
     * Requiere constructor vacío
     */
    public MainActivityFragment() {

    }

    @Override
    public void onCreate(Bundle bundle) {
        Log.i(TAG, "onCreate()");
        super.onCreate(bundle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        Log.i(TAG, "onCreateView()");
        return super.onCreateView(inflater, container, bundle);
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        Log.i(TAG, "onActivityCreate()");
        super.onActivityCreated(bundle);
        lastUsedToolbarText = getToolbar().getTitle();
    }

    @Override
    public void onStart() {
        Log.i(TAG, "onStart()");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.i(TAG, "onResume()");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.i(TAG, "onPause()");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.i(TAG, "onStop()");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy()");
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        Log.i(TAG, "onDestroyView()");
        super.onDestroyView();
        getToolbar().setTitle(lastUsedToolbarText);
    }

    @Override
    public void onDetach() {
        Log.i(TAG, "onDetach()");
        super.onDetach();
    }

    /**
     * Enlaza los componentes visuales con sus vistas
     */
    protected View inflate(LayoutInflater inflater, ViewGroup container, int resource){
        View view = inflater.inflate(resource, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    /**
     * Es invocada cuando un fragmento ocupa colocar un listener
     * de firebase
     */
    public MainActivityFirebase getFirebase(){
        return (MainActivityFirebase) getActivity();
    }

    /**
     * Es invocada cuando un fragmento ocupa ocultar o mostrar la toolbar
     */
    public MainActivityFirebase getNavigation(){
        return (MainActivityNavigation) getActivity();
    }

    /**
     * Obtiene el lenguaje de la configuracíon. Si
     * es la primera vez, guarda en la configuración el idioma del dispositivo
     * @return "es" o "en"
     */
    public String getCurrentLang(){

        String lang = prefs.getStringPreference(getActivity(), Preferences.LANG_KEY_VALUE);

        if(lang == null) {
            lang = Locale.getDefault().getLanguage();
            setCurrentLang(lang);
        }

        return lang;

    }

    public void setCurrentLang(String lang){
        prefs.setPreference(getActivity(), Preferences.LANG_KEY_VALUE, lang);
    }

    /**
     * Obtiene el actual nombre de usuario, si el la primera vez toma
     * el usuario desde el login por defecto
     */
    public String getCurrentUsername(){

        String username = prefs.getStringPreference(getActivity(), Preferences.USER_KEY_VALUE);

        if(username == null) {
            username = getDefaultUsername(getActivity());
            setCurrentUsername(username);
        }

        return username;

    }

    public void setCurrentUsername(String username){
        prefs.setPreference(getActivity(), Preferences.USER_KEY_VALUE, username);
    }

    /**
     * Obtiene el estado actual de la alarma
     */
    public Boolean getCurrentAlarmState(){
        return prefs.getBooleanPreference(getActivity(), Preferences.ALARM_STATE_KEY_VALUE);
    }

    public void setCurrentAlarmState(boolean state){
        prefs.setPreference(getActivity(), Preferences.ALARM_STATE_KEY_VALUE, state);
    }

    /**
     * Cuando el usuario hace login con el # de teléfono, su nombre está vacío por lo que
     * hay que crear uno. Al hacer login con Google o correo el nombre por defecto que se
     * coloca es el de dicha cuenta.
     * @param context: Actividad desde donde se llama la aplicación
     */
    public String getDefaultUsername(Context context){

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        assert user != null;
        return user.getDisplayName() == null ||
                !user.getDisplayName().isEmpty() ?
                context.getResources().getString(R.string.text_anonymous) : user.getDisplayName();

    }

    /**
     * Coloca en la pantalla un fragmento previamente creado
     * @param fragment Asociado a la opción elegida por el usuario
     */
    public void switchFragment(Fragment fragment){
        assert getActivity() != null;
        MainActivityNavigation activity = (MainActivityNavigation) getActivity();
        activity.switchFragment(fragment);
    }

    /**
     * Reinicia la aplicación. Se utiliza cuando se cambia el idioma o el tema
     */
    public void restartApplication(){
        Intent refresh = new Intent(getActivity(), MainActivityNavigation.class);
        startActivity(refresh);
        getNavigation().finish();
    }

    public void setToolbarVisibility(int visibility){
        ActionBar toolbar = getNavigation().getSupportActionBar();
        if(toolbar != null) {
            if (visibility != View.VISIBLE) toolbar.hide();
            else toolbar.show();
        }
    }

    public void setStatusBarColor(int color){
        MainActivityNavigation activity = (MainActivityNavigation) getActivity();
        activity.getWindow().setStatusBarColor(color);
    }

    public int getStatusBarColor(){
        MainActivityNavigation activity = (MainActivityNavigation) getActivity();
        return activity.getWindow().getStatusBarColor();
    }

    public void setTabLayoutVisibility(int visibility){
        getNavigation().getTabLayout().setVisibility(visibility);
    }

    public Toolbar getToolbar(){
        return getNavigation().getToolbar();
    }

    public void setToolbarText(int resource){
        getNavigation().getToolbar().setTitle(resource);
    }

    /**
     * Print temporal en la parte inferior de la aplicación
     * @param msg Mensaje que se desea mostrar
     */
    public void showStatusMessage(String msg){
        assert getActivity() != null;
        MainActivityNavigation activity = (MainActivityNavigation) getActivity();
        activity.showStatusMessage(msg);
    }

}
