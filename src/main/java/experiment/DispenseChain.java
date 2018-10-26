package experiment;

import experiment.common.ATM;
import experiment.common.Notes;

import java.util.List;
import java.util.Map;

public interface DispenseChain {
    void setNextChain(DispenseChain nextChain);

    List<Map<Notes, Integer>> dispense(Currency currency, ATM atm, Map<Notes, Integer> notesSubtracted);
}
