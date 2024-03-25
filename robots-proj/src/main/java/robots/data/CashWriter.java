package robots.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

public class CashWriter {
	private String originalName;
	private String path;

	public CashWriter(String filename) {
		originalName = filename;
		setPath();
		System.out.println("\u001B[32m[WRITING]\u001B[0m " + filename);
	}

	private void setPath() throws IllegalArgumentException {
		try {
			String noJarPath = GetPathWthoutJar.getPath();
			path = String.format("%s/cash/", noJarPath);
		} catch (URISyntaxException e) {
			System.out.println(String.format("\u001B[33m[WARNING]\u001B[0m with %s", originalName));
			throw new IllegalArgumentException("Invalid URI");
		}
	}

	private File getPath() throws IOException {
		File dir = new File(path);
		if (!dir.exists()) {
			boolean created = dir.mkdirs();
			if (!created) {
				throw new IOException("Could not create directory " + path);
			}
		}
		return dir;
	}

	private PrintWriter createWriter() throws IllegalArgumentException, IOException {
		File file = new File(String.format("%s/%s", path, originalName));
		return new PrintWriter(file);
	}

	public void write(String massage) throws IOException {
		PrintWriter pw = createWriter();
		pw.write(massage);
		pw.flush();
		pw.close();
	}

	public void writeObjects(ArrayList<HashMap<String, Object>> objects) throws IOException {
		File dir = getPath();
		String filename = String.format("%s/%s", dir, originalName);
		File file = new File(filename);
		if (!file.exists()) {
			boolean created = file.createNewFile();
			if (!created) {
				throw new IOException("Can't create " + file + " in " + dir);
			}
		}
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(filename);
			ObjectOutputStream oos;
			oos = new ObjectOutputStream(fos);
			oos.writeObject(objects);
			oos.flush();
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
