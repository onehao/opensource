package com.onehao.acts.testgen.service.extension.post;

import java.util.Map;

import com.onehao.acts.testgen.common.SUT;
import com.onehao.acts.testgen.common.TestSet;
import com.onehao.acts.testgen.service.extension.IFetchOriginalData;
import com.onehao.acts.testgen.service.extension.Utils;

import edu.uta.cse.fireeye.common.Parameter;

public class RandFillingStrategy implements IPostExtensionStrategy, IFetchOriginalData {

    protected Map<String, Integer> originalParamDomainSize;

    protected RandomRange randRange = RandomRange.OriginalValues;

    public enum RandomRange {
        OriginalValues, AllValues
    }

    public RandFillingStrategy() {
        this.init(RandomRange.OriginalValues);
    }

    public RandFillingStrategy(RandomRange randomRange) {
        this.init(randomRange);
    }

    protected void init(RandomRange randomRange) {
        this.randRange = randomRange;
    }

    @Override
    public void extend(TestSet ts, SUT sut) {
        if ((null == ts) || (null == sut)) {
            return;
        }

        this.fillRandomValue(ts, sut);
    }

    @Override
    public void setOriginalParamDomainSize(Map<String, Integer> originalParamDomainSize) {
        this.originalParamDomainSize = originalParamDomainSize;
    }

    protected void fillRandomValue(TestSet ts, SUT sut) {
        int[] paramDomainSize = this.getUsingParamDomainSize(ts);

        for (int[] row : ts.getMatrix()) {
            for (int i = 0; i < row.length; i++) {
                if (TestSet.DONT_CARE == row[i]) {
                    row[i] = this.pickRandomValue(paramDomainSize[i]);
                }
            }
        }
    }

    protected int pickRandomValue(int domainSize) {
        return Utils.getRandomInt(domainSize);
    }

    protected int[] getUsingParamDomainSize(TestSet ts) {
        int[] paramDomainSize = new int[ts.getNumOfParams()];
        int index = 0;
        for (Parameter param : ts.getParams()) {
            if ((RandomRange.OriginalValues == this.randRange) && (this.originalParamDomainSize != null)
                    && this.originalParamDomainSize.containsKey(param.getName())) {
                paramDomainSize[index] = this.originalParamDomainSize.get(param.getName());
            } else {
                paramDomainSize[index] = param.getDomainSize();
            }
            index++;
        }

        return paramDomainSize;
    }

}
