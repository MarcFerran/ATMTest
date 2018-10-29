package com.banking.atm;

import com.banking.account.exceptions.AccountServiceException;
import com.banking.atm.common.Notes;
import com.banking.atm.exceptions.ATMServiceException;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.banking.atm.common.Notes.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ATMServiceTest {

    @Test
    public void should_get_notes_after_replenish() {
        ATMService atmService = new ATMServiceImpl();
        atmService.replenish(createNotesMap(10, 10, 10, 10));
        assertThat(atmService.checkBalance()).isEqualTo("FIFTY - 10, TWENTY - 10, TEN - 10, FIVE - 10");
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_fail_inserting_to_the_atm_a_negative_amount() {
        ATMService atmService = new ATMServiceImpl();
        atmService.replenish(createNotesMap(-10, -10, -10, -10));
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_fail_when_inserting_null_values_to_the_atm() {
        new ATMServiceImpl().replenish(createNotesMap(null, null, null, null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_fail_inserting_to_the_atm_a_null() {
        ATMService atmService = new ATMServiceImpl();
        atmService.replenish(createNotesMap(null, null, null, null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_fail_if_the_account_number_is_null() {
        ATMService atmService = new ATMServiceImpl();
        withdrawAmountFromAccount(null, atmService, createNotesMap(100, 100, 100, 100), 50);
    }

    @Test(expected = AccountServiceException.class)
    public void should_fail_if_the_amount_to_withdraw_is_bigger_than_the_account_saves() {
        ATMService atmService = new ATMServiceImpl();
        withdrawAmountFromAccount("01002", atmService, createNotesMap(100, 100, 100, 100), 50);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_fail_if_the_amount_to_withdraw_is_not_divisible_by_5() {
        ATMService atmService = new ATMServiceImpl();
        atmService.withdraw("01003", 53);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_fail_if_the_amount_to_withdraw_is_less_than_20() {
        ATMService atmService = new ATMServiceImpl();
        atmService.withdraw("01003", 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_fail_if_the_amount_to_withdraw_is_bigger_than_250() {
        ATMService atmService = new ATMServiceImpl();
        atmService.withdraw("01003", 300);
    }

    @Test(expected = ATMServiceException.class)
    public void should_fail_if_the_atm_not_have_enough_notes() {
        ATMService atmService = new ATMServiceImpl();
        withdrawAmountFromAccount("01001", atmService, null, 100);
    }

    @Test
    public void should_withdraw_some_amounts() {
        final List<Integer> amountList = Arrays.asList(20, 50, 100, 105, 110, 125, 150, 170, 200, 210, 250);
        final List<Integer> resultSizeList = Arrays.asList(3, 4, 5, 3, 4, 4, 6, 6, 7, 6, 8);
        for (int x = 0; x < amountList.size(); x++) {
            ATMService atmService = new ATMServiceImpl();
            final Map<Notes, Integer> result = withdrawAmountFromAccount("01001", atmService, createNotesMap(100, 100, 100, 100), amountList.get(x));
            assertThat(sumNotesAmounts(result)).isEqualTo(resultSizeList.get(x));
        }
    }

    @Test
    public void should_withdraw_some_amounts_without_five_notes() {
        final List<Integer> amountList = Arrays.asList(20, 50, 100, 110, 150, 170, 200, 210, 250);
        final List<Integer> resultSizeList = Arrays.asList(1, 1, 2, 3, 3, 4, 4, 5, 5);
        for (int x = 0; x < amountList.size(); x++) {
            ATMService atmService = new ATMServiceImpl();
            final Map<Notes, Integer> result = withdrawAmountFromAccount("01001", atmService, createNotesMap(100, 100, 100, 0), amountList.get(x));
            assertThat(sumNotesAmounts(result)).isEqualTo(resultSizeList.get(x));
        }
    }

    @Test
    public void should_withdraw_some_times_from_the_same_atm_from_some_accounts() {
        ATMService atmService = new ATMServiceImpl();
        Map<Notes, Integer> withdrawResult = withdrawAmountFromAccount("01001", atmService, createNotesMap(100, 100, 100, 100), 100);
        assertThat(withdrawResult.toString()).isEqualTo("{FIFTY=1, TWENTY=2, TEN=0, FIVE=2}");
        assertThat(atmService.checkBalance()).isEqualTo("FIFTY - 99, TWENTY - 98, TEN - 100, FIVE - 98");
        withdrawResult = withdrawAmountFromAccount("01002", atmService, null, 20);
        assertThat(withdrawResult.toString()).isEqualTo("{FIFTY=0, TWENTY=0, TEN=1, FIVE=2}");
        assertThat(atmService.checkBalance()).isEqualTo("FIFTY - 99, TWENTY - 98, TEN - 99, FIVE - 96");
        withdrawResult = withdrawAmountFromAccount("01001", atmService, null, 110);
        assertThat(withdrawResult.toString()).isEqualTo("{FIFTY=2, TWENTY=0, TEN=0, FIVE=2}");
        assertThat(atmService.checkBalance()).isEqualTo("FIFTY - 97, TWENTY - 98, TEN - 99, FIVE - 94");
    }

    @Test
    public void should_withdraw_in_a_complex_case() {
        ATMService atmService = new ATMServiceImpl();
        final Map<Notes, Integer> withdrawResult = withdrawAmountFromAccount("01001", atmService, createNotesMap(2, 3, 0, 0), 110);
        assertThat(withdrawResult.toString()).isEqualTo("{FIFTY=1, TWENTY=3, TEN=0, FIVE=0}");
    }

    @Test(expected = ATMServiceException.class)
    public void should_fail_if_the_atm_not_have_the_correct_notes_to_deliver_the_amount() {
        ATMService atmService = new ATMServiceImpl();
        withdrawAmountFromAccount("01001", atmService, createNotesMap(100, 100, 100, 0), 115);
    }

    @Test(expected = ATMServiceException.class)
    public void should_withdraw_until_atm_get_out_of_notes() {
        ATMService atmService = new ATMServiceImpl();
        Map<Notes, Integer> withdrawResult = withdrawAmountFromAccount("01001", atmService, createNotesMap(2, 2, 2, 2), 100);
        assertThat(withdrawResult.toString()).isEqualTo("{FIFTY=1, TWENTY=2, TEN=0, FIVE=2}");
        assertThat(atmService.checkBalance()).isEqualTo("FIFTY - 1, TWENTY - 0, TEN - 2, FIVE - 0");
        withdrawAmountFromAccount("01001", atmService, null, 100);
    }

    private Map<Notes, Integer> withdrawAmountFromAccount(final String accountNumber, ATMService atmService, final Map<Notes, Integer> replenish, final Integer amountToExtract) {
        if (replenish != null) atmService.replenish(replenish);
        return atmService.withdraw(accountNumber, amountToExtract);
    }

    private Integer sumNotesAmounts(Map<Notes, Integer> withdraw) {
        return withdraw.entrySet().stream().map(entry -> entry.getValue()).collect(Collectors.summingInt(Integer::intValue));
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
