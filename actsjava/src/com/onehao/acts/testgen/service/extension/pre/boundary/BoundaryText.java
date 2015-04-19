package com.onehao.acts.testgen.service.extension.pre.boundary;

import java.util.Collection;
import java.util.List;

import com.onehao.acts.testgen.service.extension.Utils;

import edu.uta.cse.fireeye.common.Parameter;

public class BoundaryText implements IBoundaryGenerator, IBoundaryParser {

    @Override
    public boolean parse(Parameter param) {
        return true;
    }

    @Override
    public void generate(List<String> srcValues, Collection<String> genValues) {
        genValues.add("");
        genValues.add(" ");
        genValues.add(Utils.repeatStr(Utils.genRandomString(), 100 + Utils.getRandomInt(10)));
        genValues.add("中文");
        genValues.add("繁體");
        genValues.add("にほんご");
    }

}
