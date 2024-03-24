package robots.data;

import java.io.File;
import java.net.URISyntaxException;

public class GetPathWthoutJar {
	static public final String getPath() throws URISyntaxException {
		String path = new File(
				GetPathWthoutJar.class.getProtectionDomain().getCodeSource().getLocation().toURI())
						.getPath();
		return path.replaceAll("[\\\\\\/]robots-proj-.+\\.jar$", "");
	}
}
