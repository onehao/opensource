package com.onehao.acts.testgen.service.engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;

import com.onehao.acts.testgen.common.SUT;
import com.onehao.acts.testgen.common.TestGenProfile;
import com.onehao.acts.testgen.common.TestSet;
import com.onehao.acts.testgen.service.constraint.ConstraintManagerInterface;

import edu.uta.cse.fireeye.common.Parameter;
import edu.uta.cse.fireeye.common.Relation;
import edu.uta.cse.fireeye.service.constraint.ChocoContext;
import edu.uta.cse.fireeye.service.engine.Combinatorics;
import edu.uta.cse.fireeye.service.engine.RelationManager;
import edu.uta.cse.fireeye.service.engine.TimeHelper;
import edu.uta.cse.fireeye.service.engine.TupleList;
import edu.uta.cse.fireeye.util.Util;

/***
 * 
 * @author wanhao01
 * 
 */

public class IpoEngine {
    private SUT sut;
    private ArrayList<Parameter> params;
    private ArrayList<Parameter> outputParams;
    private ConstraintManagerInterface constraintManager;
    private TestSet ts;
    private int nCoveredCombos;
    private Random random = new Random();
    private boolean extend;
    private boolean hasConstraint;
    private float sovlerInitTime;
    String processText = "";

    /**
     * @author wanhao01 refactor and add debug print.
     */
    public void debugPrint() {
        System.out.println("Number Of Tests::" + this.ts.getNumOfTests());

        StringBuffer buff1 = new StringBuffer();
        for (int v = 0; v < this.ts.getNumOfParams(); v++) {
            Parameter param = this.ts.getParam(v);
            buff1.append(param.getName() + " ");
        }
        System.out.println(buff1.toString());
        for (int i = 0; i < this.ts.getNumOfTests(); i++) {
            StringBuffer buff = new StringBuffer();
            for (int n = 0; n < this.ts.getNumOfParams(); n++) {
                Parameter param = this.ts.getParam(n);

                int col = this.ts.getColumnID(param.getID());
                int value = this.ts.getValue(i, col);
                if (value == -1) {
                    buff.append("* ,");
                } else {
                    buff.append(param.getValue(value) + " ,");
                }
            }
            System.out.println(buff.toString());
        }
    }

    private void init(SUT sut) {
        this.params = Util.orderParams(sut.getParams());
        this.sut = sut;
        this.outputParams = sut.getOutputParameters();
        if (sut.getNumOfRelations() == 0) {
            Relation defaultRelation = new Relation(TestGenProfile.instance().getDOI(), this.params);
            sut.addRelation(defaultRelation);
        }
        if (this.outputParams == null) {
            this.outputParams = new ArrayList<Parameter>();
        }
        this.ts = new TestSet(this.params, this.outputParams);
        for (int i = 0; i < this.params.size(); i++) {
            ((Parameter) this.params.get(i)).setActiveID(i);
        }

        this.random = new Random();
        this.extend = false;
    }

    public IpoEngine(SUT sut, ConstraintManagerInterface mgr) {
        this.sut = sut;
        this.constraintManager = mgr;
        // init();
    }

    public IpoEngine(SUT sut) {
        this.sut = sut;
        this.constraintManager = sut.getConstraintManager();
        // init();
    }

    public TestSet getTestSet() {
        return this.ts;
    }

    public int getNumberOfCoveredCombos() {
        return this.nCoveredCombos;
    }

