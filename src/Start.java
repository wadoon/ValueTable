import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import weigl.valtab.Parser;
import weigl.valtab.SyntaxTree;

public class Start {
	public static void main(String[] args) {
		Parser p = new Parser();
		p.run("abc+cd+ab+(a*-b)");
		SyntaxTree stx = new SyntaxTree(p.getParseTree());

		Set<Character> varlist = stx.getVariables();
		char[] vars = new char[varlist.size()];
		{
			int i = 0;
			for (char c : varlist)
				vars[i++] = c;
			varlist = null;
		}
		int max = 0x1 << vars.length;
		Map<Character, Boolean> vals = new TreeMap<Character, Boolean>();

		System.out.format(" %c ", 'I');
		for (char c : vars)
			System.out.format(" %c ", c);
		System.out.println();

		for (int i = 0; i < max; i++) {
			System.out.format("%2d ", i);
			for (int j = 0; j < vars.length; j++) {
				boolean value = (i & (1 << j)) > 0;
				vals.put(vars[j], value);
				System.out.format(" %d ", btoi(value));
			}
			System.out.format(" %d%n", btoi(stx.evaluate(vals)));
		}
	}

	private static int btoi(boolean value) {
		return value ? 1 : 0;
	}
}
