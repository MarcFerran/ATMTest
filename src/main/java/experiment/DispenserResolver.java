package experiment;


import experiment.common.ATM;
import experiment.common.Notes;

import java.util.*;

public class DispenserResolver {

    public static final Notes FIFTY = Notes.FIFTY;
    private DispenseChain chain;

    public static ATM createATM() {
        ATM atm = new ATM();
        Map<Notes, Integer> notes = new EnumMap<>(Notes.class);
        notes.put(Notes.FIFTY, 10);
        notes.put(Notes.TWENTY, 10);
        notes.put(Notes.TEN, 10);
        notes.put(Notes.FIVE, 10);
        atm.addNotes(notes);
        return atm;
    }


    public List<Map<Notes, Integer>> dispense2(Currency currency, ATM atm, Map<Notes, Integer> notesSubtracted, Notes note) {
        List<Map<Notes, Integer>> result = new ArrayList<>();
        Integer value = DispenserHelper.calculate(notesSubtracted);
        if (value < currency.getAmount()) {
            for (Notes noteAux = note; noteAux != null; noteAux = noteAux.next()) {
                if (atm.getAvailableNotes().get(noteAux) > notesSubtracted.get(noteAux)) {
                    Map<Notes, Integer> notesSubtractedBackup = new EnumMap<>(notesSubtracted);
                    notesSubtractedBackup.put(noteAux, notesSubtractedBackup.get(noteAux) + 1);

//                    List<Map<Notes, Integer>> newList = dispense2(currency, atm, notesSubtractedBackup, noteAux);
//                    if (newList != null) {
////                        System.out.println("adeu");
////                        System.out.println(newList);
//                        result.addAll(newList);
//                    }
                    List<Map<Notes, Integer>> maps = dispense2(currency, atm, notesSubtractedBackup, noteAux);
                    if (maps.size() != 0) System.out.println(maps.size());
                    result.addAll(maps);
                }
            }

        } else if (value == currency.getAmount()) {
//            System.out.println("hola");
//            System.out.println(notesSubtracted);
            result.add(new EnumMap<>(notesSubtracted));
        }
        return result;
    }
    public List<Map<Notes, Integer>> experimental(Integer amount, ATM atm, Map<Notes, Integer> notesSubtracted, Notes note) {
        if(notesSubtracted == null) notesSubtracted = initializeSubstraction();
        List<Map<Notes, Integer>> result = new ArrayList<>();
        Integer value = DispenserHelper.calculate(notesSubtracted);
        if (value < amount) {
            for (Notes noteAux = note; noteAux != null; noteAux = noteAux.next()) {
                if (atm.getAvailableNotes().get(noteAux) > notesSubtracted.get(noteAux)) {
                    Map<Notes, Integer> notesSubtractedBackup = new EnumMap<>(notesSubtracted);
                    notesSubtractedBackup.put(noteAux, notesSubtractedBackup.get(noteAux) + 1);

                    result.addAll(experimental(amount, atm, notesSubtractedBackup, noteAux));
                }
            }

        } else if (amount.equals(value)) {
            result.add(new EnumMap<>(notesSubtracted));
        }
        return result;
    }

    private Map<Notes, Integer> initializeSubstraction() {
        Map<Notes, Integer> notesSubtracted = new EnumMap<>(Notes.class);
        notesSubtracted.put(Notes.FIFTY, 0);
        notesSubtracted.put(Notes.TWENTY, 0);
        notesSubtracted.put(Notes.TEN, 0);
        notesSubtracted.put(Notes.FIVE, 0);
        return notesSubtracted;
    }

    public static void main(String[] args) {
        DispenserResolver dispenserResolver = new DispenserResolver();
//        dispenserResolver.setNextChain(dispenserResolver);
        Map<Notes, Integer> notesSubtracted = new EnumMap<>(Notes.class);
        notesSubtracted.put(Notes.FIFTY, 0);
        notesSubtracted.put(Notes.TWENTY, 0);
        notesSubtracted.put(Notes.TEN, 0);
        notesSubtracted.put(Notes.FIVE, 0);
        Currency currency = new Currency(400);
        Integer amount = 110;
//        List<Map<Notes, Integer>> dispense = dispenserResolver.dispense2(currency, createATM(), notesSubtracted, Notes.FIFTY);
        List<Map<Notes, Integer>> dispense = dispenserResolver.experimental(amount, createATM(), null, Notes.FIFTY);
        System.out.println("----RESULT:---");

        System.out.print("MIN WITH 5: ");
        System.out.println(DispenserHelper.getMinWithdrawWith5Note(dispense));

        System.out.print("MIN: ");
        System.out.println(DispenserHelper.getMinWithdraw(dispense));

        System.out.println(Arrays.toString(dispense.toArray()));

        System.out.println(dispense.size());
    }

}