    private void setExistingTestSet(TestSet ts, TestSet ets) {
        int length = 0;

        int numOfParams = this.sut.getNumOfParams();
        if (ts.getParams().equals(ets.getParams())) {
            ts.setMatrix(ets.getMatrix());
        } else {
            ArrayList<Parameter> etsParams = ets.getParams();
            ArrayList<Parameter> tsParams = ts.getParams();
            for (int i = 0; i < ets.getNumOfTests(); i++) {
                int[] test = ets.getTest(i);
                length = test.length;
                int[] dup = new int[numOfParams];
                Arrays.fill(dup, -1);
                for (int j = 0; j < etsParams.size(); j++) {
                    String name = ((Parameter) etsParams.get(j)).getName();
                    if (j < numOfParams) {
                        for (int k = 0; k < tsParams.size(); k++) {
                            if ((((Parameter) tsParams.get(k)).getName().equals(name))
                                    && (test[j] < ((Parameter) tsParams.get(k)).getDomainSize())) {
                                dup[k] = test[j];
                            }
                        }
                    }
                }
                if (this.sut.getConstraintManager().isValid(dup)) {
                    ts.addTest(dup);
                }
            }
        }
        ts.setExistingRows(ts.getNumOfTests());
        ts.setExistingCols(length);
    }

    public void build() {
        build(TestGenProfile.Algorithm.ipog);
    }

    public void build(TestGenProfile.Algorithm algo) {
        this.params = Util.orderParams(this.sut.getParams());
        for (int i = 0; i < this.params.size(); i++) {
            ((Parameter) this.params.get(i)).setActiveID(i);
        }
        if (this.constraintManager.numOfConstraints() == 0) {
            this.hasConstraint = false;
        } else {
            this.hasConstraint = true;
        }
        long startTime = System.currentTimeMillis();
        this.constraintManager.init(this.params);
        this.sovlerInitTime = ((float) (System.currentTimeMillis() - startTime) / 1000.0F);

        this.ts =
                generate(algo, this.sut.getExistingTestSet(), this.sut.getRelationManager(), this.sut.getNumOfParams());
        if (TestGenProfile.instance().randstar()) {
            carefy(this.ts);
        }
        float ipogTime = (float) (System.currentTimeMillis() - startTime) / 1000.0F;
        if (TestGenProfile.instance().isProgressOn()) {
            System.out.println("Time for solver init: " + this.sovlerInitTime);
            System.out.println("Total time for test generation: " + ipogTime);
            this.constraintManager.debug();
        }
        System.out.println("Parameters\t: " + this.params.size());
        System.out.println("Constraints\t: " + this.constraintManager.numOfConstraints());
        if (algo != TestGenProfile.Algorithm.ipog_r) {
            System.out.println("Covered Tuples\t: " + getNumberOfCoveredCombos());
        }
    }

