package experiment;

import experiment.common.ATM;
import experiment.common.Notes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dollar50Dispenser implements DispenseChain {

    public static final Notes FIFTY = Notes.FIFTY;
    private DispenseChain chain;

    @Override
    public void setNextChain(DispenseChain nextChain) {
        this.chain = nextChain;
    }

    @Override
    public List<Map<Notes, Integer>> dispense(Currency currency, ATM atm, Map<Notes, Integer> notesSubtracted) {
        int remainder = DispenserHelper.calculateRemainder(currency, notesSubtracted);
        int availableNotes = DispenserHelper.getAvailableNotes(FIFTY, atm, notesSubtracted);
        List<Map<Notes, Integer>> result = new ArrayList<>();
        if (remainder >= FIFTY.getAmount() && availableNotes > 0) {
            int num = 0;
            while (availableNotes > 0 && remainder >= FIFTY.getAmount()) {
                remainder = remainder - FIFTY.getAmount();
                num++;
                availableNotes--;
                notesSubtracted.put(FIFTY, num);
                HashMap<Notes, Integer> notesSubtractedBackup = new HashMap<>(notesSubtracted);
                this.chain.dispense(currency, atm, notesSubtractedBackup);

                if (DispenserHelper.calculate(notesSubtractedBackup) == currency.getAmount()) {
                    System.out.print("OK: ");
                    result.add(notesSubtractedBackup);
                }
                else System.out.print("Error: ");
                DispenserHelper.prettyPrint(notesSubtractedBackup);
            }
        } else {
            this.chain.dispense(currency, atm, notesSubtracted);
        }
        return result;
    }

}
