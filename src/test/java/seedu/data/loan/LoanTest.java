package seedu.data.loan;

import org.junit.jupiter.api.Test;
import seedu.dukeofbooks.data.book.Book;
import seedu.dukeofbooks.data.book.Isbn;
import seedu.dukeofbooks.data.book.Title;
import seedu.dukeofbooks.data.book.Topic;
import seedu.dukeofbooks.data.exception.IllegalValueException;
import seedu.dukeofbooks.data.person.Person;
import seedu.dukeofbooks.data.person.Phone;
import seedu.dukeofbooks.data.loan.Loan;
import seedu.dukeofbooks.data.person.PersonName;
import seedu.dukeofbooks.data.user.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoanTest {
    Loan toTest = null;

    private Book createBook() throws IllegalValueException {
        Isbn isbn = new Isbn("123456");
        Title title = new Title("Java Programming");
        Topic topic = new Topic("Coffee");
        Person author = new Person(new PersonName("Starbucks"),
                new Phone(12345678));
        return new Book(isbn, title, topic, author);
    }

    private Loan createLoan() throws IllegalValueException {
        User borrower = new User("Someone", "1234", "name");
        LocalDate today = LocalDate.now();
        return new Loan(createBook(), borrower, today.atStartOfDay(),
                today.plusDays(30).atStartOfDay());
    }

    @Test
    public void testConstructor() {
        assertDoesNotThrow(() -> {
            this.toTest = createLoan();
        });
    }

    @Test
    public void testBorrowedState() {
        assertDoesNotThrow(() -> toTest = createLoan());

        // initially, the book is not returned
        assertFalse(toTest.isReturned());

        // return the book
        assertDoesNotThrow(() -> toTest.setReturned(true));
        assertTrue(toTest.isReturned());

        // borrow the book
        assertDoesNotThrow(() -> toTest.setReturned(false));
        assertFalse(toTest.isReturned());
    }
}
