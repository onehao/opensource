package com.onehao.acts.testgen.service.engine;

import com.onehao.acts.testgen.common.SUT;
import com.onehao.acts.testgen.common.TestGenProfile;
import com.onehao.acts.testgen.common.TestSet;

/**
 * 
 * @author wanhao01 refactor from FireEye2.8
 */

public class Builder {
    private SUT sut;

    public Builder(SUT sut) {
        this.sut = sut;
    }

    public TestSet generate(TestGenProfile.Algorithm flag) {
        TestSet rval = null;

        IpoEngine ipo = new IpoEngine(this.sut);

        ipo.build(flag);
        rval = ipo.getTestSet();

        return rval;
    }

    public TestSet generate() {
        return generate(TestGenProfile.Algorithm.ipog);
    }
}
