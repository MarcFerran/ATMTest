package experiment;

import experiment.common.ATM;
import experiment.common.Notes;

import java.util.List;
import java.util.Map;

public class Dollar5Dispenser implements DispenseChain {

    public static final Notes FIVE = Notes.FIVE;
    private DispenseChain chain;

    @Override
    public void setNextChain(DispenseChain nextChain) {
        this.chain = nextChain;
    }

    @Override
    public List<Map<Notes, Integer>> dispense(Currency currency, ATM atm, Map<Notes, Integer> notesSubtracted) {
        int remainder = DispenserHelper.calculateRemainder(currency, notesSubtracted);
        int availableNotes = DispenserHelper.getAvailableNotes(FIVE, atm, notesSubtracted);
        if (remainder >= FIVE.getAmount() && availableNotes > 0) {
            int num = 0;
            while(availableNotes > 0 && remainder >= FIVE.getAmount()){
                remainder = remainder - FIVE.getAmount();
                num++;
                availableNotes--;
            }
            notesSubtracted.put(FIVE, (notesSubtracted.get(FIVE) == null ? 0 : notesSubtracted.get(FIVE)) + num);
            if(remainder != 0){
                System.out.println("CRASH");
            }
        } else {
            System.out.println("CRASH");
        }
        return null;
    }
}
