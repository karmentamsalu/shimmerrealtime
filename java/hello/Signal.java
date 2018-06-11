package hello;

public class Signal {

    private double xAxis;
    private double yAxis;
    private double zAxis;
    private double time;

    public Signal() {

    }

    public Signal(double xAxis, double yAxis, double zAxis, double time) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.zAxis = zAxis;
        this.time = time;
    }

    public double getxAxis() {
        return xAxis;
    }

    public double getyAxis() {
        return yAxis;
    }

    public double getzAxis() {
        return zAxis;
    }

    public double getTime() {
        return time;
    }

    public void setxAxis(double xAxis) {
        this.xAxis = xAxis;
    }

    public void setyAxis(double yAxis) {
        this.yAxis = yAxis;
    }

    public void setzAxis(double zAxis) {
        this.zAxis = zAxis;
    }

    public void setTime(double time) {
        this.time = time;
    }
}
