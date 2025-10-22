package bank;

public class BankProcess {
	public enum Operation {
		DEPOSIT, WITHDRAW;

		String sign() {
			return DEPOSIT.equals(this) ? "+" : "-";
		}
	}

	private static final String STATEMENT_FILE = "statement.txt";

	public static void main(String[] args) {
		Operation op;
		
		AccountMonitor monitor =new AccountMonitor(STATEMENT_FILE); 
		
		do {
			op = ProcessInputUtils.readOperationMenu();

			// Implement the main process loop
			if (op == null) {
				break;
			}
			
			// Use ProcessInputUtils to read user input
			String concept = ProcessInputUtils.readConcept();
			double amount = ProcessInputUtils.readAmount();
			
			// Use AccountMonitor to write operations to the statement file
			monitor.writeOperation(op, concept, amount);
			ProcessInputUtils.confirmAnotherOperation();
						
			
		} while (true);
		// Handle exit condition gracefully
		System.out.println("Bank process finished.");
	}
}
