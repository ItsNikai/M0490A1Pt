package bank;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import bank.BankProcess.Operation;

public class AccountMonitor {
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	private static final String CURRENCY = "EUR";

	private String fileName;

	public AccountMonitor(String fileName) {
		this.fileName = fileName;
	}

	// Write operation to the account statement
	public void writeOperation(Operation operation, String concept, double amount) {
		// Implement file locking to prevent concurrent write issues

		// exclusive blocking lock on the file to prevent concurrent writes
		try (RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
				FileChannel fileChannel = raf.getChannel();
				FileLock fileLock = fileChannel.lock()) {

			// Move to the end of the file to append
			raf.seek(raf.length());

			// Format the entry as: date time | operation sign amount currency |
			// concept
			String dayTime = LocalDateTime.now().format(FORMATTER); // current date and time
			String sign = operation.sign(); // get the sign from the operation
			String amountFormated = String.format("%.2f", amount); // format the amount to 2 decimal
			String actionText = operation.name().toUpperCase();
			String entry = String.format("%s | %s | %s | %s%s", dayTime, actionText, concept, sign,
					amountFormated + " " + CURRENCY);

			// Append the entry to the file
			raf.writeBytes(entry + System.lineSeparator());

			System.out.println(entry); // print the entry to the console

		} catch (IOException e) {
			System.err.println("Error en escriure al fitxer: " + e.getMessage());
		}

		System.out.println("Operation saved successfully.");
	}
}
