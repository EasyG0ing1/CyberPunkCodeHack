package com.simtechdata.process;

import java.util.HashMap;
import java.util.Map;


/**
 * This class represents a single code sequence from the game. When we are
 * checking values using the Handler class, we simply call the checkValue()
 * method in this class, passing to it, the currently interesting value and if
 * that value matches the index 0 slot of the sequence, the entire sequence
 * gets shifted to the left just as you can see the game doing it as you enter
 * in values that match.
 *
 * If the value does not match, then sequence gets reset so that the first
 * value is in position zero.
 *
 * Essentially, this lets us use the Handler class to simply pass values into
 * this class and to find out if the entire sequence was successfully loaded or not
 */

public class ValueSet {

    private Map<Integer, String> map = new HashMap<>();
    private final String[] values;
    private final int totalValues;
    private int leftShiftCount = 0;
    public ValueSet(String[] values) {
        this.values = values;
        this.totalValues = values.length;
        reloadMap();
    }

    public void checkValue(String value) {
        if(!loaded()) {
            String v = map.get(0);
            if(v == null) {
                System.out.print("Map Keys: ");
                for(int key : map.keySet()) {
                    System.out.print(key + " ");
                }
                System.out.println();
            }
            else {
                if(v.equals(value)) {
                    shiftLeft();
                }
                else {
                    reloadMap();
                }
            }
        }
    }

    public void reset() {
        reloadMap();
    }

    public boolean notLoaded() {
        return !loaded();
    }

    public String getZero() {
        return map.get(0);
    }

    public String[] getCoreSet() {
        return values;
    }

    private boolean loaded() {
        return totalValues == leftShiftCount;
    }

    private void reloadMap() {
        map.clear();
        for (int x = 0; x < values.length; x++) {
            String value = values[x];
            map.put(x, value);
        }
        leftShiftCount = 0;
    }

    private void shiftLeft() {
        Map<Integer, String> tempMap = new HashMap<>();
        for(Integer key : map.keySet()) {
            tempMap.put(key - 1, map.get(key));
        }
        map = new HashMap<>(tempMap);
        leftShiftCount++;
    }
}
