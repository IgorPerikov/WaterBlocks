package org.clayman.waterblocks.service;

import org.clayman.waterblocks.domain.Result;
import org.clayman.waterblocks.domain.WaterArea;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class WaterBlocksServiceNaiveImpl implements WaterBlocksService {
    public int countWaterCapacity(String sequence, Set<WaterArea> waterAreas) {
        int[] array = getIntArrayFromSequence(sequence);

        int sum = 0;
        for (WaterArea waterArea : waterAreas) {
            int minBorder = Math.min(array[waterArea.getLeftBorder()], array[waterArea.getRightBorder()]);
            for (int i = waterArea.getLeftBorder() + 1; i < waterArea.getRightBorder(); i++) {
                if (array[i] <= minBorder) {
                    sum = sum + (minBorder - array[i]);
                }
            }
        }
        return sum;
    }

    public Set<WaterArea> foundWaterAreaBorders(String sequence) {
        int[] array = getIntArrayFromSequence(sequence);

        Set<WaterArea> waterAreas = new HashSet<>();

        for (int i = 1; i < array.length - 1; i++) {
            boolean leftBorder = false;
            boolean rightBorder = false;
            int leftBorderIndex = -1;
            int rightBorderIndex = -1;

            int contender = array[i];
            if (contender == 0) continue;

            for (int j = i - 1; j >= 0; j--) {
                if (array[j] <= 0) {
                    break;
                } else {
                    if (leftBorder) {
                        if (array[j] >= array[leftBorderIndex]) {
                            leftBorderIndex = j;
                        } else {
                            break;
                        }
                    } else {
                        if (array[j] > contender) {
                            leftBorder = true;
                            leftBorderIndex = j;
                        }
                    }
                }
            }

            for (int j = i + 1; j < array.length; j++) {
                if (array[j] <= 0) {
                    break;
                } else {
                    if (rightBorder) {
                        if (array[j] >= array[rightBorderIndex]) {
                            rightBorderIndex = j;
                        } else {
                            break;
                        }
                    } else {
                        if (array[j] > contender) {
                            rightBorder = true;
                            rightBorderIndex = j;
                        }
                    }
                }
            }

            if (leftBorder && rightBorder) {
                waterAreas.add(new WaterArea(leftBorderIndex, rightBorderIndex));
            }
        }

        return waterAreas;
    }

    @Override
    public int[][] createGridFromSequenceArray(int[] array) {
        int arraySize = array.length;
        int[][] grid = new int[arraySize][arraySize];

        for (int i = 0; i < arraySize; i++) {
            for (int j = 0; j < arraySize; j++) {
                if (arraySize - i > array[j]) {
                    grid[i][j] = 0;
                } else {
                    grid[i][j] = 1;
                }
            }
        }

        return grid;
    }

    public int[] getIntArrayFromSequence(String sequence) {
        String[] stringArray = sequence.split(" ");
        int[] array = new int[stringArray.length];
        for (int i = 0; i < stringArray.length; i++) {
            array[i] = Integer.parseInt(stringArray[i]);
        }
        return array;
    }
}

