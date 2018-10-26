package experiment;

import experiment.common.ATM;
import experiment.common.Notes;

import java.util.List;
import java.util.Map;

public class Dollar20Dispenser implements DispenseChain {

    public static final Notes TWENTY = Notes.TWENTY;
    private DispenseChain chain;

    @Override
    public void setNextChain(DispenseChain nextChain) {
        this.chain = nextChain;
    }

    @Override
    public List<Map<Notes, Integer>> dispense(Currency currency, ATM atm, Map<Notes, Integer> notesSubtracted) {
        int remainder = DispenserHelper.calculateRemainder(currency, notesSubtracted);
        int availableNotes = DispenserHelper.getAvailableNotes(TWENTY, atm, notesSubtracted);
        if (remainder >= TWENTY.getAmount() && availableNotes > 0) {
            int num = 0;
            while (availableNotes > 0 && remainder >= TWENTY.getAmount()) {
                remainder = remainder - TWENTY.getAmount();
                num++;
                availableNotes--;
            }
            notesSubtracted.put(TWENTY, num);
            if (remainder != 0) {
                this.chain.dispense(currency, atm, notesSubtracted);
            }
        } else {
            this.chain.dispense(currency, atm, notesSubtracted);
        }
        return null;
    }
}
