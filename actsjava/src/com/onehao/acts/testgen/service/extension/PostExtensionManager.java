package com.onehao.acts.testgen.service.extension;

import com.onehao.acts.testgen.common.SUT;
import com.onehao.acts.testgen.common.TestSet;
import com.onehao.acts.testgen.service.extension.post.IPostExtensionStrategy;

public class PostExtensionManager extends GenericExtensionManager<IPostExtensionStrategy> {

    public PostExtensionManager() {
    }

    public void extend(TestSet ts, SUT sut) {
        if ((null == sut) || (null == ts) || this.strategyList.isEmpty()) {
            return;
        }

        for (IPostExtensionStrategy strategy : this.strategyList) {
            if (strategy instanceof IFetchOriginalData) {
                Utils.as(IFetchOriginalData.class, strategy).setOriginalParamDomainSize(originalParamDomainSize);
            }

            strategy.extend(ts, sut);
        }
    }

}
