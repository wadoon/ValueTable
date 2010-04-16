package weigl;

public class NormalForm {
	public static String createDisjunct(ValueTable vt) {
		char[] c = vt.getChars();
		int[][] t = vt.getTable();
		int v = c.length;
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < t.length; i++) {
			if (t[i][v] == 1) {
				for (int j = 0; j < v; j++) {
					if (t[i][j] == 0)
						sb.append('-');
					sb.append(c[j]);
				}
				sb.append('+');
			}
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}

	public static String createConjunct(ValueTable vt) {
		char[] c = vt.getChars();
		int[][] t = vt.getTable();

		int v = c.length;

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < t.length; i++) {
			if (t[i][v] == 0) {
				sb.append('(');
				for (int j = 0; j < v; j++) {
					if (t[i][j] == 1)
						sb.append('-');
					sb.append(c[j]);
					sb.append('+');
				}
				sb.replace(sb.length()-1, sb.length(), ")");
			}
		}
		return sb.toString();
	}

}
