package com.onehao.acts.testgen.service.extension.range;

import java.util.Collection;

public class NumberParser implements ITypeParser {

    protected long intNum = 0;
    protected long decNum = 0;
    protected int minStep = 0;

    @Override
    public boolean parse(Collection<String> valList) {
        this.reset();

        if (null == valList) {
            return false;
        }

        for (String item : valList) {
            try {
                Long.parseLong(item);
                this.intNum++;
                continue;
            } catch (Exception ex) {
                // it's not numeric; that's fine, just continue
            }

            try {
                Double.parseDouble(item);
                this.decNum++;
                this.updateMinStep(item);
                continue;
            } catch (Exception ex) {
                // it's not numeric; that's fine, just continue
            }

            this.reset();
            return false;
        }

        return true;
    }

    protected void reset() {
        this.intNum = 0;
        this.decNum = 0;
        this.minStep = 0;
    }

    protected int analyzeMinStep(String src) {
        String[] tokens = src.split("\\.");
        if ((null == tokens) || (tokens.length < 2)) {
            return 0;
        }

        return tokens[tokens.length - 1].length();
    }

    protected void updateMinStep(String src) {
        int newStep = this.analyzeMinStep(src);
        this.minStep = newStep > minStep ? newStep : minStep;
    }

    public long getIntNum() {
        return intNum;
    }

    public long getDecNum() {
        return decNum;
    }

    public int getMinStep() {
        return minStep;
    }

}
