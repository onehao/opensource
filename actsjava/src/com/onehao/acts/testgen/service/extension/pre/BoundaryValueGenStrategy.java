package com.onehao.acts.testgen.service.extension.pre;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.onehao.acts.testgen.common.SUT;
import com.onehao.acts.testgen.service.extension.IFetchOriginalData;
import com.onehao.acts.testgen.service.extension.Pair;
import com.onehao.acts.testgen.service.extension.common.ParameterWrapper;
import com.onehao.acts.testgen.service.extension.pre.boundary.BoundaryCoord;
import com.onehao.acts.testgen.service.extension.pre.boundary.BoundaryIPv4;
import com.onehao.acts.testgen.service.extension.pre.boundary.BoundaryNumber;
import com.onehao.acts.testgen.service.extension.pre.boundary.BoundaryText;
import com.onehao.acts.testgen.service.extension.pre.boundary.IBoundaryGenerator;
import com.onehao.acts.testgen.service.extension.pre.boundary.IBoundaryParser;

import edu.uta.cse.fireeye.common.Parameter;

public class BoundaryValueGenStrategy extends GenericPreExtensionStrategy implements IFetchOriginalData {

    protected List<Pair<IBoundaryParser, IBoundaryGenerator>> workerList;

    protected Map<String, Integer> originalParamDomainSize = null;

    public BoundaryValueGenStrategy() {
        workerList = new ArrayList<Pair<IBoundaryParser, IBoundaryGenerator>>();
    }

    @Override
    protected void doProcess(SUT sut) {
        if (null == sut) {
            return;
        }

        for (Parameter param : sut.getParameters()) {
            ParameterWrapper paramWrapper = new ParameterWrapper(param);
            if (paramWrapper.getBoundaryGen()) {
                this.analyze(param);
            }
        }
    }

    protected void analyze(Parameter param) {
        if ((null == param) || (null == param.getValues()) || param.getValues().isEmpty()) {
            return;
        }

        if (!this.workerList.isEmpty()) {
            List<String> paramValues = null;
            if ((this.originalParamDomainSize != null) && this.originalParamDomainSize.containsKey(param.getName())) {
                paramValues = param.getValues().subList(0, originalParamDomainSize.get(param.getName()));
            } else {
                paramValues = new ArrayList<String>(param.getValues());
            }

            Collection<String> genValues = this.extParamValsMap.get(param.getName());
            for (Pair<IBoundaryParser, IBoundaryGenerator> worker : workerList) {
                if (worker.getFirst().parse(param)) {
                    worker.getSecond().generate(paramValues, genValues);
                    break;
                }
            }
        }
    }

    public void setParserAndGenerator(IBoundaryParser parser, IBoundaryGenerator generator) {
        if ((null == parser) || (null == generator)) {
            return;
        }

        workerList.add(new Pair<IBoundaryParser, IBoundaryGenerator>(parser, generator));
    }

    public void setParserAndGenerator(Object obj) {
        if (null == obj) {
            return;
        }

        if ((obj instanceof IBoundaryParser) && (obj instanceof IBoundaryGenerator)) {
            this.setParserAndGenerator((IBoundaryParser) obj, (IBoundaryGenerator) obj);
        }
    }

    public static BoundaryValueGenStrategy buildStrategy() {
        return buildStrategy(GenValueAppendStrategy.Post);
    }

    public static BoundaryValueGenStrategy buildStrategy(GenValueAppendStrategy valueAppendStrategy) {
        BoundaryValueGenStrategy strategy = new BoundaryValueGenStrategy();
        strategy.setGenValueAppendStrategy(valueAppendStrategy);

        strategy.setParserAndGenerator(new BoundaryNumber());
        strategy.setParserAndGenerator(new BoundaryIPv4());
        strategy.setParserAndGenerator(new BoundaryCoord());
        strategy.setParserAndGenerator(new BoundaryText());

        return strategy;
    }

    @Override
    public void setOriginalParamDomainSize(Map<String, Integer> originalParamDomainSize) {
        this.originalParamDomainSize = originalParamDomainSize;
    }
}
