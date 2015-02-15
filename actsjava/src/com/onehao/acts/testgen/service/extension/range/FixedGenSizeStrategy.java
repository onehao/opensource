package com.onehao.acts.testgen.service.extension.range;

public class FixedGenSizeStrategy implements IGenSizeStrategy {

    protected int size;

    public FixedGenSizeStrategy() {
        this.size = 20;
    }

    public FixedGenSizeStrategy(int size) {
        this.size = size;
    }

    @Override
    public int getGenSize() {
        return this.size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
