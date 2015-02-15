/**
 * Copyright (C) 2014 Baidu, Inc. All Rights Reserved.
 */
package com.onehao.acts.testgen.service.extension.post;

import java.util.Arrays;
import java.util.List;

import com.onehao.acts.testgen.common.SUT;
import com.onehao.acts.testgen.common.TestSet;
import com.onehao.acts.testgen.service.extension.pre.OneWayExtSlashStrategy;

import edu.uta.cse.fireeye.common.Parameter;

/**
 * @author yinxu
 *
 */
public class OneWayExtStrategy extends OneWayExtSlashStrategy implements IPostExtensionStrategy {

    @Override
    public void extend(TestSet ts, SUT sut) {
        if (null == ts || null == sut || null == this.slashedParameters || this.slashedParameters.isEmpty()) {
            return;
        }
        
        this.extendTestSet(ts);
        this.extendParameters(sut.getParameters());
    }
    
    protected void extendTestSet(TestSet ts) {
        this.extendParameters(ts.getParams());
        int extLen = ts.getParams().size();
        for (int i = 0; i < ts.getMatrix().size(); i++) {
            int[] srcRow = ts.getMatrix().get(i);
            if (srcRow.length >= extLen) {
                continue;
            }
            
            int[] extRow = Arrays.copyOf(srcRow, extLen);
            Arrays.fill(extRow, srcRow.length, extRow.length, -1);
            ts.getMatrix().set(i, extRow);
        }
    }

    protected void extendParameters(List<Parameter> params) {
        if (null == params) {
            return;
        }
        
        params.addAll(this.slashedParameters);
    }
}
