package com.onehao.acts.testgen.service.extension.pre.boundary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;

import com.onehao.acts.testgen.service.extension.Pair;
import com.onehao.acts.testgen.service.extension.Utils;
import com.onehao.acts.testgen.service.extension.range.CoordParser;

import edu.uta.cse.fireeye.common.Parameter;

public class BoundaryCoord extends CoordParser implements IBoundaryParser, IBoundaryGenerator {

    protected BoundaryNumber boundaryNumber = null;

    public BoundaryCoord() {
        this.boundaryNumber = new BoundaryNumber();
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
        Pair<List<String>, List<String>> coordVals = this.splitCoordList(srcValues);
        Collection<String> latGenValues = new HashSet<String>();
        Collection<String> lngGenValues = new HashSet<String>();

        this.addSpecialCoordValue(latGenValues);
        this.addSpecialCoordValue(lngGenValues);

        if (this.boundaryNumber.parse(coordVals.getFirst())) {
            this.boundaryNumber.generate(coordVals.getFirst(), latGenValues);
        }

        if (this.boundaryNumber.parse(coordVals.getSecond())) {
            this.boundaryNumber.generate(coordVals.getSecond(), lngGenValues);
        }

        Collection<String> genCoordValues = this.genCoords(latGenValues, lngGenValues);

        genValues.addAll(genCoordValues);
        
        genValues.add("");
        genValues.add(",");
    }

    protected Pair<List<String>, List<String>> splitCoordList(List<String> srcValues) {
        List<String> latValues = new ArrayList<String>();
        List<String> lngValues = new ArrayList<String>();
        for (String coord : srcValues) {
            Pair<String, String> valPair = this.splitCoord(coord);
            if (valPair != null) {
                latValues.add(valPair.getFirst());
                lngValues.add(valPair.getSecond());
            }
        }

        return new Pair<List<String>, List<String>>(latValues, lngValues);
    }

    protected Pair<String, String> splitCoord(String coord) {
        Matcher matcher = this.pattern.matcher(coord);
        if (matcher.find()) {
            Pair<String, String> ret = new Pair<String, String>(matcher.group(1), matcher.group(4));
            return ret;
        }

        return null;
    }

    protected void addSpecialCoordValue(Collection<String> genValues) {
        genValues.add("179." + Utils.repeatStr("9", Utils.getRandomInt(9) + 1));
        genValues.add("-179." + Utils.repeatStr("9", Utils.getRandomInt(9) + 1));
        genValues.add("89." + Utils.repeatStr("9", Utils.getRandomInt(9) + 1));
        genValues.add("-89." + Utils.repeatStr("9", Utils.getRandomInt(9) + 1));
        genValues.add("180");
        genValues.add("-180");
        genValues.add("90");
        genValues.add("-90");
    }

    protected Collection<String> genCoords(Collection<String> latGenValues, Collection<String> lngGenValues) {
        Collection<String> genCoordValues = new HashSet<String>();
        List<String> latValList = new ArrayList<String>(latGenValues);
        List<String> lngValList = new ArrayList<String>(lngGenValues);

        int maxCount = latValList.size() >= lngValList.size() ? latValList.size() : lngValList.size();

        Collections.shuffle(latValList);
        Collections.shuffle(lngValList);

        for (int i = 0; i < maxCount; i++) {
            String latValue = i < latValList.size() ? latValList.get(i) : latValList.get((i % latValList.size()));
            String lngValue = i < lngValList.size() ? lngValList.get(i) : lngValList.get((i % lngValList.size()));
            genCoordValues.add(latValue + COORD_SPLITTERS[Utils.getRandomInt(COORD_SPLITTERS.length)] + lngValue);
        }

        return genCoordValues;
    }
}
