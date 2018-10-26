package experiment.common;

import java.util.EnumMap;
import java.util.Map;

public class ATM {

    private Map<Notes, Integer> availableNotes;

    public void addNotes(final Map<Notes, Integer> income) {
        income.forEach((k, v) -> availableNotes.merge(k, v, Integer::sum));
    }

    public Map<Notes, Integer> getAvailableNotes() {
        return availableNotes;
    }

    public void substractNotes(final Map<Notes, Integer> outcome) {
        outcome.forEach((k, v) -> availableNotes.replace(k, availableNotes.get(k) - v));

    }
    public ATM () {
        this.availableNotes = new EnumMap<>(Notes.class);
    }

    public ATM (ATM atm) {
        this.availableNotes = new EnumMap<>(atm.getAvailableNotes());
    }
}
