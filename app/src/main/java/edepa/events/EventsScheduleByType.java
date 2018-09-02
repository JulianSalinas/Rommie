package edepa.events;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import edepa.cloud.CloudEvents;
import edepa.cloud.CloudFavorites;
import edepa.model.EventType;

/**
 * Contiene todos los eventos del cronograma, incluidos
 * los favoritos y los no favoritos
 */
public class EventsScheduleByType extends EventsSchedule implements IEventsByType {

    public static final String TYPE_KEY = "type";

    protected EventType eventype;

    public EventType getEventype() {
        return eventype;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null && args.containsKey(TYPE_KEY)){
            String type = args.getString(TYPE_KEY);
            eventype = EventType.valueOf(type);
        }
        super.onCreate(savedInstanceState);
    }

    /**
     * Query realizado a la base de datos para
     * obtener toda la lista de eventos para un fecha
     * en específico (que es pasada al fragmento como arg)
     * @return Query
     */
    public Query getEventsQuery(){
        return CloudEvents.getEventsQueryUsingType(getEventype());
    }

}
