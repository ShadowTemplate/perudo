package entity;

import java.io.Serializable;

public class PlayerTO implements Serializable {

    private String name;
    private boolean man;
    private SerializableBufferedImage avatar;

    private final boolean physical; //Exists in real life
    private boolean CPUControlled; //Bets automatically

    private double bluffCoefficient;
    private int betMadeNumber;
    private int exactBetMadeNumber;
    private double bluffBetPercentage;

    public PlayerTO(String name, boolean isMan, SerializableBufferedImage avatar, boolean isPhysical, boolean isCPUControlled,
            double bluffCoefficient, int betMadeNumber, int exactBetMadeNumber, double bluffBetPercentage) {
        this.name = name;
        this.man = isMan;
        this.avatar = avatar;

        this.physical = isPhysical;
        this.CPUControlled = isCPUControlled;

        this.bluffCoefficient = bluffCoefficient;
        this.betMadeNumber = betMadeNumber;
        this.exactBetMadeNumber = exactBetMadeNumber;
        this.bluffBetPercentage = bluffBetPercentage;
    }

    public PlayerTO(PlayerTO playerTO) {
        this.name = playerTO.name;
        this.man = playerTO.man;
        this.avatar = playerTO.avatar;

        this.physical = playerTO.physical;
        this.CPUControlled = playerTO.CPUControlled;

        this.bluffCoefficient = playerTO.bluffCoefficient;
        this.betMadeNumber = playerTO.betMadeNumber;
        this.exactBetMadeNumber = playerTO.exactBetMadeNumber;
        this.bluffBetPercentage = playerTO.bluffBetPercentage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isMan() {
        return man;
    }

    public void setMan(boolean isMan) {
        this.man = isMan;
    }

    public SerializableBufferedImage getAvatar() {
        return avatar;
    }

    public void setAvatar(SerializableBufferedImage avatar) {
        this.avatar = avatar;
    }

    public boolean isPhysical() {
        return physical;
    }

    public boolean isCPUControlled() {
        return CPUControlled;
    }

    public void setCPUControlled(boolean CPUControlled) {
        this.CPUControlled = CPUControlled;
    }

    public double getBluffCoeff() {
        return bluffCoefficient;
    }

    public void setBluffCoeff(double bluffCoeff) {
        this.bluffCoefficient = bluffCoeff;
    }

    public int getBetMadeNumber() {
        return betMadeNumber;
    }

    public void setBetMadeNumber(int num) {
        this.betMadeNumber = num;
    }

    public void increaseBetMadeNumber() {
        this.betMadeNumber += 1;
    }

    public int getExactBetMadeNumber() {
        return exactBetMadeNumber;
    }

    public void setExactBetMadeNumber(int num) {
        this.exactBetMadeNumber = num;
    }

    public void increaseExactBetMadeNumber() {
        this.exactBetMadeNumber += 1;
    }

    public double getBluffBetPercentage() {
        return bluffBetPercentage;
    }

    public void setBluffBetPercentage(double coeff) {
        this.bluffBetPercentage = coeff;
    }

    public double getPercentageWrongBets() {
        return (double) (betMadeNumber - exactBetMadeNumber) / (double) betMadeNumber;
    }

    @Override
    public String toString() {
        return name + " " + (man ? "man " : "woman ") + (avatar == null ? "no_avatar " : "with_avatar ")
                + (physical ? "physical " : "robot ") + (CPUControlled ? "CPU " : "human ")
                + " - BC: " + bluffCoefficient + ", bet made: " + betMadeNumber + " (exact: " + exactBetMadeNumber + "), B%:  " + bluffBetPercentage;
    }
}
