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
		// TODO Implement file locking to prevent concurrent write issues

		// create a RandomAccessFile, give the process a channel and lock the channel
		try (RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
				FileChannel fileChannel = raf.getChannel();
				FileLock fileLock = fileChannel.lock()) {

			// TODO Format the entry as: date time | operation sign amount currency |
			// concept
			String dayTime = LocalDateTime.now().format(FORMATTER); // current date and time
			String sign = operation.sign(); // get the sign from the operation
			String amountFormated = String.format("%.2f", amount); // format the amount to 2 decimal places with space
																	// between the amount and the sign
			String entry = String.format("%s | %s%s %s | %s%n", dayTime, sign, amountFormated, CURRENCY, concept);

			// TODO Append the entry to the file

		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Operation saved successfully.");
	}
}
