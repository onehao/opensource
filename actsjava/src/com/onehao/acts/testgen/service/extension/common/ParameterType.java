package com.onehao.acts.testgen.service.extension.common;

import java.util.BitSet;

import com.onehao.acts.testgen.service.extension.Utils;

public class ParameterType {
    
    private int paramType;
    
    public static class CommonType {
        public static final int NUMBER  = 0;
        public static final int ENUM  = 1;
        public static final int BOOLEAN  = 2;
        public static final int STRING  = 3;
        public static final int RANGE  = 4;
        public static final int DICT  = 5;
    }

    public static final int REQUIRED = 8;
    public static final int ONE_WAY_EXT = 9;
    public static final int BOUNDARY_GEN = 10;
    
    public ParameterType() {
        paramType = 0;
    }
    
    public ParameterType(int type) {
        paramType = type;
    }
    
    public int getParamType() {
        return paramType;
    }

    public ParameterType setParamType(int paramType) {
        this.paramType = paramType;
        return this;
    }
    
    public boolean getFlag(int index) {
        return Utils.convertIntToBitSet(this.paramType).get(index);
    }
    
    public ParameterType setFlag(int index, boolean val) {
        BitSet bitSet = Utils.convertIntToBitSet(this.paramType);
        bitSet.set(index, val);
        this.paramType = Utils.convertBitSetToInt(bitSet);
        return this;
    }
}
