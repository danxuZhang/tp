package seedu.data.book;

import org.junit.jupiter.api.Test;

import seedu.dukeofbooks.data.book.Book;
import seedu.dukeofbooks.data.book.BorrowHistory;
import seedu.dukeofbooks.data.book.Title;
import seedu.dukeofbooks.data.book.Isbn;
import seedu.dukeofbooks.data.book.Topic;
import seedu.dukeofbooks.data.exception.IllegalValueException;
import seedu.dukeofbooks.data.loan.Loan;
import seedu.dukeofbooks.data.person.Person;
import seedu.dukeofbooks.data.person.PersonName;
import seedu.dukeofbooks.data.person.Phone;


class BorrowHistoryTest {

    Person borrower;
    Book book;
    Loan loan;

    @Test
    public void createBorrowerSuccess() throws IllegalValueException {
        PersonName borrowerName = new PersonName("Borrower");
        Phone borrowerPhone = new Phone(12345678);
        borrower = new Person(borrowerName, borrowerPhone);
    }

    @Test
    public void createBorrowableItemSuccess() throws IllegalValueException {
        // Create Book in this test
        Isbn isbn = new Isbn("testIsbn");
        Title title = new Title("testTitle");
        Topic topic = new Topic("testTopic");
        PersonName authorName = new PersonName("Author");
        Phone authorPhone = new Phone(87654321);
        Person author = new Person(authorName, authorPhone);
        book = new Book(isbn, title, topic, author);
    }

    @Test
    public void createLoanSuccess() {
        // loan = new Loan(book, borrower, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
    }

    @Test
    String checkHistory() {
        borrower.addLoan(loan);
        assert BorrowHistory.checkHistory(borrower) != null;
        return BorrowHistory.checkHistory(borrower);
    }
}
