package weigl.valtab;

/**
 * Symbol identifiers
 * 
 * @author Alexander Weigl <alexweigl@gmail.com>
 * @date 2009-12-19
 */
public interface Constants {
	/**
	 * an term like a+a
	 */
	public static final String SYMBOL_TERM = "T";
	/**
	 * an factor a*b or ab
	 */
	public static final String SYMBOL_FACTOR = "F";
	/**
	 * a letter a-z
	 */
	public static final String SYMBOL_IDENTIFIER = "I";
	/**
	 * braces (a)
	 */
	public static final String SYMBOL_BRACES = "B";

	/**
	 * negation -a
	 */
	public static final String SYMBOL_NEGATION = "N";

	public static final String SYMBOL_EXPR = "E";

	public static final String SYMBOL_IMPL = "=>";
	public static final String SYMBOL_GDW = "<=>";
	public static final String SYMBOL_XOR = "%";
}