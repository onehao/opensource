package com.onehao.acts.testgen.service.extension;

import java.util.Collection;
import java.util.Map;

import com.onehao.acts.testgen.common.SUT;
import com.onehao.acts.testgen.service.extension.pre.IPreExtensionStrategy;

public class PreExtensionManager extends GenericExtensionManager<IPreExtensionStrategy> {

    private Map<String, Collection<String>> totalValsMap;

    public PreExtensionManager() {
    }

    public void process(SUT sut) {
        if ((null == sut) || sut.getParameters().isEmpty() || this.strategyList.isEmpty()) {
            return;
        }

        this.resetValsMap(sut);

        for (IPreExtensionStrategy strategy : this.strategyList) {
            if (strategy instanceof IFetchOriginalData) {
                Utils.as(IFetchOriginalData.class, strategy).setOriginalParamDomainSize(originalParamDomainSize);
            }

            strategy.process(sut, this.totalValsMap);
        }
    }

    public Map<String, Collection<String>> getGenValuesMap() {
        return this.totalValsMap;
    }

    protected void resetValsMap(SUT sut) {
        this.totalValsMap = Utils.newParameterValuesMap(sut);
    }

}
