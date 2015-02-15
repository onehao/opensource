package com.onehao.acts.testgen.service.extension.pre;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.onehao.acts.testgen.common.SUT;
import com.onehao.acts.testgen.service.extension.Utils;

import edu.uta.cse.fireeye.common.Parameter;

public abstract class GenericPreExtensionStrategy implements IPreExtensionStrategy {

    protected GenValueAppendStrategy appendStrategy = GenValueAppendStrategy.Post;

    protected Map<String, Collection<String>> extParamValsMap = null;

    @Override
    public void process(SUT sut, Map<String, Collection<String>> genParamValsMap) {
        if ((null == sut) || (null == genParamValsMap) || genParamValsMap.isEmpty()) {
            return;
        }

        this.resetExtParamValsMap(sut);

        this.doProcess(sut);

        if ((this.extParamValsMap != null) && !this.extParamValsMap.isEmpty()) {
            this.appendValues(sut, genParamValsMap);
        }
    }

    @Override
    public IPreExtensionStrategy setGenValueAppendStrategy(GenValueAppendStrategy strategy) {
        this.appendStrategy = strategy;
        return this;
    }

    @Override
    public GenValueAppendStrategy getGenValueAppendStrategy() {
        return this.appendStrategy;
    }

    protected void resetExtParamValsMap(SUT sut) {
        this.extParamValsMap = Utils.newParameterValuesMap(sut);
    }

    protected void appendValues(SUT sut, Map<String, Collection<String>> genParamValsMap) {
        for (Map.Entry<String, Collection<String>> entry : this.extParamValsMap.entrySet()) {
            String paramName = entry.getKey();
            List<String> genVals = new ArrayList<String>(entry.getValue());
            Parameter param = sut.getParam(paramName);

            switch (this.appendStrategy) {
                case Full:
                    if (param != null) {
                        for (String val : genVals) {
                            Utils.addUniqValToParameter(param, val);
                        }
                    }
                    break;
                case Random:
                    Collections.shuffle(genVals);
                    int orgSize = param.getDomainSize();
                    for (String val : genVals) {
                        Utils.addUniqValToParameter(param, val);
                        if (param.getDomainSize() > orgSize) {
                            break;
                        }
                    }
                    break;
                case Post:
                    if (genParamValsMap.containsKey(paramName)) {
                        Collection<String> valColl = genParamValsMap.get(paramName);
                        for (String val : genVals) {
                            if (!param.getValues().contains(val)) {
                                valColl.add(val);
                            }
                        }
                    }
                    break;
                case PostAutoSize:
                    if (genParamValsMap.containsKey(paramName)) {
                        Collection<String> valColl = genParamValsMap.get(paramName);
                        int autoSize = 0;
                        if (param.getValues().size() >= (genVals.size() * 2)) {
                            autoSize = genVals.size();
                        } else {
                            autoSize = param.getValues().size() / 2;
                            autoSize = autoSize > 0 ? autoSize : 1;
                            Collections.shuffle(genVals);
                        }
                        int count = 0;
                        for (String val : genVals) {
                            if (count >= autoSize) {
                                break;
                            }

                            if (!param.getValues().contains(val)) {
                                valColl.add(val);
                                count++;
                            }
                        }
                    }
                    break;
                default:
                    throw new RuntimeException("Unhandled GenValueAppendStrategy Enum : " + this.appendStrategy);
            }
        }
    }

    protected abstract void doProcess(SUT sut);
}
