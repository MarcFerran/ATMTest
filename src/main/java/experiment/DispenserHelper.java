package experiment;


import experiment.common.ATM;
import experiment.common.Notes;

import java.util.*;
import java.util.stream.Collectors;

public class DispenserHelper {

    public static Integer calculate(Map<Notes, Integer> map) {
        return map.entrySet().stream()
                .map(entry -> entry.getKey().getAmount() * entry.getValue())
                .collect(Collectors.summingInt(Integer::intValue));
    }

    public static void prettyPrint(Map<Notes, Integer> map) {
        System.out.println(map.entrySet()
                .stream()
                .map(entry -> entry.getKey() + " - " + entry.getValue())
                .collect(Collectors.joining(", ")));
    }

    public static Integer calculateRemainder(Currency currency, Map<Notes, Integer> map) {
        return currency.getAmount() - calculate(map);
    }

    public static Integer getAvailableNotes(Notes note, ATM atm, Map<Notes, Integer> notesSubtracted) {
        ATM atmBackup = new ATM(atm);
        atmBackup.substractNotes(notesSubtracted);
        return atmBackup.getAvailableNotes().get(note);
    }

    public static Map<Notes, Integer> getMinWithdraw(List<Map<Notes, Integer>> withdrawList) {
        final Map<Notes, Integer> collect = withdrawList.stream()
                .map(Map::entrySet)
                .min(Comparator.comparingInt(DispenserHelper::sumNumberOfNotes))
                .get().stream().collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
        return new EnumMap<>(collect);
    }

    public static Map<Notes, Integer> getMinWithdrawWith5Note(List<Map<Notes, Integer>> withdrawList) {
        final Map<Notes, Integer> collect = withdrawList.stream()
                .filter(map -> have5Note(map))
                .map(Map::entrySet)
                .min(Comparator.comparingInt(DispenserHelper::sumNumberOfNotes))
                .get().stream().collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
        return new EnumMap<>(collect);
    }

    private static boolean have5Note(Map<Notes, Integer> map) {
        return (map.get(Notes.FIVE) != null && map.get(Notes.FIVE) > 0);
    }

    private static Integer sumNumberOfNotes(Set<Map.Entry<Notes, Integer>> entrySet) {
        return entrySet.stream().map(Map.Entry::getValue).collect(Collectors.summingInt(Integer::intValue));
    }
}
