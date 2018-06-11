package hello;

import java.util.ArrayList;
import java.util.List;


public class StepCounter {
    private ArrayList<Signal> signals = new ArrayList<>();
    private ArrayList<Double> magnitudes = new ArrayList<>();
    private ArrayList<Double> unfilteredMagnitudes = new ArrayList<>();
    private final static int BUFFER_SIZE = 22;
    private final static int UNFILTERED_BUFFER_SIZE = 5;
    private final static double C = 0.8;
    private final static double K = 11.5;
    private double firstPreviousMagnitude = 10.2;
    private double secondPreviousMagnitude = 10.2;
    private static int steps;
    //Butterworth butterworth = new Butterworth();

    public StepCounter(){
    }


    public void addToCountingList(Signal incoming) {
        //signals.addAll(incoming.getSignals());
        //for(Signal signal : incoming.getSignals()) {
            double mag = calculateMagnitude(incoming);
            unfilteredMagnitudes.add(mag);

            if (magnitudes.size() == 0) {
                magnitudes.add(firstPreviousMagnitude);
                magnitudes.add(secondPreviousMagnitude);
            }

            if (unfilteredMagnitudes.size() == UNFILTERED_BUFFER_SIZE) {
                filterMagnitudes(unfilteredMagnitudes);
            }

            if (magnitudes.size() == BUFFER_SIZE) {
                //steps += stepCountingAlgoritm();
                stepCountingAlgoritm(magnitudes);
                System.out.println("Current steps: " + steps);

                secondPreviousMagnitude = magnitudes.get(magnitudes.size() - 1);
                firstPreviousMagnitude = magnitudes.get(magnitudes.size() - 2);

                magnitudes.clear();
            }
       // }


    }

    public void filterMagnitudes(List<Double> signals) {
        double sum = 0;
        for(double i : signals) {
            sum += i;
        }
        double mean = sum/signals.size();
        magnitudes.add(mean);
        unfilteredMagnitudes.clear();
    }

    public double calculateMagnitude(Signal signal) {
        return Math.sqrt(Math.pow(signal.getxAxis(), 2) + Math.pow(signal.getyAxis(), 2)
                + Math.pow(signal.getzAxis(), 2));
    }

    public void stepCountingAlgoritm(List<Double> mags) {
        double peakMean = getPeakMean();

        for (int i = 1; i < mags.size() - 1; i++) {
            double forwardSlope = mags.get(i + 1) - mags.get(i);
            double backwardSlope = mags.get(i) - mags.get(i - 1);
            if (forwardSlope < 0 && backwardSlope > 0 && mags.get(i) > C * peakMean && mags.get(i) > K) {
                steps++;
            }
        }
    }

    public double getPeakMean() {
        int peakCount = 0;
        double peakAccumulate = 0;

        for (int i = 1; i < magnitudes.size() - 1; i++) {
            double forwardSlope = magnitudes.get(i + 1) - magnitudes.get(i);
            double backwardSlope = magnitudes.get(i) - magnitudes.get(i - 1);
            if (forwardSlope < 0 && backwardSlope > 0 && magnitudes.get(i) > K) {
                peakCount++;
                peakAccumulate += magnitudes.get(i);
            }
        }

        return peakAccumulate/peakCount;
    }

    public int getCurrentStepCount() {
        return steps;
    }

    public void setCurrentStepCountToZero() { steps = 0; }
}
