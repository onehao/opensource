package com.onehao.acts.testgen.service.extension.post;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.onehao.acts.testgen.common.SUT;
import com.onehao.acts.testgen.common.TestSet;

import edu.uta.cse.fireeye.common.Parameter;

public class PreGenValueAppendStrategy implements IPostExtensionStrategy {

    public enum VerticalExtStrategy {
        Full, Smart
    }

    protected Map<String, List<String>> preGenValues = null;

    protected VerticalExtStrategy verticalExtStrategy = VerticalExtStrategy.Smart;

    protected Map<String, Integer> preGenValsFillingSizeMap = null;

    protected float fillingPercent = 0.05F;

    protected int maxFillingSize = 0;

    public PreGenValueAppendStrategy(Map<String, Collection<String>> preGenVals) {

        if (null == preGenVals) {
            return;
        }

        this.preGenValues = new HashMap<String, List<String>>();
        for (Map.Entry<String, Collection<String>> entry : preGenVals.entrySet()) {
            this.preGenValues.put(entry.getKey(), new ArrayList<String>(entry.getValue()));
        }
    }

    public PreGenValueAppendStrategy setVerticalExtStrategy(VerticalExtStrategy verticalExtStrategy) {
        this.verticalExtStrategy = verticalExtStrategy;
        return this;
    }

    @Override
    public void extend(TestSet ts, SUT sut) {
        if ((null == sut) || (null == ts)) {
            return;
        }

        if ((null == this.preGenValues) || this.preGenValues.isEmpty()) {
            return;
        }

        this.calcMaxFillingSize(ts);
        int verExtSize = this.anylazeVertExtSize(ts);
        if (verExtSize > 0) {
            this.doVerticalExtension(ts, verExtSize);
        }

        this.shufflePreGenValues();
        this.fillPreGenValues(sut, ts, false);
    }

    protected int anylazeVertExtSize(TestSet ts) {
        int retVal = 0;
        int[] dontCareCount = new int[ts.getNumOfParams()];
        for (int[] row : ts.getMatrix()) {
            for (int i = 0; i < row.length; i++) {
                if (TestSet.DONT_CARE == row[i]) {
                    dontCareCount[i]++;
                }
            }
        }

        int index = 0;
        for (Parameter param : ts.getParams()) {
            if (this.preGenValues.containsKey(param.getName())) {
                int diffCount = calcDiffSize(param, dontCareCount[index]);
                if (diffCount > retVal) {
                    retVal = diffCount;
                }
            }
            index++;
        }

        return retVal;
    }

    protected void doVerticalExtension(TestSet ts, int extSize) {
        if ((extSize <= 0) || (null == ts)) {
            return;
        }

        int[] test = new int[ts.getNumOfParams()];
        Arrays.fill(test, -1);

        for (int i = 0; i < extSize; i++) {
            ts.addTest(test);
        }
    }

    protected void fillPreGenValues(SUT sut, TestSet ts, boolean reverseFilling) {
        int[] pickValueCount = new int[ts.getNumOfParams()];
        List<int[]> sortList = null;
        if (reverseFilling) {
            sortList = new ArrayList<int[]>(ts.getMatrix());
            Collections.reverse(sortList);
        } else {
            sortList = ts.getMatrix();
        }
        for (int[] row : sortList) {
            for (int i = 0; i < row.length; i++) {
                if ((TestSet.DONT_CARE == row[i]) && (pickValueCount[i] >= 0)) {
                    Parameter param = ts.getParam(i);
                    String val = this.pickPreGenValues(param.getName(), pickValueCount[i]);
                    if (val != null) {
                        row[i] = param.getValues().size();
                        param.addValue(val);
                        pickValueCount[i]++;
                    } else {
                        pickValueCount[i] = -1;
                    }
                }
            }
        }
    }

    protected void shufflePreGenValues() {
        if ((null == this.preGenValues) || this.preGenValues.isEmpty()) {
            return;
        }

        for (Map.Entry<String, List<String>> entry : this.preGenValues.entrySet()) {
            Collections.shuffle(entry.getValue());
        }
    }

    protected String pickPreGenValues(String paramName, int index) {
        if ((null == this.preGenValues) || this.preGenValues.isEmpty()) {
            return null;
        }

        if (VerticalExtStrategy.Smart == this.verticalExtStrategy) {
            if (index >= this.maxFillingSize) {
                return null;
            }
        }

        if (this.preGenValues.containsKey(paramName)) {
            List<String> genValues = this.preGenValues.get(paramName);
            if ((index >= 0) && (index < genValues.size())) {
                return genValues.get(index);
            }
        }

        return null;
    }

    protected int calcDiffSize(Parameter param, int dontCareCount) {
        int requiredSize = this.preGenValues.get(param.getName()).size();

        if (VerticalExtStrategy.Smart == this.verticalExtStrategy) {
            if (requiredSize > this.maxFillingSize) {
                requiredSize = this.maxFillingSize;
            }
        }

        return (requiredSize - dontCareCount);
    }

    protected void calcMaxFillingSize(TestSet ts) {
        if (VerticalExtStrategy.Smart == this.verticalExtStrategy) {
            this.maxFillingSize = (int) (ts.getNumOfTests() * this.fillingPercent);
            if (this.maxFillingSize < 1) {
                this.maxFillingSize = 1;
            }
        }
    }

}
