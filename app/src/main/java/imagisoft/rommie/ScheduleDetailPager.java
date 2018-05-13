package imagisoft.rommie;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import butterknife.BindView;
import fr.castorflex.android.verticalviewpager.VerticalViewPager;
import imagisoft.edepa.ScheduleEvent;
import imagisoft.miscellaneous.ColorConverter;

public class ScheduleDetailPager extends MainActivityFragment {

    private int statusBarColor;

    @BindView(R.id.view_pager)
    protected ViewPager pager;

    /**
     * Referencia al evento del que se muestran los detalles
     */
    private ScheduleEvent event;

    /**
     * El adaptador contiene los fragmentos del páginador
     */
    protected ScheduleDetailPagerAdapter adapter;

    /**
     * No se pueden crear constructores con parámetros, por tanto,
     * se pasan los parámetros de esta forma
     */
    public static ScheduleDetailPager newInstance(ScheduleEvent event) {

        ScheduleDetailPager fragment = new ScheduleDetailPager();

        Bundle args = new Bundle();
        args.putParcelable("event", event);

        fragment.setArguments(args);
        return fragment;

    }

    /**
     * Se define cúal es el layout que va a utilizar
     * @param bundle: No se utiliza
     */
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.resource = R.layout.schedule_detail_vertical;

        Bundle args = getArguments();
        if(args != null)
            event = args.getParcelable("event");

    }

    /**
     * Se configura la vista después de que la actividad se reinicia
     * ya sea por cambio de idioma o al girar la pantalla
     * @param bundle: No se utiliza
     */
    @Override
    public void onActivityCreated(Bundle bundle) {

        super.onActivityCreated(bundle);
        this.setupViewPager();

        statusBarColor = getStatusBarColor();
        setToolbarVisibility(View.GONE);
        setTabLayoutVisibility(View.GONE);

        if(bundle != null)
            pager.setCurrentItem(bundle.getInt("currentPage"));

        int color = getResources().getColor(event.getEventype().getColor());
        setStatusBarColor(ColorConverter.darken(color, 12));

    }

    @Override
    public void onPause() {
        super.onPause();
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
    }

    @Override
    public void onResume() {
        super.onResume();
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * Al cambiar a otra sección se deben volver a colocar la toolbar
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        setStatusBarColor(statusBarColor);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentPage", pager.getCurrentItem());
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null)
            pager.setCurrentItem(savedInstanceState.getInt("currentPage"));
    }


    protected void setupViewPager() {

        // Se revisa porque al entrar por seguna vez, no es necesario colocar el adaptador
        // de lo contrario se reinicia a la posición inicial (visualmente)
        if(adapter == null)
            adapter = new ScheduleDetailPagerAdapter(this.getChildFragmentManager(), event);

        pager.setAdapter(adapter);

    }

}
