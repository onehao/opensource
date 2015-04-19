package com.onehao.acts.testgen.service.extension.range;

import java.util.Collection;

public abstract class GenericRangeGen<T extends ITypeParser> implements ITypeParser, IRangeValueGenerator {

    protected T typeParser = null;

    protected IGenSizeStrategy genSizeStrategy = new FixedGenSizeStrategy();

    @Override
    public boolean parse(Collection<String> valList) {
        if (null == typeParser) {
            this.initParser();
        }

        return typeParser.parse(valList);
    }

    @Override
    public IRangeValueGenerator setGenSizeStrategy(IGenSizeStrategy newStrategy) {
        this.genSizeStrategy = newStrategy;
        return this;
    }

    @Override
    public IGenSizeStrategy getGenSizeStrategy() {
        return this.genSizeStrategy;
    }

    protected abstract void initParser();
}
