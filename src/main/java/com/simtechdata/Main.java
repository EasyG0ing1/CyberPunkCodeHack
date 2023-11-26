package com.simtechdata;

import com.simtechdata.process.Handler;
import com.simtechdata.settings.AppSettings;

import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("\n\nCyberPunk Code Hacker\n\n");
        for (String arg : args) {
            if (arg.equalsIgnoreCase("test")) {
                test();
            }
            if (arg.equalsIgnoreCase("clean")) {
                AppSettings.clear.bufferSize();
                System.out.println("Buffer size cleared");
                System.exit(0);
            }
        }
        setBuffer();
        setCodeSequences();
        showSolutions(Handler.getSolutionList());
        System.exit(0);
    }

    private static void setBuffer() {
        String message = "Enter the size of your buffer";
        int bufferSize = AppSettings.get.bufferSize();
        message += ((bufferSize > 0) ? " or just hit enter (" + bufferSize + "): " : ": ");
        String bufferString = "";
        while (bufferString.isEmpty() || bufferSize < 1) {
            System.out.print(message);
            bufferString = scanner.nextLine().replaceAll("[^0-9]+", "");
            boolean empty = bufferString.isEmpty();
            if (empty && bufferSize == 0) {
                System.out.println("\n\nMust enter in a valid number.\n");
            }
            else if (!empty) {
                bufferSize = Integer.parseInt(bufferString);
                AppSettings.set.bufferSize(bufferSize);
            }
            if (bufferSize < 1) {
                System.out.println("Buffer size must be larger than 0.\n\n");
            }
            else if(empty) {
                bufferString = String.valueOf(bufferSize);
            }
        }
        Handler.setBufferSize(bufferSize);
    }

    private static void setCodeSequences() {
        int valueSet = 1;
        System.out.print("\nNext, enter each code sequence (separate each value with a space). To end, just hit enter.\n\n");
        String line = "seed";
        while (!line.isEmpty()) {
            System.out.print("Code Sequence " + valueSet + ": ");
            line = scanner.nextLine().toUpperCase().replaceAll("\\s+", " ").trim();
            if (!line.isEmpty()) {
                String[] set = line.trim().split(" ");
                boolean error = false;
                for (String value : set) {
                    if (notValidHexNumber(value)) {
                        System.out.println("\nEach value in the sequence must be two characters and can only contain the numbers 0-9 and/or the letters A-F (ex: 2A BD C5 6F)\n");
                        error = true;
                        break;
                    }
                }
                if (!error) {
                    Handler.addSet(set);
                    valueSet++;
                }
            }
        }
    }

    private static void showSolutions(LinkedList<String[]> solutionList) {
        String message;
        if (solutionList == null || solutionList.isEmpty()) {
            message = "\nNo solutions for all sets";
        }
        else if (solutionList.size() == 1) {
            message = "\nSolution:\n\n\t";
        }
        else {
            message = "\nSolutions:\n\n\t";
        }
        StringBuilder sb = new StringBuilder(message);
        for (String[] set : solutionList) {
            int len = set.length;
            for (int x = 0; x < len; x++) {
                sb.append(set[x]).append(" ");
            }
            sb.append("\n\t");
        }
        System.out.println(sb);
    }

    public static boolean notValidHexNumber(String hexString) {
        String regex = "^[0-9A-F]{2}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(hexString);
        return !matcher.matches();
    }


    public static void test() {
        String input1 = "1C 1C 55";
        String input2 = "55 FF 1C";
        String input3 = "BD E9 BD 55";
        String input4 = "55 1C FF BD";

        String[] set1 = input1.split(" ");
        String[] set2 = input2.split(" ");
        String[] set3 = input3.split(" ");
        String[] set4 = input4.split(" ");

        Handler.setBufferSize(7);
        Handler.addSet(set1);
        Handler.addSet(set2);
        Handler.addSet(set3);
        Handler.addSet(set4);
        showSolutions(Handler.getSolutionList());
        System.exit(0);
    }
}
