package seedu.dukeofbooks;

import seedu.dukeofbooks.command.AccessCommand;
import seedu.dukeofbooks.command.AccessHelpCommand;
import seedu.dukeofbooks.command.AccessResponse;
import seedu.dukeofbooks.command.CommandResult;
import seedu.dukeofbooks.command.ExitCommand;
import seedu.dukeofbooks.command.LogoutCommand;
import seedu.dukeofbooks.command.UserCommand;
import seedu.dukeofbooks.data.inventory.Inventory;
import seedu.dukeofbooks.data.loan.LoanRecords;
import seedu.dukeofbooks.data.user.User;
import seedu.dukeofbooks.data.user.UserRecords;
import seedu.dukeofbooks.parser.AccessCommandParser;
import seedu.dukeofbooks.parser.UserCommandParser;
import seedu.dukeofbooks.ui.TextUi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Main file to run
 */
public class DukeOfBooks {

    public static final String VERSION = "DukeOfBooks - Version 2.1";
    private LoanRecords loanRecords;
    private UserRecords userRecords;
    private User currentUser;
    private TextUi ui;
    private Inventory inventory = new Inventory();

    public static void main(String[] args) {
        new DukeOfBooks().run();
    }

    public void run() {
        start();
        runCommandLoopUntilSystemExit();
        exit();
    }

    private void start() {
        this.loanRecords = new LoanRecords();
        this.userRecords = new UserRecords();
        this.ui = new TextUi();
        // Add book data into inventory
        try {
            ReadWriteData.readBookData((inventory));
        } catch (FileNotFoundException e) {
            File f = new File(ReadWriteData.BOOK_FILEPATH);
        }
        try {
            ReadWriteData.readUserData(userRecords);
        } catch (FileNotFoundException e) {
            File f = new File(ReadWriteData.USER_FILEPATH);
        }
        ui.showWelcomeMessage(VERSION);
    }

    private void exit() {
        ui.showExitMessage();
        // Write book data into inventory
        try {
            ReadWriteData.writeBookData(inventory);
        } catch (IOException e) {
            System.out.println("Failed to save book data!");
            System.exit(1);
        }
        try {
            ReadWriteData.writeUserData(userRecords);
        } catch (IOException e) {
            System.out.println("Failed to save user data!");
            System.exit(1);
        }
        System.exit(0);
    }

    private void runCommandLoopUntilSystemExit() {
        AccessCommand accessCommand;
        do {
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
            userCommand = new UserCommandParser(currentUser, loanRecords)
                    .parseCommand(userCommandText);
            CommandResult result = executeUserCommand(userCommand);
            ui.showResultToUser(result);
        } while (!LogoutCommand.isLogout(userCommand));
    }

    private User login(AccessCommand accessCommand) {
        if (AccessHelpCommand.isHelp(accessCommand)) {
            ui.showToUser(AccessHelpCommand.getUsage());
            return null;
        }

        try {
            AccessResponse response = accessCommand.execute();
            ui.showToUser(response.message == null ? "" : response.message);
            return response.user;
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
