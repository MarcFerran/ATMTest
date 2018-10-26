package experiment;

import experiment.common.ATM;
import experiment.common.Notes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FirstDollar5Dispenser implements DispenseChain {

    public static final Notes FIVE = Notes.FIVE;
    private DispenseChain chain;

    @Override
    public void setNextChain(DispenseChain nextChain) {
        this.chain = nextChain;
    }

    @Override
    public List<Map<Notes, Integer>> dispense(Currency currency, ATM atm, Map<Notes, Integer> notesSubtracted) {
        List<Map<Notes, Integer>> result = new ArrayList<>();
        if(currency.getAmount() >= FIVE.getAmount()
                && atm.getAvailableNotes().get(FIVE) > 0){
            int remainder = currency.getAmount();
            int num = 0;
            int availableNotes = atm.getAvailableNotes().get(FIVE) == 0 ? 0 : 1;
            while(availableNotes > 0 && remainder >= FIVE.getAmount()){
                remainder = remainder - FIVE.getAmount();
                num++;
                availableNotes--;
            }
            notesSubtracted.put(FIVE, num);
            if(remainder != 0){
                result = this.chain.dispense(currency, atm, notesSubtracted);
            }
        }else {
            result = this.chain.dispense(currency, atm, notesSubtracted);
        }
        return result;
    }
}
