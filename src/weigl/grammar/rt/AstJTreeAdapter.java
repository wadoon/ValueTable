package weigl.grammar.rt;

import java.util.LinkedList;

import java.util.List;
import java.util.Stack;

import javax.swing.JTree;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;


/**
 * Decorator for an AST to an {@link TreeModel} for using in {@link JTree}.
 * 
 * @author Alexander Weigl <alexweigl@gmail.com>
 * @date 07.12.2009
 * @version 1
 * 
 */
public class AstJTreeAdapter implements TreeModel {

	private List<TreeModelListener> listeners = new LinkedList<TreeModelListener>();

	private PTree ast;

	public AstJTreeAdapter(PTree ast) {
		this.ast = ast;
	}

	@Override
	public void addTreeModelListener(TreeModelListener l) {
		listeners.add(l);
	}

	@Override
	public Object getChild(Object parent, int index) {
		PTree.Leaf l = searchFor(parent);
		if (l instanceof PTree.Node) {
			PTree.Node node = (PTree.Node) l;
			return node.getElements().get(index);
		}
		return null;
	}

	private PTree.Leaf searchFor(Object parent) {
		Stack<PTree.Node> stack = new Stack<PTree.Node>();
		stack.push(ast.getRoot());

		if (ast.getRoot().equals(parent))
			return ast.getRoot();

		while (stack.size() != 0) {
			PTree.Node node = stack.pop();
			for (PTree.Leaf n : node.getElements()) {
				if (parent.equals(n))
					return n;
				if (n instanceof PTree.Node)
					stack.push((PTree.Node) n);
			}
		}
		return null;
	}

	@Override
	public int getChildCount(Object parent) {
		PTree.Leaf l = searchFor(parent);
		if (l instanceof PTree.Node) {
			PTree.Node node = (PTree.Node) l;
			return node.getElements().size();
		}
		return 0;
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		PTree.Leaf l = searchFor(parent);
		if (l instanceof PTree.Node) {
			PTree.Node node = (PTree.Node) l;
			return node.getElements().indexOf(child);
		}
		return -1;
	}

	@Override
	public Object getRoot() {
		return ast.getRoot();
	}

	@Override
	public boolean isLeaf(Object node) {
		PTree.Leaf l = searchFor(node);
		if (l instanceof PTree.Node) {
			return ((PTree.Node) l).getElements().size() == 0;
		}
		return true;
	}

	@Override
	public void removeTreeModelListener(TreeModelListener l) {
		listeners.remove(l);
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {
		// EMPTY
	}
}
