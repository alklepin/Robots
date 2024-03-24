package robots.data;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Scanner;

public class CashReader {
	private String originalName;

	public CashReader(String filename) {
		this.originalName = filename;
		System.out.println("\u001B[32m[READING]\u001B[0m " + filename);
	}

	private InputStream getInputStream() throws IllegalArgumentException, IOException {
		try {
			String noJarPath = GetPathWthoutJar.getPath();
			String path = String.format("%s/cash/", noJarPath);
			File dir = new File(path);
			if (!dir.exists()) {
				boolean created = dir.mkdirs();
				if (!created) {
					throw new IOException("Could not create directory.");
				}
			}
			FileInputStream file = new FileInputStream(String.format("%s/%s", path, originalName));
			return new BufferedInputStream(file);
		} catch (URISyntaxException e) {
			System.out.println(String.format("\u001B[33m[WARNING]\u001B[0m with %s", originalName));
			throw new IllegalArgumentException("Invalid URI");
		}
	}

	public String getString() throws IOException {
		StringBuilder res = new StringBuilder();
		try (Scanner s = new Scanner(getInputStream())) {
			while (s.hasNextLine()) {
				res.append(s.nextLine());
			}
			return res.toString();
		}
	}
}
