package main.java;

import com.mxgraph.layout.*;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxMorphing;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventSource;
import com.mxgraph.view.mxEdgeStyle;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxPerimeter;
import com.mxgraph.view.mxStylesheet;

import javax.swing.*;
import java.awt.*;
import java.util.*;

class GraphX {
    public GraphX() {

    }

    public static void runVisualization(MooreMachines moore)  {
        //JFrame frame = new JFrame();
        //frame.setSize(1100, 700);
       // frame.setLocation(0, 0);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mxGraph graph = new mxGraph();

        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        //frame.getContentPane().add(graphComponent, BorderLayout.CENTER);
        //frame.setVisible(true);
        Object parent = graph.getDefaultParent();
        Map<String, Object> vertex = new LinkedHashMap<>();

        //ArrayList<Object> vertexObject = new ArrayList<Object>();
        graph.getModel().beginUpdate();
        try {

            for(int i = 0; i < moore.getOutputSignals().size(); ++i) {
                String id = "q" + moore.getState().get(i) + "/" + "y" + moore.getOutputSignals().get(i);
                Object object =  graph.insertVertex(parent, id, id, 0, 0, 80, 30);
                vertex.put(moore.getState().get(i), object);
            }
            for(int index = 0; index < moore.getState().size(); index++){
                Object start = vertex.get(moore.getState().get(index));
                for(int i = 0; i < moore.getTable().size(); ++i) {
                    ArrayList<String> value = moore.getTable().get(i);
                    Object end = vertex.get(value.get(index));
                    String id = "x" + (i + 1);
                    graph.insertEdge(parent, id, id, start, end);
                }
            }



            mxStylesheet stylesheet = graph.getStylesheet();
            Map<String, Object> vertexStyle = stylesheet.getDefaultVertexStyle();
            vertexStyle.put(mxConstants.STYLE_PERIMETER, mxPerimeter.RectanglePerimeter);
            vertexStyle.put(mxConstants.STYLE_GRADIENTCOLOR, "#fff");
            vertexStyle.put(mxConstants.STYLE_PERIMETER_SPACING, 6);
            vertexStyle.put(mxConstants.STYLE_ROUNDED, true);
            vertexStyle.put(mxConstants.STYLE_SHADOW, true);


            Map<String, Object> edgeStyle = stylesheet.getDefaultEdgeStyle();
            //edgeStyle.put(mxConstants.STYLE_ROUNDED, true);


            new mxCircleLayout(graph).execute(graph.getDefaultParent());
            new mxParallelEdgeLayout(graph).execute(graph.getDefaultParent());

        } finally {
            graph.getModel().endUpdate();
        }

        JFrame frame = new JFrame("Graph image");
        frame.getContentPane().add(graphComponent, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }

}