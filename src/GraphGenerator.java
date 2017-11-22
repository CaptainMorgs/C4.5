import java.util.List;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

/**
 * Class to generate a graph from a decision tree
 * 
 * @author John
 *
 */
public class GraphGenerator {

	private static List<MyNode<List<Sample>>> decisionTreeList;

	/**
	 * Generates a graph from a decision tree using the Graphstream library
	 */
	public static void generateGraph() {
		MultiGraph graph = new MultiGraph("C4.5");

		decisionTreeList = C45.decisionTreeList;

		// Create a graph of the first decision tree, could improve by combining
		// the trees into one tree and graphing that
		MyNode<List<Sample>> decisionTree = decisionTreeList.get(0);

		// Could improve by creating the graph with loops, found it quite hard
		// to do that
		graph.addNode("A");
		Node a = graph.getNode("A");
		a.addAttribute("ui.label", "A: " + decisionTree.attributeName + ", split value: " + decisionTree.splitValue);

		List<MyNode<List<Sample>>> aChildren = decisionTree.getChildren();

		graph.addNode("B");
		Node b = graph.getNode("B");
		MyNode<List<Sample>> aChild0 = aChildren.get(0);
		b.addAttribute("ui.label", "B: " + aChild0.attributeName + ", split value: " + aChild0.splitValue);
		graph.addEdge("AB", "A", "B");

		graph.addNode("C");
		Node c = graph.getNode("C");
		MyNode<List<Sample>> aChild1 = aChildren.get(1);
		c.addAttribute("ui.label", "C: " + aChild1.attributeName + ", split value: " + aChild1.splitValue);
		graph.addEdge("AC", "A", "C");

		graph.addNode("D");
		Node d = graph.getNode("D");
		MyNode<List<Sample>> bChild0 = aChild0.getChildren().get(0);
		d.addAttribute("ui.label", "D: " + bChild0.attributeName + ", split value: " + bChild0.splitValue);
		graph.addEdge("BD", "B", "D");

		graph.addNode("E");
		Node e = graph.getNode("E");
		MyNode<List<Sample>> bChild1 = aChild0.getChildren().get(1);
		e.addAttribute("ui.label", "E: " + bChild1.attributeName + ", split value: " + bChild1.splitValue);
		graph.addEdge("BE", "B", "E");

		graph.addNode("F");
		Node f = graph.getNode("F");
		MyNode<List<Sample>> cChild0 = aChild1.getChildren().get(0);
		f.addAttribute("ui.label", "F: " + cChild0.attributeName + ", split value: " + cChild0.splitValue);
		graph.addEdge("CF", "C", "F");

		graph.addNode("G");
		Node g = graph.getNode("G");
		MyNode<List<Sample>> cChild1 = aChild1.getChildren().get(1);
		g.addAttribute("ui.label", "G: " + cChild1.attributeName + ", split value: " + cChild1.splitValue);
		graph.addEdge("CG", "C", "G");

		// not whole graph but writing the code for the next level would be too
		// time consuming

		graph.display();
	}
}
