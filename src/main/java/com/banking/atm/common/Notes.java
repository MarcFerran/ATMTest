package com.banking.atm.common;

public enum Notes {

    FIFTY(50),
    TWENTY(20),
    TEN(10),
    FIVE(5);

    private int note;

    Notes(final int note) {
        this.note = note;
    }

    public int getAmount() {
        return this.note;
    }

    public Notes next() {
        final Notes[] vals = values();
        return (this.ordinal() + 1) % vals.length == 0 ? null : vals[(this.ordinal() + 1) % vals.length];
    }
}
