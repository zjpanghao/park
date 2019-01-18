package demo.springboot.domain;

public class ParkResult {
    private ParkType parkType;
    private float score;

    public ParkType getParkType() {
        return parkType;
    }

    public void setParkType(ParkType parkType) {
        this.parkType = parkType;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }
}
