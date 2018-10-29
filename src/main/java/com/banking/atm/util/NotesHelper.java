package com.banking.atm.util;

import com.banking.atm.common.Notes;

import java.util.*;
import java.util.stream.Collectors;

import static com.banking.atm.common.Notes.FIVE;

public class NotesHelper {

    public static Integer calculate(Map<Notes, Integer> map) {
        return map.entrySet().stream()
                .map(entry -> entry.getKey().getAmount() * entry.getValue())
                .collect(Collectors.summingInt(Integer::intValue));
    }

    public static Map<Notes, Integer> getMinWithdraw(List<Map<Notes, Integer>> withdrawList) {
        final Set<Map.Entry<Notes, Integer>> entries = withdrawList.stream()
                .map(Map::entrySet)
                .min(Comparator.comparingInt(NotesHelper::sumNumberOfNotes))
                .orElse(null);

        return (entries == null) ? null : convertToMap(entries);
    }

    public static Map<Notes, Integer> getMinWithdrawWith5Note(List<Map<Notes, Integer>> withdrawList) {
        final Set<Map.Entry<Notes, Integer>> entries = withdrawList.stream()
                .filter(map -> have5Note(map))
                .map(Map::entrySet)
                .min(Comparator.comparingInt(NotesHelper::sumNumberOfNotes))
                .orElse(null);

        return (entries == null) ? null : convertToMap(entries);
    }

    private static Map<Notes, Integer> convertToMap(final Set<Map.Entry<Notes, Integer>> set) {
        final Map<Notes, Integer> collect = set.stream().collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
        return new EnumMap<>(collect);
    }

    private static boolean have5Note(Map<Notes, Integer> map) {
        return (map.get(FIVE) != null && map.get(FIVE) > 0);
    }

    private static Integer sumNumberOfNotes(Set<Map.Entry<Notes, Integer>> entrySet) {
        return entrySet.stream().map(Map.Entry::getValue).collect(Collectors.summingInt(Integer::intValue));
    }
}
