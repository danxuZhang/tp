package seedu.dukeofbooks.command;

import seedu.dukeofbooks.controller.LoanController;
import seedu.dukeofbooks.data.exception.DuplicateActionException;
import seedu.dukeofbooks.data.loan.LoanRecords;
import seedu.dukeofbooks.data.book.BorrowableItem;
import seedu.dukeofbooks.data.user.User;

import java.time.LocalDateTime;

public final class BorrowCommand extends LoanCommand {
    public static final String COMMAND_WORD = "borrow";
    private static final String SUCCESS_MSG = "Borrow is successful.";
    private static final String FAIL_MSG = "This item is not borrowable.";
    private static final String ERROR_MSG = "Cannot borrow: %s";
    private static final String NOT_FOUND_MSG = "Item not found!";
    private final User person;
    private final BorrowableItem item;

    public BorrowCommand(LoanRecords loanRecords, User person, BorrowableItem item) {
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
        if (item.isBorrowed()) {
            return new CommandResult(FAIL_MSG);
        }
        LocalDateTime borrowTime = LocalDateTime.now();
        try {
            LoanController.borrowItem(loanRecords, person, item, borrowTime);
        } catch (DuplicateActionException dae) {
            return new CommandResult(String.format(ERROR_MSG, dae.getMessage()));
        }
        return new CommandResult(SUCCESS_MSG);
    }
}
