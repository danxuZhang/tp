package seedu.command;

import org.junit.jupiter.api.Test;
import seedu.dukeofbooks.command.BorrowCommand;
import seedu.dukeofbooks.command.CommandResult;
import seedu.dukeofbooks.data.book.Book;
import seedu.dukeofbooks.data.exception.IllegalValueException;
import seedu.dukeofbooks.data.loan.LoanRecords;
import seedu.dukeofbooks.data.user.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BorrowCommandTest {
    private static final LoanRecords loanRecords = new LoanRecords();
    private static final Book sampleBook = createBook("java coffee");
    private static final User sampleUser = createUser("john");
    private static final String SUCCESS_MSG = "Borrow is successful.";
    private static final String FAIL_MSG = "This item is not borrowable.";

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

    @Test
    public void borrowCommand_success() {
        // prepare data
        loanRecords.clear();
        if (sampleBook.isBorrowed()) {
            sampleBook.returnItem();
        }

        // execution
        BorrowCommand borrowCommand = new BorrowCommand(loanRecords, sampleUser, sampleBook);
        CommandResult result = borrowCommand.execute();

        // check borrow is borrowed
        assertTrue(sampleBook.isBorrowed());
        // check command result
        assertEquals(SUCCESS_MSG, result.feedbackToUser);
    }

    @Test
    public void borrowCommand_fail() {
        // prepare data
        loanRecords.clear();
        if (!sampleBook.isBorrowed()) {
            sampleBook.borrowItem();
        }

        // execution
        BorrowCommand borrowCommand = new BorrowCommand(loanRecords, sampleUser, sampleBook);
        CommandResult result = borrowCommand.execute();

        // check book is still borrowed
        assertTrue(sampleBook.isBorrowed());
        // check command result
        assertEquals(FAIL_MSG, result.feedbackToUser);
    }
}
