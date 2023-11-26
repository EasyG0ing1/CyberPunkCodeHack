package com.simtechdata.process;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * The process of finding all solutions is bit obtuse from a coding perspective.
 *
 * The process is to start with the first code sequence and iterate through all of its values and check each
 * code sequence using the checkValue() method in the ValueSet class. That will always cause the first code
 * sequence to be loaded. Then you have to iterate through all code sequences starting from the first and
 * get the first code that is in a slot 0 of that sequence. This becomes the next number to check etc.
 *
 * Next you reset all the code sequences so that the first value is in slot 0.
 *
 * Once you've run through all the sequences using the first sequence as the initial values, you have
 * to do the same process again, only you start with the next code sequence ... keep repeating that pattern
 * until all the code sequences have been the starting values to check.
 *
 * Because of the way the ValueSet class works, the shifting of numbers and the providing of the correct
 * next value to check is handled automatically where this class simply performs the checking logic that I
 * just described.
 */
public class Handler {

    private static final Map<Integer, ValueSet> setMap = new HashMap<>();
    private static int mapIndex = 1;
    private static int bufferSize = 0;
    private static String[] startSet;
    private static int startSetIndex;
    private static String[] solSet;
    private static boolean solutionFull;
    private static final LinkedList<String[]> solutionList = new LinkedList<>();


    public static void setBufferSize(int size) {
        bufferSize = size;
    }

    public static void addSet(String[] set) {
        setMap.put(mapIndex, new ValueSet(set));
        mapIndex++;
    }

    public static LinkedList<String[]> getSolutionList() {
        findSolutions();
        return solutionList;
    }

    public static void findSolutions() {
        int lastNumber = mapIndex - 1;  // Replace this with your actual last number
        for (int i = 1; i <= lastNumber; i++) {
            solSet = newSolutionSet();
            boolean failed = false;
            solutionFull = false;
            reset();
            boolean solved = false;
            startSet = setMap.get(i).getCoreSet();
            startSetIndex = 0;
            while (!failed && !solved) {
                String nextValue = getNextValue();
                if (!failed) {
                    for (int j = i; j <= lastNumber; j++) {
                        checkValueFor(j, nextValue);
                    }
                    for (int k = 1; k < i; k++) {
                        checkValueFor(k, nextValue);
                    }
                    nextSolValue(nextValue);
                    if (solutionFull && !allLoaded()) {
                        failed = true;
                        break;
                    }
                }
                //Here we have findSolutions through all the ValueSets from the next starting position.
                solved = allLoaded();
            }
            if (!failed) {
                solutionList.addLast(solSet);
            }
        }
    }

    private static String getNextValue() {
        int lastIndex = startSet.length - 1;
        boolean pullFromSet = startSetIndex <= lastIndex;
        if (pullFromSet) {
            String value = startSet[startSetIndex];
            startSetIndex++;
            return value;
        }
        for (int key : setMap.keySet()) {
            if (setMap.get(key).notLoaded()) {
                return setMap.get(key).getZero();
            }
        }
        return null;
    }

    private static void checkValueFor(int index, String value) {
        setMap.get(index).checkValue(value);
    }

    private static boolean allLoaded() {
        boolean allLoaded = true;
        for (ValueSet set : setMap.values()) {
            if (set.notLoaded()) {
                allLoaded = false;
                break;
            }
        }
        return allLoaded;
    }

    private static void reset() {
        for (ValueSet valueSet : setMap.values()) {
            valueSet.reset();
        }
    }

    private static void nextSolValue(String value) {
        int usedIndex = 0;
        for (int x = 0; x < solSet.length; x++) {
            String currentValue = solSet[x];
            if (currentValue.isEmpty()) {
                solSet[x] = value;
                usedIndex = x;
                break;
            }
        }
        if (usedIndex == (solSet.length - 1)) {
            solutionFull = true;
        }
    }

    private static String[] newSolutionSet() {
        String[] set = new String[bufferSize];
        for (int x = 0; x < bufferSize; x++) {
            set[x] = "";
        }
        return set;
    }

}
