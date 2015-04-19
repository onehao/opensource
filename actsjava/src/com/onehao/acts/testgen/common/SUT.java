package com.onehao.acts.testgen.common;

import java.util.ArrayList;
import java.util.Iterator;

import com.onehao.acts.testgen.service.constraint.ConstraintManager;

import edu.uta.cse.fireeye.common.Parameter;
import edu.uta.cse.fireeye.common.Relation;
import edu.uta.cse.fireeye.service.engine.RelationManager;

/**
 * 
 * @author wanhao01 refactor from FireEye2.8
 */
public class SUT {
    private String name;
    private ArrayList<Parameter> parameters;
    private TestSet ts;
    private RelationManager relationMgr;
    private ConstraintManager constraintMgr;
    private ArrayList<Parameter> outputParameters;

    public SUT() {
        this.name = "Unnamed";
        setParameters(new ArrayList<Parameter>());
        this.outputParameters = new ArrayList<Parameter>();
        initRelationMgr();
        setConstraintManager(new ConstraintManager(this));
    }

    public SUT(ArrayList<Parameter> params) {
        this.name = "Unnamed";
        setParameters(params);
        this.outputParameters = new ArrayList<Parameter>();
        initRelationMgr();
        setConstraintManager(new ConstraintManager(this));
    }

    public SUT(ArrayList<Parameter> params, ArrayList<Parameter> outputParams) {
        this.name = "Unnamed";
        setParameters(params);
        setOutputParameters(outputParams);
        initRelationMgr();
        setConstraintManager(new ConstraintManager(this));
    }

    public SUT(String name) {
        this.name = name;
        setParameters(new ArrayList<Parameter>());
        this.outputParameters = new ArrayList<Parameter>();
        initRelationMgr();
        setConstraintManager(new ConstraintManager(this));
    }

    private void initRelationMgr() {
        this.relationMgr = new RelationManager();
    }

    public SUT buildSUT(int numOfParams, int domainSize) {
        SUT sut = new SUT();
        for (int i = 0; i < numOfParams; i++) {
            Parameter param = sut.addParam("P" + i);
            for (int j = 0; j < domainSize; j++) {
                param.addValue("v" + j);
            }
        }
        return sut;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Parameter addParam(String name) {
        int id = getParameters().size();
        Parameter param = new Parameter(name);
        param.setID(id);
        getParameters().add(param);

        return param;
    }

    public Parameter getParam(int index) {
        return (Parameter) getParameters().get(index);
    }

    public Parameter getParam(String name) {
        for (Parameter param : this.parameters) {
            if (param.getName().equals(name)) {
                return param;
            }
        }
        return null;
    }

    public int getNumOfParams() {
        return getParameters().size();
    }

    public Iterator<Parameter> getParamsIterator() {
        return getParameters().iterator();
    }

    public ArrayList<Parameter> getParams() {
        return getParameters();
    }

    public ArrayList<Parameter> getParameters() {
        return this.parameters;
    }

    public int getMaxDomainSize() {
        int rval = 0;
        for (Parameter param : this.parameters) {
            if (rval < param.getDomainSize()) {
                rval = param.getDomainSize();
            }
        }
        return rval;
    }

    public int getMinDomainSize() {
        int rval = 0;
        for (Parameter param : this.parameters) {
            if ((rval == 0) || (rval > param.getDomainSize())) {
                rval = param.getDomainSize();
            }
        }
        return rval;
    }

    public int getFixedDomainSize() {
        int rval = -1;
        for (Parameter param : this.parameters) {
            if (rval == -1) {
                rval = param.getDomainSize();
            } else if (rval != param.getDomainSize()) {
                rval = -1;
                break;
            }
        }
        return rval;
    }

    public void addRelation(Relation relation) {
        this.relationMgr.addRelation(relation);
    }

    public void addDefaultRelation(int t) {
        Relation r = new Relation(t, this.parameters);
        this.relationMgr.addRelation(r);
    }

    public RelationManager getRelationManager() {
        return this.relationMgr;
    }

    public ArrayList<Relation> getRelations() {
        return this.relationMgr.getRelations();
    }

    public int getNumOfRelations() {
        return this.relationMgr.getNumOfRelations();
    }

    public void addConstraint(Constraint constraint) {
        this.constraintMgr.addConstraint(constraint);
    }

    public ArrayList<Constraint> getConstraints() {
        return this.constraintMgr.getConstraints();
    }

    public int getNumOfConstraints() {
        return this.constraintMgr.getConstraints().size();
    }

    public void setExistingTestSet(TestSet ts) {
        this.ts = ts;
    }

    public TestSet getExistingTestSet() {
        return this.ts;
    }

    public void setParameters(ArrayList<Parameter> parameters) {
        this.parameters = parameters;
    }

    public ConstraintManager getConstraintManager() {
        return this.constraintMgr;
    }

    public void setConstraintManager(ConstraintManager constraintMgr) {
        this.constraintMgr = constraintMgr;
    }

    public ArrayList<Parameter> getOutputParameters() {
        return this.outputParameters;
    }

    public void setOutputParameters(ArrayList<Parameter> outputParameters) {
        int i = 0;
        if (outputParameters != null) {
            for (Parameter param : outputParameters) {
                param.setID(i++);
            }
            this.outputParameters = outputParameters;
        } else {
            this.outputParameters = new ArrayList<Parameter>();
        }
    }

    public int getNumOfOutputParams() {
        return this.outputParameters.size();
    }

    public Parameter getOutputParam(int index) {
        return (Parameter) getOutputParameters().get(index);
    }

    public Parameter getOutputParam(String name) {
        for (Parameter param : this.outputParameters) {
            if (param.getName().equals(name)) {
                return param;
            }
        }
        return null;
    }

    public String toString() {
        StringBuffer rval = new StringBuffer();
        rval.append("System Under Test:\n");
        rval.append("===========================================\n");
        rval.append("Name: " + this.name + "\n");
        rval.append("Number of Params: " + getNumOfParams() + "\n");
        rval.append("\nParameters: \n");

        Iterator<Parameter> paramIt = getParameters().iterator();
        while (paramIt.hasNext()) {
            rval.append(paramIt.next());
        }
        rval.append("\nRelations: \n");
        Iterator<Relation> relationIt = getRelations().iterator();
        while (relationIt.hasNext()) {
            rval.append(relationIt.next());
        }
        rval.append("\nConstraints: \n");
        Iterator<Constraint> constraintIt = getConstraintManager().getConstraints().iterator();
        while (constraintIt.hasNext()) {
            rval.append(constraintIt.next());
        }
        return rval.toString();
    }
}
