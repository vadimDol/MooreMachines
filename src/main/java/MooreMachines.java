package main.java;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class MooreMachines implements Cloneable{
    private final String DELIMITER = " ";
    private ArrayList<ArrayList<String>> mTable;
    private ArrayList<String> mState;
    private ArrayList<String> moutSignals;
    public MooreMachines(){
        this.mTable = new ArrayList<ArrayList<String>>();
        this.mState = new ArrayList<String>();
        this.moutSignals = new ArrayList<String>();
    }
    public MooreMachines(MooreMachines original) {
        this.mTable = new ArrayList<ArrayList<String>>(original.getTable());
        this.mState = new ArrayList<String> (original.getState());
        this.moutSignals = new ArrayList<String> (original.getOutputSignals());
    }

    public ArrayList<ArrayList<String>> getTable() {
        return mTable;
    }

    public ArrayList<String> getOutputSignals() {
        return moutSignals;
    }

    public ArrayList<String> getState() {
        return mState;
    }

    public void readTableFromFile(String filePath) throws FileNotFoundException {
        Scanner scanner = null;
        FileReader file = new FileReader(filePath);
        scanner = new Scanner(new BufferedReader(file));

        String line = scanner.nextLine();
        moutSignals = new ArrayList<String>();
        for(String element : line.split(DELIMITER)) {
            if(!element.isEmpty()) {
                moutSignals.add(element);
            }
        }

        line = scanner.nextLine();
        mState = new ArrayList<String>();
        for(String element : line.split(DELIMITER)) {
            if(!element.isEmpty()) {
                mState.add(element);
            }
        }


        mTable = new ArrayList<ArrayList<String>>();
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            String[] elements = line.split(DELIMITER);
            ArrayList<String> transitions = new ArrayList<String>();
            for(int i = 1; i < elements.length; ++i) {
                if(!elements[i].isEmpty()) {
                    transitions.add(elements[i]);
                }
            }
            mTable.add(transitions);
        }

    }

    public void printTable() {
        System.out.println("  " + moutSignals);
        System.out.println("  " + mState);
        System.out.println("  " + mTable);

    }
}
