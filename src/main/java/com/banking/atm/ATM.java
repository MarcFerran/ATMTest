package com.banking.atm;

import com.banking.atm.common.Notes;
import com.banking.util.Validator;

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
        Map<Notes, Integer> aux = new EnumMap<>(availableNotes);
        income.forEach((k, v) -> {
            Validator.checkNotNull(v, "Null values not allowed");
            if (aux.get(k) == null) aux.put(k, 0);
            if (Integer.valueOf(0) > v) throw new IllegalArgumentException("Negative values not allowed");
            aux.merge(k, v, Integer::sum);
        });
        availableNotes = aux;
    }

    public void subtractNotes(final Map<Notes, Integer> outcome) {
        Map<Notes, Integer> aux = new EnumMap<>(availableNotes);
        outcome.forEach((k, v) -> {
            Validator.checkNotNull(v, "A null value cannot be subtracted");
            if (aux.get(k) == null) aux.put(k, 0);
            if (aux.get(k) < v) throw new IllegalArgumentException(String.format("Impossible to extract %s notes of %s", v, k.toString()));
            aux.replace(k, aux.get(k) - v);
        });
        availableNotes = aux;
    }

    public String allAvailableNotesPrettyPrint() {
        return availableNotes.entrySet()
                .stream()
                .map(entry -> entry.getKey() + " - " + entry.getValue())
                .collect(Collectors.joining(", "));
    }

}
