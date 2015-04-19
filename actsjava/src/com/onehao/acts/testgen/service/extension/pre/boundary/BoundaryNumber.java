package com.onehao.acts.testgen.service.extension.pre.boundary;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.onehao.acts.testgen.service.extension.Utils;
import com.onehao.acts.testgen.service.extension.range.NumberParser;

import edu.uta.cse.fireeye.common.Parameter;

public class BoundaryNumber extends NumberParser implements IBoundaryGenerator, IBoundaryParser {

    public static final long MIN_ENUM = 0;
    public static final long MAX_ENUM = 25;

    @Override
    public boolean parse(Parameter param) {
        if (null == param) {
            return false;
        }

        return super.parse(param.getValues());
    }

    @Override
    public void generate(List<String> srcValues, Collection<String> genValues) {
        this.genCommon(genValues);

        if (0 == this.minStep) {
            this.genInt(srcValues, genValues);
        } else {
            this.genNum(srcValues, genValues);
        }

        this.genNonDec(genValues);
    }

    protected void genCommon(Collection<String> genValues) {
        genValues.add("0");
        genValues.add("-0");
        genValues.add("1");
        genValues.add("-1");
        genValues.add("");
    }

    protected void genInt(List<String> srcValues, Collection<String> genValues) {
        List<Long> list = new ArrayList<Long>();
        for (String item : srcValues) {
            list.add(Long.parseLong(item));
        }

        Collections.sort(list);

        long minVal = list.get(0);
        long maxVal = list.get(list.size() - 1);

        genValues.add(String.valueOf(minVal - 1));
        genValues.add(String.valueOf(maxVal + 1));

        if ((minVal >= MIN_ENUM) && (maxVal <= MAX_ENUM) && (list.size() <= MAX_ENUM)) {
            this.genEnum(list, genValues);
            return;
        }

        genValues.add(String.valueOf(minVal + 1));
        genValues.add(String.valueOf(maxVal - 1));

        genValues.add(String.valueOf(Integer.MIN_VALUE));
        genValues.add(String.valueOf(Integer.MAX_VALUE));
        genValues.add(String.valueOf(Long.MIN_VALUE));
        genValues.add(String.valueOf(Long.MAX_VALUE));

        // Non-Integer
        genValues.add((new BigDecimal(Utils.getThreadLocalRandom().nextDouble())).toPlainString());
    }

    protected void genNum(List<String> srcValues, Collection<String> genValues) {
        List<BigDecimal> list = new ArrayList<BigDecimal>();
        for (String item : srcValues) {
            list.add(new BigDecimal(item));
        }

        Collections.sort(list);
        String floatMaxValue = (new BigDecimal(Float.MAX_VALUE)).toPlainString();
        String floatMinValue = (new BigDecimal(Float.MIN_VALUE)).toPlainString();
        String doubleMaxValue = (new BigDecimal(Double.MAX_VALUE)).toPlainString();
        String doubleMinValue = (new BigDecimal(Double.MIN_VALUE)).toPlainString();

        BigDecimal minStepValue = new BigDecimal("0." + Utils.repeatStr("0", this.minStep - 1) + "1");

        genValues.add(list.get(0).subtract(minStepValue).toPlainString());
        genValues.add(list.get(list.size() - 1).add(minStepValue).toPlainString());
        genValues.add("0." + Utils.repeatStr("0", minStep));
        genValues.add("-0." + Utils.repeatStr("0", minStep));
        genValues.add(floatMaxValue);
        genValues.add(floatMinValue);
        genValues.add("-" + floatMaxValue);
        genValues.add("-" + floatMinValue);
        genValues.add(doubleMaxValue);
        genValues.add(doubleMinValue);
        genValues.add("-" + doubleMaxValue);
        genValues.add("-" + doubleMinValue);
    }

    protected void genEnum(List<Long> enumList, Collection<String> genValues) {
        genValues.add(String.valueOf(Utils.getRandomInt(100000) + MAX_ENUM));
    }

    protected void genNonDec(Collection<String> genValues) {
        genValues.add(Utils.genRandomString());
    }

}
