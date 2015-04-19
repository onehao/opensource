package com.onehao.acts.testgen.service.extension.range;

import java.util.Collection;

public interface IRangeValueGenerator {

    IRangeValueGenerator setGenSizeStrategy(IGenSizeStrategy newStrategy);

    IGenSizeStrategy getGenSizeStrategy();

    void genRangeValues(Collection<String> valList, String start, String end);

}