    private TestSet generate(TestGenProfile.Algorithm algo, TestSet existingTestSet, RelationManager relationManager,
            int lastColumn) {
        ArrayList<Parameter> subParams = new ArrayList<Parameter>();
        for (int j = 0; j < lastColumn; j++) {
            subParams.add((Parameter) this.params.get(j));
        }
        this.ts = new TestSet(subParams);
        if ((existingTestSet != null)
                && ((algo == TestGenProfile.Algorithm.ipog_r) || (TestGenProfile.instance().getMode() == TestGenProfile.Mode.extend))) {
            this.extend = true;
            setExistingTestSet(this.ts, existingTestSet);
        } else {
            this.extend = false;
        }
        long startTime = System.currentTimeMillis();

        int minStrength = relationManager.getMinStrength();
        int startColumn = 0;
        if (TestGenProfile.instance().getCombine().equals("all")) {
            ArrayList<int[]> allCombinationMatrix = buildAllCombinationMatrix(this.ts);
            this.ts.addMatrix(allCombinationMatrix);
            return this.ts;
        }
        if (this.extend) {
            startColumn = minStrength - 1;
        } else {
            ArrayList<int[]> initMatrix = buildInitialMatrix(minStrength);
            this.ts.addMatrix(initMatrix);
            this.nCoveredCombos = initMatrix.size();
            startColumn = minStrength;
            int numOfCoveredParams = +this.params.size() < minStrength ? this.params.size() : minStrength;
            System.out.println("Number Of Covered Params::" + numOfCoveredParams);
            debugPrint();
        }
        float expandTime = 0.0F;
        float currExpandTime = 0.0F;
        float growTime = 0.0F;
        float currGrowTime = 0.0F;
        float buildTime = 0.0F;
        float currBuildTime = 0.0F;
        float carefyTime = 0.0F;
        for (int column = startColumn; column < lastColumn; column++) {
            this.processText =
                    ("Generating Test Set for " + (column + 1) + "/" + this.ts.getNumOfParams() + " Parameters ...");

            TimeHelper.instance().countDown();

            TupleListGroup lists = null;
            lists = new TupleListGroup(this.params, relationManager, column);
            lists.build();

            lists.setConstraintManager(this.constraintManager);
            if (!ChocoContext.DONNOT_REMOVE_MISSING) {
                lists.removeInvalidTuples();
            }
            currBuildTime = TimeHelper.instance().getDuration();
            buildTime += currBuildTime;
            TimeHelper.instance().countDown();

            int nCoveredCombosBackup = this.nCoveredCombos;
            ArrayList<int[]> backup = new ArrayList<int[]>();
            if (algo == TestGenProfile.Algorithm.ipog_hybrid) {
                for (int i = 0; i < this.ts.getNumOfTests(); i++) {
                    backup.add((int[]) this.ts.getTest(i).clone());
                }
            }
            if (algo == TestGenProfile.Algorithm.ipof) {
                expand_F(this.ts, column, lists);
            } else {
                expand(this.ts, column, lists);
            }
            currExpandTime = TimeHelper.instance().getDuration();
            expandTime += currExpandTime;
            TimeHelper.instance().countDown();
            if (algo == TestGenProfile.Algorithm.ipog_r) {
                grow_r(this.ts, column, lists, relationManager);
            } else {
                grow(this.ts, column, lists);
            }
            if (algo == TestGenProfile.Algorithm.ipog_hybrid) {
                int size1 = this.nCoveredCombos;
                int testSize1 = this.ts.getNumOfTests();

                ArrayList<int[]> backup1 = new ArrayList<int[]>();
                for (int i = 0; i < this.ts.getNumOfTests(); i++) {
                    backup1.add((int[]) this.ts.getTest(i).clone());
                }
                this.nCoveredCombos = nCoveredCombosBackup;
                this.ts.setMatrix(backup);

                TupleListGroup lists2 = null;
                lists2 = new TupleListGroup(this.ts.getParams(), relationManager, column);
                lists2.build();
                lists2.setConstraintManager(this.constraintManager);

                expand_F(this.ts, column, lists2);
                grow(this.ts, column, lists2);

                int size2 = this.nCoveredCombos;
                int testSize2 = this.ts.getNumOfTests();
                if (testSize1 < testSize2) {
                    this.ts.setMatrix(backup1);
                } else {
                    lists = lists2;
                }
            }
            currGrowTime = TimeHelper.instance().getDuration();
            growTime += currGrowTime;
            if (TestGenProfile.instance().isProgressOn()) {
                System.out.println("[" + column + "]: " + this.ts.getParam(column).getName() + " "
                        + this.ts.getNumOfTests() + " tests, " + (currGrowTime + currExpandTime + currBuildTime));
            }
        }
        TimeHelper.instance().countDown();

        this.ts.numberOfCoveredCombos = getNumberOfCoveredCombos();

        return this.ts;
    }

