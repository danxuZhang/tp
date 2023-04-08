package seedu.dukeofbooks.parser;

import seedu.dukeofbooks.command.BorrowCommand;
import seedu.dukeofbooks.command.CheckBorrowingStatusCommand;
import seedu.dukeofbooks.command.CheckItemAvailabilityCommand;
import seedu.dukeofbooks.command.HistoryCommand;
import seedu.dukeofbooks.command.IncorrectUserCommand;
import seedu.dukeofbooks.command.InventoryCommand;
import seedu.dukeofbooks.command.ListCommand;
import seedu.dukeofbooks.command.LogoutCommand;
import seedu.dukeofbooks.command.RenewCommand;
import seedu.dukeofbooks.command.ReturnCommand;
import seedu.dukeofbooks.command.SearchCommand;
import seedu.dukeofbooks.command.UserCommand;
import seedu.dukeofbooks.command.UserExitCommand;
import seedu.dukeofbooks.common.Messages;
import seedu.dukeofbooks.controller.SearchController;
import seedu.dukeofbooks.data.book.Book;
import seedu.dukeofbooks.data.book.BorrowableItem;
import seedu.dukeofbooks.data.exception.IllegalValueException;
import seedu.dukeofbooks.data.loan.LoanRecords;
import seedu.dukeofbooks.data.person.Person;
import static seedu.dukeofbooks.common.Messages.TITLE_ARG;
import static seedu.dukeofbooks.common.Messages.TOPIC_ARG;
import static seedu.dukeofbooks.common.Messages.AUTHOR_ARG;
import static seedu.dukeofbooks.common.Messages.ACTION_ARG;
import static seedu.dukeofbooks.common.Messages.ISBN_ARG;
import static seedu.dukeofbooks.common.Messages.SPACE_CHAR;
import static seedu.dukeofbooks.common.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

public class UserCommandParser implements IParser {
    private final Person currentUser;
    // todo set loan records
    private final LoanRecords loanRecords;

    public UserCommandParser(Person user, LoanRecords loanRecords) {
        this.currentUser = user;
        this.loanRecords = loanRecords;
    }

