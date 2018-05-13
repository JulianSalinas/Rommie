package imagisoft.edepa;

import android.os.Parcel;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import imagisoft.miscellaneous.DateConverter;


/**
 * Contiene toda la información de un evento en particular
 * Es necesario colocar los resumenes después de haber creado el evento
 * De igual forma es necesario agregar los expositores después de crearlo
 */
public class ScheduleEvent extends ScheduleBlock {

    /**
     * Atributos fundamentales
     */
    private String id;
    private String title;
    private String location;
    private ScheduleEventType eventype;

    /**
     * Atributos requeridos pero excluyentes, es decir,
     * si se usa uno no se debe usar el otro
     */
    private String briefEnglish;
    private String briefSpanish;

    /**
     * Atributo opcional, no importa que este vacio
     */
    private List<Exhibitor> exhibitors;

    /**
     * Contructor vacío requerido por firebase
     */
    public ScheduleEvent(){

    }

    /**
     * Constructor usado solo para tests, no utiliza los resumenes
     * por no se tiene información para eso.
     */
    public ScheduleEvent(String id, String start, String end,
                         String location, String title, ScheduleEventType eventype){

        super(start, end);
        this.id = id;
        this.title = title;
        this.location = location;
        this.eventype = eventype;
        this.exhibitors = new ArrayList<>();

    }

    /**
     * Getters de los atributos del evento
     */
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public ScheduleEventType getEventype() {
        return eventype;
    }

    public List<Exhibitor> getExhibitors() {
        return exhibitors;
    }

    /**
     * Solo se retorna uno de los resumenes
     * Esto va a depender del idioma que tenga el usuario en la configuración
     * Si el resumen solo está disponible en un idioma entonces se usa ese
     * @param lang: "es" o "en"
     */
    public String getBrief(String lang) {

        String brief =
                briefEnglish != null && briefSpanish == null ? briefEnglish:
                briefEnglish == null && briefSpanish != null ? briefSpanish: null;

        if(brief != null) return brief;

        else if (briefEnglish != null & briefSpanish != null){
            return lang.equals("es") ? briefSpanish : briefEnglish;
        }

        else return createDefaultBrief();

    }

    /**
     * Se usa en caso que el administrador no haya régistrado ningún resumen
     * para el evento.
     * TODO: Mover esto de lugar
     */
    public String createDefaultBrief(){

        return "La actividad está programada en el horario de " +
                DateConverter.extractTime(getStart()) + " a " +
                DateConverter.extractTime(getEnd()) + " en el " + location +
                ". Puede utilizar el chat para realizar cualquier consulta.";

    }

    /**
     * Agrega un nuevo expositro al evento.
     * Esta función solo es usada para generar pruebas
     */
    public ScheduleEvent addExhibitor(Exhibitor exhibitor){
        exhibitors.add(exhibitor);
        return this;
    }

    /**
     * Permite pasar como parámetro al servicio de alarma
     */
    public static String toJson(ScheduleEvent event){
        Gson gson = new Gson();
        return gson.toJson(event);
    }

    /**
     * Proceso inversa de la función toJSon
     */
    public static ScheduleEvent fromJson(String event){
        Gson gson = new Gson();
        return gson.fromJson(event, ScheduleEvent.class);
    }

    /**
     * Un evento es el mismo si tiene el mismo id y el mismo título
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ScheduleEvent)) return false;
        ScheduleEvent event = (ScheduleEvent) o;
        return id.equals(event.id) && title.equals(event.title);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.location);
        dest.writeInt(this.eventype == null ? -1 : this.eventype.ordinal());
        dest.writeString(this.briefEnglish);
        dest.writeString(this.briefSpanish);
        dest.writeTypedList(this.exhibitors);
    }

    protected ScheduleEvent(Parcel in) {
        super(in);
        this.id = in.readString();
        this.title = in.readString();
        this.location = in.readString();
        int tmpEventype = in.readInt();
        this.eventype = tmpEventype == -1 ? null : ScheduleEventType.values()[tmpEventype];
        this.briefEnglish = in.readString();
        this.briefSpanish = in.readString();
        this.exhibitors = in.createTypedArrayList(Exhibitor.CREATOR);
    }

    public static final Creator<ScheduleEvent> CREATOR = new Creator<ScheduleEvent>() {

        @Override
        public ScheduleEvent createFromParcel(Parcel source) {
            return new ScheduleEvent(source);
        }

        @Override
        public ScheduleEvent[] newArray(int size) {
            return new ScheduleEvent[size];
        }

    };

}
