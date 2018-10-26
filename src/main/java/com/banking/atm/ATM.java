package com.banking.atm;

import com.banking.atm.common.Notes;
import com.banking.util.Validator;
import org.apache.commons.lang3.Validate;

import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ATM {

    private Map<Notes, Integer> availableNotes;

    public ATM() {
        this.availableNotes = new EnumMap<>(Notes.class);
    }

    public Integer getAvailableNotes(final Notes note) {
        return (availableNotes.get(note) == null) ? 0 : availableNotes.get(note);
    }

    public void addNotes(final Map<Notes, Integer> income) {
        income.forEach((k, v) -> {
            Validator.checkNotNull(v, "Null values not allowed");
            if (Integer.valueOf(0) > v) throw new IllegalArgumentException("Negative values not allowed");
            availableNotes.merge(k, v, Integer::sum);
        });
    }

    public void substractNotes(final Map<Notes, Integer> outcome) {
        outcome.forEach((k, v) -> {
            Validate.notNull(availableNotes.get(k));
            Validate.notNull(v);
            if (availableNotes.get(k) < v) throw new IllegalArgumentException(String.format("Impossible to extract %s notes of %s", v, k.toString()));
            availableNotes.replace(k, availableNotes.get(k) - v);
        });
    }

    public String allAvailableNotesPrettyPrint() {
        return availableNotes.entrySet()
                .stream()
                .map(entry -> entry.getKey() + " - " + entry.getValue())
                .collect(Collectors.joining(", "));
    }

}
