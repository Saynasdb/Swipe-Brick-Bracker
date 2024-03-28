import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class CustomRandom {
    private final Random random;
    private final double x; // Parameter to adjust the probability

    public CustomRandom(double x) {
        this.random = ThreadLocalRandom.current();
        this.x = x;
    }

    public int nextInt() {
        double r = random.nextDouble(); // Generate a random double between 0.0 and 1.0
        if (r < x) {
            // Higher probability for numbers 4 to 6
            return  ThreadLocalRandom.current().nextInt(4,6); // Random number between 4 and 6
        } else {
            // Lower probability for numbers -1 to 3
            return ThreadLocalRandom.current().nextInt(-1,3); // Random number between -1 and 3
        }
    }


}
