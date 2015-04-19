package com.onehao.acts.testgen.service.extension;

public class Pair<FT, ST> {

    private FT first;
    private ST second;

    public Pair() {
        this.first = null;
        this.second = null;
    }

    public Pair(FT first, ST second) {
        this.first = first;
        this.second = second;
    }

    public FT getFirst() {
        return first;
    }

    public void setFirst(FT first) {
        this.first = first;
    }

    public ST getSecond() {
        return second;
    }

    public void setSecond(ST second) {
        this.second = second;
    }
}
