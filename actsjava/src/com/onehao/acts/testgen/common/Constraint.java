package com.onehao.acts.testgen.common;

import java.util.ArrayList;

import com.onehao.acts.testgen.service.constraint.HiddenParameterHelper;

import edu.uta.cse.fireeye.common.Parameter;
import edu.uta.cse.fireeye.service.constraint.ChocoContext;
import edu.uta.cse.fireeye.service.constraint.ConstraintParser;
import edu.uta.cse.fireeye.service.engine.PVPair;
import edu.uta.cse.fireeye.service.engine.Tuple;

/**
 * 
 * @author wanhao01 refactor from FireEye2.8
 */
public class Constraint {
    private ArrayList<Parameter> params;
    private static ConstraintParser parser;
    private String text;
    private HiddenParameterHelper helper;

    public Constraint(String text, ArrayList<Parameter> params) {
        this.text = text;
        this.params = params;

        this.helper = new HiddenParameterHelper(this);
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ArrayList<Parameter> getParams() {
        return this.params;
    }

    public void setParams(ArrayList<Parameter> params) {
        this.params = params;
    }

    public HiddenParameterHelper getHiddenParameterHelper() {
        return this.helper;
    }

    public boolean involved(Parameter param) {
        boolean rval = false;
        for (Parameter it : this.params) {
            if (param.getID() == it.getID()) {
                rval = true;
                break;
            }
        }
        return rval;
    }

    public boolean eval(int[] test) {
        boolean rval = true;
        parser = new ConstraintParser(getText());

        ArrayList<PVPair> pairs = new ArrayList<PVPair>();
        boolean nonBinary = false;
        if (this.params.size() > 2) {
            nonBinary = true;
        }
        boolean noDontCare = true;
        for (Parameter param : this.params) {
            int index = test[param.getActiveID()];
            if (index == -1) {
                noDontCare = false;
            } else {
                Object value = convert(param, index);
                parser.addParam(param.getName(), value);
                pairs.add(new PVPair(param, index));
            }
        }
        if (noDontCare) {
            rval = parser.evaluate();
        } else {
            if (ChocoContext.USE_CHOCO_SOLVER) {
                return true;
            }
            if (nonBinary) {
                if (!this.helper.isHasbuildHiddenParameter()) {
                    this.helper.buildHiddenParameter();
                }
                rval = this.helper.parsePVPairs(pairs);
            }
        }
        return rval;
    }

    public boolean eval(Tuple tuple) {
        boolean rval = true;
        ConstraintParser parser = new ConstraintParser(getText());

        ArrayList<PVPair> pairs = new ArrayList<PVPair>();
        boolean nonBinary = false;
        if (this.params.size() > 2) {
            nonBinary = true;
        }
        boolean allParamPresent = true;
        for (Parameter param : this.params) {
            PVPair pair = tuple.getPair(param);
            if (pair == null) {
                allParamPresent = false;
                if (nonBinary) {
                }
            } else {
                Object value = convert(param, pair.value);
                parser.addParam(param.getName(), value);
                pairs.add(pair);
            }
        }
        if (allParamPresent) {
            rval = parser.evaluate();
        } else {
            if (ChocoContext.USE_CHOCO_SOLVER) {
                return true;
            }
            if (nonBinary) {
                if (!this.helper.isHasbuildHiddenParameter()) {
                    this.helper.buildHiddenParameter();
                }
                rval = this.helper.parsePVPairs(pairs);
            }
        }
        return rval;
    }

    public boolean eval(PVPair p1, PVPair p2) {
        boolean rval = true;
        ConstraintParser parser = new ConstraintParser(getText());

        Object val1 = convert(p1.param, p1.value);
        Object val2 = convert(p2.param, p2.value);
        parser.addParam(p1.param.getName(), val1);
        parser.addParam(p2.param.getName(), val2);
        rval = parser.evaluate();

        return rval;
    }

    public boolean eval(PVPair p) {
        boolean rval = true;
        ConstraintParser parser = new ConstraintParser(getText());

        Object value = convert(p.param, p.value);
        parser.addParam(p.param.getName(), value);
        rval = parser.evaluate();

        return rval;
    }

    public Object convert(Parameter param, int index) {
        Object rval = null;
        if (param.getParamType() == 2) {
            rval = new Boolean(param.getValue(index));
        } else if (param.getParamType() == 1) {
            rval = param.getValue(index);
        } else if (param.getParamType() == 0) {
            rval = Integer.valueOf(Integer.parseInt(param.getValue(index)));
        }
        return rval;
    }

    public Parameter getHiddenParameter() {
        return this.helper.getHiddenParameter();
    }

    public String toString() {
        StringBuffer rval = new StringBuffer();
        rval.append("Constraint: ").append(this.text).append("\n");
        return rval.toString();
    }
}
