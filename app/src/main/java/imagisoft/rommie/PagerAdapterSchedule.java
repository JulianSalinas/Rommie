package imagisoft.rommie;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.Query;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.ArrayList;
import imagisoft.edepa.ScheduleEvent;


public class PagerAdapterSchedule extends PagerAdapter {

    /**
     * En el constructor se agrega el listener para colocar las fechas
     * en el paginador
     */
    public PagerAdapterSchedule(PagerFragment fragment) {

        super(fragment);

        // Se deben ordenar los eventos por fecha de inicio
        Query query = this.fragment.activity
                .getScheduleReference()
                .orderByChild("start");

        // Este implementa las dos interfaces necesarias para sincronizar los datos
        SchedulePagerAdapterValueEventListener listener
                = new SchedulePagerAdapterValueEventListener();

        // Se cargan todos los datos una única vez
        query.addListenerForSingleValueEvent(listener);

    }

    /**
     * Clase que conecta las fechas del paginador con las extraídas del
     * cronograma
     */
    class SchedulePagerAdapterValueEventListener implements ValueEventListener {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            for (DataSnapshot postSnapshot: dataSnapshot.getChildren())
                events.add(postSnapshot.getValue(ScheduleEvent.class));

            notifyDataSetChanged();

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.i(fragment.getTag(), databaseError.toString());
        }

    }

}