    private void grow_r(TestSet ts, int column, TupleListGroup lists, RelationManager relationManager) {
        Parameter currParam = (Parameter) this.params.get(column);

        int totalMissing = 0;
        for (int i = 0; i < currParam.getDomainSize(); i++) {
            totalMissing += lists.getMissingCountForValue(i);
        }
        if (totalMissing == 0) {
            return;
        }
        int minStrength = relationManager.getMinStrength();
        if ((minStrength <= 2) || (totalMissing <= currParam.getDomainSize())) {
            grow(ts, column, lists);
            return;
        }
        lists.removeInvalidTuples();
        ArrayList<Integer> paramValues = new ArrayList<Integer>();
        int[] missing = new int[currParam.getDomainSize()];
        for (int i = 0; i < currParam.getDomainSize(); i++) {
            missing[i] = lists.getMissingCountForValue(i);
            totalMissing += missing[i];
            paramValues.add(Integer.valueOf(i));
        }
        ArrayList<Parameter> subParams = new ArrayList<Parameter>();
        for (int j = 0; j < column; j++) {
            subParams.add((Parameter) this.params.get(j));
        }
        RelationManager newRelations = new RelationManager();
        ArrayList<Relation> relations = new ArrayList<Relation>();
        for (Relation oldR : relationManager.getRelations()) {
            if (oldR.getParams().contains(currParam)) {
                Relation r = new Relation(oldR.getStrength() - 1);
                for (int i = 0; i < column; i++) {
                    if (oldR.getParams().contains(this.params.get(i))) {
                        r.addParam((Parameter) this.params.get(i));
                    }
                }
                relations.add(r);
            }
        }
        newRelations.setRelations(relations);

        // onehao refactor
        Iterator<Integer> iterator = paramValues.iterator();
        while (iterator.hasNext()) {
            int value = ((Integer) iterator.next()).intValue();
            int missCount = lists.getMissingCountForValue(value);
            if (missCount != 0) {
                TestSet ets = new TestSet(subParams);

                ArrayList<int[]> matrix = new ArrayList<int[]>();
                ArrayList<Integer> selectedRows = new ArrayList<Integer>();
                for (int row = 0; row < ts.getNumOfTests(); row++) {
                    if ((ts.getValue(row, column) == value) || (ts.getValue(row, column) == -1)) {
                        selectedRows.add(Integer.valueOf(row));
                        int[] test = new int[column];
                        for (int j = 0; j < test.length; j++) {
                            test[j] = ts.getValue(row, j);
                        }
                        matrix.add(test);
                    }
                }
                ets.addMatrix(matrix);

                TestSet newTs = generate(TestGenProfile.Algorithm.ipog_r, ets, newRelations, column);
                for (int row = 0; row < newTs.getNumOfTests(); row++) {
                    if (row < selectedRows.size()) {
                        for (int j = 0; j < newTs.getNumOfParams(); j++) {
                            int row2 = ((Integer) selectedRows.get(row)).intValue();
                            int value2 = newTs.getValue(row, j);
                            int oldValue = ts.getValue(row2, j);
                            if ((oldValue != -1) && (oldValue != value2)) {
                                System.err.println("error in recursion");
                            }
                            if ((oldValue == -1) && (oldValue != value2)) {
                                ts.setValue(row2, j, value2);
                                ts.setValue(row2, column, value);
                            }
                        }
                    } else {
                        int[] test = new int[column + 1];
                        for (int j = 0; j < column; j++) {
                            test[j] = newTs.getValue(row, j);
                        }
                        test[column] = value;
                        ts.addNewTest(test);
                    }
                }
            }
        }
    }

    private ArrayList<int[]> buildAllCombinationMatrix(TestSet ts) {
        ArrayList<int[]> rval = new ArrayList<int[]>();
        ArrayList<Parameter> params = ts.getParams();
        ArrayList<int[]> matrix = Combinatorics.getValueCombos(params);
        for (int[] row : matrix) {
            int[] test = new int[ts.getNumOfParams()];
            for (int i = 0; i < params.size(); i++) {
                test[i] = row[i];
            }
            if (this.constraintManager.isValid(test)) {
                rval.add(row);
            }
        }
        return rval;
    }

    private ArrayList<int[]> buildInitialMatrix(int strength) {
        ArrayList<int[]> rval = new ArrayList<int[]>();
        int numOfParams = this.params.size() < strength ? this.params.size() : strength;
        ArrayList<Parameter> initialParams = new ArrayList<Parameter>(numOfParams);
        for (int i = 0; i < numOfParams; i++) {
            initialParams.add((Parameter) this.params.get(i));
        }
        ArrayList<int[]> matrix = Combinatorics.getValueCombos(initialParams);
        for (int[] row : matrix) {
            int[] test = new int[numOfParams];
            Arrays.fill(test, -1);
            for (int i = 0; i < numOfParams; i++) {
                test[i] = row[i];
            }
            if (this.constraintManager.isValid(test)) {
                rval.add(row);
            }
        }
        return rval;
    }

