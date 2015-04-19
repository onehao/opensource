package com.onehao.acts.testgen.service.extension.range;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;

import com.onehao.acts.testgen.service.extension.Utils;

public class CoordRangeGen extends CoordParser implements IRangeValueGenerator {

    protected NumberRangeGen numberRangeGen = new NumberRangeGen();

    protected String splitter = ",";

    @Override
    public IRangeValueGenerator setGenSizeStrategy(IGenSizeStrategy newStrategy) {
        numberRangeGen.setGenSizeStrategy(newStrategy);
        return this;
    }

    @Override
    public IGenSizeStrategy getGenSizeStrategy() {
        return numberRangeGen.getGenSizeStrategy();
    }

    @Override
    public void genRangeValues(Collection<String> valList, String start, String end) {

        Matcher startMatcher = this.pattern.matcher(start);
        Matcher endMatcher = this.pattern.matcher(end);

        if (startMatcher.find() && endMatcher.find()) {
            this.splitter = startMatcher.group(3);

            List<String> latValues = new ArrayList<String>();
            if (this.numberRangeGen.parse(Arrays.asList(new String[] { startMatcher.group(1), endMatcher.group(1) }))) {
                this.numberRangeGen.genRangeValues(latValues, startMatcher.group(1), endMatcher.group(1));
            }
            List<String> lngValues = new ArrayList<String>();
            if (this.numberRangeGen.parse(Arrays.asList(new String[] { startMatcher.group(4), endMatcher.group(4) }))) {
                this.numberRangeGen.genRangeValues(lngValues, startMatcher.group(4), endMatcher.group(4));
            }

            Collection<String> coords = new HashSet<String>();
            coords.add(start);
            coords.add(end);
            genRandomCoord(coords, latValues, lngValues);

            valList.addAll(coords);
        } else {
            return;
        }
    }

    protected void genRandomCoord(Collection<String> valList, List<String> latValues, List<String> lngValues) {
        for (String lat : latValues) {
            valList.add(lat + this.splitter + lngValues.get(Utils.getRandomInt(lngValues.size())));
        }
    }

}
