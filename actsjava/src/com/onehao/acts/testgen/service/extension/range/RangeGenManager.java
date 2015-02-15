package com.onehao.acts.testgen.service.extension.range;

import static com.onehao.acts.testgen.service.extension.Utils.as;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.onehao.acts.testgen.service.extension.Pair;

public class RangeGenManager implements IRangeValueGenerator {

    protected List<Pair<ITypeParser, IRangeValueGenerator>> rangeGenStrategyList;

    protected IGenSizeStrategy genSizeStrategy = new FixedGenSizeStrategy();

    public RangeGenManager() {
        rangeGenStrategyList = new ArrayList<Pair<ITypeParser, IRangeValueGenerator>>();
    }

    public static RangeGenManager buildDefRangeGenManager() {
        RangeGenManager inst = new RangeGenManager();
        inst.registerRangeGenStrategy(new NumberRangeGen());
        inst.registerRangeGenStrategy(new IPv4Parser(true), new IPv4RangeGen());
        inst.registerRangeGenStrategy(new CoordRangeGen());

        return inst;
    }

    @Override
    public IRangeValueGenerator setGenSizeStrategy(IGenSizeStrategy newStrategy) {
        if (newStrategy != null) {
            this.genSizeStrategy = newStrategy;
            this.updateGenSizeStrategy();
        }

        return this;
    }

    @Override
    public IGenSizeStrategy getGenSizeStrategy() {
        return this.genSizeStrategy;
    }

    @Override
    public void genRangeValues(Collection<String> valList, String start, String end) {
        if ((null == valList) || (null == start) || (null == end)) {
            return;
        }

        if ((null == this.rangeGenStrategyList) || this.rangeGenStrategyList.isEmpty()) {
            return;
        }

        List<String> srcRangeVals = Arrays.asList(start, end);
        for (Pair<ITypeParser, IRangeValueGenerator> entry : this.rangeGenStrategyList) {
            if (entry.getFirst().parse(srcRangeVals)) {
                entry.getSecond().genRangeValues(valList, start, end);
                break;
            }
        }
    }

    public boolean registerRangeGenStrategy(Object obj) {
        if (null == obj) {
            return false;
        }

        if ((obj instanceof ITypeParser) && (obj instanceof IRangeValueGenerator)) {
            return this.registerRangeGenStrategy(as(ITypeParser.class, obj), as(IRangeValueGenerator.class, obj));
        }

        return false;
    }

    public boolean registerRangeGenStrategy(ITypeParser parser, IRangeValueGenerator generator) {
        if ((this.rangeGenStrategyList != null) && (parser != null) && (generator != null)) {
            this.rangeGenStrategyList.add(new Pair<ITypeParser, IRangeValueGenerator>(parser, generator));
            generator.setGenSizeStrategy(this.genSizeStrategy);
            return true;
        }

        return false;
    }

    public void clearRangeGenStrategy() {
        if (this.rangeGenStrategyList != null) {
            this.rangeGenStrategyList.clear();
        }
    }

    protected void updateGenSizeStrategy() {
        if ((null == this.rangeGenStrategyList) || this.rangeGenStrategyList.isEmpty()) {
            return;
        }

        for (Pair<ITypeParser, IRangeValueGenerator> entry : this.rangeGenStrategyList) {
            entry.getSecond().setGenSizeStrategy(this.genSizeStrategy);
        }
    }

}
