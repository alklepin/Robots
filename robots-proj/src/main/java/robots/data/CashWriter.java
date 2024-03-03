package robots.data;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;

public class CashWriter {
	private String originalName;

	public CashWriter(String filename) {
		originalName = filename;
		System.out.println("\u001B[32m[WRITING]\u001B[0m " + filename);
	}

	private PrintWriter createWriter() throws IllegalArgumentException, IOException {
		try {
			String noJarPath = GetPathWthoutJar.getPath();
			String path = String.format("%s/cash/", noJarPath);
			File dir = new File(path);
			if (!dir.exists()) {
				boolean created = dir.mkdirs();
				if (!created) {
					throw new IOException("Could not create directory " + path);
				}
			}
			File file = new File(String.format("%s/%s", path, originalName));
			return new PrintWriter(file);
		} catch (URISyntaxException e) {
			System.out.println(String.format("\u001B[33m[WARNING]\u001B[0m with %s", originalName));
			throw new IllegalArgumentException("Invalid URI");
		}

	}

	public void write(String massage) throws IOException {
		PrintWriter pw = createWriter();
		pw.write(massage);
		pw.flush();
		pw.close();
	}
}
