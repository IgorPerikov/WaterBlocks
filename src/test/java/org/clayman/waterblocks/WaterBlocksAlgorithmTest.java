package org.clayman.waterblocks;

import org.clayman.waterblocks.service.WaterBlocksService;
import org.clayman.waterblocks.service.WaterBlocksServiceNaiveImpl;
import org.junit.Assert;
import org.junit.Test;

public class WaterBlocksAlgorithmTest {
    private static WaterBlocksService wbs = new WaterBlocksServiceNaiveImpl();
    private static String sequence1 = "7 4 3 1 4 0 3 5 2 4 6 7 7";
    private static String sequence2 = "4 2 3 4 0 3";

    @Test
    public void algTest() {
        Assert.assertEquals(wbs.countWaterCapacity(sequence1, wbs.foundWaterAreaBorders(sequence1)), 8);
        Assert.assertEquals(wbs.countWaterCapacity(sequence2, wbs.foundWaterAreaBorders(sequence2)), 3);
    }
}
