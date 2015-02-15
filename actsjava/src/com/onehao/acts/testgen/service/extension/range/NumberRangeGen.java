package com.onehao.acts.testgen.service.extension.range;

import java.math.BigDecimal;
import java.util.Collection;

import com.onehao.acts.testgen.service.extension.Utils;

public class NumberRangeGen extends GenericRangeGen<NumberParser> {

    protected BigDecimal extBaseValue = null;

    @Override
    protected void initParser() {
        this.typeParser = new NumberParser();
    }

    @Override
    public void genRangeValues(Collection<String> valList, String start, String end) {
        BigDecimal startVal = new BigDecimal(start);
        BigDecimal endVal = new BigDecimal(end);
        if (startVal.compareTo(endVal) >= 0) {
            Utils.addUniqValToCollection(valList, startVal.toPlainString());
            Utils.addUniqValToCollection(valList, endVal.toPlainString());
            return;
        }

        this.genValues(valList, startVal, endVal);
    }

    protected void genValues(Collection<String> valList, BigDecimal start, BigDecimal end) {

        BigDecimal startVal = this.doExt(start);
        BigDecimal endVal = this.doExt(end);
        BigDecimal avgStep = this.getAvgStep(startVal, endVal);

        if (null == avgStep) {
            return;
        }

        BigDecimal randomVal = new BigDecimal(startVal.toPlainString());
        BigDecimal avgBaseVal = new BigDecimal(startVal.toPlainString());
        while (avgBaseVal.compareTo(endVal) < 0) {
            valList.add(this.restoreExt(randomVal).toPlainString());
            randomVal = avgBaseVal.add(this.genNextStep(avgStep));
            avgBaseVal = avgBaseVal.add(avgStep);
        }

        valList.add(end.toPlainString());
    }

    protected BigDecimal getAvgStep(BigDecimal start, BigDecimal end) {
        int genSize = this.getGenSize();
        if (genSize <= 0) {
            return null;
        }

        BigDecimal avgStep = null;
        BigDecimal diff = end.subtract(start);
        if (diff.longValue() > genSize) {
            long newStep = diff.longValue() / genSize;
            avgStep = new BigDecimal(newStep);
        } else {
            avgStep = new BigDecimal("1");
        }

        return avgStep;
    }

    protected int getGenSize() {
        if (this.genSizeStrategy != null) {
            return this.genSizeStrategy.getGenSize();
        }

        return 1;
    }

    protected BigDecimal genNextStep(BigDecimal avgStep) {
        if ((avgStep.longValue() > Integer.MAX_VALUE) || (avgStep.intValue() < 2)) {
            return avgStep;
        }

        return new BigDecimal(Utils.getRandomInt(avgStep.intValue()));
    }

    protected BigDecimal doExt(BigDecimal orgVal) {
        if (this.typeParser.getMinStep() > 0) {
            return orgVal.multiply(getExtBaseValue());
        } else {
            return orgVal;
        }
    }

    protected BigDecimal restoreExt(BigDecimal extVal) {
        if (this.typeParser.getMinStep() > 0) {
            return extVal.divide(getExtBaseValue());
        } else {
            return extVal;
        }
    }

    protected BigDecimal getExtBaseValue() {
        if (null == this.extBaseValue) {
            this.extBaseValue = new BigDecimal((long) Math.pow(10, this.typeParser.getMinStep()));
        }

        return extBaseValue;
    }

}
