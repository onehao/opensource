package com.onehao.acts.testgen.service.engine;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;

import com.onehao.acts.testgen.common.SUT;
import com.onehao.acts.testgen.common.TestGenProfile;
import com.onehao.acts.testgen.common.TestSet;

import edu.uta.cse.fireeye.common.Parameter;
import edu.uta.cse.fireeye.common.Relation;
import edu.uta.cse.fireeye.service.CoverageCheckInfo;
import edu.uta.cse.fireeye.service.engine.Combinatorics;
import edu.uta.cse.fireeye.service.engine.PVPair;
import edu.uta.cse.fireeye.service.engine.Tuple;

/**
 * 
 * @author wanhao01 refactor from FireEye2.8
 */

public class CoverageChecker implements CoverageRetreiver {
    private SUT sut;
    private TestSet ts;
    private int allCoveredTuples;
    private int allCoverableTuples;
    private float[] coverageRatios;
    private ArrayList<Relation> relations = new ArrayList<Relation>();

    public CoverageChecker(TestSet ts, SUT sut) {
        this.sut = sut;
        this.ts = ts;
        for (int i = 0; i < ts.getNumOfParams(); i++) {
            ts.getParam(i).setActiveID(i);
        }
        setAllCoveredTuples(0);
        setAllCoverableTuples(0);

        int doi = TestGenProfile.instance().getDOI();
        if (-1 == doi) {
            this.relations = sut.getRelationManager().getRelations();
        } else {
            Relation e = new Relation(doi, sut.getParams());
            this.relations.add(e);
        }
    }

    public ArrayList<int[]> getParamGroups() {
        ArrayList<int[]> rval = new ArrayList<int[]>();
        for (Relation relation : this.relations) {
            ArrayList<int[]> groups = getParamGroups(relation);
            rval.addAll(groups);
        }
        return rval;
    }

    public ArrayList<int[]> getParamGroups(Relation relation) {
        ArrayList<int[]> rval = new ArrayList<int[]>();
        ArrayList<Parameter> params = relation.getParams();
        ArrayList<int[]> paramCombos = Combinatorics.getParamCombos(params.size(), relation.getStrength());
        for (int[] paramCombo : paramCombos) {
            ArrayList<Parameter> allParams = this.ts.getParams();
            int[] array = new int[relation.getStrength()];
            int j = 0;
            for (int i = 0; i < paramCombo.length; i++) {
                if (paramCombo[i] == 1) {
                    for (int k = 0; k < allParams.size(); k++) {
                        if (((Parameter) allParams.get(k)).getID() == ((Parameter) params.get(i)).getID()) {
                            array[(j++)] = k;
                            break;
                        }
                    }
                }
            }
            rval.add(array);
        }
        return rval;
    }

    // private void add(ArrayList<Parameter> group, ArrayList<ArrayList<Parameter>> groups)
    // {
    // boolean flag = false;
    // for (ArrayList<Parameter> it : groups) {
    // if (isContains(group, it))
    // {
    // flag = true;
    // break;
    // }
    // }
    // if (!flag) {
    // groups.add(group);
    // }
    // }

    // private boolean isContains(ArrayList<Parameter> first, ArrayList<Parameter> second)
    // {
    // boolean rval = true;
    // for (Parameter aParam : first)
    // {
    // boolean flag = false;
    // for (Parameter bParam : second) {
    // if ((aParam.getInputOrOutput() == 0) &&
    // (bParam.getInputOrOutput() == 0) &&
    // (aParam.getID() == bParam.getID()))
    // {
    // flag = true;
    // break;
    // }
    // }
    // if (!flag)
    // {
    // rval = false;
    // break;
    // }
    // }
    // return rval;
    // }

    public int getCoveredTuples(TestSet ts) {
        int rval = 0;

        ArrayList<int[]> groups = getParamGroups();
        for (int[] list : groups) {
            ArrayList<Parameter> group = getParamList(list);
            HashSet<Integer> tuples = new HashSet<Integer>();
            int[] weights = new int[group.size()];
            int maxTuples = 1;
            int w = 0;
            for (Parameter param : group) {
                weights[(w++)] = maxTuples;
                maxTuples *= param.getDomainSize();
            }
            for (int row = 0; row < ts.getNumOfTests(); row++) {
                int hash = 0;
                boolean hasDontCares = false;
                for (int i = 0; i < group.size(); i++) {
                    int column = ts.getParams().indexOf(group.get(i));
                    int v = ts.getValue(row, column);
                    if (v == -1) {
                        hasDontCares = true;
                        break;
                    }
                    hash += v * weights[i];
                }
                if (!hasDontCares) {
                    tuples.add(Integer.valueOf(hash));
                    if (tuples.size() == maxTuples) {
                        break;
                    }
                }
            }
            rval += tuples.size();
        }
        return rval;
    }

