package imagisoft.rommie;

import android.support.v4.app.Fragment;

public class MainViewFragment extends Fragment {

    /**
     * Posibles parámetros para usar con la función switchFragment
     */
    public int FADE_ANIMATION = 0;
    public int SLIDE_ANIMATION = 1;

    /**
     * Es un invocada cuando un fragmento ocupa colocar un listener
     * de firebase
     */
    public MainViewFirebase getFirebase(){
        return (MainViewFirebase) getActivity();
    }

    /**
     * Coloca en la pantalla un fragmento previamente creado
     * @param fragment Asociado a la opción elegida por el usuario
     */
    public void switchFragment(Fragment fragment){
        switchFragment(fragment, FADE_ANIMATION);
    }

    /**
     * Coloca en la pantalla un fragmento previamente creado usando un animación
     * @param fragment Asociado a la opción elegida por el usuario
     */
    public void switchFragment(Fragment fragment, int animation){
        MainViewNavigation activity = (MainViewNavigation) getActivity();
        activity.switchFragment(fragment, animation);
    }

    /**
     * Print temporal en la parte inferior de la aplicación
     * @param msg Mensaje que se desea mostrar
     */
    public void showStatusMessage(String msg){
        MainViewNavigation activity = (MainViewNavigation) getActivity();
        activity.showStatusMessage(msg);
    }

}