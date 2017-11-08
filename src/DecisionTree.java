import java.util.ArrayList;

public class DecisionTree<T> {
    
	private Node<T> rootNode;

    public DecisionTree(T rootValue) {
    	
    	rootNode = new Node<T>();
    	
    	rootNode.setValue(rootValue);
    	
    	rootNode.setNodeChildren(new ArrayList<Node<T>>());
    	
    }
}
