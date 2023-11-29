package com.simtechdata.process.models;

/**
 * This class makes it easier to sort the solutions by their size.
 */

public class Solution {
    private final String[] solution;
    private final int size;

    public Solution(String[] solution) {
        this.solution = solution;
        int count = 0;
        for(String code : solution) {
            if (code.length() == 2)
                count++;
        }
        this.size = count;
    }

    public int getSize() {
        return size;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(String s : solution) {
            sb.append(s).append(" ");
        }
        return sb.toString().trim();
    }
}
