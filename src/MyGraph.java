import java.util.ArrayList;
import java.util.List;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

import Owls.Sample;

public class MyGraph {

	private static List<MyNode<List<Sample>>> decisionTreeList;

	private List<Node> nodes = new ArrayList<>();

	public static void main(String args[]) {

		SingleGraph graph = new SingleGraph("Tutorial 1");

		decisionTreeList = Algorithm.decisionTreeList;

		// for(MyNode myNode: decisionTreeList){
		for (int i = 0; i < decisionTreeList.size(); i++) {
			graph.addNode(Integer.toString(i));
			// nodes.

		}

		graph.addNode("A");
		graph.addNode("B");
		graph.addNode("C");
		graph.addEdge("AB", "A", "B");
		graph.addEdge("BC", "B", "C");
		graph.addEdge("CA", "C", "A");

		Node n = graph.getNode("A");
		n.addAttribute("ui.label", "A");

		graph.display();
	}

	public static void generateGraph() {
		MultiGraph graph = new MultiGraph("Tutorial 1");

		decisionTreeList = Algorithm.decisionTreeList;

		MyNode<List<Sample>> decisionTree = decisionTreeList.get(0);

		graph.addNode("A");
		Node a = graph.getNode("A");
		a.addAttribute("ui.label", decisionTree.attributeName + ", split value: " + decisionTree.splitValue);

		List<MyNode<List<Sample>>> children = decisionTree.getChildren();

		graph.addNode("B");
		Node b = graph.getNode("B");
		b.addAttribute("ui.label", children.get(0).attributeName + ", split value: " + children.get(0).splitValue);
		graph.addEdge("AB", "A", "B");

		graph.addNode("C");
		Node c = graph.getNode("C");
		c.addAttribute("ui.label", children.get(1).attributeName + ", split value: " + children.get(1).splitValue);
		graph.addEdge("AC", "A", "C");

		graph.addNode("D");
		Node d = graph.getNode("D");
		d.addAttribute("ui.label", children.get(0).getChildren().get(0).attributeName + ", split value: "
				+ children.get(0).getChildren().get(0).splitValue);
		graph.addEdge("BD", "B", "D");

		graph.addNode("E");
		Node e = graph.getNode("E");
		e.addAttribute("ui.label", children.get(0).getChildren().get(1).attributeName + ", split value: "
				+ children.get(0).getChildren().get(0).splitValue);
		graph.addEdge("BE", "B", "E");

		graph.addNode("F");
		Node f = graph.getNode("F");
		f.addAttribute("ui.label", children.get(1).getChildren().get(0).attributeName+ ", split value: "
				+ children.get(1).getChildren().get(0).splitValue);
		graph.addEdge("CF", "C", "F");

		graph.addNode("G");
		Node g = graph.getNode("G");
		g.addAttribute("ui.label", children.get(1).getChildren().get(1).attributeName+ ", split value: "
				+ children.get(1).getChildren().get(1).splitValue);
		graph.addEdge("CG", "C", "G");

		graph.display();
	}

	/*
	 * if (children != null) { for (int i = 0; i < children.size(); i++) {
	 * graph.addNode(Integer.toString(i)); Node k =
	 * graph.getNode(Integer.toString(i)); k.addAttribute("ui.label",
	 * children.get(i).attributeName); } for (int i = 0; i < children.size() -1;
	 * i++) { // Node k = graph.getNode(Integer.toString(i));
	 * graph.addEdge("AB", "A", Integer.toString(i)); graph.addEdge("BC",
	 * Integer.toString(i), Integer.toString(i + 1));
	 * 
	 * }
	 * 
	 * List<MyNode<List<Sample>>> children2 = children.get(0).getChildren();
	 * List<MyNode<List<Sample>>> children3 = children.get(1).getChildren();
	 * 
	 * if (children2 != null) { for (int i = 0; i < children2.size(); i++) {
	 * graph.addNode("6"); Node k = graph.getNode("6");
	 * k.addAttribute("ui.label", children2.get(i).attributeName); } for (int i
	 * = 0; i < children2.size() -1; i++) { // Node k =
	 * graph.getNode(Integer.toString(i)); graph.addEdge("XY", "B", "6");
	 * graph.addEdge("YZ", "5", "6");
	 * 
	 * } }
	 * 
	 * if (children3 != null) { for (int i = 0; i < children3.size(); i++) {
	 * graph.addNode("7"); Node k = graph.getNode("7");
	 * k.addAttribute("ui.label", children3.get(i).attributeName); } for (int i
	 * = 0; i < children3.size() -1; i++) { // Node k =
	 * graph.getNode(Integer.toString(i)); graph.addEdge("DE", "C", "7");
	 * graph.addEdge("EF", "5", "7");
	 * 
	 * } }
	 */
	/*
	 * graph.display(); }
	 * 
	 * }
	 */
}
