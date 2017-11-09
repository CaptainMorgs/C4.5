import java.util.ArrayList;

public class DecisionTree<T> {
    
	private Node<T> rootNode;

    public DecisionTree(Node rootNode) {
    	
    	this.rootNode = rootNode;
    	
    //	rootNode.setValue(rootNode);
    	
    	rootNode.setNodeChildren(new ArrayList<Node<T>>());
    	
    }
}