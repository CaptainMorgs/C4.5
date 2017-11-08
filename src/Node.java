import java.util.List;

public class Node<T> {
//TODO add traversal options
	 private T value;
	 
     private Node<T> nodeParent;
     
     private List<Node<T>> nodeChildren;

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	public Node<T> getNodeParent() {
		return nodeParent;
	}

	public void setNodeParent(Node<T> nodeParent) {
		this.nodeParent = nodeParent;
	}

	public List<Node<T>> getNodeChildren() {
		return nodeChildren;
	}

	public void setNodeChildren(List<Node<T>> nodeChildren) {
		this.nodeChildren = nodeChildren;
	}
     
     
}
