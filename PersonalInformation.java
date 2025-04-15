package FlightProject;

import java.util.UUID;

public class PersonalInformation {
    private String name;
    private String LastName;
    private long phoneNumber;
    private String email;
    private UUID id;
    private String seat;

    public PersonalInformation(){
        this.name = null;
        this.LastName = null;
        this.phoneNumber = 0;
        this.email = null;
        this.id = null;
    }

    public PersonalInformation(String name, String lastName, long phoneNumber, String email) {
        this.name = name;
        LastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return LastName;
    }
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public String getSeat(){
        return seat;
    }
    public void setSeat(String seat){
        this.seat = seat;

    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) throws PersonalException {
        if(phoneNumber != 0 && String.valueOf(phoneNumber).length() != 10){
            throw new PersonalException("Invalid; phone number must be 10 digits");
        }
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
