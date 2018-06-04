package imagisoft.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Se hereda de ScheduleBlock solo para reutilizar los atributos
 * Start y End
 */
public class Congress extends ScheduleBlock implements Parcelable {

    /**
     * Atributos considerados como fundamentales
     */
    private String name;
    private String location;
    private String description;

    /**
     * Atributos marcados como opcionales
     * Se utilizan para poder obtener los datos que se pueden\
     * colocar en Google Maps
     */
    private float xCoord;
    private float yCoord;
    private String locationTag;

    /**
     * Getters y Setters de los atributos fundamentales
     */
    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Getters y Setters de los atributos opcionales
     */
    public float getxCoord() {
        return xCoord;
    }

    public float getyCoord() {
        return yCoord;
    }

    public String getLocationTag() {
        return locationTag;
    }

    public Congress() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.location);
        dest.writeString(this.description);
        dest.writeFloat(this.xCoord);
        dest.writeFloat(this.yCoord);
        dest.writeString(this.locationTag);
    }

    protected Congress(Parcel in) {
        this.name = in.readString();
        this.location = in.readString();
        this.description = in.readString();
        this.xCoord = in.readFloat();
        this.yCoord = in.readFloat();
        this.locationTag = in.readString();
    }

    public static final Parcelable.Creator<Congress> CREATOR = new Parcelable.Creator<Congress>() {

        @Override
        public Congress createFromParcel(Parcel source) {
            return new Congress(source);
        }

        @Override
        public Congress[] newArray(int size) {
            return new Congress[size];
        }

    };

}