package weigl.grammar.rt;

import java.util.Arrays;

import weigl.grammar.rt.PTree.Leaf;

public abstract class ParserFather implements weigl.grammar.rt.Parser {
	private static final char EPSILON_CHAR = '€';
	private char[] input;
	protected PTree syntaxTree;
	private int position = 0;
	
	public ParserFather() {
	}

	/**
	 * creates a new node with the given text
	 * 
	 * @param text
	 *            nodes contents
	 * @return a node
	 */
	public PTree.Node newNode(String text) {
		return new PTree.Node(text);
	}

	/**
	 * 
	 * @param c
	 * @return
	 */
	public Leaf match(char c) {
		if (c == curpos()) {
			consume();
			return new PTree.Leaf(new String(new char[] { c }));
		}
		error(c);
		return null;
	}

	/**
	 * method for error reporting
	 * 
	 * @throws IllegalStateException
	 */
	protected void error() throws IllegalStateException {
		throw new IllegalStateException("ERROR! expected: []");
	}

	/**
	 * method for error reporting
	 * 
	 * @param c
	 *            character that was expected
	 * @throws IllegalStateException
	 */
	protected void error(char c) throws IllegalStateException {
		throw new IllegalStateException("ERROR! expected: [" + c + "]");
	}

	/**
	 * method for error reporting
	 * 
	 * @param c
	 *            characters that were expected
	 * @throws IllegalStateException
	 */
	protected void error(char... c) throws IllegalStateException {
		throw new IllegalStateException("ERROR! expected: "
				+ Arrays.toString(c));
	}

	/**
	 * @return current character aka the lookahead
	 */
	protected char curpos() {
		try {
			return input[position];
		} catch (ArrayIndexOutOfBoundsException e) {
			return EPSILON_CHAR;
		}
	}

	/**
	 * lookahead for the end of the input.
	 * 
	 * @return true if end is reached else false
	 */
	protected boolean lookahead() {
		return position >= input.length;
	}

	/**
	 * searches the lookahead after each char in the string. on match return
	 * true
	 * 
	 * @param s
	 *            chars for looking ahead
	 * @return true if one character in s matched the lookahead
	 */
	protected boolean lookahead(String s) {
		for (char c : s.toCharArray()) {
			if (c == EPSILON_CHAR && lookahead())
				return true;
			if (c == curpos())
				return true;
		}
		return false;
	}

	/**
	 * read one char forward
	 */
	protected void consume() {
		position++;
	}

	/**
	 * start method. should only invoke the start symbol of the grammar
	 */
	public abstract void start();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run(String source) {
		reset();
		input = source.toCharArray();
		start();
	}

	private void reset() {
		position = 0;
		syntaxTree = null;
		input = new char[] {};
	}

	/** {@inheritDoc} */
	@Override
	public PTree getParseTree() {
		return syntaxTree;
	};
}
