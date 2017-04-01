package smartindia.santas.bloodrelations.objects;

/**
 * Created by adityadesai on 01/04/17.
 */

public class Donor {

    private String donorName;
    private String donorLocation;
    private String donorBloodGroup;
    private String donorPhone;

    public Donor(String donorName, String donorLocation, String donorBloodGroup, String donorPhone) {
        this.donorName = donorName;
        this.donorLocation = donorLocation;
        this.donorBloodGroup = donorBloodGroup;
        this.donorPhone = donorPhone;
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
}
