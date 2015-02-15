package com.onehao.acts.testgen.service.extension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class GenericExtensionManager<StrategyT> implements IFetchOriginalData {

    protected List<StrategyT> strategyList;

    protected Map<String, Integer> originalParamDomainSize = null;

    public GenericExtensionManager() {
        strategyList = new ArrayList<StrategyT>();
    }

    public GenericExtensionManager<StrategyT> register(StrategyT strategy) {
        if ((null != strategy) && !this.strategyList.contains(strategy)) {
            this.strategyList.add(strategy);
        }

        return this;
    }

    public boolean unregister(StrategyT strategy) {
        if (this.strategyList.contains(strategy)) {
            this.strategyList.remove(strategy);
            return true;
        } else {
            return false;
        }
    }

    public void clearAllStrategy() {
        this.strategyList.clear();
    }

    @Override
    public void setOriginalParamDomainSize(Map<String, Integer> originalParamDomainSize) {
        this.originalParamDomainSize = originalParamDomainSize;
    }

}
