package smartindia.santas.bloodrelations.objects;

/**
 * Created by adityadesai on 31/03/17.
 */

public class BloodBank {

    private String bbName;
    private String bbLocation;
    private String bbPhone;
    private double bbLatitude;
    private double bbLongitude;

    public BloodBank(String bbName, String bbLocation, String bbPhone, double lat, double lng) {
        this.bbName = bbName;
        this.bbLocation = bbLocation;
        this.bbPhone = bbPhone;
        this.bbLatitude = lat;
        this.bbLongitude = lng;
    }

    public String getBbName() {
        return bbName;
    }

    public String getBbLocation() {
        return bbLocation;
    }

    public String getBbPhone() {
        return bbPhone;
    }

    public double getBbLatitude() {
        return bbLatitude;
    }

    public double getBbLongitude() {
        return bbLongitude;
    }
}
