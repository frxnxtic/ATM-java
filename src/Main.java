import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //init scanner
        Scanner scanner = new Scanner(System.in);

        //init bank
        Bank theBank = new Bank("LenaBank");

        //add a user, which also creates a savings account
        User aUser = theBank.addUser("Denys", "Kozlov", "1234");

        //add a checking account for our user
        Account newAccount = new Account("Checking", aUser, theBank);
        aUser.addAccount(newAccount);
        theBank.addAccount(newAccount);

        User curUser;

        while(true){
            //stay in the login prompt until succesful login
            curUser = Main.mainMenuPrompt(theBank, scanner);

            //stay in main menu until user quits
            Main.printUserMenu(curUser, scanner);
        }
    }
    public static User mainMenuPrompt(Bank theBank, Scanner scanner){
        //inits
        String userID;
        String pin;
        User authUser;
        //prompt the user for user ID/PIN combo until a correct one is reached
        do {
            System.out.printf("\n\nWelcome to %s\n\n", theBank.getName());
            System.out.print("Enter user ID: ");
            userID = scanner.nextLine();
            System.out.print("Enter PIN: ");
            pin = scanner.nextLine();

            //try to get the user object corresponding to the Id and pin combo
            authUser = theBank.userLogin(userID, pin);
            if(authUser == null){
                System.out.println("Incorrect user ID/PIN combination. " +
                        "Please try again.");
            }
        } while(authUser == null); // continue looping until successful login
        return authUser;
    }

    public static void printUserMenu(User theUser, Scanner scanner){
        //print a summary of the user's accounts
        theUser.printAccountsSummary();

        //init
        int choice;

        //user menu
        do {
            System.out.printf("Welcome %s, what would you like to do?\n",
                    theUser.getFirstName());
            System.out.println("  1) Show account transaction history");
            System.out.println("  2) Withdrawal");
            System.out.println("  3) Deposit");
            System.out.println("  4) Transfer");
            System.out.println("  5) Quit");
            System.out.println();
            System.out.print("Enter choice: ");
            choice = scanner.nextInt();

            if(choice < 1 || choice > 5){
                System.out.println("Invalid choice. Please choose 1-5");
            }

        } while(choice < 1 || choice > 5);

        //process the choice
        switch (choice){
            case 1:
                Main.showTransHistory(theUser, scanner);
                break;
            case 2:
                Main.withdrawalFunds(theUser, scanner);
                break;
            case 3:
                Main.depositFunds(theUser, scanner);
                break;
            case 4:
                Main.transferFunds(theUser, scanner);
                break;
            case 5:
                scanner.nextLine();
                break;
        }
        if (choice != 5){
            printUserMenu(theUser, scanner);
        }
    }

    public static void showTransHistory(User theUser, Scanner scanner){
        int theAcct;

        //get account whose transaction history to look at
        do {
            System.out.printf("Enter the number (1-%d) of the account \n whose transactions you want to see: ",
                    theUser.numAccounts());
            theAcct = scanner.nextInt() - 1;
            if (theAcct < 0 || theAcct >= theUser.numAccounts()){
                System.out.println("Invalid account. Please try again");
            }
        } while(theAcct < 0 || theAcct >= theUser.numAccounts());

    theUser.printAcctTransHistory(theAcct);
    }

    public static void transferFunds(User theUser, Scanner scanner){
        //inits
        int fromAcct;
        int toAcct;
        double amount;
        double acctBal;

        //get the account to transfer from
        do {
            System.out.printf("Enter the number (1-%d) of the account\n" +
                    "to transfer from: ", theUser.numAccounts());
            fromAcct = scanner.nextInt() - 1;
            if (fromAcct < 0 || fromAcct >= theUser.numAccounts()){
                System.out.println("Invalid account. Please try again.");
            }
        } while (fromAcct < 0 || fromAcct >= theUser.numAccounts());
        acctBal = theUser.getAcctBalance(fromAcct);

        //get the account to transfer to
        do {
            System.out.printf("Enter the number (1-%d) of the account\n" +
                    "to transfer to: ", theUser.numAccounts());
            toAcct = scanner.nextInt() - 1;
            if (toAcct < 0 || toAcct >= theUser.numAccounts()){
                System.out.println("Invalid account. Please try again.");
            }
        } while (toAcct < 0 || toAcct >= theUser.numAccounts());

        //get the amount to transfer
        do {
            System.out.printf("Enter the amount to transfer (max $%.02f): $",
                    acctBal);
            amount = scanner.nextDouble();
            if (amount < 0) {
                System.out.println("Amount must be greater than zero.");
            }
            else if (amount > acctBal) {
                System.out.printf("Amount must not be greater than\n" +
                        "balance of $%.02f.\n", acctBal);
            }
        } while (amount < 0 || amount > acctBal);

        theUser.addAcctTransaction(fromAcct, -1 * amount, String.format(
                "Transfer to account %s", theUser.getAcctUUID(toAcct)));
        theUser.addAcctTransaction(toAcct, amount, String.format(
                "Transfer to account %s", theUser.getAcctUUID(fromAcct)));
    }

    public static void withdrawalFunds(User theUser, Scanner scanner){
        //inits
        int fromAcct;
        double amount;
        double acctBal;
        String memo;
        //get the account to transfer from
        do {
            System.out.printf("Enter the number (1-%d) of the account\n" +
                    "to withdraw from: ", theUser.numAccounts());
            fromAcct = scanner.nextInt() - 1;
            if (fromAcct < 0 || fromAcct >= theUser.numAccounts()){
                System.out.println("Invalid account. Please try again.");
            }
        } while (fromAcct < 0 || fromAcct >= theUser.numAccounts());
        acctBal = theUser.getAcctBalance(fromAcct);

        do {
            System.out.printf("Enter the amount to withdraw (max $%.02f): $",
                    acctBal);
            amount = scanner.nextDouble();
            if (amount < 0) {
                System.out.println("Amount must be greater than zero.");
            }
            else if (amount > acctBal) {
                System.out.printf("Amount must not be greater than\n" +
                        "balance of $%.02f.\n", acctBal);
            }
        } while (amount < 0 || amount > acctBal);

        //gobble up rest of previous input
        scanner.nextLine();

        //get a memo
        System.out.println("Enter a memo: ");
        memo = scanner.nextLine();

        //do the withdrawal
        theUser.addAcctTransaction(fromAcct, -1 * amount, memo);
    }

    public static void depositFunds(User theUser, Scanner scanner){
        //inits
        int toAcct;
        double amount;
        double acctBal;
        String memo;
        //get the account to transfer from
        do {
            System.out.printf("Enter the number (1-%d) of the account\n" +
                    "to deposit in: ", theUser.numAccounts());
            toAcct = scanner.nextInt() - 1;
            if (toAcct < 0 || toAcct >= theUser.numAccounts()){
                System.out.println("Invalid account. Please try again.");
            }
        } while (toAcct < 0 || toAcct >= theUser.numAccounts());
        acctBal = theUser.getAcctBalance(toAcct);

        do {
            System.out.printf("Enter the amount to deposit (max $%.02f): $",
                    acctBal);
            amount = scanner.nextDouble();
            if (amount < 0) {
                System.out.println("Amount must be greater than zero.");
            }
        } while (amount < 0);

        //gobble up rest of previous input
        scanner.nextLine();

        //get a memo
        System.out.println("Enter a memo: ");
        memo = scanner.nextLine();

        //do the withdrawal
        theUser.addAcctTransaction(toAcct, amount, memo);
    }
}