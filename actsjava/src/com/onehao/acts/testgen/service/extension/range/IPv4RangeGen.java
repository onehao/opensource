package com.onehao.acts.testgen.service.extension.range;

import java.util.ArrayList;
import java.util.Collection;

import com.onehao.acts.testgen.service.extension.Utils;

public class IPv4RangeGen extends NumberRangeGen {

    @Override
    public boolean parse(Collection<String> valList) {
        return false;
    }

    @Override
    public void genRangeValues(Collection<String> valList, String start, String end) {
        long startVal = Utils.ipv4ToLong(start);
        long endVal = Utils.ipv4ToLong(end);

        if ((startVal < 0) || (endVal < 0)) {
            return;
        }

        Collection<String> numList = new ArrayList<String>();
        super.parse(null);
        super.genRangeValues(numList, String.valueOf(startVal), String.valueOf(endVal));

        if (numList.isEmpty()) {
            return;
        }

        for (String val : numList) {
            valList.add(Utils.longToIPv4(Long.valueOf(val)));
        }
    }

}
