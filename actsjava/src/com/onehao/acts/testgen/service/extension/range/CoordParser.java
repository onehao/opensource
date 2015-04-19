package com.onehao.acts.testgen.service.extension.range;

import java.util.Collection;
import java.util.regex.Pattern;

public class CoordParser implements ITypeParser {

    public static final String COORD_PATTERN = "(\\-?\\d+(\\.\\d+)?)(,|%2C)\\s*(\\-?\\d+(\\.\\d+)?)";

    public static final String[] COORD_SPLITTERS = new String[] { ",", "%2C" };

    protected Pattern pattern = null;

    public CoordParser() {
        pattern = Pattern.compile(COORD_PATTERN);
    }

    @Override
    public boolean parse(Collection<String> valList) {
        if (null == valList) {
            return false;
        }

        for (String item : valList) {
            if (!this.verify(item)) {
                return false;
            }
        }

        return true;
    }

    protected boolean verify(String coord) {
        return pattern.matcher(coord).matches();
    }
}
