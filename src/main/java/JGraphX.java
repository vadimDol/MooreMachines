package main.java;


import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.mxFastOrganicLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxMorphing;
import com.mxgraph.util.*;
import com.mxgraph.view.mxGraph;
import sun.security.provider.certpath.Vertex;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JGraphX {
    private  mxGraph graph;
    private  mxGraphComponent graphComponent;
    private  Object parent;
    private  Map<String, Object> vertex;
    private  JFrame frame;
    private  String exportFileName;

    public JGraphX(String fileName, MooreMachines moore)throws IOException {
        exportFileName = fileName;
        runVisualization(moore);
    }

    public  void close() {
        frame.dispose();
        graph = new mxGraph();
        graphComponent = new mxGraphComponent(graph);
        parent = new Object();
        vertex = new HashMap<String, Object>();
    }

    private  void runVisualization(MooreMachines moore) throws IOException {
        frame  = new JFrame();
        frame.setSize(1366, 900);
        frame.setLocation(0, 0);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        graph = new mxGraph();

        graphComponent = new mxGraphComponent(graph);
        frame.getContentPane().add(graphComponent, BorderLayout.CENTER);
        frame.setVisible(true);
        parent = graph.getDefaultParent();
        vertex = new HashMap<>();
        graph.getModel().beginUpdate();

        try {
            initVertices(moore.getOutputSignals(), moore.getState());
            initArc(moore.getState(), moore.getTable());

        } finally {
            graph.getModel().endUpdate();
        }

        layoutGraph();
        //saveAsImage();
    }

    private void saveAsImage() throws IOException {
        BufferedImage image = mxCellRenderer.createBufferedImage(graph, null, 1, Color.WHITE, true, null);
        ImageIO.write(image, "PNG", new File(exportFileName));
    }

    private void initVertices(ArrayList<String> outputSignals, ArrayList<String> state) {
        for(int i = 0; i < outputSignals.size(); ++i) {
            String id = "q" + state.get(i) + "/" + "y" + outputSignals.get(i);
            Object object =  graph.insertVertex(parent, id, id, 0, 0, 80, 30);
            vertex.put(state.get(i), object);
        }
    }

    private  void initArc(ArrayList<String> state, ArrayList<ArrayList<String>> table) {
        for(int index = 0; index < state.size(); index++){
            Object start = vertex.get(state.get(index));
            for(int i = 0; i < table.size(); ++i) {
                ArrayList<String> value = table.get(i);
                Object end = vertex.get(value.get(index));
                graph.insertEdge(parent, null, "x" + (i + 1), start, end);
            }
        }
    }

    private void layoutGraph() {
        mxHierarchicalLayout layout = new mxHierarchicalLayout(graph);
        layout.setOrientation(SwingConstants.WEST);
        layout.setMoveParent(false);
        layout.setResizeParent(false);
        layout.setFineTuning(true);

        graph.getModel().beginUpdate();
        try {
            layout.execute(graph.getDefaultParent());
        } finally {
            graph.getModel().endUpdate();
        }
    }
}