package weigl.valtab;

public enum TriState {
    True, False, Reduced;

    public static TriState[] conv(boolean[] ary) {
	TriState[] s = new TriState[ary.length];
	for (int i = 0; i < ary.length; i++) {
	    if (ary[i])
		s[i] = True;
	    else
		s[i] = False;
	}
	return s;
    }
}
