package com.onehao.acts.testgen.service.engine;

import java.util.ArrayList;

import com.onehao.acts.testgen.common.TestGenProfile;
import com.onehao.acts.testgen.common.TestSet;

import edu.uta.cse.fireeye.common.Parameter;
import edu.uta.cse.fireeye.service.engine.BinaryTupleTree;
import edu.uta.cse.fireeye.service.engine.BinaryTupleTreeGroup;
import edu.uta.cse.fireeye.service.engine.PVPair;
import edu.uta.cse.fireeye.service.engine.Tuple;
import edu.uta.cse.fireeye.service.engine.TupleTreeGroup;

/**
 * 
 * @author wanhao01 refactor from FireEye2.8
 */

public class BinaryIpoEngine {
    private TestSet ts;
    private int doi;
    private ArrayList<Parameter> firstGroup;
    private ArrayList<Parameter> secondGroup;
    private int startRow;

    public BinaryIpoEngine(TestSet ts, ArrayList<Parameter> firstGroup, ArrayList<Parameter> secondGroup) {
        this.ts = ts;
        this.firstGroup = firstGroup;
        this.secondGroup = secondGroup;

        this.doi = TestGenProfile.instance().getDOI();
        this.startRow = ts.getNumOfTests();
    }

    public void extend() {
        for (int paramIndex = 1; paramIndex < this.secondGroup.size(); paramIndex++) {
            BinaryTupleTreeGroup trees =
                    new BinaryTupleTreeGroup(this.firstGroup, this.secondGroup, paramIndex, this.doi);
            trees.build();

            int column = 2 * paramIndex + 1;
            if (this.startRow < this.ts.getNumOfTests()) {
                expand(column, trees);
            }
            grow(trees);
        }
    }

    private void expand(int column, BinaryTupleTreeGroup trees) {
        Parameter param = this.ts.getParam(column);
        for (int row = this.startRow; row < this.ts.getNumOfTests(); row++) {
            boolean progressOn = TestGenProfile.instance().isProgressOn();
            short hunit = TestGenProfile.instance().getHUnit();
            if ((progressOn) && (row % hunit == 0)) {
                System.out.print(".");
            }
            boolean hasDontCares = hasDontCares(row, column);

            int choice = -1;
            int maxWeight = 0;
            ArrayList<Tuple> maxTuples = null;
            if (this.ts.getValue(row, column) != -1) {
                choice = this.ts.getValue(row, column);
            } else {
                if (row < param.getDomainSize()) {
                    choice = row;
                } else {
                    if (trees.getNextMissingTuple() == null) {
                        break;
                    }
                    for (int value = 0; value < param.getDomainSize(); value++) {
                        ArrayList<Tuple> currTuples = null;

                        currTuples = getCoveredTuples(row, column, value, trees, hasDontCares);
                        int currWeight = currTuples.size();
                        if (currWeight > maxWeight) {
                            choice = value;
                            maxWeight = currWeight;
                            maxTuples = currTuples;
                        }
                    }
                }
                if (choice != -1) {
                    this.ts.setValue(row, column, choice);
                    if (maxTuples == null) {
                        maxTuples = getCoveredTuples(row, column, choice, trees, hasDontCares);
                    }
                    setCovered(row, trees, hasDontCares);
                }
            }
        }
    }

    private ArrayList<Tuple> getCoveredTuples(int row, int column, int value, BinaryTupleTreeGroup trees,
            boolean hasDontCares) {
        ArrayList<Tuple> rval = new ArrayList<Tuple>();

        int[] currTest = this.ts.clone(row);
        for (int i = 0; i < trees.getNumOfTrees(); i++) {
            BinaryTupleTree tree = trees.getTree(i);
            ArrayList<Parameter> combo = tree.getParams();

            Tuple tuple = new Tuple(this.doi);
            for (int k = 0; k < this.doi - 1; k++) {
                Parameter param = (Parameter) combo.get(k);
                tuple.addPair(new PVPair(param, currTest[param.getActiveID()]));
            }
            tuple.addPair(new PVPair((Parameter) combo.get(this.doi - 1), value));
            if (tree.lookup(tuple) != null) {
                rval.add(tuple);
                if (hasDontCares) {
                    int numOfPairs = tuple.getNumOfPairs();
                    for (int j = 0; j < numOfPairs - 1; j++) {
                        int columnIndex = tuple.getPair(j).param.getActiveID();
                        if (currTest[columnIndex] == -1) {
                            currTest[columnIndex] = tuple.getPair(j).value;
                        }
                    }
                }
            }
        }
        return rval;
    }

    private void setCovered(int row, BinaryTupleTreeGroup trees, boolean hasDontCares) {
        int[] currTest = this.ts.getTest(row);
        for (int i = 0; i < trees.getNumOfTrees(); i++) {
            BinaryTupleTree tree = trees.getTree(i);
            ArrayList<Parameter> combo = tree.getParams();

            Tuple tuple = new Tuple(this.doi);
            for (Parameter param : combo) {
                tuple.addPair(new PVPair(param, currTest[param.getActiveID()]));
            }
            if ((tree.setCovered(tuple)) && (hasDontCares)) {
                int numOfPairs = tuple.getNumOfPairs();
                for (int j = 0; j < numOfPairs - 1; j++) {
                    int columnIndex = tuple.getPair(j).param.getActiveID();
                    if (currTest[columnIndex] == -1) {
                        currTest[columnIndex] = tuple.getPair(j).value;
                    }
                }
            }
        }
    }

    private boolean hasDontCares(int row, int column) {
        boolean rval = false;
        for (int i = 0; i < this.firstGroup.size(); i++) {
            if ((this.ts.getValue(row, 2 * i) == -1)
                    || ((2 * i + 1 < column) && (this.ts.getValue(row, 2 * i + 1) == -1))) {
                rval = true;
                break;
            }
        }
        return rval;
    }

    private void grow(TupleTreeGroup trees) {
        Tuple tuple = null;

        int progress = 0;
        boolean progressOn = TestGenProfile.instance().isProgressOn();
        int vunit = TestGenProfile.instance().getVUnit();
        while ((tuple = trees.getNextMissingTuple()) != null) {
            boolean covered = false;
            for (int row = this.startRow; row < this.ts.getNumOfTests(); row++) {
                if (this.ts.isCompatible(row, tuple)) {
                    this.ts.cover(row, tuple);
                    covered = true;
                    break;
                }
            }
            if (!covered) {
                this.ts.addNewTest(tuple);
            }
            trees.coverNextMissingTuple();
            if (progressOn) {
                progress++;
                if (progress % vunit == 0) {
                    System.out.print(".");
                }
            }
        }
    }
}
