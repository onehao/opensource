package com.onehao.acts.testgen.common;

import java.util.ArrayList;

import choco.Choco;
import choco.kernel.model.variables.integer.IntegerVariable;
import choco.kernel.solver.variables.integer.IntDomain;
import edu.uta.cse.fireeye.common.Parameter;

/**
 * 
 * @author wanhao01 refactor from FireEye2.8
 */
public class ChocoVariable {
    String name;
    IntegerVariable var;
    Parameter param;
    ArrayList<Integer> values;
    private IntDomain newDomain;
    private int groupID;
    private int[] countSolved;

    public ChocoVariable(Parameter par, ArrayList<Integer> val) {
        this.param = par;
        this.name = par.getName();
        this.values = new ArrayList<Integer>(val);
        this.var = Choco.makeIntVar(par.getName(), this.values, new String[0]);
        this.groupID = -1;
        this.countSolved = new int[val.size()];
    }

    public int getGroupID() {
        return this.groupID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    public boolean checkValue(int v) {
        if (this.newDomain == null) {
            if (this.values.contains(Integer.valueOf(v))) {
                return true;
            }
            return false;
        }
        return this.newDomain.contains(v);
    }

    public void setNewDomain(IntDomain newDomain) {
        this.newDomain = newDomain;
    }

    public int getValue(int idx) {
        this.countSolved[idx] += 1;
        return ((Integer) this.values.get(idx)).intValue();
    }

    public int getIdx(int value) {
        return this.values.indexOf(Integer.valueOf(value));
    }

    public void printInfo() {
        System.out.print(this.name + ": \t");
        for (int i : this.countSolved) {
            if (i > 0) {
                System.out.print(i + "\t");
            }
        }
        System.out.println("\t" + this.newDomain);
    }
}
