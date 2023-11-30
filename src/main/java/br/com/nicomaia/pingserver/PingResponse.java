package br.com.nicomaia.pingserver;

public class PingResponse {
    private boolean success;
    private String address;
    private long time;

    private PingResponse() {
    }

    public boolean isSuccess() {
        return success;
    }

    public String getAddress() {
        return address;
    }

    public long getTime() {
        return time;
    }

    public String toString() {
        if (success) {
            return String.format("%s: tempo=%dms%n", address, time);
        }

        return String.format("%s: timeout", address);
    }

    public static PingResponse success(String address, long time) {
        PingResponse pingResponse = new PingResponse();
        pingResponse.success = true;
        pingResponse.address = address;
        pingResponse.time = time;

        return pingResponse;
    }

    public static PingResponse timeout(String address) {
        PingResponse pingResponse = new PingResponse();
        pingResponse.success = false;
        pingResponse.address = address;

        return pingResponse;
    }
}
