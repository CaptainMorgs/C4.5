
import java.util.ArrayList;
import java.util.List;

/**
 * Class acts as the building blocks for a decision tree
 * @author John
 *
 * @param <T>
 */
public class MyNode<T> {

	public List<MyNode<T>> children = new ArrayList<MyNode<T>>();
	private MyNode<T> parent = null;
	public String attributeName;
	public boolean isLeaf;
	public double splitValue;

	public MyNode(String attributeName, boolean terminal) {
		this.attributeName = attributeName;
		this.isLeaf = terminal;
	}

	public List<MyNode<T>> getChildren() {
		return children;
	}

	public void setParent(MyNode<T> parent) {
		this.parent = parent;
	}

	public void addChild(MyNode<T> child) {
		child.setParent(this);
		this.children.add(child);
	}

	public boolean isRoot() {
		return (this.parent == null);
	}

	public boolean isLeaf() {
		if (this.children.size() == 0)
			return true;
		else
			return false;
	}

	public void removeParent() {
		this.parent = null;
	}

	@Override
	public String toString() {
		return "Node [children=" + children + ", attributeName=" + attributeName + "]";
	}
}