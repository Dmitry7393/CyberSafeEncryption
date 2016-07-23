package encryption.com.AES;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Decrypt extends AES implements Runnable {
	private String source_text = "";
	private byte Round[][][];
	private Thread thread;
	private List<Uri> sourceFilesList = new ArrayList<>();
	private List<String> outputPathList = new ArrayList<>();
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

	public String get_text() {
		return source_text;
	}

	private byte[] Decrypt_block(byte[][] block_16, String typeDecryption) {
		byte plain_text[][] = new byte[4][4];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				plain_text[i][j] = block_16[i][j];
			}
		}
		// show(plain_text);
		XOR(plain_text, Round[Nr]);
		ShiftRows(plain_text, true);
		SubBytes(plain_text, true);
		for (int i = Nr - 1; i >= 1; i--) {
			XOR(plain_text, Round[i]);
			MixColumns(plain_text, true);
			ShiftRows(plain_text, true);
			SubBytes(plain_text, true);
		}
		XOR(plain_text, Round[0]);

		// show(plain_text);
		if (typeDecryption.equals("string")) {
			source_text += hex_to_string(plain_text);
		}
		byte decryptedText[] = new byte[16];
		int k = 0;
		for (int j = 0; j < 4; j++) {
			for (int i = 0; i < 4; i++) {
				decryptedText[k] = plain_text[i][j];
				k++;
			}
		}
		return decryptedText;
	}

	public void DecryptText(String str_plain_text) {
		source_text = "";
		byte byteList[] = Base64ToByte(str_plain_text);
		byte[][] block16 = new byte[4][4];
		int r = 0;
		for (int i = 0; i < byteList.length; i += 16) {
			r = byteList.length - i;
			if (r >= 16) {
				block16 = getBlock4_4(byteList, i, i + 16);
				Decrypt_block(block16, "string");
			} else {
				block16 = getBlock4_4(byteList, i, i + r);
				Decrypt_block(block16, "string");
			}
		}
	}

	public void DecryptGroupsOfFiles(List<Uri> sourceFile, List<String> outputPath) {
		sourceFilesList = sourceFile;
		outputPathList = outputPath;
		thread = new Thread(this, "Decryption files");
		thread.start();
	}

	public Decrypt(Context c, String key) {
		context = c;
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
		CommonSizeOfFiles = 0;
	}
	protected  void showPreviousBytes(byte[] plain_text) {
		for (int j = 0; j < 16; j++) {
			System.out.print(String.format("0x%02X", plain_text[j]));
			System.out.print(" ");
		}
		System.out.println("");
	}
	public void DecryptFile(Uri uri, String pathOutput) throws IOException {

		InputStream is = context.getContentResolver().openInputStream(uri);
		FileOutputStream fos = new FileOutputStream(pathOutput);
		int value;
		byte tempBytes[][];
		byte decryptedBytes[];
		byte currentBytes[] = new byte[16];

		long countBytesSkip = is.skip(81980);
		Log.d("SKIP", Long.toString(countBytesSkip));
		while ((value = is.read(currentBytes)) != -1) {
			tempBytes = getBlock4_4(currentBytes, 16);
			decryptedBytes = Decrypt_block(tempBytes, "file");
			fos.write(decryptedBytes, 0, decryptedBytes.length);
			CommonSizeOfFiles += decryptedBytes.length;
			for (int i = 0; i < 16; i++) {
				currentBytes[i] = 0;
			}
		}
		fos.close();
		is.close();
	}

	private byte[][] getBlock4_4(byte[] cipher_code, int index1, int index2) {
		byte[][] tmp = new byte[4][4];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				tmp[i][j] = 0;
			}
		}
		int i1 = 0;
		int j1 = 0;
		for (int i = index1; i < index2; i++) {
			tmp[i1][j1] = cipher_code[i];
			i1++;
			if (i1 == 4) {
				i1 = 0;
				j1++;
			}
		}
		return tmp;
	}

	@Override
	public void run() {
		final long startTime = System.currentTimeMillis();
		for (int i = 0; i < sourceFilesList.size(); i++) {
			if (!Thread.currentThread().isInterrupted()) {
				try {
					DecryptFile(sourceFilesList.get(i), outputPathList.get(i));
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

	public void stopDecryption() {
		thread.interrupt();
	}

	public double getTimeEncryption() {
		return timeEcryption;
	}
}
