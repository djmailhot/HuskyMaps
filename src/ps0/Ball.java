/**
 * This is part of the Problem Set 0: Introduction for CSE 331.
 */
package ps0;

/**
 * This is a simple object that has a volume.
 */
public class Ball {

    private double volume = 0;

    /**
     * Constructor that creates a new ball object with the specified volume.
     * @param volume Volume of the new object.
     */
    public Ball(double volume) {
        this.volume = volume;
    }

    /**
     * Returns the volume of the Ball.
     * @return the volume of the Ball.
     */
    public double getVolume() {
        return volume;
    }

}
