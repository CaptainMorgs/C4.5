import java.util.ArrayList;
import java.util.List;

public class Node<T> {
    public List<Node<T>> children = new ArrayList<Node<T>>();
    private Node<T> parent = null;
    public String attributeName;
    public boolean terminal;
    public double splitValue;

   /* public Node(T data) {
        this.data = data;
    }*/
    
    public Node(String attributeName, boolean terminal) {
        this.attributeName = attributeName;
        this.terminal = terminal;
    }
    
    /*public Node(String attributeName, Node parent) {
        this.attributeName = attributeName;
    }*/


    /*public Node(T data, Node<T> parent) {
        this.data = data;
        this.parent = parent;
    }*/
    
   /* public Node(T data, Node<T> parent, String attributeName) {
        this.data = data;
        this.parent = parent;
        this.attributeName = attributeName;
    }*/

   /* public Node(T data, String attributeName) {
        this.data = data;
        this.attributeName = attributeName;
    }*/
    
    public List<Node<T>> getChildren() {
        return children;
    }

    public void setParent(Node<T> parent) {
     //   parent.addChild(this);
        this.parent = parent;
    }

    /*public void addChild(T data) {
        Node<T> child = new Node<T>(data);
        child.setParent(this);
        this.children.add(child);
    }*/

    public void addChild(Node<T> child) {
        child.setParent(this);
        this.children.add(child);
    }

    /*public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }*/

    public boolean isRoot() {
        return (this.parent == null);
    }

    public boolean isLeaf() {
        if(this.children.size() == 0) 
            return true;
        else 
            return false;
    }

    public void removeParent() {
        this.parent = null;
    }

	@Override
	public String toString() {
		return "Node [children=" + children + ", attributeName="
				+ attributeName + "]";
	}
	
	public void print(){
		System.out.println("Label " + attributeName);
		
		for(Node child: children) {
			child.print();
		}
		
		
	}
    
    
}