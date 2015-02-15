package com.onehao.acts.testgen.service.engine;

import java.util.ArrayList;

import com.onehao.acts.testgen.service.constraint.ConstraintManagerInterface;

import edu.uta.cse.fireeye.common.Parameter;
import edu.uta.cse.fireeye.service.engine.RelationManager;
import edu.uta.cse.fireeye.service.engine.TupleList;

/**
 * 
 * @author wanhao01 refactor from FireEye2.8
 */

public class TupleListGroup {
    protected ConstraintManagerInterface constraintMgr;
    int firstMissingTree;
    int currentList;
    protected int doi;
    protected ArrayList<Parameter> params;
    protected RelationManager relationMgr;
    protected ArrayList<TupleList> lists;
    protected ArrayList<ArrayList<Integer>> relatedLists;
    protected ArrayList<int[]> groups;
    int[] domainSizes;

    public TupleListGroup(ArrayList<Parameter> params, RelationManager relationMgr, int column) {
        this.params = new ArrayList<Parameter>();
        this.domainSizes = new int[column + 1];
        this.relatedLists = new ArrayList<ArrayList<Integer>>(column + 1);
        this.groups = new ArrayList<int[]>();
        for (int i = 0; i <= column; i++) {
            this.params.add((Parameter) params.get(i));
            this.domainSizes[i] = ((Parameter) params.get(i)).getDomainSize();
            this.relatedLists.add(new ArrayList<Integer>());
        }
        this.relationMgr = relationMgr;
        this.doi = relationMgr.getMinStrength();

        this.lists = new ArrayList<TupleList>();
        this.firstMissingTree = 0;
        this.currentList = -1;
    }

    public void build() {
        this.groups = this.relationMgr.getAllParamComboIdx(this.params);
        for (int j = 0; j < this.groups.size(); j++) {
            this.lists.add(new TupleList(this.domainSizes, (int[]) this.groups.get(j)));
            for (int i : (int[]) this.groups.get(j)) {
                ((ArrayList<Integer>) this.relatedLists.get(i)).add(Integer.valueOf(j));
            }
        }
    }

    public void setConstraintManager(ConstraintManagerInterface constraintMgr) {
        this.constraintMgr = constraintMgr;
    }

    public int getMissingCountForValue(int v) {
        int total = 0;
        for (TupleList g : this.lists) {
            total += g.getMissingCount(v);
        }
        return total;
    }

    public int getMissingCount() {
        int total = 0;
        for (TupleList g : this.lists) {
            total += g.getMissingCount();
        }
        return total;
    }

    public int getNumOfLists() {
        return this.lists.size();
    }

    public TupleList getList(int index) {
        return (TupleList) this.lists.get(index);
    }

    public int[] getGroup(int index) {
        return (int[]) this.groups.get(index);
    }

    public ArrayList<Integer> getRelatedLists(int index) {
        return (ArrayList<Integer>) this.relatedLists.get(index);
    }

    public int getFirstMissingTree() {
        return this.firstMissingTree;
    }

    public String toString() {
        StringBuffer rval = new StringBuffer();
        for (int i = 0; i < this.lists.size(); i++) {
            rval.append(this.lists.get(i)).append("\n");
        }
        return rval.toString();
    }

    public int[] peekNextTuple() {
        int[] rval = (int[]) null;
        while (this.firstMissingTree < this.lists.size()) {
            if (this.lists.get(this.firstMissingTree) != null) {
                rval = ((TupleList) this.lists.get(this.firstMissingTree)).peekNextTuple();
            }
            if (rval != null) {
                break;
            }
            this.firstMissingTree += 1;
        }
        return rval;
    }

    public void coverNextTuple() {
        while (this.lists.get(this.firstMissingTree) == null) {
            this.firstMissingTree += 1;
        }
        ((TupleList) this.lists.get(this.firstMissingTree)).coverNextTuple();
        if (((TupleList) this.lists.get(this.firstMissingTree)).peekNextTuple() == null) {
            this.firstMissingTree += 1;
        }
    }

    public int[] peekNextTupleByPolling() {
        int[] rval = (int[]) null;
        int count = 0;
        do {
            this.currentList += 1;
            count++;
            if (count > this.lists.size()) {
                return null;
            }
            if (this.currentList >= this.lists.size()) {
                this.currentList = 0;
            }
            if (this.lists.get(this.currentList) != null) {
                rval = ((TupleList) this.lists.get(this.currentList)).peekNextTuple();
            }
        } while (rval == null);
        return rval;
    }

    public void coverCurrrentPollingTuple() {
        ((TupleList) this.lists.get(this.currentList)).coverNextTuple();
    }

    public void removeInvalidTuples() {
        if (this.constraintMgr != null) {
            for (TupleList list : this.lists) {
                int[] tuple = (int[]) null;
                while ((tuple = list.peekNextTuple()) != null) {
                    if (!this.constraintMgr.isValid(tuple)) {
                        list.coverNextTuple();
                    } else {
                        list.setNextTupleAsValid();
                    }
                }
                list.resetValidTuples();
            }
        }
    }
}
