package seedu.dukeofbooks.controller;

import seedu.dukeofbooks.data.book.BorrowableItem;
import seedu.dukeofbooks.data.exception.DuplicateActionException;
import seedu.dukeofbooks.data.exception.IllegalDateException;
import seedu.dukeofbooks.data.exception.LoanRecordNotFoundException;
import seedu.dukeofbooks.data.exception.PaymentUnsuccessfulException;
import seedu.dukeofbooks.data.loan.Loan;
import seedu.dukeofbooks.data.loan.LoanRecords;
import seedu.dukeofbooks.data.user.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class LoanController {
    static final DateTimeFormatter CUSTOM_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String AVAILABLE_STATUS_FORMAT = "Status: Not borrowed";
    private static final String BORROWED_STATUS_FORMAT = "Status: Borrowed (borrower: %s, due: %s)";
    private static final String OVERDUE_STATUS_FORMAT = "Status: Overdue (borrower: %s, due: %s)";

    private static final String AVAILABLE_STATUS_STRING = "Status: Not borrowed";
    private static final String BORROWED_STATUS_STRING = "Status: Borrowed";

    private static final int DEFAULT_RENEW_DAYS = 30;
    
    public static String checkBorrowingStatus(BorrowableItem item) {
        if (!item.isBorrowed()) {
            return AVAILABLE_STATUS_FORMAT;
        } else {
            Loan loan = LoanRecords.getLastActiveLoan(item);
            String strDate = loan.getLoanEnd().format(CUSTOM_FORMATTER);
            int daysOverdue = (int) ChronoUnit.DAYS.between(loan.getLoanEnd(), LocalDateTime.now());
            if (daysOverdue <= 0) {
                return String.format(BORROWED_STATUS_FORMAT, loan.getBorrower().getName().toString(), strDate);
            } else {
                return String.format(OVERDUE_STATUS_FORMAT, loan.getBorrower().getName().toString(), strDate);
            }
        }
    }

    public static String checkItemAvailability(BorrowableItem item) {
        if (!item.isBorrowed()) {
            return AVAILABLE_STATUS_STRING;
        } else {
            return BORROWED_STATUS_STRING;
        }
    }

    public static void borrowItem(LoanRecords loanRecords, User borrower,
            BorrowableItem toBorrow, LocalDateTime borrowTime)
            throws DuplicateActionException {
        if (toBorrow.isBorrowed()) {
            throw new DuplicateActionException("Book is not available!");
        }
        LocalDateTime dueTime = borrowTime.plusDays(DEFAULT_RENEW_DAYS);
        Loan newLoan = new Loan(toBorrow, borrower, borrowTime, dueTime);

        loanRecords.add(newLoan);
        toBorrow.borrowItem();
        borrower.addLoan(newLoan);
    }

    public static void returnItem(LoanRecords loanRecords, User borrower, BorrowableItem toReturn)
            throws LoanRecordNotFoundException, PaymentUnsuccessfulException {
        if (!toReturn.isBorrowed()) {
            throw new LoanRecordNotFoundException("Book is not borrowed!");
        }

        Loan loan = loanRecords.getLastActiveLoan(borrower);
        if (loan == null) {
            throw new LoanRecordNotFoundException("Cannot find an active loan!");
        }

        double dueAmount = loan.calculateOverduePayment(LocalDateTime.now());
        if (dueAmount > 0.0) {
            PaymentController.makePayment(toReturn, dueAmount);
        }

        loan.setReturned(true);
        toReturn.returnItem();
    }

    public static void renewItem(LoanRecords loanRecords, User person,
            BorrowableItem toRenew)
            throws LoanRecordNotFoundException {
        LocalDateTime newDue = LocalDateTime.now().plusDays(DEFAULT_RENEW_DAYS);
        try {
            renewItem(loanRecords, person, toRenew, newDue);
        } catch (IllegalDateException ide) {
            assert false;
        }
    }

    public static void renewItem(LoanRecords loanRecords, User person,
            BorrowableItem toRenew, LocalDateTime newDue)
            throws IllegalDateException, LoanRecordNotFoundException {
        Loan loan = loanRecords.getLastActiveLoan(person, toRenew);
        if (loan == null) {
            throw new LoanRecordNotFoundException("Cannot find an active loan!");
        }
        if (!loan.isReturned()) {
            loan.setLoanEnd(newDue);
        }

    }
}
