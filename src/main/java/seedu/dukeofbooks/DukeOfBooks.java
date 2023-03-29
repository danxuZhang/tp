package seedu.dukeofbooks;

import seedu.dukeofbooks.command.*;
import seedu.dukeofbooks.controller.AccessController;
import seedu.dukeofbooks.controller.InventoryController;
import seedu.dukeofbooks.controller.SearchController;
import seedu.dukeofbooks.data.book.Book;
import seedu.dukeofbooks.data.book.Isbn;
import seedu.dukeofbooks.data.book.Title;
import seedu.dukeofbooks.data.book.Topic;
import seedu.dukeofbooks.data.exception.IllegalOperationException;
import seedu.dukeofbooks.data.exception.IllegalValueException;
import seedu.dukeofbooks.data.inventory.Inventory;
import seedu.dukeofbooks.data.loan.LoanRecords;
import seedu.dukeofbooks.data.person.Person;
import seedu.dukeofbooks.data.person.PersonName;
import seedu.dukeofbooks.data.person.Phone;
import seedu.dukeofbooks.data.user.User;
import seedu.dukeofbooks.data.user.UserRecords;
import seedu.dukeofbooks.parser.AccessCommandParser;
import seedu.dukeofbooks.parser.UserCommandParser;
import seedu.dukeofbooks.ui.TextUi;

/**
 * Main file to run
 */
public class DukeOfBooks {

    public static final String VERSION = "DukeOfBooks - Version 1.0";
    private LoanRecords allLoanRecords;
    private UserRecords userRecords;
    private SearchController searchController = new SearchController();
    private User currentUser;
    private TextUi ui;

    public static void main(String[] args) {
        // System.out.println("Hello, world!");

        new DukeOfBooks().run();
    }

    public void run() {
        start();
        runCommandLoopUntilSystemExit();
        exit();
    }

    private void start() {
        this.allLoanRecords = new LoanRecords();
        this.userRecords = new UserRecords();
        try {
            Isbn isbn = new Isbn("testIsbn");
            Title title = new Title("testTitle");
            Topic topic = new Topic("testTopic");
            PersonName authorName = new PersonName("Author");
            Phone authorPhone = new Phone(87654321);
            Person author = new Person(authorName, authorPhone);
            Book book = new Book(isbn, title, topic, author);
            Inventory inventory = new Inventory();
            InventoryController.setData(inventory);
            InventoryController.addBook(book);
            SearchController.setData(inventory);
        } catch (IllegalValueException | IllegalOperationException ive) {
            ive.printStackTrace();
        }
        this.ui = new TextUi();
        ui.showWelcomeMessage(VERSION);
    }

    private void exit() {
        ui.showExitMessage();
        System.exit(0);
    }

    private void runCommandLoopUntilSystemExit() {
        AccessCommand accessCommand;
        do{
            ui.showLoginMessage();
            String commandText = ui.getUserCommand();
            accessCommand = new AccessCommandParser(userRecords).parseCommand(commandText);

            currentUser = login(accessCommand);
            if (currentUser != null) {
                ui.showGreetingMessage(currentUser.getName().toString());
                runCommandLoopUntilUserLogout(currentUser);
            }
        } while (!ExitCommand.isExit(accessCommand));
    }

    private void runCommandLoopUntilUserLogout(User user) {
        UserCommand userCommand;
        do {
            String userCommandText = ui.getUserCommand();
            userCommand = new UserCommandParser(currentUser, allLoanRecords, searchController)
                    .parseCommand(userCommandText);
            CommandResult result = executeUserCommand(userCommand);
            ui.showResultToUser(result);
        } while (!LogoutCommand.isLogout(userCommand));
    }

    private User login(AccessCommand accessCommand) {
        try {
            return accessCommand.execute();
        } catch (IllegalValueException ive) {
            ui.showToUser(ive.getMessage());
            return null;
        } catch (Exception e) {
            ui.showToUser(e.getMessage());
            throw new RuntimeException();
        }
    }

    private CommandResult executeUserCommand(UserCommand command) {
        try {
            // command.setData(#placeholder);
            return command.execute();
        } catch (Exception e) {
            ui.showToUser(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
