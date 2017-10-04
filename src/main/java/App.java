package main.java;

import java.io.FileNotFoundException;
import java.io.IOException;
public class App{

    public static void Draw(MooreMachines moore)  {
        GraphX graphX1 = new GraphX();
        graphX1.runVisualization(moore);
    }

    public static void main(String[] args) {
        try {
            MooreMachines moore = new MooreMachines();
            moore.readTableFromFile("C:\\Users\\vadim\\IdeaProjects\\Visualization\\src\\main\\resources/input.txt");
            Minimization minimization = new Minimization(moore);
            MooreMachines resultMinimization = minimization.getMinimizeMooreMachines();

            Draw(moore);
            Draw(resultMinimization);


            //JGraphX graphX2 = new JGraphX("graph2.png");
            //graphX2.runVisualization(table.getOutputSignals(), table.getState(), table.getDicList());

        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}