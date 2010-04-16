package weigl.valtab;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class QuinMcCluskey {
    private Multimap<Integer, TableRow> groups = HashMultimap.create();

    public QuinMcCluskey(TableRow[] table) {
	for (TableRow tableRow : table)
	    if (tableRow != null)
		groups.put(tableRow.getGroup(), tableRow);
	simplify();
    }

    private void simplify() {
	Multimap<Integer, TableRow> newgrp = HashMultimap.create();
	boolean changed = true;
	while (changed) {
	    changed = false;
	    for (Integer group : groups.keys()) {
		if (groups.keys().contains(group + 1)) {
		    for (TableRow row : groups.get(group)) {
			for (TableRow ngrow : groups.get(group + 1)) {
			    if (ngrow.diff(row) == 1) {
				TableRow tr = row.merge(ngrow);
				newgrp.put(tr.getGroup(), tr);
				changed = true;
			    }
			}
		    }
		}
	    }
//	    reduce(newgrp);
	    groups = newgrp;
	}
    }

    /**
     * 
     * @param newgrp
     */
    private void reduce(Multimap<Integer, TableRow> newgrp) {
	HashMultimap<Integer, TableRow> copy = HashMultimap.create(newgrp);
	for (TableRow iter1 : copy.values()) {
	    for (TableRow iter2 : copy.values()) {
		if (iter1 != iter2) {
		    if (iter1.primis(iter2)) {
			newgrp.remove(iter1.getGroup(), iter1);
		    }
		}
	    }
	}
    }

    public void out(char[] c) {
	for (TableRow tr : groups.values()) {
	    System.out.print(tr.getDesc() + "  :");
	    int i = 0;
	    for (TriState s : tr.getVar()) {
		if (s == TriState.True)
		    System.out.print(c[i++]);
		else if (s == TriState.False)
		    System.out.print("-" + c[i++]);
	    }
	    System.out.print(" + ");
	}
	System.out.println("1");
    }
}