    private void expand_F(TestSet ts, int column, TupleListGroup lists) {
        long startTime = System.currentTimeMillis();

        RelationManager relationMgr = this.sut.getRelationManager();

        Parameter param = ts.getParam(column);
        int domainSize = param.getDomainSize();
        int rowCount = ts.getNumOfTests();

        int[][] canCoverAtMost = new int[rowCount][domainSize];
        ArrayList<Integer> restRows = new ArrayList<Integer>(rowCount);
        int[] values = new int[2 * domainSize];
        for (int i = 0; i < domainSize; i++) {
            int tmp82_80 = i;
            values[(i + domainSize)] = tmp82_80;
            values[i] = tmp82_80;
        }
        boolean[][] isInvalid = new boolean[rowCount][domainSize];

        ArrayList<Parameter> pars = new ArrayList<Parameter>();
        for (int i = 0; i <= column; i++) {
            pars.add((Parameter) this.params.get(i));
        }
        int base = relationMgr.getNumberOfAllParamCombo(pars);

        int maxCover = base;
        int lastChoice = 0;
        for (int row = 0; row < rowCount; row++) {
            if (ts.getValue(row, column) != -1) {
                coverTuples(lists, getCoveredTupleIdx(ts.getTest(row), lists));
            } else {
                int maxCoverableTuples = findMaxCoverable(ts, base, row, column);
                restRows.add(Integer.valueOf(row));
                for (int v = 0; v < domainSize; v++) {
                    canCoverAtMost[row][v] = maxCoverableTuples;

                    // TODO: onehao need debugging, attempt to init
                    isInvalid[row][v] = false; // onehao: temp set false from the original value 0.
                }
            }
        }
        for (int round = 0; round < rowCount; round++) {
            startTime = System.currentTimeMillis();

            int chosenRow = -1;
            int chosenValue = -1;
            for (int i = 0; i < restRows.size(); i++) {
                int row = ((Integer) restRows.get(i)).intValue();
                for (int j = lastChoice; j < lastChoice + domainSize; j++) {
                    int v = values[j];

                    // TODO: onehao need verification, isInvalid[row][v] == 0 - > isInvalid[row][v] == false
                    if (((!this.hasConstraint) || (isInvalid[row][v] == false)) && (canCoverAtMost[row][v] == maxCover)) {
                        chosenRow = row;
                        chosenValue = v;
                        break;
                    }
                }
            }
            if (chosenRow == -1) {
                maxCover = 0;
                for (int i = 0; i < restRows.size(); i++) {
                    int row = ((Integer) restRows.get(i)).intValue();
                    for (int j = lastChoice; j < lastChoice + domainSize; j++) {
                        int v = values[j];

                        // TODO: onehao isInvalid[row][v] == 0 - > isInvalid[row][v] == false
                        if (((!this.hasConstraint) || (isInvalid[row][v] == false))
                                && (canCoverAtMost[row][v] > maxCover)) {
                            chosenRow = row;
                            chosenValue = v;
                            maxCover = canCoverAtMost[row][v];
                        }
                    }
                }
            }
            lastChoice = chosenValue;
            if (maxCover == 0) {
                break;
            }
            startTime = System.currentTimeMillis();
            int[] test = (int[]) ts.getTest(chosenRow).clone();
            test[column] = chosenValue;
            if (!this.constraintManager.isValid(test, column)) {
                // TODO: isInvalid[row][v] == 1 - > isInvalid[row][v] == true
                isInvalid[chosenRow][chosenValue] = true;
            } else {
                ArrayList<int[]> groups = new ArrayList();
                ArrayList<Integer> newlyCovered = getCoveredTupleIdx(test, lists, groups);
                if (newlyCovered.size() / 2 != maxCover) {
                    System.out.println("error! newlyCovered!= maxCover, " + newlyCovered + ", " + maxCover);
                    break;
                }
                ts.setValue(chosenRow, column, chosenValue);
                test = ts.getTest(chosenRow);
                restRows.remove(Integer.valueOf(chosenRow));

                startTime = System.currentTimeMillis();

                int[] samePos = new int[column + 1];
                samePos[column] = 1;
                for (Iterator localIterator = restRows.iterator(); localIterator.hasNext();) {
                    int row = ((Integer) localIterator.next()).intValue();

                    // TODO: onehao , isInvalid[row][v] == 0 - > isInvalid[row][v] == false
                    if ((!this.hasConstraint) || (isInvalid[row][chosenValue] == false)) {
                        int[] test2 = ts.getTest(row);
                        for (int i = 0; i < column; i++) {
                            samePos[i] = (test2[i] == test[i] ? 1 : 0);
                        }
                        canCoverAtMost[row][chosenValue] -= findCommonTuples(groups, samePos);
                    }
                }
                coverTuples(lists, newlyCovered);
            }
        }
    }

