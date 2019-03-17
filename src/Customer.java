import java.util.ArrayList;
import java.lang.Math;
import java.text.DecimalFormat;
public class Customer{
	private Account currentAccount;
	private Account savingsAccount;
	private ArrayList <Transaction> transactions = new ArrayList<>();
	public Customer(){
		currentAccount = new Account("CURRENT");
		savingsAccount = new Account("SAVINGS");
	}
	public Account getCurrentAccount(){
		return currentAccount;
	}
	public Account getSavingsAccount(){
		return savingsAccount;
	}
	public int transactionsSize(){
		return transactions.size();
	}
	public void addTransaction(String newAccount, String newInitiatorType, String newDateTime, float newTransactionValue){
		if(newAccount.equals("CURRENT")){
			transactions.add(new Transaction(currentAccount, newInitiatorType, newDateTime, newTransactionValue));
			currentAccount.transferFunds(newTransactionValue);
		}else{
			transactions.add(new Transaction(savingsAccount, newInitiatorType, newDateTime, newTransactionValue));
			savingsAccount.transferFunds(newTransactionValue);
		}
	}
	public String getTransactionToString(int i){
		return transactions.get(i).toString();
	}
	public Transaction getTransaction(int i){
		return transactions.get(i);
	}
	public void minimiseOverDraft(String date){
		DecimalFormat df = new DecimalFormat("#.##");
		float currMoney = Float.valueOf(df.format(currentAccount.getBalance()));
		float saviMoney = Float.valueOf(df.format(savingsAccount.getBalance()));
		String newDateTime = date+"T23:59:52Z";
		if(Math.abs(currMoney) >= saviMoney){
			addTransaction("SAVINGS", "SYSTEM", newDateTime, -saviMoney);
			newDateTime = date+"T23:59:54Z";
			addTransaction("CURRENT", "SYSTEM", newDateTime, saviMoney);
		}else{
			addTransaction("SAVINGS", "SYSTEM", newDateTime, currMoney);
			newDateTime = date+"T23:59:54Z";
			addTransaction("CURRENT", "SYSTEM", newDateTime, -currMoney);
		}
	}
}