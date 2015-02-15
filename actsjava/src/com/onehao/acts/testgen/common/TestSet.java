package com.onehao.acts.testgen.common;

import java.util.ArrayList;
import java.util.Arrays;

import edu.uta.cse.fireeye.common.Parameter;
import edu.uta.cse.fireeye.service.engine.PVPair;
import edu.uta.cse.fireeye.service.engine.Tuple;

/**
 * 
 * @author wanhao01 refactor from FireEye2.8
 */

public class TestSet {
    public static final int DONT_CARE = -1;
    public static final String DONT_CARE_STRING = "*";
    private ArrayList<Parameter> params;
    private ArrayList<int[]> matrix;
    private ArrayList<Boolean> colors;
    private ArrayList<Parameter> outputParams = new ArrayList<Parameter>();
    private int existingRows;
    private int existingCols;

    public int getExistingRows() {
        return this.existingRows;
    }

    public int numberOfCoveredCombos = 0;
    private float generationTime;

    public void setExistingRows(int existingRows) {
        this.existingRows = existingRows;
    }

    public int getExistingCols() {
        return this.existingCols;
    }

    public void setExistingCols(int existingCols) {
        this.existingCols = existingCols;
    }

    public TestSet() {
        this.matrix = new ArrayList<int[]>();
        this.colors = new ArrayList<Boolean>();
    }

    public TestSet(ArrayList<Parameter> params) {
        this.params = params;
        this.matrix = new ArrayList<int[]>();
        this.colors = new ArrayList<Boolean>();
    }

    public TestSet(ArrayList<Parameter> params, ArrayList<Parameter> outputParams) {
        this.params = params;
        this.outputParams = outputParams;
        this.matrix = new ArrayList<int[]>();
        this.colors = new ArrayList<Boolean>();
    }

    public void setParams(ArrayList<Parameter> params) {
        this.params = params;
    }

    public ArrayList<Parameter> getParams() {
        return this.params;
    }

    public int getNumOfParams() {
        return this.params.size();
    }

    public Parameter getParam(int index) {
        return (Parameter) this.params.get(index);
    }

    public boolean containsParam(Parameter param) {
        return getColumnID(param.getID()) != -1;
    }

    public boolean containsOutputParam(Parameter outputParam) {
        boolean rval = false;
        for (Parameter param : this.outputParams) {
            if (param.getID() == outputParam.getID()) {
                rval = true;
                break;
            }
        }
        return rval;
    }

    public int getColumnID(int paramID) {
        int rval = -1;
        int index = 0;
        for (Parameter param : this.params) {
            if (param.getID() == paramID) {
                rval = index;
                break;
            }
            index++;
        }
        return rval;
    }

    public int getOutputParamColumnID(int outputParamID) {
        int rval = -1;
        int index = 0;
        for (Parameter param : this.outputParams) {
            if (param.getID() == outputParamID) {
                rval = index;
                break;
            }
            index++;
        }
        rval = index + getNumOfParams();
        return rval;
    }

    public void addMatrix(ArrayList<int[]> matrix) {
        for (int[] row : matrix) {
            int numOfColumns = row.length < getNumOfParams() ? row.length : getNumOfParams();
            int[] test = new int[getNumOfParams() + this.outputParams.size()];
            Arrays.fill(test, -1);
            for (int i = 0; i < numOfColumns; i++) {
                test[i] = row[i];
            }
            for (int i = 0; i < this.outputParams.size(); i++) {
                test[(getNumOfParams() + i)] = 1000000000;
            }
            this.matrix.add(test);
        }
    }

    public ArrayList<int[]> getMatrix() {
        return this.matrix;
    }

    public void setMatrix(ArrayList<int[]> matrix) {
        this.matrix = matrix;
    }

    public int[] getTest(int row) {
        return (int[]) this.matrix.get(row);
    }

    public int getNumOfTests() {
        return this.matrix.size();
    }

    public int getValue(int row, int column) {
        return ((int[]) this.matrix.get(row))[column];
    }

    public void setValue(int row, int column, int value) {
        ((int[]) this.matrix.get(row))[column] = value;
    }

    public boolean isCompatible(int row, Tuple tuple) {
        boolean rval = true;
        int numOfPairs = tuple.getNumOfPairs();
        for (int i = 0; i < numOfPairs; i++) {
            PVPair pair = tuple.getPair(i);
            int column = pair.param.getActiveID();
            if ((((int[]) this.matrix.get(row))[column] != -1)
                    && (((int[]) this.matrix.get(row))[column] != pair.value)) {
                rval = false;
                break;
            }
        }
        return rval;
    }

