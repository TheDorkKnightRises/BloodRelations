package smartindia.santas.bloodrelations.objects;

/**
 * Created by adityadesai on 01/04/17.
 */

public class Donor {

    private String donorName;
    private String donorLocation;
    private String donorBloodGroup;
    private String donorPhone;
    private double latitude;
    private double longitude;

    public Donor(String donorName, String donorLocation, String donorBloodGroup, String donorPhone, double lat, double lng) {
        this.donorName = donorName;
        this.donorLocation = donorLocation;
        this.donorBloodGroup = donorBloodGroup;
        this.donorPhone = donorPhone;
        this.latitude = lat;
        this.longitude = lng;
    }

    public String getDonorName() {
        return donorName;
    }

    public String getDonorLocation() {
        return donorLocation;
    }

    public String getDonorBloodGroup() {
        return donorBloodGroup;
    }

    public String getDonorPhone() {
        return donorPhone;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
