package seedu.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.dukeofbooks.command.CommandResult;
import seedu.dukeofbooks.command.RenewCommand;
import seedu.dukeofbooks.controller.LoanController;
import seedu.dukeofbooks.data.book.Book;
import seedu.dukeofbooks.data.exception.DuplicateActionException;
import seedu.dukeofbooks.data.exception.IllegalValueException;
import seedu.dukeofbooks.data.exception.LoanRecordNotFoundException;
import seedu.dukeofbooks.data.exception.PaymentUnsuccessfulException;
import seedu.dukeofbooks.data.loan.LoanRecords;
import seedu.dukeofbooks.data.user.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RenewCommandTest {
    private static final LoanRecords loanRecords = new LoanRecords();
    private static final Book sampleBook = createBook("java coffee");
    private static final User sampleUser = createUser("john");
    private static final User randomUser = createUser("Toe");
    private static final String SUCCESS_MSG = "Item has been renewed!";
    private static final String FAIL_MSG = "This book is not borrowed!";
    private static final String ERROR_MSG_F = "Cannot renew: %s";
    private static final String EXCEPTION_MSG = "Cannot find an active loan!";
    private static final int DEFAULT_RENEW_DAYS = 30;

    private static Book createBook(String title) {
        try {
            return new Book("isbn", title, "a topic", "an author");
        } catch (IllegalValueException ive) {
            throw new RuntimeException();
        }
    }

    private static User createUser(String username) {
        try {
            return new User(username, "123456", "name");
        } catch (IllegalValueException ive) {
            throw new RuntimeException(ive.getMessage());
        }
    }

    @BeforeEach
    public void prepareData() {
        loanRecords.clear();
        if (sampleBook.isBorrowed()) {
            sampleBook.returnItem();
        }
        LocalDateTime now = LocalDateTime.now();
        try {
            LoanController.borrowItem(loanRecords, sampleUser, sampleBook, now);
        } catch (DuplicateActionException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void renewCommand_success() {
        assertTrue(sampleBook.isBorrowed());
        assertFalse(loanRecords.get(0).isReturned());

        RenewCommand command = new RenewCommand(loanRecords, sampleUser, sampleBook);
        CommandResult result = command.execute();

        assertTrue(sampleBook.isBorrowed());
        assertFalse(loanRecords.get(0).isReturned());
        LocalDate now = LocalDate.now();
        LocalDate newDue = LocalDate.from(loanRecords.get(0).getLoanEnd());
        assertEquals(now.plusDays(DEFAULT_RENEW_DAYS), newDue);
        assertEquals(SUCCESS_MSG, result.feedbackToUser);
    }

    @Test
    public void renewCommand_fail() throws PaymentUnsuccessfulException {
        assertTrue(sampleBook.isBorrowed());
        assertFalse(loanRecords.get(0).isReturned());
        try {
            LoanController.returnItem(loanRecords, sampleUser, sampleBook);
        } catch (LoanRecordNotFoundException e) {
            e.printStackTrace();
        }

        RenewCommand command = new RenewCommand(loanRecords, sampleUser, sampleBook);
        CommandResult result = command.execute();

        assertFalse(sampleBook.isBorrowed());
        assertTrue(loanRecords.get(0).isReturned());
        assertEquals(FAIL_MSG, result.feedbackToUser);
    }

    @Test
    public void renewCommand_error() {
        RenewCommand command = new RenewCommand(loanRecords, randomUser, sampleBook);
        CommandResult result = command.execute();

        assertEquals(String.format(ERROR_MSG_F, EXCEPTION_MSG), result.feedbackToUser);
    }
}
