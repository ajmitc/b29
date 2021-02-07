package b29.util;

import java.util.Date;
import java.util.Random;

public class Util {
    private static Random GEN = new Random(new Date().getTime());

    public static int roll() {
        return GEN.nextInt(6) + 1;
    }

    public static int roll(int numDice, boolean sum) {
        if (numDice == 0) {
            return 0;
        }
        StringBuilder sb = new StringBuilder();
        int total = 0;
        for (int i = 0; i < numDice; ++i) {
            int result = roll();
            total += result;
            sb.append(result);
        }
        if (sum) {
            return total;
        }
        return Integer.decode(sb.toString());
    }

    public static int roll2d(){
        return roll(2, true);
    }

    public static int getRandomEngine(){
        int die = roll2d();
        if (die == 4 || die == 10 || die == 11)
            return 2;
        else if (die == 5 || die == 6 || die == 12)
            return 3;
        else if (die == 8 || die == 9)
            return 4;
        return 1;
    }

    private Util() {
    }
}
