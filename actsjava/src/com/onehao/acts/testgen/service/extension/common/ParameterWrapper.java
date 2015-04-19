package com.onehao.acts.testgen.service.extension.common;

import edu.uta.cse.fireeye.common.Parameter;

public class ParameterWrapper {

    protected Parameter parameter = null;
    
    protected ParameterType paramType = new ParameterType();
    
    public ParameterWrapper(Parameter param) {
        this.parameter = param;
    }
    
    public Parameter getParameter() {
        return this.parameter;
    }

    public boolean getIsRequired() {
        return this.getParamTypeFlag(ParameterType.REQUIRED);
    }

    public ParameterWrapper setIsRequired(boolean isRequired) {
        this.setParamTypeFlag(ParameterType.REQUIRED, isRequired);
        return this;
    }

    public boolean getOneWayExt() {
        return this.getParamTypeFlag(ParameterType.ONE_WAY_EXT);
    }

    public ParameterWrapper setOneWayExt(boolean oneWayExt) {
        this.setParamTypeFlag(ParameterType.ONE_WAY_EXT, oneWayExt);
        return this;
    }

    public boolean getBoundaryGen() {
        return this.getParamTypeFlag(ParameterType.BOUNDARY_GEN);
    }

    public ParameterWrapper setBoundaryGen(boolean boundaryGen) {
        this.setParamTypeFlag(ParameterType.BOUNDARY_GEN, boundaryGen);
        return this;
    }
    
    protected boolean getParamTypeFlag(int index) {
        if (this.parameter != null && this.paramType != null) {
            return this.paramType.setParamType(this.parameter.getParamType()).getFlag(index);
        } else {
            return false;
        }
    }
    
    protected void setParamTypeFlag(int index, boolean val) {
        if (this.parameter != null && this.paramType != null) {
            this.paramType.setParamType(this.parameter.getParamType()).setFlag(index, val);
            this.parameter.setType(this.paramType.getParamType());
        }
    }
}