    private int findCommonTuples(ArrayList<int[]> groups, int[] samePos) {
        int count = 0;
        for (int i = 0; i < groups.size(); i++) {
            boolean match = true;
            int[] group = (int[]) groups.get(i);
            for (int j : group) {
                if (samePos[j] == 0) {
                    match = false;
                    break;
                }
            }
            if (match) {
                count++;
            }
        }
        return count;
    }

    private int findMaxCoverable(TestSet ts, int base, int row, int column) {
        int[] test = ts.getTest(row);
        boolean foundDontCares = false;
        for (int i = 0; i < column; i++) {
            if (test[i] == -1) {
                foundDontCares = true;
                break;
            }
        }
        if (!foundDontCares) {
            return base;
        }
        RelationManager relationMgr = this.sut.getRelationManager();
        ArrayList<Parameter> pars = new ArrayList<Parameter>();
        for (int i = 0; i < column; i++) {
            if (test[i] != -1) {
                pars.add((Parameter) this.params.get(i));
            }
        }
        pars.add((Parameter) this.params.get(column));
        return relationMgr.getNumberOfAllParamCombo(pars);
    }

    private void expand(TestSet ts, int column, TupleListGroup lists) {
        Parameter param = ts.getParam(column);
        int domainSize = param.getDomainSize();
        int lastChoice = 0;

        int[] appearances = new int[domainSize];
        ArrayList<Integer> maxTupleIdx = null;
        ArrayList<int[]> valuesAndWeights = new ArrayList<int[]>(domainSize);
        ArrayList<ArrayList<Integer>> allCurrTuples = new ArrayList<ArrayList<Integer>>(domainSize);
        for (int row = 0; row < ts.getNumOfTests(); row++) {
            if (lists.peekNextTuple() == null) {
                break;
            }
            int choice = -1;
            int maxWeight = 0;

            int[] copy = (int[]) ts.getTest(row).clone();
            if (ts.getValue(row, column) != -1) {
                choice = ts.getValue(row, column);
                maxTupleIdx = getCoveredTupleIdx(ts.getTest(row), lists);
            } else if (ChocoContext.SORT_IN_EXPAND) {
                valuesAndWeights.clear();
                allCurrTuples.clear();
                int idx = 0;
                int base = lastChoice + 1;
                if (this.sut.getRelationManager().getMinStrength() > 2) {
                    base = 0;
                }
                int maxCovers = 0;
                ArrayList<Integer> maxCoverTuples = null;
                int maxCoversValue = -1;
                for (int v = 0; v < domainSize; v++) {
                    int value = (v + base) % domainSize;

                    copy[column] = value;
                    ArrayList<Integer> currTuples = getCoveredTupleIdx(copy, lists);
                    allCurrTuples.add(currTuples);
                    valuesAndWeights.add(new int[] { value, currTuples.size(), appearances[value], idx });
                    if ((currTuples.size() > maxCovers)
                            || ((currTuples.size() == maxCovers) && (maxCoversValue != -1) && (appearances[value] < appearances[maxCoversValue]))) {
                        maxCovers = currTuples.size();
                        maxCoverTuples = currTuples;
                        maxCoversValue = value;
                    }
                    idx++;
                }
                copy[column] = maxCoversValue;
                if (maxCoversValue != -1) {
                    if ((ts.getNeedConstraintCheck(row).booleanValue())
                            && (this.constraintManager.isValid(copy, column))) {
                        choice = maxCoversValue;
                        maxTupleIdx = maxCoverTuples;
                    } else {
                        // onehao: refactor with generic.
                        Collections.sort(valuesAndWeights, new Comparator<int[]>() {
                            public int compare(int[] x, int[] y) {
                                if (y[1] == x[1]) {
                                    return x[2] - y[2];
                                }
                                return y[1] - x[1];
                            }

                        });
                        for (int i = 0; i < domainSize; i++) {
                            int[] array = (int[]) valuesAndWeights.get(i);
                            if (array[1] == 0) {
                                break;
                            }
                            copy[column] = array[0];
                            if ((ts.getNeedConstraintCheck(row).booleanValue())
                                    && (this.constraintManager.isValid(copy, column))) {
                                choice = array[0];
                                maxTupleIdx = (ArrayList<Integer>) allCurrTuples.get(array[3]);
                                break;
                            }
                        }
                    }
                }
            } else {
                for (int value = 0; value < param.getDomainSize(); value++) {
                    copy[column] = value;
                    if ((ts.getNeedConstraintCheck(row).booleanValue())
                            && (this.constraintManager.isValid(copy, column))) {
                        int currWeight = 0;
                        ArrayList<Integer> currTuples = null;

                        currTuples = getCoveredTupleIdx(copy, lists);
                        currWeight = currTuples.size();
                        if (currWeight > maxWeight) {
                            choice = value;
                            maxWeight = currWeight;
                            maxTupleIdx = currTuples;
                        } else if (currWeight == maxWeight) {
                            if (TestGenProfile.instance().getTieBreaker() == TestGenProfile.TieBreaker.random) {
                                if (this.random.nextBoolean()) {
                                    choice = value;
                                    maxWeight = currWeight;
                                    maxTupleIdx = currTuples;
                                }
                            } else if ((this.sut.getRelationManager().getMinStrength() <= 4) && (choice != -1)
                                    && (appearances[value] < appearances[choice])) {
                                choice = value;
                                maxWeight = currWeight;
                                maxTupleIdx = currTuples;
                            }
                        }
                    }
                }
            }
            if (choice != -1) {
                ts.setValue(row, column, choice);
                lastChoice = choice;
                coverTuples(lists, maxTupleIdx);
                appearances[choice] += 1;
            }
        }
    }

