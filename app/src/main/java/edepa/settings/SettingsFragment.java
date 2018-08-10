package edepa.settings;

import android.os.Bundle;
import android.view.View;
import android.support.v7.preference.PreferenceFragmentCompat;

import edepa.modelview.R;
import edepa.activity.MainNavigation;

/**
 * Las preferencias que se cambian en el fragmento son
 * automáticamente aplicadas y reflejadas en la instancia
 * de {@link edepa.model.Preferences}
 */
public class SettingsFragment extends PreferenceFragmentCompat {

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.prefs_general);
    }

    /**
     * {@inheritDoc}
     * Este fragmento solo funciona si es colocado en una actividad
     * del tipo {@link MainNavigation} de lo contrario el comportamiento
     * es indeterminado
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setDividerHeight(1);
    }

    /**
     * {@inheritDoc}
     * El fondo es cambiado a blanco para no desentonar el
     * color de la fuente
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setBackgroundColor(getResources().getColor(R.color.app_white));
    }

}