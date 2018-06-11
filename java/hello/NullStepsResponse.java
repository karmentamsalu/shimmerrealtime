package hello;


public class NullStepsResponse {
    private String status;
    private NullSteps data;
    public NullStepsResponse() {

    }

    public NullStepsResponse(String status, NullSteps data) {
        this.status = status;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public NullSteps getData() {
        return data;
    }

    public void setData(NullSteps data) {
        this.data = data;
    }
}
