package weigl.valtab;

import java.util.EnumSet;
import java.util.Random;

public class TableRow {
    private TriState[] var;
    private String desc;

    public TableRow(String desc, boolean[] ary, boolean eval) {
	var = TriState.conv(ary);
	this.desc = desc;
    }

    public TableRow(String string) {
	desc = string;
    }

    public int getGroup() {
	int cnt = 0;
	for (TriState v : var)
	    if (v == TriState.True)
		cnt++;
	return cnt;
    }

    public String getDesc() {
	return desc;
    }

    public void setDesc(String desc) {
	this.desc = desc;
    }

    public TriState[] getVar() {
	return var;
    }

    public void setVar(boolean[] var) {
	this.var = TriState.conv(var);
    }

    public void setVar(TriState[] var) {
	this.var = (var);
    }

    public int diff(TableRow o) {
	int d = 0;
	for (int i = 0; i < var.length; i++)
	    if (var[i] != o.var[i])
		d++;
	return d;
    }

    public TableRow merge(TableRow ngrow) {
	TableRow tr = new TableRow(desc + '+' + ngrow.desc);
	TriState[] ts = new TriState[var.length];
	for (int i = 0; i < var.length; i++) {
	    if (var[i] != ngrow.var[i])
		ts[i] = TriState.Reduced;
	    else
		ts[i] = var[i];
	}
	tr.setVar(ts);
	return tr;
    }

    public boolean primis(TableRow o) {
	for (int i = 0; i < var.length; i++) {
	    if (o.var[i] == TriState.Reduced) {
		continue;
	    } else if (o.var[i] == var[i]) {
		continue;
	    } else
		return false;
	}
	return true;
    }
}