    private ArrayList<Integer> getCoveredTupleIdx(int[] test, TupleListGroup lists) {
        return getCoveredTupleIdx(test, lists, null);
    }

    private ArrayList<Integer> getCoveredTupleIdx(int[] test, TupleListGroup lists, ArrayList<int[]> groups) {
        ArrayList<Integer> rval = new ArrayList<Integer>();
        for (int i = lists.getFirstMissingTree(); i < lists.getNumOfLists(); i++) {
            TupleList list = lists.getList(i);
            if (list.hasMissingTuple()) {
                int idx = list.getIndex(test);
                if ((idx >= 0) && (!list.isCovered(idx))) {
                    if (groups != null) {
                        groups.add(lists.getGroup(i));
                    }
                    rval.add(Integer.valueOf(i));
                    rval.add(Integer.valueOf(idx));
                }
            }
        }
        return rval;
    }

    private ArrayList<Integer> getCoveredTupleIdx(int[] test, int col, TupleListGroup lists) {
        ArrayList<Integer> rval = new ArrayList<Integer>();
        for (Iterator<?> localIterator = lists.getRelatedLists(col).iterator(); localIterator.hasNext();) {
            int i = ((Integer) localIterator.next()).intValue();
            TupleList list = lists.getList(i);
            if (list.hasMissingTuple()) {
                int idx = list.getIndex(test);
                if ((idx >= 0) && (!list.isCovered(idx))) {
                    rval.add(Integer.valueOf(i));
                    rval.add(Integer.valueOf(idx));
                }
            }
        }
        return rval;
    }

