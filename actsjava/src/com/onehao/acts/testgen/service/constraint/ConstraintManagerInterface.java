package com.onehao.acts.testgen.service.constraint;

import java.util.ArrayList;

import edu.uta.cse.fireeye.common.Parameter;
import edu.uta.cse.fireeye.service.engine.Tuple;

/**
 * 
 * @author wanhao01 refactor from FireEye2.8
 */

public abstract interface ConstraintManagerInterface {
    public abstract boolean init(ArrayList<Parameter> paramArrayList);

    public abstract void debug();

    public abstract int numOfConstraints();

    public abstract boolean isSolvable();

    public abstract boolean isValid(Tuple paramTuple);

    public abstract boolean isValid(int[] paramArrayOfInt, int paramInt);

    public abstract boolean isValid(int[] paramArrayOfInt);

    public abstract boolean isValid(int[] paramArrayOfInt, ArrayList<Integer> paramArrayList);
}
