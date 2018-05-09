package entity;

import java.io.Serializable;
import java.util.ArrayList;
import miscellanea.Constants;

public class OpponentRepresentation implements Serializable {

    private ArrayList<Die> predictedDice;
    private double estimatedCB;

    public OpponentRepresentation() {
        predictedDice = new ArrayList<>();
        estimatedCB = Constants.DEFAULT_BLUFF_COEFF;
    }

    public void addPrediction(Die d) {
        predictedDice.add(d);
    }

    public void resetPrediction() {
        predictedDice = new ArrayList<>();
    }

    public int count(Die d) {
        int count = 0;
        for (Die die : predictedDice) {
            if (die == d) {
                count++;
            }
        }
        return count;
    }

    public int size() {
        return predictedDice.size();
    }

    public void remove(int index) {
        predictedDice.remove(index);
    }

    public Die get(int index) {
        return predictedDice.get(index);
    }

    @Override
    public String toString() {
        String ret = "";
        for (Die d : predictedDice) {
            ret += d.toString() + "  ";
        }
        return ret;
    }

    public double getEstimatedCB() {
        return estimatedCB;
    }

    public void setEstimatedCB(double estimatedCB) {
        this.estimatedCB = estimatedCB;
    }
}
