public class Transaction{
	private Account account;
	private String initiatorType;
	private String dateTime;
	private float transactionValue;
	public Transaction(Account newAccount, String newInitiatorType, String newDateTime, float newTransactionValue){
		account = newAccount;
		initiatorType = newInitiatorType;
		dateTime = newDateTime;
		transactionValue = newTransactionValue;
	}
	public String toString(){
		return account.toString()+","+initiatorType+","+dateTime+","+transactionValue;
	}
	public String getDateTime(){
		return dateTime;
	}
	public String getDate(){
		return dateTime.substring(0,10);
	}
	public String getTime(){
		return dateTime.substring(11,19);
	}
}