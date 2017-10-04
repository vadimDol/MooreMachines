package main.java;

import java.lang.reflect.Array;
import java.util.*;
public class Minimization {
    private final String DELIMITER = "/";
    private MooreMachines mMoore;
    public Minimization(MooreMachines moore) throws CloneNotSupportedException {
        mMoore = new MooreMachines(moore);
        //check copy constructor
        /*mMoore.getState().clear();
        mMoore.printTable();
        System.out.println("_______________");
        moore.printTable();*/
    }

    private MooreMachines getResultingTable(Map<String, ArrayList<String>> equivalenceMap ) {
        MooreMachines moore = new MooreMachines();

        //init output signal
        for (Map.Entry<String, ArrayList<String>> pair : equivalenceMap.entrySet()) {
            String key = pair.getKey();
            moore.getOutputSignals().add(key.split(DELIMITER)[0]);
        }

        //init state
        for (int i = 0; i < moore.getOutputSignals().size(); ++i) {
            moore.getState().add(/*"q" + */"" + (i + 1));
        }

        ArrayList<ArrayList<String>> mooreTable =  moore.getTable();
        for(int i = 0; i < mMoore.getTable().size(); ++i) {
            mooreTable.add(new ArrayList<String>());

        }

        for(Map.Entry<String, ArrayList<String>> pair : equivalenceMap.entrySet()) {
            String state = pair.getValue().get(0);
            int indexStateInStartTable =  getIndexByStateName(state);
            for(int indexStartTable = 0; indexStartTable < mMoore.getTable().size(); indexStartTable++) {
                String transition = mMoore.getTable().get(indexStartTable).get(indexStateInStartTable);

                int i = 0;
                for(Map.Entry<String, ArrayList<String>> pairEquivalence : equivalenceMap.entrySet()) {
                    if (pairEquivalence.getValue().contains(transition)) {
                        mooreTable.get(indexStartTable).add("" + (i + 1));

                    }
                    ++i;
                }
            }
        }
        return moore;
    }

    private boolean compareEquivalentClass(Map<String, ArrayList<String>> oldEquivalentMap,
                                           Map<String, ArrayList<String>> newEquivalentMap) {

        if(oldEquivalentMap.size() != newEquivalentMap.size()) {
            return false;
        }
        Set<ArrayList<String>> values1 = new HashSet<>(oldEquivalentMap.values());
        Set<ArrayList<String>> values2 = new HashSet<>(newEquivalentMap.values());
        return values1.equals(values2);
    }

    private void splittingClassesEquivalence(Map<String, ArrayList<String>> equivalenceMap, MooreMachines resultMoore ) {
        while(true) {
            Map<String, ArrayList<String>> newEquivalentMap = getEquivalent(resultMoore);

            resultMoore.getTable().clear();
            resultMoore.getState().clear();
            resultMoore.getOutputSignals().clear();

            //init output signal
            for (Map.Entry<String, ArrayList<String>> pair : newEquivalentMap.entrySet()) {
                String key = pair.getKey();
                for(int i = 0; i < pair.getValue().size(); ++i) {
                    resultMoore.getOutputSignals().add(key.split(DELIMITER)[0]);
                }
            }

            //init state
            for(Map.Entry<String, ArrayList<String>> pair : newEquivalentMap.entrySet()) {
                for(String state : pair.getValue()) {
                    resultMoore.getState().add(state);
                }
            }

            ArrayList<ArrayList<String>> mooreTable =  resultMoore.getTable();
            for(int i = 0; i < mMoore.getTable().size(); ++i) {
                mooreTable.add(new ArrayList<String>());
            }

            for(Map.Entry<String, ArrayList<String>> pair : newEquivalentMap.entrySet()) {
                MooreMachines copyMooreMachines = new MooreMachines(mMoore);
                for(int i = 0; i < pair.getValue().size(); ++i) {
                    int indexStateInStartTable =  getIndexByStateName(pair.getValue().get(i));
                    for(int indexStartTable = 0; indexStartTable < copyMooreMachines.getTable().size(); indexStartTable++) {
                        String transition = copyMooreMachines.getTable().get(indexStartTable).get(indexStateInStartTable);
                        int index = 0;
                        for(Map.Entry<String, ArrayList<String>> pairEquivalence : newEquivalentMap.entrySet()) {

                            if (pairEquivalence.getValue().contains(transition)) {
                                mooreTable.get(indexStartTable).add("" + (index + 1));
                                break;
                            }
                            ++index;
                        }
                    }
                }
            }
            System.out.println("=======================");
            System.out.println(newEquivalentMap);
            System.out.println("=======================");
            if(compareEquivalentClass(equivalenceMap, newEquivalentMap)) {
                break;
            }
            equivalenceMap.putAll(newEquivalentMap);

        }
    }

