package FlightProject;

import java.util.UUID;

public class PlaneTicket {
    private boolean sold;
    private UUID uuid;

    public PlaneTicket() {
        this.uuid = null;
        this.sold = false;
    }

    public boolean isSold() {
        return sold;
    }

    public void setSold(boolean sold) {
        this.sold = sold;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        if(sold){
            return "PlaneTicket has been sold";
        }
        return "PlaneTicket is available";
    }
}
