package seedu.dukeofbooks.data.loan;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import seedu.dukeofbooks.common.IVerifiable;
import seedu.dukeofbooks.data.book.BorrowableItem;
import seedu.dukeofbooks.data.person.Person;
import seedu.dukeofbooks.data.exception.IllegalDateException;
import seedu.dukeofbooks.data.user.User;

public class Loan implements IVerifiable {
    public static final String LOAN_DATE_IS_AFTER_LOAN_START =
            "Error: loan date after loan start date";
    public static final double OVERDUE_FEE_PER_DAY = 1.0;
    private BorrowableItem item;
    private User borrower;
    private LocalDateTime loanStart;
    private LocalDateTime loanEnd;
    private boolean isReturned;
    

    public Loan(BorrowableItem toBorrow, User borrower,
                LocalDateTime loanStart, LocalDateTime loanEnd) {
        this.item = toBorrow;
        this.borrower = borrower;
        this.loanStart = loanStart;
        this.loanEnd = loanEnd;
    }
    public Person getBorrower() {
        return borrower;
    }
    public BorrowableItem getBorrowedItem() {
        return item;
    }
    public void setBorrowedItem (BorrowableItem newItem) {
        item = newItem;
    }
    public void setBorrower(User borrower) {
        this.borrower = borrower;
    }
    public LocalDateTime getLoanStart() {
        return loanStart;
    }
    public void setLoanStart(LocalDateTime loanStart) {
        this.loanStart = loanStart;
    }
    public LocalDateTime getLoanEnd() {
        return loanEnd;
    }
    public void setLoanEnd(LocalDateTime loanEnd) throws IllegalDateException {
        if (loanEnd.isBefore(loanStart)) {
            // Impossible to return before start date
            // Throw exception
            throw new IllegalDateException(LOAN_DATE_IS_AFTER_LOAN_START);
        }
        this.loanEnd = loanEnd;
    }
    public boolean isReturned() {
        return isReturned;
    }
    public void setReturned(boolean isReturned) {
        this.isReturned = isReturned;
    }

    public double getOverdueFeePerDay() {
        return OVERDUE_FEE_PER_DAY;
    }

    public double calculateOverduePayment(LocalDateTime returnDate) {
        long daysOverdue = ChronoUnit.DAYS.between(loanEnd, returnDate);
        if (daysOverdue <= 0) {
            return 0.0;
        } else {
            return daysOverdue * OVERDUE_FEE_PER_DAY;
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else {
            return other instanceof Loan && this.hasSameData((Loan) other);
        }
    }

    private boolean hasSameData(Loan other) {
        boolean sameBorrower = other.getBorrower().equals(borrower);
        boolean sameItem = other.getBorrowedItem().equals(item);
        boolean sameStart = other.getLoanStart().equals(loanStart);
        boolean sameEnd = other.getLoanEnd().equals(loanEnd);
        boolean sameReturn = other.isReturned() == isReturned;
        return sameBorrower && sameItem && sameStart && sameEnd && sameReturn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, borrower, loanStart, loanEnd);
    }

    @Override
    public String toString() {
        return String.format("Loan\nBorrower: %s, Item: %s, Start date: %s, End date: %s",
                borrower, item.toString(),loanStart,loanEnd);
    }
}
