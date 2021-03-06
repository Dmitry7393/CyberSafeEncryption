package encryption.com.AES;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Encrypt extends AES implements Runnable {
	private Thread thread;
	private String cipherTextBase64 = "";
	private byte Round[][][];
	private ArrayList<Byte> arrayListBytes = new ArrayList<Byte>();

	private List<Uri> sourceFilesList = new ArrayList<Uri>();
	private List<String> outputPathsList = new ArrayList<String>();
	private long CommonSizeOfFiles = 0;
	private double timeEcryption = 0;
	private static final int NB = 4;
	private int keySize;
	private int Nk;
	private int Nr;
	private Context context;
	private void createRoundKeys(String str_key) {
		Round = new byte[Nr + 1][4][4];
		Round = initRoundKeys(str_key, keySize, NB, Nk, Nr);
	}

	private byte[] Encrypt_block(byte[][] plain_text, String typeEncryption) {
		XOR(plain_text, Round[0]);
		for (int i = 1; i < Nr; i++) {
			SubBytes(plain_text, false);
			ShiftRows(plain_text, false);
			MixColumns(plain_text, false);
			XOR(plain_text, Round[i]);
		}
		// AES Round 10 no Mix columns
		SubBytes(plain_text, false);
		ShiftRows(plain_text, false);
		XOR(plain_text, Round[Nr]);
		
		byte[] ciphertextBytes = new byte[16];
		if (typeEncryption.equals("file")) {
			int k = 0;
			for (int j = 0; j < 4; j++) {
				for (int i = 0; i < 4; i++) {
					ciphertextBytes[k] = plain_text[i][j];
					k++;
				}
			}
		}
		if (typeEncryption.equals("string")) // encrypt text
		{
			for (int j = 0; j < 4; j++) {
				for (int i = 0; i < 4; i++) {
					arrayListBytes.add(plain_text[i][j]);
				}
			}
		}
		return ciphertextBytes;
	}

	public void EncryptText(String str_plain_text) {
		int r;
		cipherTextBase64 = "";
		for (int i = 0; i < str_plain_text.length(); i += 16) {
			r = str_plain_text.length() - i;
			if (r >= 16) {
				Encrypt_block(getBlock16(str_plain_text.substring(i, i + 16)), "string");
			} else {
				Encrypt_block(getBlock16(str_plain_text.substring(i, i + r)), "string");
			}
		}
	}

	public void EncryptGroupsOfFiles(List<Uri> sourceFile, List<String> outputPath) {
		this.sourceFilesList = sourceFile;
		this.outputPathsList = outputPath;
		thread = new Thread(this, "Encryption file1");
		thread.start();
	}
	protected  void showPreviousBytes(byte[] plain_text) {
		for (int j = 0; j < 16; j++) {
			System.out.print(String.format("0x%02X", plain_text[j]));
			System.out.print(" ");
		}
		System.out.println("");
	}
	public void EncryptFile(Uri uri, String pathOutput) throws IOException {

		InputStream is = context.getContentResolver().openInputStream(uri);
		FileOutputStream fos = new FileOutputStream(pathOutput);
		int value;
		InputStream inputStreamImage = context.getResources().openRawResource(
				context.getResources().getIdentifier("imageencryptedfile",
						"raw", context.getPackageName()));
		while ((value = inputStreamImage.read()) != -1) {
			fos.write((byte) value);
		}
		inputStreamImage.close();

		byte block4_4[][];
		byte encryptedBytes[];
		byte[] currentBytes = new byte[16];
		while ((value = is.read(currentBytes)) != -1) {
			block4_4 = getBlock4_4(currentBytes, 16);
			encryptedBytes = Encrypt_block(block4_4, "file");
			fos.write(encryptedBytes, 0, encryptedBytes.length);
			CommonSizeOfFiles += encryptedBytes.length;
			for(int i = 0; i < currentBytes.length; i++) {
				currentBytes[i] = 0;
			}
		}
		fos.close();
		is.close();
	}
	public void EncryptBitmap(byte[] bytesBitmap, String pathOutput) throws IOException  {
		FileOutputStream fos = new FileOutputStream(pathOutput);
		int value;
		InputStream inputStreamImage = context.getResources().openRawResource(
				context.getResources().getIdentifier("imageencryptedfile",
						"raw", context.getPackageName()));
		while ((value = inputStreamImage.read()) != -1) {
			fos.write((byte) value);
		}
		inputStreamImage.close();

		byte block4_4[][];
		byte encryptedBytes[];
		int j = 0;
		int bytesCounter = 0;
		byte[] currentBytes = new byte[16];
		for(int i = 0; i < bytesBitmap.length; i++) {
			currentBytes[j] = bytesBitmap[i];
			j++;
			if(bytesCounter == 15) {
				block4_4 = getBlock4_4(currentBytes, 16);
				encryptedBytes = Encrypt_block(block4_4, "file");
				fos.write(encryptedBytes, 0, encryptedBytes.length);
				bytesCounter = 0;
				j = 0;
				CommonSizeOfFiles += encryptedBytes.length;
				for(int k = 0; k < 16; k++) {
					currentBytes[k] = 0;
				}
			} else {
				bytesCounter++;
			}
		}
		if(bytesCounter != 0) {
			block4_4 = getBlock4_4(currentBytes, 16);
			encryptedBytes = Encrypt_block(block4_4, "file");
			fos.write(encryptedBytes, 0, encryptedBytes.length);
		}
		fos.close();
	}
	public Encrypt(Context current, String key) {
		this.context = current;
		if (key.length() <= 16) {
			keySize = 16;
			Nk = 4;
			Nr = 10;
		}
		if (key.length() > 16 && key.length() <= 24) {
			keySize = 24;
			Nk = 6;
			Nr = 12;
		}
		if (key.length() > 24 && key.length() <= 32) {
			keySize = 32;
			Nk = 8;
			Nr = 14;
		}
		createRoundKeys(key);
		CommonSizeOfFiles = 81980;
	}

	public String getCipherText() {
		byte tempBytes[] = new byte[arrayListBytes.size()];
		for (int i = 0; i < arrayListBytes.size(); i++) {
			tempBytes[i] = arrayListBytes.get(i);
		}
		cipherTextBase64 += ConvertToBase64(tempBytes);
		return cipherTextBase64;
	}

	@Override
	public void run() {
		final long startTime = System.currentTimeMillis();
		for (int i = 0; i < sourceFilesList.size(); i++) {
			if (!Thread.currentThread().isInterrupted()) {
				try {
					System.out.println("thread wo1rks1");
					EncryptFile(sourceFilesList.get(i), outputPathsList.get(i));
				} catch (IOException e) {
				}
			}
		}
		final long endTime = System.currentTimeMillis();
		timeEcryption = (endTime - startTime) / 1000.0;
	}

	public Boolean threadIsAlive() {
		return thread.isAlive();
	}

	public long getCommonSizeOfFiles() {
		return CommonSizeOfFiles;
	}

	public void stopEncryption() {
		thread.interrupt();
	}

	public double getTimeEncryption() {
		return timeEcryption;
	}
}
