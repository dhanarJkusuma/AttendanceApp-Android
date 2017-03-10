package com.dhanarjkusuma.sarihusada.sarihusada.model;

/**
 * Created by Dhanar J Kusuma on 08/03/2017.
 */

public class SpinnerValue
{
    private String key;
    private Object value;

    public SpinnerValue(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public SpinnerValue() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
