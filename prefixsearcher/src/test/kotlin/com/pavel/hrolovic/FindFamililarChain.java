package com.pavel.hrolovic;

import org.junit.Assert;
import org.junit.Test;

public class FindFamililarChain {


    public boolean hasChain(int[] input, int total) {
        int sum = 0, previousNumber = 0;
        for (int nextNumber : input) {
            sum = nextNumber + sum;
            while (sum > total) {
                sum = sum - input[previousNumber];
                previousNumber++;
            }
            if (sum == total) return true;
        }
        return false;
    }

    @Test
    public void test() {
        Assert.assertTrue(hasChain(new int[]{6, 1, 3, 5, 7, 87}, 8));
        Assert.assertTrue(hasChain(new int[]{6, 1, 3, 5, 7, 87}, 87));
        Assert.assertFalse(hasChain(new int[]{6, 1, 3, 5, 7, 87}, 800));
    }

}