    private void coverTuples(TupleListGroup lists, ArrayList<Integer> idx) {
        for (int i = 0; i < idx.size(); i += 2) {
            int treeIdx = ((Integer) idx.get(i)).intValue();
            int tupleIdx = ((Integer) idx.get(i + 1)).intValue();
            lists.getList(treeIdx).setCover(tupleIdx);
            this.nCoveredCombos += 1;
        }
    }

    private boolean hasDontCares(TestSet ts, int row, int column) {
        for (int i = 0; i <= column; i++) {
            if (ts.getValue(row, i) == -1) {
                return true;
            }
        }
        return false;
    }

    private void grow(TestSet ts, int column, TupleListGroup lists) {
        int[] tuple = (int[]) null;

        ArrayList<Integer> rowWithDontCares = new ArrayList<Integer>(ts.getNumOfTests());
        for (int row = 0; row < ts.getNumOfTests(); row++) {
            if (hasDontCares(ts, row, column)) {
                rowWithDontCares.add(Integer.valueOf(row));
            }
        }
        ArrayList<Integer> changedCols = new ArrayList<Integer>();
        while ((tuple = lists.peekNextTuple()) != null) {
            changedCols.clear();
            for (int i = 0; i < tuple.length; i++) {
                if (tuple[i] != -1) {
                    changedCols.add(Integer.valueOf(i));
                }
            }
            if (!this.constraintManager.isValid(tuple, changedCols)) {
                lists.coverNextTuple();
            } else {
                boolean covered = false;
                for (int i = 0; i < rowWithDontCares.size(); i++) {
                    int row = ((Integer) rowWithDontCares.get(i)).intValue();

                    int[] newtest = ts.makeCompatibleTest(row, tuple, changedCols);
                    if ((newtest != null) && (this.constraintManager.isValid(newtest, changedCols))) {
                        ts.cover(row, tuple);
                        covered = true;
                        for (Iterator<Integer> localIterator = changedCols.iterator(); localIterator.hasNext();) {
                            int v = ((Integer) localIterator.next()).intValue();
                            coverTuples(lists, getCoveredTupleIdx(newtest, v, lists));
                        }
                        break;
                    }
                }
                if (!covered) {
                    ts.addNewTest(tuple);
                    this.nCoveredCombos += 1;
                    lists.coverNextTuple();

                    rowWithDontCares.add(Integer.valueOf(ts.getNumOfTests() - 1));
                }
            }
        }
    }

    private void carefy(TestSet ts) {
        this.random = new Random(0L);
        for (int i = 0; i < ts.getNumOfTests(); i++) {
            int[] test = ts.getTest(i);
            if (!this.constraintManager.isValid(test)) {
                Util.dump(test);
            } else {
                int numOfInputParamColumns = ts.getNumOfParams() > test.length ? test.length : ts.getNumOfParams();
                for (int j = 0; j < numOfInputParamColumns; j++) {
                    if (test[j] == -1) {
                        Parameter param = ts.getParam(j);
                        boolean flag = false;
                        for (int k = 0; k < param.getDomainSize(); k++) {
                            int choice = this.random.nextInt(param.getDomainSize());

                            ts.setValue(i, j, choice);
                            if (this.constraintManager.isValid(test)) {
                                flag = true;
                                break;
                            }
                        }
                        if (!flag) {
                            for (int k = 0; k < param.getDomainSize(); k++) {
                                ts.setValue(i, j, k);
                                if (this.constraintManager.isValid(test)) {
                                    flag = true;
                                    break;
                                }
                            }
                            if (!flag) {
                                System.out.println("Failure to carefy. This should never happen.");

                                ts.setValue(i, j, -1);
                            }
                        }
                    }
                }
            }
        }
    }
}
