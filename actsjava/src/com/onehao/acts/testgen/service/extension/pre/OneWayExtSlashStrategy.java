/**
 * Copyright (C) 2014 Baidu, Inc. All Rights Reserved.
 */
package com.onehao.acts.testgen.service.extension.pre;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.onehao.acts.testgen.common.SUT;
import com.onehao.acts.testgen.service.extension.common.ParameterWrapper;

import edu.uta.cse.fireeye.common.Parameter;
import edu.uta.cse.fireeye.common.Relation;

/**
 * 
 * @author yinxu
 *
 */
public class OneWayExtSlashStrategy implements IPreExtensionStrategy {
    
    protected List<Parameter> slashedParameters;
    
    public OneWayExtSlashStrategy() {
        slashedParameters = new ArrayList<Parameter>();
    }

    @Override
    public void process(SUT sut, Map<String, Collection<String>> genParamValsMap) {
        if (null == sut || null == sut.getParameters()) {
            return;
        }
        
        slashedParameters.clear();
        
        for (Parameter param : sut.getParameters()) {
            ParameterWrapper paramWrapper = new ParameterWrapper(param);
            if (paramWrapper.getOneWayExt()) {
                this.slashedParameters.add(param);
                this.checkRelations(sut, param);
            }
        }
        
        sut.getParameters().removeAll(this.slashedParameters);
    }
    
    protected void checkRelations(SUT sut, Parameter param) {
        for (Relation relation : sut.getRelations()) {
            if (relation.getParams().contains(param)) {
                relation.getParams().remove(param);
            }
        }
    }

    //To be removed, split to another interface
    @Override
    public IPreExtensionStrategy setGenValueAppendStrategy(GenValueAppendStrategy strategy) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public GenValueAppendStrategy getGenValueAppendStrategy() {
        // TODO Auto-generated method stub
        return null;
    }

}
