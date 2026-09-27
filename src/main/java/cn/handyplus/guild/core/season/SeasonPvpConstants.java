package cn.handyplus.guild.core.season;

import java.util.Arrays;
import java.util.List;

public class SeasonPvpConstants {
    public final static int M = 60;
    public final static List<Integer> F;
    public final static List<Integer> zGTXUt;

    private SeasonPvpConstants() {
    }

    static {
        Integer[] rankArray = new Integer[5];
        rankArray[0] = 1;
        rankArray[1] = 10;
        rankArray[2] = 20;
        rankArray[3] = 30;
        rankArray[4] = 50;
        zGTXUt = Arrays.asList(rankArray);
        Integer[] secondArray = new Integer[9];
        secondArray[0] = 20;
        secondArray[1] = 30;
        secondArray[2] = 40;
        secondArray[3] = 50;
        secondArray[4] = 56;
        secondArray[5] = 57;
        secondArray[6] = 58;
        secondArray[7] = 59;
        secondArray[8] = 60;
        F = Arrays.asList(secondArray);
    }
}
