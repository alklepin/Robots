package robots.data;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.NoSuchFileException;
import java.security.CodeSource;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ReaderFromResouce {
	private String originalName;

	public ReaderFromResouce(String name) {
		this.originalName = name;
		System.out.println("\u001B[32m[READING]\u001B[0m " + name);
	}

	private InputStream getInputStream() throws NoSuchFileException {
		String name = String.format("/%s", originalName);
		InputStream is = ReaderFromResouce.class.getResourceAsStream(name);
		if (is == null) {
			System.out.println(String.format("\u001B[33m[WARNING]\u001B[0m with %s", originalName));
			throw new NoSuchFileException(name);
		}
		return is;
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

	static public Set<String> getAllFiles(String path) throws IOException {
		Set<String> files = new HashSet<String>();
		CodeSource src = ReaderFromResouce.class.getProtectionDomain().getCodeSource();
		if (null != src) {
			URL jar = src.getLocation();
			ZipInputStream zip = new ZipInputStream(jar.openStream());
			ZipEntry e;
			while (null != (e = zip.getNextEntry())) {
				String name = e.getName();
				if (name.matches(String.format("%s.+", path))) {
					files.add(name);
				}
			}
		}
		return files;
	}
}