    @Override
    public UserCommand parseCommand(String userInput) {
        String[] words = userInput.trim().split(" ", 2); // split the input into command and arguments
        if (words.length == 0) {
            return new IncorrectUserCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT));
        }

        final String commandWord = words[0];
        final String arguments = userInput.replaceFirst(commandWord, "").trim();

        switch (commandWord) {
        case InventoryCommand.COMMAND_WORD:
            return prepareInventoryCommand(arguments);
        case ListCommand.COMMAND_WORD:
            return new ListCommand();
        case LogoutCommand.COMMAND_WORD:
            return new LogoutCommand();
        case RenewCommand.COMMAND_WORD:
            return prepareRenewCommand(arguments);
        case BorrowCommand.COMMAND_WORD:
            return prepareBorrowCommand(arguments);
        case HistoryCommand.COMMAND_WORD:
            return new HistoryCommand(currentUser);
        case ReturnCommand.COMMAND_WORD:
            return prepareReturnCommand(arguments);
        case SearchCommand.COMMAND_WORD:
            return prepareSearchCommand(arguments);
        case CheckItemAvailabilityCommand.COMMAND_WORD:
            return prepareCheckItemAvailabilityCommand(arguments);
        case CheckBorrowingStatusCommand.COMMAND_WORD:
            return prepareCheckBorrowingStatusCommand(arguments);
        case UserExitCommand.COMMAND_WORD:
            return new UserExitCommand();
        default:
            return new IncorrectUserCommand(MESSAGE_INVALID_COMMAND_FORMAT);
        }
    }

    private UserCommand prepareCheckBorrowingStatusCommand(String arguments) {
        String[] parts = arguments.split(SPACE_CHAR);
        int titleIndex = -1;
        for (int i = 0; i < parts.length; ++i) {
            if (parts[i].equals(TITLE_ARG)) {
                titleIndex = i;
                break;
            }
        }
        if (titleIndex != -1) {
            try {
                StringBuilder sb = new StringBuilder();
                for (int i = titleIndex + 1; i < parts.length; ++i) {
                    sb.append(parts[i]).append(" ");
                }
                String title = sb.toString().trim();
                BorrowableItem toCheckStatus = SearchController.searchBookByTitle(title);
                return new CheckBorrowingStatusCommand(toCheckStatus);
            } catch (IllegalValueException e) {
                return new IncorrectUserCommand(MESSAGE_INVALID_COMMAND_FORMAT);
            }
        } else {
            return new IncorrectUserCommand(CheckItemAvailabilityCommand.INCORRECT_SYNTAX);
        }
    }

    private UserCommand prepareCheckItemAvailabilityCommand(String arguments) {
        String[] parts = arguments.split(SPACE_CHAR);
        int titleIndex = -1;
        for (int i = 0; i < parts.length; ++i) {
            if (parts[i].equals(TITLE_ARG)) {
                titleIndex = i;
                break;
            }
        }
        if (titleIndex != -1) {
            try {
                StringBuilder sb = new StringBuilder();
                for (int i = titleIndex + 1; i < parts.length; ++i) {
                    sb.append(parts[i]).append(" ");
                }
                String title = sb.toString().trim();
                BorrowableItem toCheck = SearchController.searchBookByTitle(title);
                return new CheckItemAvailabilityCommand(toCheck);
            } catch (IllegalValueException e) {
                return new IncorrectUserCommand(MESSAGE_INVALID_COMMAND_FORMAT);
            }
        } else {
            return new IncorrectUserCommand(CheckItemAvailabilityCommand.INCORRECT_SYNTAX);
        }
    }

    private UserCommand prepareInventoryCommand(String arguments) {
        String[] parts = arguments.split(" ");
        int titleIndex = -1;
        int topicIndex = -1;
        int authorIndex = -1;
        int actionIndex = -1;
        int isbnIndex = -1;
        for (int i = 0; i < parts.length; ++i) {
            if (parts[i].equals(TITLE_ARG)) {
                titleIndex = i;
            }
            if (parts[i].equals(TOPIC_ARG)) {
                topicIndex = i;
            }
            if (parts[i].equals(AUTHOR_ARG)) {
                authorIndex = i;
            }
            if (parts[i].equals(ACTION_ARG)) {
                actionIndex = i;
            }
            if (parts[i].equals(ISBN_ARG)) {
                isbnIndex = i;
            }
        }

        if (titleIndex == -1 || topicIndex == -1 || authorIndex == -1
                || actionIndex == -1 || isbnIndex == -1) {
            return new IncorrectUserCommand(InventoryCommand.INCORRECT_SYNTAX);
        }

        try {
            String title = getWord(titleIndex, parts);
            String topic = getWord(topicIndex, parts);
            String author = getWord(authorIndex, parts);
            String action = getWord(actionIndex, parts);
            String isbn = getWord(isbnIndex, parts);
            
            Book target = new Book(isbn, title, topic, author);

            if (action.equals(InventoryCommand.ADD_WORD)) {
                return new InventoryCommand(target, InventoryCommand.ADD_WORD);
            } else if (action.equals(InventoryCommand.DELETE_WORD)) {
                return new InventoryCommand(target, InventoryCommand.DELETE_WORD);
            } else {
                throw new IllegalValueException(InventoryCommand.INCORRECT_SYNTAX);
            }
        } catch (IllegalValueException e) {
            return new IncorrectUserCommand(e.getMessage());
        }
    }

    private UserCommand prepareRenewCommand(String arguments) {
        String[] parts = arguments.split(" ");
        int titleIndex = -1;
        for (int i = 0; i < parts.length; ++i) {
            if (parts[i].equals(TITLE_ARG)) {
                titleIndex = i;
                break;
            }
        }
        if (titleIndex == -1) {
            return new IncorrectUserCommand(MESSAGE_INVALID_COMMAND_FORMAT);
        }
        try {
            StringBuilder sb = new StringBuilder();
            for (int i = titleIndex + 1; i < parts.length; ++i) {
                sb.append(parts[i]).append(" ");
            }
            String title = sb.toString().trim();
            BorrowableItem toRenew = SearchController.searchBookByTitle(title);
            return new RenewCommand(loanRecords, currentUser, toRenew);
        } catch (IllegalValueException ive) {
            return new IncorrectUserCommand(MESSAGE_INVALID_COMMAND_FORMAT);
        }
    }

    private UserCommand prepareBorrowCommand(String arguments) {
        String[] parts = arguments.split(" ");
        int titleIndex = -1;
        for (int i = 0; i < parts.length; ++i) {
            if (parts[i].equals("-title")) {
                titleIndex = i;
                break;
            }
        }
        if (titleIndex == -1) {
            return new IncorrectUserCommand(MESSAGE_INVALID_COMMAND_FORMAT);
        }
        try {
            StringBuilder sb = new StringBuilder();
            for (int i = titleIndex + 1; i < parts.length; ++i) {
                sb.append(parts[i]).append(" ");
            }
            String title = sb.toString().trim();
            BorrowableItem toBorrow = SearchController.searchBookByTitle(title);
            return new BorrowCommand(loanRecords, currentUser, toBorrow);
        } catch (IllegalValueException e) {
            return new IncorrectUserCommand(MESSAGE_INVALID_COMMAND_FORMAT);
        }
    }

    private UserCommand prepareReturnCommand(String arguments) {
        String[] parts = arguments.split(" ");
        int titleIndex = -1;
        for (int i = 0; i < parts.length; ++i) {
            if (parts[i].equals("-title")) {
                titleIndex = i;
                break;
            }
        }
        if (titleIndex == -1) {
            return new IncorrectUserCommand(MESSAGE_INVALID_COMMAND_FORMAT);
        }
        try {
            StringBuilder sb = new StringBuilder();
            for (int i = titleIndex + 1; i < parts.length; ++i) {
                sb.append(parts[i]).append(" ");
            }
            String title = sb.toString().trim();
            BorrowableItem toReturn = SearchController.searchBookByTitle(title);
            return new ReturnCommand(loanRecords, currentUser, toReturn);
        } catch (IllegalValueException e) {
            return new IncorrectUserCommand(MESSAGE_INVALID_COMMAND_FORMAT);
        }
    }

    private UserCommand prepareSearchCommand(String arguments) {
        String[] parts = arguments.split(SPACE_CHAR);
        int titleIndex = -1;
        int topicIndex = -1;
        for (int i = 0; i < parts.length; ++i) {
            if (parts[i].equals(TITLE_ARG)) {
                titleIndex = i;
            }
            if (parts[i].equals(TOPIC_ARG)) {
                topicIndex = i;
            }
        }

        if ((titleIndex != -1) && (topicIndex != -1)) {
            String title = getWord(titleIndex, parts);
            String topic = getWord(topicIndex, parts);
            return new SearchCommand(title.concat(Messages.NEW_LINE).concat(topic), SearchCommand.COMBINED_SEARCH);
        } else if (titleIndex != -1) {
            String title = getWord(titleIndex, parts);
            return new SearchCommand(title, SearchCommand.TITLE_SEARCH);
        } else if (topicIndex != -1) {
            String topic = getWord(topicIndex, parts);
            return new SearchCommand(topic, SearchCommand.TOPIC_SEARCH);
        } else {
            return new IncorrectUserCommand(SearchCommand.INCORRECT_SYNTAX);
        }
    }

    private String getWord(int index, String[] parts) {
        StringBuilder sb = new StringBuilder();

        for (int i = index + 1; i < parts.length; ++i) {
            if (checkIfArg(parts[i])) {
                return sb.toString().trim();
            }
            sb.append(parts[i]).append(SPACE_CHAR);
        }
        return sb.toString().trim();
    }

    private boolean checkIfArg(String word) {
        if (word.startsWith("-")) {
            return true;
        }
        return false;
    }
}
