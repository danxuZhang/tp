package seedu.dukeofbooks.command;

import seedu.dukeofbooks.controller.LoanController;
import seedu.dukeofbooks.data.exception.LoanRecordNotFoundException;
import seedu.dukeofbooks.data.exception.PaymentUnsuccessfulException;
import seedu.dukeofbooks.data.loan.LoanRecords;
import seedu.dukeofbooks.data.book.BorrowableItem;
import seedu.dukeofbooks.data.user.User;

public final class ReturnCommand extends LoanCommand {
    public static final String COMMAND_WORD = "return";
    private static final String SUCCESS_MSG = "Item has been returned!";
    private static final String FAIL_MSG = "Item is not borrowed!";
    private static final String ERROR_MSG_F = "Cannot return item: %s";
    private static final String NOT_FOUND_MSG = "Item not found!";
    private final User person;
    private final BorrowableItem item;
    
    public ReturnCommand(LoanRecords loanRecords, User person, BorrowableItem item) {
        super(loanRecords);
        this.person = person;
        this.item = item;
    }

    @Override
    public CommandResult execute() {
        assert person != null;
        if (item == null) {
            return new CommandResult(NOT_FOUND_MSG);
        }
        if (!item.isBorrowed()) {
            return new CommandResult(FAIL_MSG);
        }
        try {
            LoanController.returnItem(loanRecords, person, item);
        } catch (LoanRecordNotFoundException | PaymentUnsuccessfulException e) {
            return new CommandResult(String.format(ERROR_MSG_F, e.getMessage()));
        }
        return new CommandResult(SUCCESS_MSG);
    }
}
