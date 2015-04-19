package com.onehao.acts.testgen.service.engine;

import java.util.ArrayList;

import com.onehao.acts.testgen.common.SUT;
import com.onehao.acts.testgen.common.TestGenProfile;
import com.onehao.acts.testgen.common.TestSet;

import edu.uta.cse.fireeye.common.Parameter;
import edu.uta.cse.fireeye.common.Relation;
import edu.uta.cse.fireeye.util.Util;

/**
 * 
 * @author wanhao01 refactor from FireEye2.8
 */

public class BinaryBuilder {
    private ArrayList<Parameter> params;
    private ArrayList<Parameter> outputParams;
    private int doi;
    private ArrayList<Parameter> firstGroup;
    private ArrayList<Parameter> secondGroup;
    TestSet ts;

    public BinaryBuilder(ArrayList<Parameter> params, ArrayList<Parameter> outputParams) {
        this.params = Util.orderParams(params);
        this.outputParams = outputParams;
        for (int i = 0; i < this.params.size(); i++) {
            ((Parameter) this.params.get(i)).setID(i);
        }
        this.doi = TestGenProfile.instance().getDOI();

        this.firstGroup = new ArrayList<Parameter>();
        this.secondGroup = new ArrayList<Parameter>();

        this.ts = null;
    }

    public TestSet getTestSet(ArrayList<Parameter> outputParams) {
        this.outputParams = outputParams;
        if (this.ts == null) {
            build();
        }
        return this.ts;
    }

    public void build() {
        this.ts = new TestSet(this.params, this.outputParams);

        boolean progressOn = TestGenProfile.instance().isProgressOn();
        if (progressOn) {
            System.out.println("Step 1: Divide params into two groups");
        }
        divideParams();
        if (progressOn) {
            System.out.println("\nStep 2: Build a covering array CA1 for the first group");
        }
        SUT firstGroupSUT = new SUT(this.firstGroup, this.outputParams);
        Builder builder = new Builder(firstGroupSUT);
        TestSet base = builder.generate();
        if (progressOn) {
            System.out.println("\nStep 3: Double the columns in CA1");
        }
        doubleColumns(base);
        if (progressOn) {
            System.out.println("\nStep 4: Add rows to make a complete covering array");
        }
        addRows();
    }

    private void divideParams() {
        for (int i = 0; i < this.params.size(); i++) {
            if (i % 2 == 0) {
                this.firstGroup.add((Parameter) this.params.get(i));
            } else {
                this.secondGroup.add((Parameter) this.params.get(i));
            }
        }
    }

    private void doubleColumns(TestSet base) {
        for (int i = 0; i < base.getNumOfTests(); i++) {
            int[] test = new int[this.params.size()];
            for (int j = 0; j < base.getNumOfParams(); j++) {
                test[(2 * j)] = base.getValue(i, j);
                if (2 * j + 1 < this.params.size()) {
                    Parameter param = (Parameter) this.params.get(2 * j + 1);
                    if (base.getValue(i, j) < param.getDomainSize()) {
                        test[(2 * j + 1)] = base.getValue(i, j);
                    } else {
                        test[(2 * j + 1)] = -1;
                    }
                }
            }
            this.ts.addTest(test);
        }
    }

    private void addRows() {
        if (this.doi == 2) {
            addRowsFor2Way();
        } else {
            addRowsPartI();
            if (this.doi == 4) {
                addRowsPartII();
            } else {
                boolean progressOn = TestGenProfile.instance().isProgressOn();
                if (progressOn) {
                    System.out.println("\nStep 5: Add rows to cover remaining combinations.");
                }
                BinaryIpoEngine engine = new BinaryIpoEngine(this.ts, this.firstGroup, this.secondGroup);
                engine.extend();
            }
        }
    }

    private void addRowsFor2Way() {
        int startRow = this.ts.getNumOfTests();
        for (int k = 0; k < this.firstGroup.size(); k++) {
            if (2 * k + 1 < this.ts.getNumOfParams()) {
                Parameter first = this.ts.getParam(2 * k);
                Parameter second = this.ts.getParam(2 * k + 1);
                ArrayList<int[]> pairs = getUnequalPairs(first, second);
                if (k == 0) {
                    this.ts.addMatrix(pairs);
                } else {
                    int row = startRow;
                    for (int i = 0; i < pairs.size(); i++) {
                        this.ts.setValue(row, 2 * k, ((int[]) pairs.get(i))[0]);
                        this.ts.setValue(row, 2 * k + 1, ((int[]) pairs.get(i))[1]);
                        row++;
                    }
                }
            }
        }
    }

