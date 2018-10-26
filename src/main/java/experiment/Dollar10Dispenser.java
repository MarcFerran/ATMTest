package experiment;

import experiment.common.ATM;
import experiment.common.Notes;

import java.util.List;
import java.util.Map;

public class Dollar10Dispenser implements DispenseChain {

    public static final Notes TEN = Notes.TEN;
    private DispenseChain chain;

    @Override
    public void setNextChain(DispenseChain nextChain) {
        this.chain = nextChain;
    }

    @Override
    public List<Map<Notes, Integer>> dispense(Currency currency, ATM atm, Map<Notes, Integer> notesSubtracted) {
        int remainder = DispenserHelper.calculateRemainder(currency, notesSubtracted);
        int availableNotes = DispenserHelper.getAvailableNotes(TEN, atm, notesSubtracted);
        if (remainder >= TEN.getAmount() && availableNotes > 0) {
            int num = 0;
            while (availableNotes > 0 && remainder >= TEN.getAmount()) {
                remainder = remainder - TEN.getAmount();
                num++;
                availableNotes--;
            }
            notesSubtracted.put(TEN, num);
            if (remainder != 0) {
                this.chain.dispense(currency, atm, notesSubtracted);
            }
        } else {
            chain.dispense(currency, atm, notesSubtracted);
        }
        return null;
    }
}