    public int getTotalCountOfAllTuples() {
        int rval = 0;

        ArrayList<int[]> groups = getParamGroups();
        ArrayList<Parameter> params = this.ts.getParams();
        for (int[] group : groups) {
            int countOfTuples = 1;
            for (int i : group) {
                countOfTuples *= ((Parameter) params.get(i)).getDomainSize();
            }
            rval += countOfTuples;
        }
        return rval;
    }

    public int getTotalCountOfCoverableTuples() {
        if (TestGenProfile.instance().isIgnoreConstraints()) {
            return getTotalCountOfAllTuples();
        }
        ArrayList<int[]>[] missingTuples = generateAllMissingTuples();
        return getTotalCount(missingTuples);
    }

    public boolean check() {
        boolean rval = true;
        if (this.sut.getNumOfParams() != this.ts.getNumOfParams()) {
            return false;
        }
        boolean progressOn = TestGenProfile.instance().isProgressOn();

        ArrayList<int[]>[] missingTuples = generateAllMissingTuples();

        int totalCountOfCoverableTuples = getTotalCount(missingTuples);
        int countOfCoveredTuples = 0;

        int numOfTests = this.ts.getNumOfTests();
        for (int row = 0; row < numOfTests; row++) {
            if (progressOn) {
                System.out.print(".");
            }
            if (!this.sut.getConstraintManager().isValid(this.ts.getTest(row))) {
                System.out.println("Found invalid test at row " + row);
            } else {
                countOfCoveredTuples += removeCoveredTuples(row, missingTuples);
            }
        }
        int missed = getTotalCount(missingTuples);
        if (missed > 0) {
            rval = false;
        }
        setAllCoverableTuples(totalCountOfCoverableTuples);
        setAllCoveredTuples(countOfCoveredTuples);
        if (progressOn) {
            System.out.println("\n\nCoverage Statistics:");
            System.out.println("--------------------------------------");
            System.out.println("Total Count of All Possible Combinations: " + getTotalCountOfAllTuples());
            System.out.println("Total Count of Coverable Combinations: " + totalCountOfCoverableTuples);
            System.out.println("Count of Covered Combinations: " + countOfCoveredTuples);
            System.out.println("Count of Missed Combinations: " + missed);
        }
        return rval;
    }

    private int getProgressInfoUpdateValue() {
        int testCount = this.ts.getNumOfTests();
        if (testCount <= 100) {
            return testCount;
        }
        BigDecimal noOfTestCases = new BigDecimal(testCount);
        BigDecimal divisor = new BigDecimal(100);
        BigDecimal multiplier = new BigDecimal(10);

        BigDecimal numerator = noOfTestCases.multiply(multiplier);
        BigDecimal result = numerator.divide(divisor, 1);
        return result.intValue();
    }

    public float getCoverageRatio(int index) {
        if (this.coverageRatios == null) {
            computeCoverageRatios();
        }
        return this.coverageRatios[index];
    }

    public float[] getCoverageRatios() {
        if (this.coverageRatios == null) {
            computeCoverageRatios();
        }
        return this.coverageRatios;
    }

    public float[] getCoverageRatios(CoverageCheckInfo cover) {
        if (this.coverageRatios == null) {
            computeCoverageRatios(cover);
        }
        return this.coverageRatios;
    }

    private void computeCoverageRatios(CoverageCheckInfo cover) {
        this.coverageRatios = new float[this.ts.getNumOfTests()];

        ArrayList<int[]>[] missingTuples = generateAllMissingTuples();

        int totalCountOfTuples = getTotalCount(missingTuples);
        int countOfCoveredTuples = 0;

        int inc = getProgressInfoUpdateValue();
        int sum = 0;
        int mult = 1;

        int numOfTests = this.ts.getNumOfTests();
        for (int row = 0; row < numOfTests; row++) {
            if (cover.isCancelled()) {
                return;
            }
            countOfCoveredTuples += removeCoveredTuples(row, missingTuples);
            this.coverageRatios[row] = (countOfCoveredTuples / totalCountOfTuples);
            if (sum == inc) {
                cover.setProgress(10 * mult);
                sum = 0;
                mult++;
            }
            sum++;
        }
    }

