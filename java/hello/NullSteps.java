package hello;

import javax.validation.constraints.Null;

public class NullSteps {
    private int toZero;

    public NullSteps() {

    }
    public NullSteps(int toZero) {
        this.toZero = toZero;
    }

    public int getToZero() {
        return toZero;
    }

    public void setToZero(int toZero) {
        this.toZero = toZero;
    }

}
