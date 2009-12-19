package weigl.valtab;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

import weigl.grammar.rt.PTree;
import weigl.grammar.rt.PTree.Leaf;

public class SyntaxTree implements Constants {

	private Node syntaxRoot;

	public SyntaxTree(PTree ast) {
		Leaf l = makeSmaller(ast.getRoot());
		syntaxRoot = translateR(l);
	}

	public Node getSyntaxTree() {
		return syntaxRoot;
	}

	public void setSyntaxTree(Node syntaxTree) {
		this.syntaxRoot = syntaxTree;
	}

	public boolean evaluate(Map<Character, Boolean> values) {
		return syntaxRoot.valueOf(values);
	}

	private Node translateR(Leaf r) {
		weigl.grammar.rt.PTree.Node root = (PTree.Node) r;
		ParentNode stxNode = (ParentNode) translate(root);
		for (Leaf n : root.getElements()) {
			if (n.hasChildren()) {
				stxNode.add(translateR((PTree.Node) n));
			} else {
				stxNode.add(translate(n));
			}
		}
		return stxNode;
	}

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
		} else {
			System.err.println(l);
		}
		return null;
	}

	private PTree.Leaf makeSmaller(PTree.Node node) {
		List<Leaf> old = node.getElements();
		List<Leaf> list = new LinkedList<Leaf>(old);

		for (Leaf l : list) {
			if (l.hasChildren()) {
				Leaf child = makeSmaller((PTree.Node) l);
				if (child == null)
					old.remove(l);
				else
					old.set(old.indexOf(l), child);
			}
		}

		if (old.size() >= 2) {
			return node;
		} else {
			Leaf child = old.get(0);
			return child.getTerminalSymbol().equals("€") ? null : child;
		}
	}

	static interface Node {
		public boolean valueOf(Map<Character, Boolean> curVals);

		public boolean hasChildren();
	}

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

	static class Conjunction extends ParentNode {
		@Override
		public boolean valueOf(Map<Character, Boolean> curVals) {
			boolean b = true;
			for (Node n : list)
				b = b && n.valueOf(curVals);
			return b;
		}
	}

	static class Disjunction extends ParentNode {
		@Override
		public boolean valueOf(Map<Character, Boolean> curVals) {
			boolean b = false;
			for (Node n : list)
				b = b || n.valueOf(curVals);
			return b;
		}
	}

	static class Braces extends ParentNode {
		@Override
		public boolean valueOf(Map<Character, Boolean> curVals) {
			return list.get(0).valueOf(curVals);
		}
	}

	static class Negation extends ParentNode {
		@Override
		public boolean valueOf(Map<Character, Boolean> curVals) {
			return !list.get(0).valueOf(curVals);
		}

	}

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
