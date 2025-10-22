package bank.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class StatementFilter {

	private static final int EXIT_OK = 0;
	private static final int EXIT_READING_INPUT = 99;
	private static final int EXIT_NOT_ENOUGHT_ARGUMENTS = 2;
	private static final int EXIT_INVALID_OPERATOR_FOR_OPERATION = 3;
	private static final int EXIT_INVALID_OPERATOR_FOR_CONCEPT = 4;
	private static final int EXIT_INVALID_OPERATOR_FOR_AMOUNT = 5;
	private static final int EXIT_INVALID_NUMBER_FORMAT_FOR_AMOUNT = 6;

	// Column indexes in statement file
	// private static final int DATE = 0;
	private static final int OPERATION = 1;
	private static final int CONCEPT = 2;
	private static final int AMOUNT = 3;

	// Column names
	public static final String COLUMN_OPERATION = "operation";
	public static final String COLUMN_CONCEPT = "concept";
	public static final String COLUMN_AMOUNT = "amount";

	public static void main(String[] args) {
		if (args.length < 3) {
			printHelp();
			System.exit(EXIT_NOT_ENOUGHT_ARGUMENTS); // Not enough arguments
		}

		String column = args[0].toLowerCase();
		String operator = args[1].toLowerCase();
		String value = args[2];

		int exitCode = runFilter(column, operator, value);
		System.exit(exitCode);
	}

	/**
	 * Executes the filter logic based on column, operator, and value.
	 */
	private static int runFilter(String column, String operator, String value) {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
			switch (column) {
			case COLUMN_OPERATION -> processOperation(reader, operator, value);
			case COLUMN_CONCEPT -> processConcept(reader, operator, value);
			case COLUMN_AMOUNT -> processAmount(reader, operator, value);
			default -> {
				printHelp();
				return 1;
			}
			}
		} catch (IOException e) {
			System.err.println("Error reading input: " + e.getMessage());
			return EXIT_READING_INPUT;
		}
		return EXIT_OK;
	}

	private static void processOperation(BufferedReader reader, String operator, String value) throws IOException {
		String line;
		while ((line = reader.readLine()) != null) {
			if (filterOperation(line.toLowerCase(), operator, value.toLowerCase())) {
				System.out.println(line);
			}
		}
	}

	private static void processConcept(BufferedReader reader, String operator, String value) throws IOException {
		String line;
		while ((line = reader.readLine()) != null) {
			if (filterConcept(line.toLowerCase(), operator, value.toLowerCase())) {
				System.out.println(line);
			}
		}
	}

	private static void processAmount(BufferedReader reader, String operator, String value) throws IOException {
		String line;
		while ((line = reader.readLine()) != null) {
			if (filterAmount(line.toLowerCase(), operator, value)) {
				System.out.println(line);
			}
		}
	}

	// --- Extraction helpers ---
	private static String getOperation(String line) {
		String[] parts = line.split("\\|");
		return parts.length > OPERATION ? parts[OPERATION].trim() : "";
	}

	private static String getConcept(String line) {
		String[] parts = line.split("\\|");
		return parts.length > CONCEPT ? parts[CONCEPT].trim() : "";
	}

	private static double getAmount(String line) {
		String[] parts = line.split("\\|");
		if (parts.length <= AMOUNT) {
			return 0.0;
		}
		String raw = parts[AMOUNT].trim().replace("€", "").replace("+", "").replace("-", "").replace(",", ".");
		try {
			return Double.parseDouble(raw);
		} catch (NumberFormatException e) {
			return 0.0;
		}
	}

	// --- Filters ---
	private static boolean filterOperation(String line, String operator, String value) {
		String operation = getOperation(line).toLowerCase();
		return switch (operator) {
		case "-eq" -> operation.equals(value);
		case "-ne" -> !operation.equals(value);
		default -> {
			System.err.println("Invalid operator for operation: " + operator);
			printHelp();
			System.exit(EXIT_INVALID_OPERATOR_FOR_OPERATION); // Invalid operator for operation
			yield false;
		}
		};
	}

	private static boolean filterConcept(String line, String operator, String value) {
		String concept = getConcept(line).toLowerCase();
		return switch (operator) {
		case "-eq" -> concept.contains(value);
		case "-ne" -> !concept.contains(value);
		default -> {
			System.err.println("Invalid operator for concept: " + operator);
			printHelp();
			System.exit(EXIT_INVALID_OPERATOR_FOR_CONCEPT); // Invalid operator for concept
			yield false;
		}
		};
	}

	private static boolean filterAmount(String line, String operator, String value) {
		double amount = getAmount(line);
		double target;
		try {
			target = Double.parseDouble(value);
		} catch (NumberFormatException e) {
			System.err.println("Invalid number: " + value);
			System.exit(EXIT_INVALID_NUMBER_FORMAT_FOR_AMOUNT); // Invalid number format for amount
			return false;
		}

		return switch (operator) {
		case "-lt" -> amount < target;
		case "-le" -> amount <= target;
		case "-eq" -> amount == target;
		case "-ge" -> amount >= target;
		case "-gt" -> amount > target;
		case "-ne" -> amount != target;
		default -> {
			System.err.println("Invalid operator for amount: " + operator);
			printHelp();
			System.exit(EXIT_INVALID_OPERATOR_FOR_AMOUNT); // Invalid operator for amount
			yield false;
		}
		};
	}

	// --- Help ---
	private static void printHelp() {
		System.out.println("Usage: java StatementFilter <column> <operator> <value>");
		System.out.println("Columns:");
		System.out.println("  operation <operator> <text>");
		System.out.println("    Operators:");
		System.out.println("      -eq   equal");
		System.out.println("      -ne   not equal");
		System.out.println("  concept <operator> <text>");
		System.out.println("    Operators:");
		System.out.println("      -eq   contains text");
		System.out.println("      -ne   does not contain text");
		System.out.println("  amount <operator> <number>");
		System.out.println("    Operators:");
		System.out.println("      -lt   less than");
		System.out.println("      -le   less or equal");
		System.out.println("      -eq   equal");
		System.out.println("      -ge   greater or equal");
		System.out.println("      -gt   greater than");
		System.out.println("      -ne   not equal");
	}
}
