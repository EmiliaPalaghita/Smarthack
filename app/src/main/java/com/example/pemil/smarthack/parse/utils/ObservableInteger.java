package com.example.pemil.smarthack.parse.utils;

public class ObservableInteger {
    private Integer integer;
    private ChangeListener listener;

    public ObservableInteger() {
        this.integer = 0;
    }

    public void setListener(ChangeListener listener)
    {
        this.listener = listener;
    }

    public void increment() {
        integer++;
        if(integer == 0) {
            listener.onChange();
        }
    }

    public void decrement() {
        integer--;
        if(integer == 0) {
            listener.onChange();
        }
    }

    public interface ChangeListener {
        void onChange();
    }
}
