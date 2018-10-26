package experiment;

import experiment.common.ATM;
import experiment.common.Notes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {

    public static ATM createATM() {
        ATM atm = new ATM();
        Map<Notes, Integer> notes = new HashMap<>();
        notes.put(Notes.FIFTY, 2);
        notes.put(Notes.TWENTY, 3);
        notes.put(Notes.TEN, 0);
        notes.put(Notes.FIVE, 1);
        atm.addNotes(notes);
        return atm;
    }

    public static void main(String[] args) {
        Currency currency = new Currency(110);
        ATM atm = createATM();
        Map<Notes, Integer> notesSubtracted = new HashMap<>();

        DispenseChain firstDollar5Dispenser = new FirstDollar5Dispenser();
        DispenseChain dollar50Dispenser = new Dollar50Dispenser();
        DispenseChain dollar20Dispenser = new Dollar20Dispenser();
        DispenseChain dollar10Dispenser = new Dollar10Dispenser();
        DispenseChain dollar5Dispenser = new Dollar5Dispenser();

        firstDollar5Dispenser.setNextChain(dollar50Dispenser);
        dollar50Dispenser.setNextChain(dollar20Dispenser);
        dollar20Dispenser.setNextChain(dollar10Dispenser);
        dollar10Dispenser.setNextChain(dollar5Dispenser);
        /*
        try {
            firstDollar5Dispenser.dispense(currency, atm, notesSubtracted);
        } catch (RuntimeException e) {
            try {
                notesSubtracted = new HashMap<>();
                dollar50Dispenser.dispense(currency, atm, notesSubtracted);
            } catch (RuntimeException e2) {
                notesSubtracted = new HashMap<>();
                ((Dollar50Dispenser) dollar50Dispenser).setUseOneLess(true);
                dollar50Dispenser.dispense(currency, atm, notesSubtracted);
            }
        }
        */

        System.out.println("---Trying with 5---");
        List<Map<Notes, Integer>> dispense1 = firstDollar5Dispenser.dispense(currency, atm, notesSubtracted);
        System.out.println("---Regular execution---");
        atm = createATM();
        notesSubtracted = new HashMap<>();
        List<Map<Notes, Integer>> dispense2 = dollar50Dispenser.dispense(currency, atm, notesSubtracted);

        System.out.println("---Results---");
        System.out.println(dispense1.size());
        System.out.println(dispense2.size());

    }
}
