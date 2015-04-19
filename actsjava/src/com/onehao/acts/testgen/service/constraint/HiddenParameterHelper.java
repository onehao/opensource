package com.onehao.acts.testgen.service.constraint;

import java.util.ArrayList;
import java.util.HashMap;

import com.onehao.acts.testgen.common.Constraint;

import edu.uta.cse.fireeye.common.Parameter;
import edu.uta.cse.fireeye.service.constraint.ConstraintParser;
import edu.uta.cse.fireeye.service.engine.Combinatorics;
import edu.uta.cse.fireeye.service.engine.PVPair;

/**
 * 
 * @author wanhao01 refactor from FireEye2.8
 */

public class HiddenParameterHelper {
    private Constraint constraint;
    private static int hiddenVariableIndex = 0;
    private ArrayList<String[]> hiddenDomain;
    private HashMap<Parameter, Integer> paramPositionMap;
    private Parameter hiddenParameter;
    // private ArrayList<PVPair> PVPairs;
    private boolean hasBuildHiddenParameter = false;

    public HiddenParameterHelper(Constraint constraint) {
        this.constraint = constraint;

        this.hiddenDomain = new ArrayList<String[]>();
        this.paramPositionMap = new HashMap<Parameter, Integer>();
    }

    public Parameter getHiddenParameter() {
        return this.hiddenParameter;
    }

    public void buildHiddenParameter() {
        if ((this.constraint.getParams() != null) && (this.constraint.getParams().size() > 2)) {
            buildHiddenParameterDomain();
            createHiddenParameter();
        }
        this.hasBuildHiddenParameter = true;
    }

    private void buildHiddenParameterDomain() {
        createParamPositionMap();
        ArrayList<Parameter> params = this.constraint.getParams();
        ArrayList<int[]> allValueCombo = Combinatorics.getValueCombos(params);
        for (int[] combo : allValueCombo) {
            addHiddenValue(combo);
        }
    }

    private void addHiddenValue(int[] combo) {
        ConstraintParser parser = new ConstraintParser(this.constraint.getText());
        String[] values = new String[combo.length];
        int j = 0;
        for (Parameter param : this.constraint.getParams()) {
            Integer pos = (Integer) this.paramPositionMap.get(param);

            int index = combo[pos.intValue()];

            Object value = this.constraint.convert(param, index);

            values[j] = value.toString();

            parser.addParam(param.getName(), value);
            j++;
        }
        if (parser.evaluate()) {
            this.hiddenDomain.add(values);
        }
    }

    private void createHiddenParameter() {
        this.hiddenParameter = new Parameter("H" + hiddenVariableIndex);
        this.hiddenParameter.setHiddenParamValues(this.hiddenDomain);

        int id = -hiddenVariableIndex;
        hiddenVariableIndex += 1;

        this.hiddenParameter.setID(id);
        this.hiddenParameter.setHiddenVariable(true);
    }

    private void createParamPositionMap() {
        ArrayList<Parameter> params = this.constraint.getParams();
        this.paramPositionMap = new HashMap<Parameter, Integer>();
        if (params != null) {
            for (int i = 0; i < params.size(); i++) {
                this.paramPositionMap.put((Parameter) params.get(i), new Integer(i));
            }
        }
    }

    public int getParamPosition(Parameter param) {
        return ((Integer) this.paramPositionMap.get(param)).intValue();
    }

    public boolean parsePVPairs(ArrayList<PVPair> pairs) {
        for (String[] hValue : this.hiddenDomain) {
            boolean[] match = new boolean[pairs.size()];
            int j = 0;
            boolean allMatched = true;
            for (PVPair pv : pairs) {
                Parameter p = pv.param;
                int v = pv.value;
                if (v != -1) {
                    int pos = ((Integer) this.paramPositionMap.get(p)).intValue();
                    String value = this.constraint.convert(p, v).toString();
                    if (hValue[pos].equals(value)) {
                        match[j] = true;
                    }
                }
                j++;
            }
            for (int i = 0; i < match.length; i++) {

                // TODO: onehao, refactor from if (match[i] == 0) -> if (match[i] == false)
                if (match[i] == false) {
                    allMatched = false;
                }
            }
            if (allMatched) {
                return true;
            }
        }
        return false;
    }

    public boolean isHasbuildHiddenParameter() {
        return this.hasBuildHiddenParameter;
    }
}
