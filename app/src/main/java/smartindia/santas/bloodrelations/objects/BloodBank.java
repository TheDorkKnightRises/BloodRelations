package smartindia.santas.bloodrelations.objects;

/**
 * Created by adityadesai on 31/03/17.
 */

public class BloodBank {

    private String bbName;
    private String bbLocation;
    private String bbPhone;

    public BloodBank(String bbName, String bbLocation, String bbPhone) {
        this.bbName = bbName;
        this.bbLocation = bbLocation;
        this.bbPhone = bbPhone;
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
}
