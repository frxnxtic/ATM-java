import java.util.Date;

public class Transaction {
    private double amount; //The amount of this transaction

    private Date timestamp; //The time and day of this transaction

    private String memo; //A memo for this transaction

    private Account inAccount; //The account in which transaction was performed

    public Transaction(double amount, Account inAccount){
        this.amount = amount;
        this.inAccount = inAccount;
        this.timestamp = new Date();
        this.memo = "";
    }

    public Transaction(double amount, Account inAccount, String memo){
        //call the two-arg constructor first
        this(amount, inAccount);

        //set the memo
        this.memo = memo;
    }



    public double getAmount(){
        return this.amount;
    }

    public String getSummaryLine(){
        if (this.amount >= 0){
            return String.format("%s : $%.02f : %s",
                    this.timestamp.toString(),
                    this.amount, this.memo);
        }
        else {
            return String.format("%s : $(%.02f) : %s",
                    this.timestamp.toString(),
                    -this.amount, this.memo);
        }
    }

}
