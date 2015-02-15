package com.onehao.acts.testgen.service.extension.pre.boundary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.onehao.acts.testgen.service.extension.Utils;
import com.onehao.acts.testgen.service.extension.range.IPv4Parser;

import edu.uta.cse.fireeye.common.Parameter;

public class BoundaryIPv4 extends IPv4Parser implements IBoundaryGenerator, IBoundaryParser {

    public BoundaryIPv4() {
    }

    @Override
    public boolean parse(Parameter param) {
        if (null == param) {
            return false;
        }

        return super.parse(param.getValues());
    }

    @Override
    public void generate(List<String> srcValues, Collection<String> genValues) {

        List<String> boundaryList = this.genBoundaryValues();
        genValues.addAll(boundaryList);

        if ((this.maxCombineCount > 1) && (this.splitter != null)) {
            genValues.add(this.genCombineValue(boundaryList));
        }

        genValues.add("");
        genValues.add("....");
    }

    public List<String> genBoundaryValues() {
        List<String> boundaryList = new ArrayList<String>();

        boundaryList.add("0.0.0.0");
        boundaryList.add("255.255.255.255");

        boundaryList.add("10." + this.getRandomBit() + "." + this.getRandomBit() + "." + this.getRandomBit());
        boundaryList.add("127." + this.getRandomBit() + "." + this.getRandomBit() + "." + this.getRandomBit());
        boundaryList.add("192." + this.getRandomBit() + "." + this.getRandomBit() + "." + this.getRandomBit());
        boundaryList.add("239." + this.getRandomBit() + "." + this.getRandomBit() + "." + this.getRandomBit());

        boundaryList.add("999.999.999.999");
        boundaryList.add("-1.-1.-1.-1");

        return boundaryList;
    }

    public String genCombineValue(List<String> boundaryList) {
        StringBuilder sb = new StringBuilder();
        sb.append(boundaryList.get(Utils.getRandomInt(boundaryList.size())));

        for (int i = 0; i < (this.maxCombineCount - 1); i++) {
            sb.append(this.splitter);
            sb.append(boundaryList.get(Utils.getRandomInt(boundaryList.size())));
        }

        return sb.toString();
    }

    public String getRandomBit() {
        return String.valueOf(Utils.getRandomInt(256));
    }

}
