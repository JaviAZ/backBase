import java.util.ArrayList;
public class Account{
	private int accountID;
	private String accountType;
	private float balance;
	public Account(String newAccountType){
		accountType = newAccountType;
		balance = 0.00f;
	}
	public Account(int newAccountID, String newAccountType){
		accountID = newAccountID;
		accountType = newAccountType;
		balance = 0.00f;
	}
	public void setAccountID(int newAccountID){
		accountID = newAccountID;
	}
	public int getAccountID(){
		return accountID;
	}
	public String getAccountType(){
		return accountType;
	}
	public String toString(){
		return accountID+","+accountType;
	}
	public void transferFunds(float newTransactionValue){
		balance += newTransactionValue;
	}
	public float getBalance(){
		return balance;
	}
}