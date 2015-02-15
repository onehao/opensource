package com.onehao.acts.testgen.service.engine;

import edu.uta.cse.fireeye.service.CoverageCheckInfo;

/**
 * 
 * @author wanhao01 refactor from FireEye2.8
 */

abstract interface CoverageRetreiver {
    public abstract float[] getCoverageRatios(CoverageCheckInfo paramCoverageCheckInfo);
}
