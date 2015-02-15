package com.onehao.acts.testgen.service.constraint;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.onehao.acts.testgen.common.Constraint;
import com.onehao.acts.testgen.common.SUT;
import com.onehao.acts.testgen.common.TestGenProfile;

import edu.uta.cse.fireeye.common.Parameter;
import edu.uta.cse.fireeye.service.constraint.ChocoContext;
import edu.uta.cse.fireeye.service.constraint.ConstraintGraph;
import edu.uta.cse.fireeye.service.engine.Tuple;

/**
 * 
 * @author wanhao01 refactor from FireEye2.8
 */

public class ConstraintManager implements ConstraintManagerInterface {
    private SUT sut;
    private ArrayList<Constraint> constraints;
    private ConstraintGraph graph;
    public ChocoContext choco;

    public ConstraintManager(SUT sut) {
        this.sut = sut;
        this.constraints = new ArrayList<Constraint>();
    }

    public boolean init() {
        return init(this.sut.getParameters());
    }

    public boolean isSolvable() {
        return isSolvable(this.sut.getParameters());
    }

    public void debug() {
        if (this.choco != null) {
            this.choco.printInfo();
        }
    }

    public boolean init(ArrayList<Parameter> params) {
        if (TestGenProfile.instance().isIgnoreConstraints()) {
            return true;
        }
        if (this.constraints.size() == 0) {
            return true;
        }
        ArrayList<String> cons = new ArrayList<String>();
        for (Constraint c : this.constraints) {
            cons.add(c.getText());
        }
        this.choco = null;
        this.choco = new ChocoContext();

        return this.choco.initChoco(params, cons);
    }

    public boolean isSolvable(ArrayList<Parameter> params) {
        if (TestGenProfile.instance().isIgnoreConstraints()) {
            return true;
        }
        if (this.constraints.size() == 0) {
            return true;
        }
        ArrayList<String> cons = new ArrayList<String>();
        for (Constraint c : this.constraints) {
            cons.add(c.getText());
        }
        this.choco = null;
        this.choco = new ChocoContext();

        return this.choco.isSolvable(params, cons);
    }

    public void addConstraint(Constraint constraint) {
        this.constraints.add(constraint);
    }

    public void removeConstraints() {
        if (this.constraints != null) {
            this.constraints.clear();
            if (this.graph != null) {
                this.graph = null;
            }
        }
    }

    public void setConstraints(ArrayList<Constraint> constraintList) {
        this.constraints = constraintList;
    }

    public ArrayList<Constraint> getConstraints() {
        return this.constraints;
    }

    public int numOfConstraints() {
        return this.constraints.size();
    }

    public boolean isValid(int[] test) {
        if ((TestGenProfile.instance().isIgnoreConstraints()) || (this.constraints.size() == 0)) {
            return true;
        }
        if ((this.choco == null) && (!init())) {
            return false;
        }

        // this.choco.countEval += 1;

        updateChocoEvalByReflection();

        return this.choco.isSatisfied(test);
    }

    private void updateChocoEvalByReflection() {

        try {
            Class<ChocoContext> clazz = ChocoContext.class;

            Field field;
            field = clazz.getDeclaredField("countEval");
            field.setAccessible(true);

            int chocoCountEval = field.getInt(this.choco);

            field.set(this.choco, chocoCountEval + 1);
            field.setAccessible(false);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public boolean isValid(int[] test, ArrayList<Integer> pos) {
        if ((TestGenProfile.instance().isIgnoreConstraints()) || (this.constraints.size() == 0)) {
            return true;
        }
        if ((this.choco == null) && (!init())) {
            return false;
        }
        // this.choco.countEval += 1;
        updateChocoEvalByReflection();

        return this.choco.isSatisfied(test, pos);
    }

    public boolean isValid(Tuple tuple) {
        if ((TestGenProfile.instance().isIgnoreConstraints()) || (this.constraints.size() == 0)) {
            return true;
        }
        if ((this.choco == null) && (!init())) {
            return false;
        }
        // this.choco.countEval += 1;
        updateChocoEvalByReflection();

        return this.choco.isSatisfied(tuple);
    }

    public boolean isValid(int[] test, int col) {
        if ((TestGenProfile.instance().isIgnoreConstraints()) || (this.constraints.size() == 0)) {
            return true;
        }
        ArrayList<Integer> cols = new ArrayList<Integer>();
        cols.add(Integer.valueOf(col));

        return isValid(test, cols);
    }
}
