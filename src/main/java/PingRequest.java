public class PingRequest {
    private final String address;

    public PingRequest(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }
}
