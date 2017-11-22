import java.util.ArrayList;
import java.util.List;

public class MyNode<T> {
    public List<MyNode<T>> children = new ArrayList<MyNode<T>>();
    private MyNode<T> parent = null;
    public String attributeName;
    public boolean isLeaf;
    public double splitValue;
    
    

   /* public Node(T data) {
        this.data = data;
    }*/
    
    public MyNode(String attributeName, boolean terminal) {
        this.attributeName = attributeName;
        this.isLeaf = terminal;
    }
    
    public MyNode(String attributeName, boolean terminal, double splitValue) {
        this.attributeName = attributeName;
        this.isLeaf = terminal;
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
    
    public List<MyNode<T>> getChildren() {
        return children;
    }

    public void setParent(MyNode<T> parent) {
     //   parent.addChild(this);
        this.parent = parent;
    }

    /*public void addChild(T data) {
        Node<T> child = new Node<T>(data);
        child.setParent(this);
        this.children.add(child);
    }*/

    public void addChild(MyNode<T> child) {
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
		
		for(MyNode child: children) {
			child.print();
		}
		
		
	}
    
    
}