    private ArrayList<int[]> getUnequalPairs(Parameter first, Parameter second) {
        ArrayList<int[]> rval = new ArrayList<int[]>();
        for (int i = 0; i < first.getDomainSize(); i++) {
            for (int j = 0; j < second.getDomainSize(); j++) {
                if (i != j) {
                    int[] pair = new int[2];
                    pair[0] = i;
                    pair[1] = j;
                    rval.add(pair);
                }
            }
        }
        return rval;
    }

    private void addRowsPartI() {
        System.out.println("Build a covering array of strength " + (this.doi - 1) + " for the first group.");
        SUT firstGroupSUT = new SUT(this.firstGroup);
        firstGroupSUT.addRelation(new Relation(this.doi - 1, this.firstGroup));
        Builder builder = new Builder(firstGroupSUT);
        TestSet seed = builder.generate();

        Parameter firstParam = (Parameter) this.params.get(0);
        Parameter secondParam = (Parameter) this.params.get(1);
        int numOfMappings =
                firstParam.getDomainSize() == secondParam.getDomainSize() ? firstParam.getDomainSize() - 1
                        : secondParam.getDomainSize();
        for (int i = 1; i <= numOfMappings; i++) {
            for (int j = 0; j < seed.getNumOfTests(); j++) {
                int[] test = new int[this.params.size()];
                for (int k = 0; k < seed.getNumOfParams(); k++) {
                    test[(2 * k)] = seed.getValue(j, k);
                    if (2 * k + 1 < test.length) {
                        Parameter nextParam = (Parameter) this.params.get(2 * k + 1);
                        test[(2 * k + 1)] = ((test[(2 * k)] + i) % nextParam.getDomainSize());
                    }
                }
                this.ts.addTest(test);
            }
        }
    }

    private void addRowsPartII() {
        System.out.println("\nBuild a covering array of strength " + (this.doi - 2) + " for the first group.");
        ArrayList<Parameter> squaredParams = squareDomainSizes();
        SUT squaredSUT = new SUT(squaredParams);
        squaredSUT.addRelation(new Relation(this.doi - 2, squaredParams));
        Builder builder = new Builder(squaredSUT);
        TestSet seed = builder.generate();
        for (int i = 0; i < seed.getNumOfTests(); i++) {
            int[] test = new int[this.params.size()];
            for (int j = 0; j < seed.getNumOfParams(); j++) {
                if (2 * j + 1 < test.length) {
                    int[] pair = getMappedPair((Parameter) this.params.get(2 * j + 1), seed.getValue(i, j));
                    test[(2 * j)] = pair[0];
                    test[(2 * j + 1)] = pair[1];
                } else {
                    test[(2 * j)] = seed.getValue(i, j);
                }
            }
            this.ts.addTest(test);
        }
    }

    private ArrayList<Parameter> squareDomainSizes() {
        ArrayList<Parameter> rval = new ArrayList<Parameter>();
        for (int i = 0; i < this.firstGroup.size(); i++) {
            Parameter currParam = (Parameter) this.firstGroup.get(i);
            int currDomainSize = currParam.getDomainSize();
            Parameter newParam = new Parameter(currParam.getName() + "s");
            int domainSize = 0;
            if (i < this.secondGroup.size()) {
                Parameter partner = (Parameter) this.secondGroup.get(i);
                domainSize = currDomainSize * partner.getDomainSize();
            } else {
                domainSize = currDomainSize;
            }
            for (int j = 0; j < domainSize; j++) {
                newParam.addValue("v" + j);
            }
            rval.add(newParam);
        }
        return rval;
    }

    private int[] getMappedPair(Parameter param, int value) {
        int[] rval = new int[2];
        rval[0] = (value / param.getDomainSize());
        rval[1] = (value % param.getDomainSize());
        return rval;
    }
}
