import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public class MinOverDraft{
	/*	GOAL -- save customers money by minimising their overdraft fees
			Customer has current account with overdraft
			Customer has savings account
			Fees charged at end of day
			Fees proportional to overdraft used
			REQUIREMENTS
				Feature must automatically transfer funds from saving to current to minimise overdraft fees
				Take as input CSV (format described in Appendix 1)
				Output a CSV with the same format and content, as well as additional entries with InitiatorType: "SYSTEM"
			ASSUMPTIONS AND CONSTRAINTS
				Accounts start at 0
				Overdraft is unlimited
				Savings can't drop below 0
			NOT
				Don't need to apply overdraft fees/interest rates
				Don't need to persist any data
			APPENDIX 1
				1 csv per customer
				File contains ledger of transactions for all of a customer's accounts
				Each row is a transaction
				Assume 0 starting balance
				AccountID​: (Integer) The globally unique ID of the account
				AccountType​: (String) An enumerated set of account types
				InitiatorType​: (String) "ACCOUNT-HOLDER" if the customer initiated the transaction,or ​"SYSTEM​"" if we 
					programmatically initiate a transaction.
				DateTime​: (String) ISO-8601 formatted date-time of the transaction
				TransactionValue​: (Signed floating point number) GBP (£) value of the transaction.Positive values are deposits
					and	will increment account balance.  Negative valuesare withdrawals and will decrement account balance
			ASSUMPTIONS
				Customers only have one savings account and one current account
				Program is suppossed to act between input transactions. Transactions in csv are not a history, but a
					"real-time" real-life example.
				Fees to be charged are checked by the bank at 23:59:59 every day.
				Transfer between accounts takes 2 seconds to compute without exception.
				All transactions are done in the same timezone
	*/
	private static Customer customer = new Customer();
	private static ArrayList <String []> fileInfo = new ArrayList<>();
	private static String headers;
	//Main method to run the application
	public static void main (String [] args){
		//Validate the file input
		if(args.length != 2 && args.length != 1){
			System.err.println("Need at least input file, please try:\n\njava MinOverDraft \"inputFileName\"\n\nor\n\njava MinOverDraft \"inputFileName\" \"outputFileName\"");
			System.exit(0);
		}
		String inputPath = "../InputFiles/"+ args[0];
		String outputPath;
		fileInput(inputPath);
		//Iterate through transactions
		for(int i = 0; i < fileInfo.size(); i++){
			//Add transaction to customer
			customer.addTransaction(fileInfo.get(i)[1], fileInfo.get(i)[2], fileInfo.get(i)[3], Float.parseFloat(fileInfo.get(i)[4]));
			int initialSec = timeToSeconds(customer.getTransaction(i).getTime());
			i = timepass(customer.getTransaction(i).getDate(), initialSec, i+1);
			//Current time -> 23:59:50 Check if the current account's balance is below 0
			if(customer.getCurrentAccount().getBalance() < 0){
				//Call method to minimise overdraft and add system transactions to ArrayList
				customer.minimiseOverDraft(customer.getTransaction(i).getDate());
				fileInfo.add(i+1, customer.getTransaction(i+1).toString().split(","));
				fileInfo.add(i+2, customer.getTransaction(i+2).toString().split(","));
				i+=2;
			}
		}
		//Check file input to determine the name of the output file
		if(args.length == 1)
			outputPath = "../OutputFiles/"+ args[0].substring(0,args[0].length()-4)+"Out.csv";
		else
			outputPath = "../OutputFiles/"+ args[1];
		//Output ArrayList of transactions into a CSV with the original format
		fileOutput(outputPath);
	}

	//This method simulates time passing throughout the day as the transactions are input all in one go, but in a real-life example, we wouldn't know when the next transaction happens.
	private static int timepass(String date, int initialSec, int i){
		//Check if the current transaction is the last one and return it if so return it
		if(i==fileInfo.size())
			return i;
		//Find at which second of the day the current transaction happens
		int tempTime = timeToSeconds(fileInfo.get(i)[3].substring(11,19));
		String tempDate = fileInfo.get(i)[3].substring(0,10);
		//86390 seconds = 23 hours, 59 minutes and 50 seconds
		for(int sec = initialSec; sec < 86390; sec++){
			//If the current transaction is reached (on the same day)
			if(tempTime == sec && date.equals(tempDate)){
				//Add the transactions
				customer.addTransaction(fileInfo.get(i)[1], fileInfo.get(i)[2], fileInfo.get(i)[3], Float.parseFloat(fileInfo.get(i)[4]));
				i++;
				//Get the next transaction's date and time
				tempTime = timeToSeconds(fileInfo.get(i)[3].substring(11,19));
				tempDate = fileInfo.get(i)[3].substring(0,10);
			}
		}
		//Return the new position to keep iterating through the ArrayList
		return i-1;
	}

	//This method takes as a String a time (in the format described in the Appendix) and converts it from hours:minutes:seconds to seconds
	private static int timeToSeconds(String time){
		int seconds = Integer.parseInt(time.substring(6,8));
		int mins = Integer.parseInt(time.substring(3,5));
		int hours = Integer.parseInt(time.substring(0,2));
		return (((hours*60)+mins)*60+seconds);
	}

	//This method takes as a String a file name (and path if needed) and writes transactions into an ArrayList called fileInfo. It also creates a current and a savings account for the customer
	private static void fileInput(String fileName){
		File file = new File(fileName);
		boolean currFlag = true;
		boolean savFlag = true;
		try {
			Scanner sc = new Scanner(file);
			headers = sc.nextLine();
			int i = 0;
			while(sc.hasNextLine()){
				fileInfo.add(sc.nextLine().split(","));
				if(fileInfo.get(i)[1].equals("CURRENT") && currFlag){
					customer.getCurrentAccount().setAccountID(Integer.parseInt(fileInfo.get(i)[0]));
					currFlag = false;
				}
				if(fileInfo.get(i)[1].equals("SAVINGS") && savFlag){
					customer.getSavingsAccount().setAccountID(Integer.parseInt(fileInfo.get(i)[0]));
					savFlag = false;
				}
				i++;
			}
		}catch (FileNotFoundException e) {
			System.err.println("Input File was not found, please try again (File must have .csv extension).");
			System.exit(0);
		}
	}

	//This method takes as a String a file name (and path if needed) and writes the transactions in fileInfo ArrayList into it. 
	private static void fileOutput(String fileName){
		try{
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(fileName,false)));
			pw.println(headers);
			for(String[] tempLine : fileInfo){
				String lineStr = Arrays.toString(tempLine);
				pw.println(lineStr.substring(1,lineStr.length()-1));
			}
			pw.flush();
			pw.close();
		}catch(IOException e){
			System.out.println(e);
		}
	}

}