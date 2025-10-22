package bank;

import java.util.Scanner;

import bank.BankProcess.Operation;

public final class ProcessInputUtils {

	// Single shared Scanner for all methods
	private static final Scanner SCANNER = new Scanner(System.in);

	// Prevent instantiation
	private ProcessInputUtils() {
	}

	// Read operation via menu: returns BankProcess.DEPOSIT or BankProcess.WITHDRAW,
	// or null if exit confirmed
	public static Operation readOperationMenu() {
		while (true) {
			System.out.println("Select operation:");
			System.out.println("1. Deposit");
			System.out.println("2. Withdraw");
			System.out.println("0. Exit");
			System.out.print("Choice: ");

			String input = SCANNER.nextLine().trim();

			switch (input) {
			case "1":
				System.out.println("Deposit selected.");
				return Operation.DEPOSIT;
			case "2":
				System.out.println("Withdraw selected.");
				return Operation.WITHDRAW;
			case "0":
				if (confirmExit()) {
					return null; // confirmed exit
				}
				break;
			default:
				System.out.println("Invalid option. Please try again.");
			}
		}
	}

	// Read a non-empty concept string
	public static String readConcept() {
		while (true) {
			System.out.print("Concept: ");
			String concept = SCANNER.nextLine().trim();
			if (!concept.isEmpty()) {
				return concept;
			}
			System.out.println("Concept cannot be empty.");
		}
	}

	// Read amount as double, repeat until valid
	public static double readAmount() {
		while (true) {
			System.out.print("Amount: ");
			String input = SCANNER.nextLine().trim();
			try {
				return Double.parseDouble(input);
			} catch (NumberFormatException e) {
				System.out.println("Invalid value. Please enter a valid number.");
			}
		}
	}

	// Confirm whether to perform another operation
	public static boolean confirmAnotherOperation() {
		return confirm("Do you want to perform another operation?");
	}

	// Confirm exit (used by menu)
	public static boolean confirmExit() {
		return confirm("Do you really want to exit?");
	}

	private static boolean confirm(String message) {
		System.out.print(message + " (y/n): ");
		String confirm = SCANNER.nextLine().trim().toLowerCase();
		return "y".equals(confirm) || "yes".equals(confirm);
	}
}
