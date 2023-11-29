package com.simtechdata;

import com.simtechdata.process.Handler;
import com.simtechdata.process.models.Solution;
import com.simtechdata.process.models.ValueSet;
import com.simtechdata.settings.AppSettings;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Scanner;
import java.util.prefs.BackingStoreException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final boolean STEALTH = AppSettings.get.stealthMode();
    private static int bufferSize = AppSettings.get.bufferSize();
    private static final String LF = System.getProperty("line.separator");


    public static void main(String[] args) throws BackingStoreException {
        processArgs(args);

        if (STEALTH) {
            Handler.setBufferSize(bufferSize);
        }
        else {
            String title = "CyberPunk Code Hacker";
            int len = title.length();
            System.out.println(LF + "*".repeat(len + 4));
            System.out.println("* " + title + " *");
            System.out.println("*".repeat(len + 4));
            setBuffer();
            System.out.println(LF + "*".repeat(44) + LF + "* ENTER THE WORD \"HELP\" TO LEARN DIFFERENT *" + LF +
                                       "*   WAYS YOU CAN TYPE IN CODE SEQUENCES    *" + LF + "*".repeat(44));
        }

        System.out.println("\nBuffer Size: " + bufferSize);

        if(STEALTH)
            System.out.println("Use 'q' to quit");

        loopInput();
    }

    private static void processArgs(String[] args) throws BackingStoreException {
        for (String arg : args) {
            if (arg.equalsIgnoreCase("test")) {
                test();
            }
            if (arg.equalsIgnoreCase("clean")) {
                AppSettings.clear.clearAll();
                System.out.println("All settings cleared");
                exit();
            }
            if (arg.equalsIgnoreCase("help")) {
                showArgHelp();
                exit();
            }
            if (arg.equalsIgnoreCase("info")) {
                showInfo();
                exit();
            }
            if (arg.startsWith("buf=")) {
                String value = arg.split("=")[1];
                if (!value.matches("[0-9]+")) {
                    System.out.println("Incorrect, use this example: buf=7");
                    exit();
                }
                else {
                    int bufferSize = Integer.parseInt(value);
                    AppSettings.set.bufferSize(bufferSize);
                    System.out.println("Buffer size has been set to: " + bufferSize);
                }
                exit();
            }
            if (arg.startsWith("st")) {
                if (bufferSize == 0) {
                    System.out.println("You must set the size of your buffer first. Run 'cphack help' for more info.");
                    exit();
                }
                AppSettings.set.stealthMode(true);
                System.out.println("Stealth Mode Enabled");
                exit();
            }
            if (arg.startsWith("-st")) {
                AppSettings.set.stealthMode(false);
                System.out.println("Stealth Mode Disabled");
                exit();
            }
            if (arg.startsWith("ver")) {
                Properties properties = new Properties();
                try (InputStream input = Main.class.getResource("/META-INF/maven/com.simtechdata/CyberPunkCodeHacker/pom.properties").openStream()) {
                    if (input == null) {
                        System.out.println("Version Unknown");
                        exit();
                    }
                    properties.load(input);
                } catch (IOException ignored) {
                }
                String version = properties.getProperty("version");
                System.out.println("Version: " + version);
                exit();
            }
            if (arg.startsWith("gb")) {
                System.out.println("Buffer size: " + AppSettings.get.bufferSize());
                exit();
            }
        }
    }

    private static void loopInput() {
        while (true) {
            setCodeSequences();
            if (Handler.readyToSolve()) {
                showSolutions(Handler.getSolutionList());
            }
            Handler.reset();
        }
    }

    private static void showArgHelp() {
        String help = """
                                
                Command line arguments are toggles and only need to be used once. The program
                will remember the setting until you change it.
                                
                Here are your options for command line arguments:
                                
                st      Enable stealth mode which means that no messages will be displayed
                        and you will not be asked for the size of your buffer. You can only
                        enable this once you have initially set your buffer size. If you
                        upgrade your buffer, then you must either disable stealth mode to
                        enter in the new size or use the sb option (below).
                        
                        When stealth mode is enabled, cphack will show you what your buffer
                        size is set to each time you launch the program.
                        
                -st     Disable stealth mode.
                                
                buf=#   Set the size of your buffer.
                                
                gb      Get the currently set buffer size.
                                
                info    Learn about how the buffer can be used to overcome the situation when
                        it seems like the solution cannot be matched to your code matrix (ie
                        the first code of any given solution does not exist in the first row).
                        
                ver     Get the version number of cphack
                                
                Examples:
                                
                    To enable / disable stealth mode:
                                cphack st
                                cphack -st

                    Set your buffer size to 7:
                                cphack buf=7

                    See what your buffer size is set to:
                                cphack gb

                """;
        System.out.println(help);
    }

    private static void showInfo() {
        String info = """
                                
                To understand how to use the solutions that cphack generates, check the README at
                                
                            https://github.com/EasyG0ing1/CyberPunkCodeHack
                            
                                
                An interesting discovery I have made about how to use buffers in precarious situations:
                 
                Whenever you are going to enter in a solution, you are allowed to enter in a code that
                is not valid so that you can chose the next code in the row / column pattern, but only
                under two conditions:
                                
                    1) Before you start entering the actual solution for a given sequence.
                    2) As long as you have the buffer space to accommodate the non-solution code.
                                
                So lets say that for any of the solutions found by cphack, if none of the starting codes
                exists in the first row of your matrix, you'll have to start the first code using one
                of the columns in your matrix, which means that you will have to enter a non-matching code
                from the first row so that you can select the correct fist matching code from that column.
                                
                (And if you're solving sequences manually without using cphack)
                                
                This is also true if you happen to solve one of the sequences, but the first code for the
                next sequence that you want to solve is not in that last codes row or column, you can select
                an invalid code so long as the next code you choose is the correct match and then you can
                continue entering the remaining codes from there.
                                
                Again, this only works if your buffer is large enough to accommodate the solution and
                the incorrect code(s) you had to select.
                                
                """;
        System.out.println(info);
    }

    private static void showHelp() {
        String help = """
                There are two ways you can enter in code sequences, the first method being more efficient than the second:
                                
                Option 1:
                    Enter in all sequences on one line where you only use a space to separate each sequence.
                    For example These code sequences
                        
                        55 1C
                        1C 1C E9
                        BD E9 55
                   
                    Would get entered in like this:
                   
                        : 551C 1C1CE9 BDE955
                   
                Option 2:
                    Enter in each sequence one line at a time separating each code with a space.
                        
                        : 55 1C
                        : 1C 1C E9
                        : BD E9 55
                        
                You cannot use a combination of the two methods, you must use one or the other for each set of code sequences you need to solve.
                                
                """;
        System.out.println(help);
    }

    private static void setBuffer() {
        String message = "Enter the size of your buffer";
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
            else if (empty) {
                bufferString = String.valueOf(bufferSize);
            }
        }
        Handler.setBufferSize(bufferSize);
    }

    private static void setCodeSequences() {
        String line = "seed";
        if(!STEALTH) {
            System.out.println("Enter code sequences or q to quit");
        }
        while (!line.isEmpty()) {
            System.out.print(": ");
            line = scanner.nextLine().toUpperCase().replaceAll("\\s+", " ").trim();

            if (line.toLowerCase().contains("help")) {
                showHelp();
                continue;
            }

            if (line.contains("Q"))
                exit();

            if (processLine(line))
                line = "";
        }
    }

    private static boolean processLine(String line) {
        boolean allInOne = false;
        if (!line.isEmpty()) {
            String[] set = line.trim().split(" ");
            if (set.length > 0 && set[0].length() > 2) {
                processSets(set);
                allInOne = true;
            }
            else {
                if (setIsErrorFree(set)) {
                    Handler.addSet(set);
                }
            }
        }
        return allInOne;
    }

    private static boolean setIsErrorFree(String[] set) {
        boolean error = false;
        for (String value : set) {
            if (notValidHexNumber(value)) {
                System.out.println("\nEach value in the sequence must be two characters and can only contain the numbers 0-9 and/or the letters A-F (ex: 2A BD C5 6F)\n");
                error = true;
                break;
            }
        }
        return !error;
    }

    private static void processSets(String[] sets) {
        for (String setStr : sets) {
            if (setStr.length() % 2 == 0) {
                String[] set = new String[setStr.length() / 2];
                for (int i = 0, j = 0; i < setStr.length(); i += 2, j++) {
                    set[j] = setStr.substring(i, i + 2);
                }
                if (setIsErrorFree(set))
                    Handler.addSet(set);
            }
        }
    }

    private static void showSolutions(LinkedList<Solution> solutionList) {
        boolean noSolutions = solutionList == null || solutionList.isEmpty();
        boolean solutionsExist = !noSolutions;
        if (noSolutions) {
            String message = LF + "No solutions for all sequences at current buffer size";
            System.out.println(message);
        }
        if (solutionsExist) {
            presentNice(solutionList);
        }
    }

    public static boolean notValidHexNumber(String hexString) {
        String regex = "^[0-9A-F]{2}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(hexString);
        return !matcher.matches();
    }

    private static void presentNice(LinkedList<Solution> solList) {
        String divider = "|  ";
        StringBuilder sb = new StringBuilder(LF);
        String indent = " ".repeat(3);
        LinkedList<ValueSet> setList = Handler.getSequences();
        LinkedList<String> newSetList = new LinkedList<>();
        int longest = 0;
        for (ValueSet valueSet : setList) {
            newSetList.addLast(valueSet.toString());
            int len = valueSet.toString().length();
            longest = Math.max(longest, len);
        }
        int center = indent.length() + longest;
        sb.append("Sequences:").repeat(" ", center - 7).append(divider).append("Solutions:").append(LF);
        int seqListSize = newSetList.size();
        int solListSize = solList.size();
        boolean seqLarger = seqListSize > solListSize;

        int last = seqLarger ? seqListSize : solListSize;
        int maxSolIndex = solListSize - 1;
        int maxSeqIndex = seqListSize - 1;

        for (int x = 0; x < last; x++) {
            String seq = newSetList.get(x);
            String sol = solList.get(x).toString();
            int seqSize = seq.length();
            int repeat = center - seqSize;
            boolean stopSeq = x > maxSeqIndex;
            boolean stopSol = x > maxSolIndex;
            if (stopSeq) {
                sb.repeat(" ", center + indent.length()).append(divider).append(indent).append(sol).append(LF);
            }
            else if (stopSol) {
                sb.append(indent).append(seq).repeat(" ", repeat).append(divider).append(LF);
            }
            else {
                sb.append(indent).append(seq).repeat(" ", repeat).append(divider).append(indent).append(sol).append(LF);
            }
        }
        System.out.println(sb);
    }

    public static void test() {
        String input1 = "551C";
        String input2 = "1C1CE9";
        String input3 = "BDE955";
        String input4 = input1 + " " + input2 + " " + input3;
        // 551C 1C1CE9 BDE955
        processLine(input4);
        if (Handler.readyToSolve()) {
            showSolutions(Handler.getSolutionList());
        }
        else {
            System.out.println("No valid code sequences to solve, resetting.");
        }
        exit();
    }

    private static void exit() {
        System.exit(0);
    }
}
