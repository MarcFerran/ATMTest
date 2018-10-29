package com.banking.atm;

import com.banking.account.AccountService;
import com.banking.account.AccountServiceImpl;
import com.banking.atm.common.Notes;
import com.banking.atm.exceptions.ATMServiceException;
import com.banking.atm.util.NotesHelper;
import com.banking.util.Validator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static com.banking.atm.common.Notes.*;

/**
 * The purpose of this service is manage the ATM, allowing to withdraw founds from the ATM and in consequence from the bank account
 */
public class ATMServiceImpl implements ATMService {

    private final Logger logger = LogManager.getLogger(ATMServiceImpl.class);

    private AccountService accountService = new AccountServiceImpl();
    private ATM atm = new ATM();

    @Override
    public void replenish(final Map<Notes, Integer> notes) {
        atm.addNotes(notes);
    }

    @Override
    public String checkBalance() {
        return atm.allAvailableNotesPrettyPrint();
    }

    @Override
    public Map<Notes, Integer> withdraw(final String accountNumber, final Integer amount) {
        Validator.checkNotEmpty(accountNumber, "The Account Number cannot be empty");
        Validator.checkNotNull(amount, "Amount cannot be null");

        if(amount % 5 != 0) {
            logger.error(String.format("Withdrawal not divisible by 5"));
            throw new IllegalArgumentException("Withdrawal not divisible by 5");
        }
        if(amount < 20 || amount > 250) {
            logger.error(String.format("Withdrawal must be between 20 and 250"));
            throw new IllegalArgumentException("Withdrawal must be between 20 and 250");
        }

        final List<Map<Notes, Integer>> results = calculateRecursive(amount, atm, null, FIFTY);

        if (results.size() == 0) {
            logger.error(String.format("The ATM cannot deliver this amount"));
            throw new ATMServiceException("The ATM cannot deliver this amount");
        }

        Map<Notes, Integer> finalResult = NotesHelper.getMinWithdrawWith5Note(results);

        if (finalResult == null || finalResult.entrySet().isEmpty()) finalResult = NotesHelper.getMinWithdraw(results);

        accountService.withdrawAmount(accountNumber, BigDecimal.valueOf(amount));
        atm.substractNotes(finalResult);

        return finalResult;
    }

    /**
     * private method that calls recursively to himself to calculate all the possible withdraws from the ATM
     *
     */
    private List<Map<Notes, Integer>> calculateRecursive(final Integer amount, final ATM atm, Map<Notes, Integer> notesSubtracted, final Notes note) {
        List<Map<Notes, Integer>> result = new ArrayList<>();
        if (notesSubtracted == null) notesSubtracted = initializeSubtraction();
        final Integer value = NotesHelper.calculate(notesSubtracted);
        if (value < amount) {
            for (Notes noteAux = note; noteAux != null; noteAux = noteAux.next()) {
                if (atm.getAvailableNotes(noteAux) > notesSubtracted.get(noteAux)) {
                    Map<Notes, Integer> notesSubtractedBackup = new EnumMap<>(notesSubtracted);
                    notesSubtractedBackup.put(noteAux, notesSubtractedBackup.get(noteAux) + 1);

                    result.addAll(calculateRecursive(amount, atm, notesSubtractedBackup, noteAux));
                }
            }
        } else if (amount.equals(value)) {
            result.add(new EnumMap<>(notesSubtracted));
        }
        return result;
    }

    private Map<Notes, Integer> initializeSubtraction() {
        Map<Notes, Integer> notesSubtracted = new EnumMap<>(Notes.class);
        notesSubtracted.put(FIFTY, 0);
        notesSubtracted.put(TWENTY, 0);
        notesSubtracted.put(TEN, 0);
        notesSubtracted.put(FIVE, 0);
        return notesSubtracted;
    }

}
