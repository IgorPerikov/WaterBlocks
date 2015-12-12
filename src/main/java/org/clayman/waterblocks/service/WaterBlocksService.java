package org.clayman.waterblocks.service;

import org.clayman.waterblocks.domain.Result;
import org.clayman.waterblocks.domain.WaterArea;

import java.util.Set;

public interface WaterBlocksService {
    int countWaterCapacity(String sequence, Set<WaterArea> waterAreas);

    Set<WaterArea> foundWaterAreaBorders(String sequence);

    int[][] createGridFromSequenceArray(int[] array);

    int[] getIntArrayFromSequence(String sequence);
}
