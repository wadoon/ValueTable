package weigl.valtab;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

import weigl.grammar.rt.PTree;
import weigl.grammar.rt.PTree.Leaf;

/**
 * The Syntax for the formula. Uses for calculation.
 * 
 * 
 * @author Alexander Weigl <alexweigl@gmail.com>
 * @date 2009-12-19 
 * @version 1
 */
public class SyntaxTree implements Constants {

	private Node syntaxRoot;

	/**
	 * Build a syntax tree from the given parse tree.
	 * The parse tree will be compressed. 
	 * @param ast
	 */
	public SyntaxTree(PTree ast) {
		Leaf l = compress(ast.getRoot());
		syntaxRoot = translateRecursive(l);
	}

	public Node getSyntaxTree() {
		return syntaxRoot;
	}

	public boolean evaluate(Map<Character, Boolean> values) {
		return syntaxRoot.valueOf(values);
	}

	/**
	 * run recursely through the tree and translate each node/leaf.
	 * @param r
	 * @return
	 */
	private Node translateRecursive(Leaf r) {
		if (!r.hasChildren())
			return translate(r);

		weigl.grammar.rt.PTree.Node root = (PTree.Node) r;
		ParentNode stxNode = (ParentNode) translate(root);
		for (Leaf n : root.getElements()) {
			if (n.hasChildren()) {
				stxNode.add(translateRecursive((PTree.Node) n));
			} else {
				stxNode.add(translate(n));
			}
		}
		return stxNode;
	}

	/**
	 * Translate the given leaf/node from the parse tree to the representation in the syntax tree
	 * @param l parse tree node
	 * @return syntax tree node
	 */
	private Node translate(Leaf l) {
		String s = l.getTerminalSymbol();
		if (Character.isLowerCase(s.charAt(0))) {
			return new Value(s.charAt(0));
		} else if (s.equals(SYMBOL_BRACES)) {
			return new Braces();
		} else if (s.equals(SYMBOL_FACTOR)) {
			return new Conjunction();
		} else if (s.equals(SYMBOL_TERM)) {
			return new Disjunction();
		} else if (s.equals(SYMBOL_NEGATION)) {
			return new Negation();
		} else if (s.equals(SYMBOL_GDW)) {
			return new Equivalence();
		} else if (s.equals(SYMBOL_XOR)) {
			return new Xor();
		} else if (s.equals(SYMBOL_IMPL)) {
			return new Implication();
		} else {
			System.err.println(l);
		}
		return null;
	}

	/**
	 * compress the given parse tree to an minimalistic version by reducing the "chain" nodes
	 * @param node
	 * @return
	 */
	private PTree.Leaf compress(PTree.Node node) {
		List<Leaf> old = node.getElements();
		List<Leaf> list = new LinkedList<Leaf>(old);

		for (Leaf l : list) {
			if (l.hasChildren()) {
				Leaf child = compress((PTree.Node) l);
				if (child == null)
					old.remove(l);
				else
					old.set(old.indexOf(l), child);
			}
		}
		if (old.size() >= 2 || node.getTerminalSymbol().equals(SYMBOL_NEGATION)) {
			return node;
		} else {
			if (old.size()== 0)
				return null;
			Leaf child = old.get(0);
			return child.getTerminalSymbol().equals("€") ? null : child;
		}
	}

	/**
	 * Interfaces for Syntax nodex
	 * @author Alexander Weigl <alexweigl@gmail.com>
	 *
	 */
	static interface Node {
		public boolean valueOf(Map<Character, Boolean> curVals);
		public boolean hasChildren();
	}

	/**
	 * atomic variable 
	 * @author Alexander Weigl <alexweigl@gmail.com>
	 */
	static class Value implements Node {
		private Character variable;

		public Value(Character variable) {
			this.variable = variable;
		}

		@Override
		public boolean hasChildren() {
			return false;
		}

		@Override
		public boolean valueOf(Map<Character, Boolean> curVals) {
			return curVals.get(variable);
		}

		public Character getCharacter() {
			return variable;
		}

	}
	
	/**
	 * classes for nodes with children
	 * @author Alexander Weigl <alexweigl@gmail.com>
	 *
	 */
	static abstract class ParentNode implements Node {
		protected List<Node> list = new LinkedList<Node>();

		public void add(Node e) {
			list.add(e);
		}

		public List<Node> getList() {
			return list;
		}

		public void setList(List<Node> list) {
			this.list = list;
		}

		@Override
		public boolean hasChildren() {
			return true;
		}

		public int childs() {
			return list.size();
		}
	}
	
	/**
	 * and 
	 * @author Alexander Weigl <alexweigl@gmail.com>
	 */
	static class Conjunction extends ParentNode {
		@Override
		public boolean valueOf(Map<Character, Boolean> curVals) {
			boolean b = true;
			for (Node n : list)
				b = b && n.valueOf(curVals);
			return b;
		}
	}

	/**
	 * or
	 * @author Alexander Weigl <alexweigl@gmail.com>
	 */
	static class Disjunction extends ParentNode {
		@Override
		public boolean valueOf(Map<Character, Boolean> curVals) {
			boolean b = false;
			for (Node n : list)
				b = b || n.valueOf(curVals);
			return b;
		}
	}

	/**
	 * ( )
	 * @author Alexander Weigl <alexweigl@gmail.com>
	 *
	 */
	static class Braces extends ParentNode {
		@Override
		public boolean valueOf(Map<Character, Boolean> curVals) {
			return list.get(0).valueOf(curVals);
		}
	}

	/**
	 * not
	 * @author Alexander Weigl <alexweigl@gmail.com>
	 *
	 */
	static class Negation extends ParentNode {
		@Override
		public boolean valueOf(Map<Character, Boolean> curVals) {
			return !list.get(0).valueOf(curVals);
		}

	}
	
	static class Xor extends Equivalence {
		@Override
		public boolean valueOf(Map<Character, Boolean> curVals) {
			return !super.valueOf(curVals);
		}
	}
	static class Equivalence extends ParentNode {
		@Override
		public boolean valueOf(Map<Character, Boolean> curVals) {
			boolean p = list.get(0).valueOf(curVals);
			boolean k = list.get(1).valueOf(curVals);
			return p==k;
		}
	}

	static class Implication extends ParentNode {
		@Override
		public boolean valueOf(Map<Character, Boolean> curVals) {
			boolean p = list.get(0).valueOf(curVals);
			boolean k = list.get(1).valueOf(curVals);
			return !p||k;
		}
	}

	/**
	 * collect all variables in the formula
	 * @return
	 */
	public Set<Character> getVariables() {
		Stack<Node> stack = new Stack<Node>();
		Set<Character> list = new TreeSet<Character>();
		stack.push(syntaxRoot);
		while (!stack.isEmpty()) {
			Node n = stack.pop();
			if (n.hasChildren()) {
				ParentNode pn = (ParentNode) n;
				for (Node childs : pn.getList())
					stack.push(childs);

			} else { // only one element has no children
				Value v = (Value) n;
				list.add(v.getCharacter());
			}
		}
		return list;
	}
}
