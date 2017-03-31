package smartindia.santas.bloodrelations.Objects;

/**
 * Created by adityadesai on 31/03/17.
 */

public class BloodBank {

    private String name;
    private String location;
    private String phone;


    public BloodBank(String name, String location, String phone) {
        this.name = name;
        this.location = location;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getPhone(){
        return phone;
    }
}
