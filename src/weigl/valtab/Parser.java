package weigl.valtab;

import java.text.ParseException;

import weigl.grammar.rt.PTree;
import weigl.grammar.rt.ParserFather;
import weigl.grammar.rt.PTree.Node;

public class Parser extends ParserFather implements Constants {

	public void start() throws ParseException {
		syntaxTree = new PTree(expr());
	}

	public PTree.Node expr() throws ParseException {
//		final PTree.Node n = newNode(SYMBOL_EXPR);
		Node n = term();
		Node m = expr_();
		m.add(n);
		return m;
	}

	public PTree.Node expr_() throws ParseException {
		if (lookahead("=")) {
			final PTree.Node n = newNode("=>");
			match('=');
			match('>');
			n.add(term());
			n.add(expr_());
			return n;
		} else if (lookahead("<")) {
			final PTree.Node n = newNode("<=>");
			match('<');
			match('=');
			match('>');
			n.add(term());
			n.add(expr_());
			return n;
		} else if (lookahead("%")) {
			final PTree.Node n = newNode("%");
			match('%');
			n.add(term());
			n.add(expr_());
			return n;
		}else 
			return  newNode("€"); 
	}

	public PTree.Node braces() throws ParseException {
		final PTree.Node n = newNode(SYMBOL_BRACES);

		boolean matched = false;
		if (lookahead("abcdefghijklmnopqrstuvwxyz")) {
			matched = true;
			n.add(identifer());
		} else if (lookahead("(")) {
			matched = true;
			// n.add(match('('));
			match('(');
			n.add(term());
			match(')');
		}
		if (!matched)
			error('(', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
					'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
					'x', 'y', 'z');

		return n;
	}

	public PTree.Node factor() throws ParseException {
		final PTree.Node n = newNode(SYMBOL_FACTOR);

		n.add(negation());
		n.add(factor_());
		return n;
	}

	public PTree.Node identifer() throws ParseException {
		final PTree.Node n = newNode(SYMBOL_IDENTIFIER);

		boolean matched = false;
		if (lookahead("a")) {
			matched = true;
			n.add(match('a'));
		} else if (lookahead("b")) {
			matched = true;
			n.add(match('b'));
		} else if (lookahead("c")) {
			matched = true;
			n.add(match('c'));
		} else if (lookahead("d")) {
			matched = true;
			n.add(match('d'));
		} else if (lookahead("e")) {
			matched = true;
			n.add(match('e'));
		} else if (lookahead("f")) {
			matched = true;
			n.add(match('f'));
		} else if (lookahead("g")) {
			matched = true;
			n.add(match('g'));
		} else if (lookahead("h")) {
			matched = true;
			n.add(match('h'));
		} else if (lookahead("i")) {
			matched = true;
			n.add(match('i'));
		} else if (lookahead("j")) {
			matched = true;
			n.add(match('j'));
		} else if (lookahead("k")) {
			matched = true;
			n.add(match('k'));
		} else if (lookahead("l")) {
			matched = true;
			n.add(match('l'));
		} else if (lookahead("m")) {
			matched = true;
			n.add(match('m'));
		} else if (lookahead("n")) {
			matched = true;
			n.add(match('n'));
		} else if (lookahead("o")) {
			matched = true;
			n.add(match('o'));
		} else if (lookahead("p")) {
			matched = true;
			n.add(match('p'));
		} else if (lookahead("q")) {
			matched = true;
			n.add(match('q'));
		} else if (lookahead("r")) {
			matched = true;
			n.add(match('r'));
		} else if (lookahead("s")) {
			matched = true;
			n.add(match('s'));
		} else if (lookahead("t")) {
			matched = true;
			n.add(match('t'));
		} else if (lookahead("u")) {
			matched = true;
			n.add(match('u'));
		} else if (lookahead("v")) {
			matched = true;
			n.add(match('v'));
		} else if (lookahead("w")) {
			matched = true;
			n.add(match('w'));
		} else if (lookahead("x")) {
			matched = true;
			n.add(match('x'));
		} else if (lookahead("y")) {
			matched = true;
			n.add(match('y'));
		} else if (lookahead("z")) {
			matched = true;
			n.add(match('z'));
		}
		if (!matched)
			error('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
					'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
					'y', 'z');

		return n;
	}

	public PTree.Node negation() throws ParseException {
		if (lookahead("(abcdefghijklmnopqrstuvwxyz")) {
			return braces();
		} else if (lookahead("-")) {
			// n.add(match('-'));
			final PTree.Node n = newNode(SYMBOL_NEGATION);
			match('-');
			n.add(braces());
			return n;
		} else
			error('(', '-', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
					'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
					'w', 'x', 'y', 'z');
		return null;
	}

	public PTree.Node term() throws ParseException {
		final PTree.Node n = newNode(SYMBOL_TERM);
		n.add(factor());
		n.add(term_());
		return n;
	}

	public PTree.Node term_() throws ParseException {
		final PTree.Node n = newNode(SYMBOL_TERM);

		boolean matched = false;
		if (lookahead("+")) {
			matched = true;
			// n.add(match('+'));
			match('+');
			n.add(factor());
			n.add(term_());
		}
		if (!matched) {
			matched = true;
			n.add(new PTree.Leaf("€"));
		}
		if (!matched)
			error('+', '€');

		return n;
	}

	public PTree.Node factor_() throws ParseException {
		final PTree.Node n = newNode(SYMBOL_FACTOR);

		boolean matched = false;
		if (lookahead("(-abcdefghijklmnopqrstuvwxyz")) {
			matched = true;
			n.add(negation());
			n.add(factor_());
		} else if (lookahead("*")) {
			matched = true;
			// n.add(match('*'));
			match('*');
			n.add(negation());
			n.add(factor_());
		}
		if (!matched) {
			matched = true;
			n.add(new PTree.Leaf("€"));
		}
		if (!matched)
			error('(', '*', '-', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
					'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
					'v', 'w', 'x', 'y', 'z', '€');

		return n;
	}

}// end class
