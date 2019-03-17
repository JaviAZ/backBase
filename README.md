#Minimise Overdraft application
##Goal
	Save customers money by minimising their overdraft fees
##Background	
	Customer has current account with overdraft
	Customer has savings account
	Fees charged at end of day
	Fees proportional to overdraft used
##Requirements
	Feature must automatically transfer funds from saving to current to minimise overdraft fees
	Take as input CSV (format described in Appendix 1)
	Output a CSV with the same format and content, as well as additional entries with InitiatorType: "SYSTEM"
##Assumptions and constraints
	Accounts start at 0
	Overdraft is unlimited
	Savings can't drop below 0
##Out of Scope
	Don't need to apply overdraft fees/interest rates
	Don't need to persist any data
##Appendix 1
	1 csv per customer
	File contains ledger of transactions for all of a customer's accounts
	Each row is a transaction
	Assume 0 starting balance
	AccountID?: (Integer) The globally unique ID of the account
	AccountType?: (String) An enumerated set of account types
	InitiatorType?: (String) "ACCOUNT-HOLDER" if the customer initiated the transaction,or ?"SYSTEM?"" if we programmatically initiate a transaction.
	DateTime?: (String) ISO-8601 formatted date-time of the transaction
	TransactionValue?: (Signed floating point number) GBP (£) value of the transaction.Positive values are deposits and will increment account balance.  Negative valuesare withdrawals and will decrement account balance
##My assumptions
	Customers only have one savings account and one current account
	Program is suppossed to act between input transactions. Transactions in csv are not a history, but a "real-time" real-life example.
	Fees to be charged are checked by the bank at 23:59:59 every day.
	Transfer between accounts takes 2 seconds to compute without exception.
	All transactions are done in the same timezone
	CSV will always have the right format and therefore there is no need to check