    public MooreMachines getMinimizeMooreMachines() {
        MooreMachines resultMoore = mMoore.copy();

        zeroEquivalence(resultMoore);
        System.out.println("________~0 экв________");
        System.out.println(resultMoore.getOutputSignals());
        System.out.println(resultMoore.getState());
        for(ArrayList<String> array : resultMoore.getTable()) {
            System.out.println(array);
        }
        System.out.println("_______________________");


        Map<String, ArrayList<String>> equivalenceMap = new LinkedHashMap<String, ArrayList<String>>();
        splittingClassesEquivalence(equivalenceMap, resultMoore);



        resultMoore = getResultingTable(equivalenceMap);
        System.out.println("________ResultingTable________");
        System.out.println(resultMoore.getOutputSignals());
        System.out.println(resultMoore.getState());
        for(ArrayList<String> array : resultMoore.getTable()) {
            System.out.println(array);
        }
        System.out.println("_______________________");

        System.out.println("________StartTable________");
        System.out.println(mMoore.getState());
        System.out.println(mMoore.getOutputSignals());
        for(ArrayList<String> array : mMoore.getTable()) {
            System.out.println(array);
        }
        System.out.println("_______________________");
        return resultMoore;
    }

    private Map<String, ArrayList<String>> getEquivalent(MooreMachines resultMoore) {
        Map<String, ArrayList<String>> result = new LinkedHashMap<String, ArrayList<String>>();
        for(int i = 0; i < resultMoore.getState().size(); i++) {
            String key = resultMoore.getOutputSignals().get(i);

            for(ArrayList<String> row : resultMoore.getTable()) {
                key += "/" + row.get(i);
            }
            ArrayList<String> array = new ArrayList<String>();
            if(result.containsKey(key)) {
                array = new ArrayList<String>(result.get(key));
            }
            array.add(resultMoore.getState().get(i));
            result.put(key, array);
        }
        return result;
    }

    private void sort(MooreMachines resultMoore) {
        MooreMachines copyMooreMachines = mMoore.copy();

        ArrayList<String> state = resultMoore.getState(); state.clear();
        ArrayList<String> outputSignal = resultMoore.getOutputSignals(); outputSignal.clear();
        ArrayList<ArrayList<String>> table = resultMoore.getTable(); table.clear();
        table = new ArrayList<ArrayList<String>>();
        for(int i = 0; i < copyMooreMachines.getTable().size(); ++i) {
            table.add(new ArrayList<String>());
        }
        Set setOutSignalIndex = new HashSet();
        for(int index = 0; index < copyMooreMachines.getOutputSignals().size(); index++) {
            for(int outSignalIndex : getAllIndex(copyMooreMachines.getOutputSignals().get(index))) {
                if(!setOutSignalIndex.contains(outSignalIndex)) {
                    outputSignal.add(copyMooreMachines.getOutputSignals().get(index));
                    state.add("" + (outSignalIndex + 1));
                    for(int i = 0; i < table.size(); ++i) {
                        table.get(i).add(copyMooreMachines.getTable().get(i).get(outSignalIndex));
                    }
                    setOutSignalIndex.add(outSignalIndex);
                }
            }
        }
        for(int i = 0; i < table.size(); ++i) {
            resultMoore.getTable().add(table.get(i));
        }
    }

    private void zeroEquivalence(MooreMachines resultMoore) {
        resultMoore.getState().clear();
        for(int i = 0; i < resultMoore.getState().size(); ++i) {
            for(int j = 0; j < resultMoore.getTable().size(); ++j) {
                resultMoore.getTable().get(j).set(i, resultMoore.getOutputSignals().get(
                        getIndexByStateName(resultMoore.getTable().get(j).get(i))));
            }

        }

        sort(resultMoore);
    }

    private int getIndexByStateName(String name)
    {
        for(String _item : mMoore.getState()) {
            if(_item.equals(name))
                return mMoore.getState().indexOf(_item);
        }
        return -1;
    }
    private ArrayList<Integer> getAllIndex(String name)
    {
        ArrayList<Integer> array = new ArrayList<Integer>();
        ArrayList<String> outSignals = mMoore.getOutputSignals();
        for(int i = 0; i < outSignals.size(); ++i) {
            if(outSignals.get(i).equals(name)) {
                array.add(i);
            }
        }
        return array;
    }
}