    public int[] makeCompatibleTest(int row, int[] test, ArrayList<Integer> changedCols) {
        changedCols.clear();
        int[] rval = (int[]) ((int[]) this.matrix.get(row)).clone();
        for (int i = 0; i < test.length; i++) {
            if (test[i] != -1) {
                if (rval[i] == -1) {
                    rval[i] = test[i];
                    changedCols.add(Integer.valueOf(i));
                } else if (rval[i] != test[i]) {
                    return null;
                }
            }
        }
        return rval;
    }

    public boolean isCompatible(int[] row1, int[] row2) {
        boolean rval = true;
        if (row1.length != row2.length) {
            rval = false;
        }
        for (int i = 0; i < row1.length; i++) {
            if ((row1[i] != -1) && (row2[i] != -1) && (row1[i] != row2[i])) {
                rval = false;
                break;
            }
        }
        return rval;
    }

    public void cover(int row, Tuple tuple) {
        int numOfPairs = tuple.getNumOfPairs();
        for (int i = 0; i < numOfPairs; i++) {
            PVPair pair = tuple.getPair(i);
            int column = pair.param.getActiveID();
            ((int[]) this.matrix.get(row))[column] = pair.value;
        }
    }

    public void cover(int row, int[] tuple) {
        int[] test = (int[]) this.matrix.get(row);
        for (int i = 0; i < tuple.length; i++) {
            if (tuple[i] != -1) {
                test[i] = tuple[i];
            }
        }
    }

    public void addNewTest(Tuple tuple) {
        int[] test = new int[getNumOfParams() + this.outputParams.size()];
        for (int i = 0; i < getNumOfParams(); i++) {
            test[i] = -1;
        }
        for (int i = 0; i < this.outputParams.size(); i++) {
            test[(getNumOfParams() + i)] = 1000000000;
        }
        this.matrix.add(test);
        cover(this.matrix.size() - 1, tuple);
    }

    public void addNewTest(int[] tuple) {
        int[] test = new int[getNumOfParams() + this.outputParams.size()];
        for (int i = 0; i < getNumOfParams(); i++) {
            test[i] = -1;
        }
        this.matrix.add(test);
        cover(this.matrix.size() - 1, tuple);
    }

    public void addTest(int[] test) {
        int[] newTest = (int[]) null;
        if (test.length > getNumOfParams()) {
            newTest = new int[test.length];
        } else {
            newTest = new int[test.length + this.outputParams.size()];
        }
        for (int i = 0; i < test.length; i++) {
            newTest[i] = test[i];
        }
        if (test.length <= getNumOfParams()) {
            for (int i = 0; i < this.outputParams.size(); i++) {
                if (newTest[(test.length + i)] == 0) {
                    newTest[(test.length + i)] = 1000000000;
                }
            }
        }
        this.matrix.add(newTest);
    }

    private int[] translate(ArrayList<String> test) {
        int[] rval = new int[test.size()];
        for (int i = 0; i < test.size(); i++) {
            Parameter param = getParam(i);
            rval[i] = param.getIndex((String) test.get(i));
        }
        return rval;
    }

    public float getGenerationTime() {
        return this.generationTime;
    }

    public void setGenerationTime(float generationTime) {
        this.generationTime = generationTime;
    }

    public int[] clone(int row) {
        int[] rval = new int[getNumOfParams()];
        for (int i = 0; i < rval.length; i++) {
            rval[i] = getValue(row, i);
        }
        return rval;
    }

    public ArrayList<Parameter> getOutputParams() {
        return this.outputParams;
    }

    public void setOutputParams(ArrayList<Parameter> outputParams) {
        this.outputParams = outputParams;
    }

    public Boolean getNeedConstraintCheck(int row) {
        while (this.colors.size() < this.matrix.size()) {
            this.colors.add(Boolean.valueOf(true));
        }
        return (Boolean) this.colors.get(row);
    }

    public void setNeedConstraintCheck(int row, Boolean need) {
        while (this.colors.size() < this.matrix.size()) {
            this.colors.add(Boolean.valueOf(true));
        }
        this.colors.set(row, need);
    }

    public String toString() {
        StringBuffer rval = new StringBuffer();
        for (int i = 0; i < this.matrix.size(); i++) {
            for (int j = 0; j < ((int[]) this.matrix.get(i)).length; j++) {
                if (j > 0) {
                    rval.append(" ");
                }
                rval.append(((int[]) this.matrix.get(i))[j]);
            }
            rval.append("\n");
        }
        return rval.toString();
    }
}
