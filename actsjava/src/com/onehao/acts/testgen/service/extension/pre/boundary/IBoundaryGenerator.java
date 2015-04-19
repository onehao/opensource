package com.onehao.acts.testgen.service.extension.pre.boundary;

import java.util.Collection;
import java.util.List;

public interface IBoundaryGenerator {

    void generate(List<String> srcValues, Collection<String> genValues);
}
