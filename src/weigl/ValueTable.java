package weigl;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import weigl.valtab.SyntaxTree;

public class ValueTable {
	private char[] vars;
	private int[][] vt;

	public static ValueTable createTable(SyntaxTree stx) {
		ValueTable vt = new ValueTable();

		Set<Character> varlist = stx.getVariables();
		vt.vars = new char[varlist.size()];
		int i = 0;
		for (char c : varlist)
			vt.vars[i++] = c;
		varlist = null;

		int max = 0x1 << vt.vars.length;

		vt.vt = new int[max][vt.vars.length + 1];

		Map<Character, Boolean> vals = new TreeMap<Character, Boolean>();
		for (i = 0; i < max; i++) {
			for (int j = 0; j < vt.vars.length; j++) {
				boolean value = (i & (1 << j)) > 0;
				vals.put(vt.vars[j], value);
				vt.vt[i][j] = btoi(value);
			}
			vt.vt[i][vt.vars.length] = btoi(stx.evaluate(vals));
		}
		return vt;
	}

	@Override
	public String toString() {
		StringBuilder out = new StringBuilder();
		int max = 0x1 << vars.length;
		out.append(String.format(" %c |", 'I'));
		for (char c : vars)
			out.append(String.format(" %c |", c));
		out.append('\n');

		for (int i = 0; i < max; i++) {
			out.append(String.format("%2d |", i));
			for (int j = 0; j < vars.length; j++) {
				out.append(String.format(" %d |", vt[i][j]));
			}
			out.append(String.format(" %d%n", vt[i][vars.length]));
		}
		return out.toString();
	}

	private static int btoi(boolean value) {
		return value ? 1 : 0;
	}

	public char[] getChars() {
		return vars;
	}

	public int[][] getTable() {
		return vt;
	}
}
