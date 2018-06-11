package hello;

public class StepsResponse {
    private String status;
    private int steps;
    public StepsResponse() {

    }
    public StepsResponse(String status, int steps) {
        this.status = status;
        this.steps = steps;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }
}
