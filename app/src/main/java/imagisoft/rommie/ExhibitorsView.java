package imagisoft.rommie;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import butterknife.BindView;


public class ExhibitorsView extends ExhibitorsViewFragment implements SearchView.OnQueryTextListener{

    @BindView(R.id.exhibitor_search_view)
    SearchView searchView;

    /**
     * Es la capa donde se coloca cada uno de los expositores
     */
    @BindView(R.id.exhibitor_recycler_view)
    RecyclerView exhibitorsRecyclerView;

    /**
     * Se crea el contenedor de los exponentes
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflate(inflater, container, R.layout.exhibitors_view);
    }

    /**
     * Justo después de crear el fragmento se enlazan y preparan las vistas
     */
    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        setupExhibitorsView();
        setHasOptionsMenu(true);
        setToolbarText(R.string.nav_people);
        searchView.setQueryHint(getResources().getString(R.string.text_search));
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * Se configura el exhibitorsRecyclerView que contiene los expositores
     */
    public void setupExhibitorsView(){
        exhibitorsRecyclerView.setHasFixedSize(true);
        exhibitorsRecyclerView.setLayoutManager(new SmoothLayout(getActivity()));
        exhibitorsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        exhibitorsRecyclerView.setAdapter(exhibitorsAdapter);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        exhibitorsAdapter.filter(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        exhibitorsAdapter.filter(newText);
        return true;
    }

}
