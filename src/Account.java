import java.util.ArrayList;

public class Account {
    private String name; // The name of the account

    private String uuid; // Unique account's ID number

    private User holder; // User object that owns this account.

    private ArrayList<Transaction> transactions; // The list of transactions for this account

    public Account(String name, User holder, Bank theBank){
        //set the account name and holder
        this.name = name;
        this.holder = holder;

        //get new account UUID
        this.uuid = theBank.getNewAccountUUID();

        //initialise transactions
        this.transactions = new ArrayList<Transaction>();
    }

    public String getUUID(){
        return this.uuid;
    }

    public String getSummaryLine(){
        //get the account's balance
        double balance = this.getBalance();

        //format the summary line
        if (balance >= 0){
            return String.format("%s : $%.02f : %s",
                    this.uuid,
                    balance,
                    this.name);
        }
        else {
            return String.format("%s : $(%.02f) : %s",
                    this.uuid,
                    balance,
                    this.name);
        }
    }

    public double getBalance(){
        double balance = 0;
        for(Transaction t : this.transactions){
            balance += t.getAmount();
        }
        return balance;
    }

    public void printTransHistory(){
        System.out.printf("\nTransaction history for account %s\n",
                this.uuid);
        for (int t = this.transactions.size() - 1; t >= 0; t--){
            System.out.println(this.transactions.get(t).getSummaryLine());
        }
        System.out.println();
    }

    public void addTransaction(double amount, String memo){
        //create new transaction object and add it to our list
        Transaction newTrans = new Transaction(amount, this, memo);
        this.transactions.add(newTrans);

    }
}