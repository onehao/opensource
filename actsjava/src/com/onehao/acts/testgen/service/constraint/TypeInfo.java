package com.onehao.acts.testgen.service.constraint;

import choco.Choco;
import choco.kernel.model.constraints.Constraint;
import choco.kernel.model.variables.Variable;
import choco.kernel.model.variables.integer.IntegerExpressionVariable;

/**
 * 
 * @author wanhao01 refactor from FireEye2.8
 */

public class TypeInfo {
    private String text;
    private Constraint constraint;
    private IntegerExpressionVariable variable;
    int type;

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    Constraint getConstraint() {
        if ((this.constraint == null) && (this.variable != null) && (this.type == 2)) {
            this.constraint = Choco.eq(this.variable, 1);
        }
        return this.constraint;
    }

    void setConstraint(Constraint constraint) {
        this.constraint = constraint;
    }

    IntegerExpressionVariable getVariable() {
        return this.variable;
    }

    void setVariable(IntegerExpressionVariable variable) {
        this.variable = variable;
    }

    Object getObj() {
        if (this.constraint != null) {
            return this.constraint;
        }
        return this.variable;
    }

    void setObj(Object obj) {
        if ((obj instanceof Constraint)) {
            this.constraint = ((Constraint) obj);
        } else if ((obj instanceof Variable)) {
            this.variable = ((IntegerExpressionVariable) obj);
        }
    }
}