    private void computeCoverageRatios() {
        this.coverageRatios = new float[this.ts.getNumOfTests()];

        ArrayList<int[]>[] missingTuples = generateAllMissingTuples();

        int totalCountOfTuples = getTotalCount(missingTuples);
        int countOfCoveredTuples = 0;

        int numOfTests = this.ts.getNumOfTests();
        for (int row = 0; row < numOfTests; row++) {
            countOfCoveredTuples += removeCoveredTuples(row, missingTuples);
            this.coverageRatios[row] = (countOfCoveredTuples / totalCountOfTuples);
        }
    }

    private ArrayList<int[]>[] generateAllMissingTuples() {
        ArrayList<int[]> groups = getParamGroups();

        int numOfParamCombos = groups.size();
        ArrayList[] rval = new ArrayList[numOfParamCombos];

        int index = 0;
        for (int[] list : groups) {
            ArrayList<Parameter> group = getParamList(list);

            ArrayList<int[]> valueCombos = Combinatorics.getValueCombos(group);
            ArrayList<int[]> validValueCombos = new ArrayList<int[]>();
            for (int[] valueCombo : valueCombos) {
                Tuple tuple = buildTuple(group, valueCombo);
                if (this.sut.getConstraintManager().isValid(tuple)) {
                    validValueCombos.add(valueCombo);
                }
            }
            rval[(index++)] = validValueCombos;
        }
        return rval;
    }

    private Tuple buildTuple(ArrayList<Parameter> group, int[] valueCombo) {
        Tuple rval = new Tuple(group.size());
        int i = 0;
        for (Parameter param : group) {
            PVPair pair = new PVPair(param, valueCombo[i]);
            rval.addPair(pair);
            i++;
        }
        return rval;
    }

    private int getTotalCount(ArrayList<int[]>[] missingTuples) {
        int rval = 0;
        for (int i = 0; i < missingTuples.length; i++) {
            if (missingTuples[i].size() > 0) {
                rval += missingTuples[i].size();
            }
        }
        return rval;
    }

    private int removeCoveredTuples(int row, ArrayList<int[]>[] missingTuples) {
        int rval = 0;

        ArrayList<int[]> groups = getParamGroups();

        int k = 0;
        for (int[] list : groups) {
            ArrayList<Parameter> group = getParamList(list);

            int[] values = new int[group.size()];
            int j = 0;
            boolean hasDontCares = false;
            for (int i = 0; i < group.size(); i++) {
                int column = this.ts.getColumnID(((Parameter) group.get(i)).getID());
                if ((values[(j++)] = this.ts.getValue(row, column)) == -1) {
                    hasDontCares = true;
                    break;
                }
            }
            if (!hasDontCares) {
                int found = search(values, missingTuples[k]);
                if (found != -1) {
                    rval++;
                    missingTuples[k].remove(found);
                } else {
                    search(values, missingTuples[k]);
                }
            }
            k++;
        }
        return rval;
    }

    private int search(int[] combo, ArrayList<int[]> combos) {
        int rval = -1;

        int start = 0;
        int end = combos.size() - 1;
        while (start <= end) {
            int mid = (start + end) / 2;
            if (compare(combo, (int[]) combos.get(mid)) < 0) {
                end = mid - 1;
            } else if (compare(combo, (int[]) combos.get(mid)) > 0) {
                start = mid + 1;
            } else {
                rval = mid;
                break;
            }
        }
        return rval;
    }

    private int compare(int[] a, int[] b) {
        int rval = 0;
        for (int i = 0; i < a.length; i++) {
            if (a[i] < b[i]) {
                rval = -1;
                break;
            }
            if (a[i] > b[i]) {
                rval = 1;
                break;
            }
        }
        return rval;
    }

    public ArrayList<Parameter> getParamList(int[] t) {
        ArrayList<Parameter> list = new ArrayList<Parameter>();
        ArrayList<Parameter> params = this.ts.getParams();
        for (int i : t) {
            list.add((Parameter) params.get(i));
        }
        return list;
    }

    public TestSet getTs() {
        return this.ts;
    }

    public void setTs(TestSet ts) {
        this.ts = ts;
    }

    public int getAllCoveredTuples() {
        return this.allCoveredTuples;
    }

    public void setAllCoveredTuples(int allPossibleTuples) {
        this.allCoveredTuples = allPossibleTuples;
    }

    public int getAllCoverableTuples() {
        return this.allCoverableTuples;
    }

    public void setAllCoverableTuples(int allCoverableTuples) {
        this.allCoverableTuples = allCoverableTuples;
    }
}
