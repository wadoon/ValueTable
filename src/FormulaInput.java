import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import weigl.valtab.Parser;
import weigl.valtab.QuinMcCluskey;
import weigl.valtab.SyntaxTree;
import weigl.valtab.TableRow;

public class FormulaInput {
    public static void main(String[] args) throws IOException {
	// read a valid input
	SyntaxTree stx = input();

	// get the variables in the formula
	Set<Character> varlist = stx.getVariables();
	char[] vars = new char[varlist.size()];
	{
	    int i = 0;
	    for (char c : varlist)
		vars[i++] = c;
	}
	int max = 0x1 << vars.length;
	Map<Character, Boolean> vals = new TreeMap<Character, Boolean>();

	// header output
	System.out.format(" %c |", 'I');
	for (char c : vars)
	    System.out.format(" %c |", c);
	System.out.println();

	TableRow[] table = new TableRow[max];
	for (int i = 0,k = 0; i < max; i++) {
	    System.out.format("%2d |", i);
	    for (int j = 0; j < vars.length; j++) {
		boolean value = (i & (1 << j)) > 0;
		vals.put(vars[j], value);

		System.out.format(" %d |", btoi(value));
	    }
	    final boolean eval = stx.evaluate(vals);

	    if (eval) {
		boolean[] ary = toArray(varlist, vals);
		table[k++] = new TableRow("m" + i, ary, eval);
	    }
	    System.out.format(" %d%n", btoi(eval));
	}
	new QuinMcCluskey(table).out(toArray(varlist));
    }

    private static char[] toArray(Set<Character> varlist) {
	char[] c = new char[varlist.size()];
	int i = 0;
	for (Character character : varlist) {
	    c[i++] = character;
	}
	return c;
    }

    private static boolean[] toArray(Set<Character> varlist,
	    Map<Character, Boolean> vals) {
	boolean[] b = new boolean[varlist.size()];
	int i = 0;
	for (Character character : varlist) {
	    b[i++] = vals.get(character);
	}
	return b;
    }

    private static SyntaxTree input() throws IOException {
	Parser p = new Parser();
	BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	while (true) {
	    System.out.print("Input expression: ");
	    String input = in.readLine();
	    try {
		p.run(input);
		SyntaxTree st = new SyntaxTree(p.getParseTree());
		// TestDialog.showFrame(p.getParseTree());
		return st;
	    } catch (ParseException e) {
		System.err.println(input);
		for (int i = 0; i < e.getErrorOffset() - 1; i++)
		    System.err.print(' ');
		System.err.println('^');
		e.printStackTrace();
	    }
	}
    }

    private static int btoi(boolean value) {
	return value ? 1 : 0;
    }
}
