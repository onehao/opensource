package com.onehao.acts.testgen.service.extension.pre;

import java.util.Collection;
import java.util.Map;

import com.onehao.acts.testgen.common.SUT;

public interface IPreExtensionStrategy {

    enum GenValueAppendStrategy {
        Full, Random, Post, PostAutoSize
    }

    void process(SUT sut, Map<String, Collection<String>> genParamValsMap);

    IPreExtensionStrategy setGenValueAppendStrategy(GenValueAppendStrategy strategy);

    GenValueAppendStrategy getGenValueAppendStrategy();

}
