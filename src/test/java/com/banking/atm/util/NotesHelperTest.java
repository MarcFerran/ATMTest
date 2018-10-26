package com.banking.atm.util;

import com.banking.atm.common.Notes;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.banking.atm.common.Notes.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class NotesHelperTest {

    @Test
    public void should_get_the_withdraw_with_the_min_number_of_notes(){
        List<Map<Notes, Integer>> withdrawList = new ArrayList<>();
        withdrawList.add(createNotesMap(1,1,1,1));
        withdrawList.add(createNotesMap(2,2,2,2));
        withdrawList.add(createNotesMap(3,3,3,3));
        withdrawList.add(createNotesMap(4,4,4,4));

        assertThat(NotesHelper.getMinWithdraw(withdrawList).toString()).isEqualTo("{FIFTY=1, TWENTY=1, TEN=1, FIVE=1}");
    }

    @Test
    public void should_get_the_withdraw_with_the_min_number_of_notes_with_a_five(){
        List<Map<Notes, Integer>> withdrawList = new ArrayList<>();
        withdrawList.add(createNotesMap(1,1,1,0));
        withdrawList.add(createNotesMap(2,2,2,0));
        withdrawList.add(createNotesMap(3,3,3,0));
        withdrawList.add(createNotesMap(4,4,4,1));

        assertThat(NotesHelper.getMinWithdrawWith5Note(withdrawList).toString()).isEqualTo("{FIFTY=4, TWENTY=4, TEN=4, FIVE=1}");
    }


    private Map<Notes, Integer> createNotesMap(final Integer fifties, final Integer twenties, final Integer tens, final Integer fives) {
        Map<Notes, Integer> notes = new HashMap<>();
        notes.put(FIFTY, fifties);
        notes.put(TWENTY, twenties);
        notes.put(TEN, tens);
        notes.put(FIVE, fives);
        return notes;
    }

}
