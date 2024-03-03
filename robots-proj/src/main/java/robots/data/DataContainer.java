package robots.data;

import java.io.IOException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import org.json.JSONException;
import org.json.JSONObject;

public final class DataContainer {
	private static DataContainer instance = null;
	private JSONObject data;

	static final private String PATTERN = "localization/%s.json";
	static final public String CURRENT_LOCALE_FILEPATH = "current.txt";
	private String currentLocale;
	private String rememberedLocale = null;



	private DataContainer() {
		try {
			currentLocale = new CashReader(CURRENT_LOCALE_FILEPATH).getString();
		} catch (IOException e) {
			currentLocale = "en";
			try {
				saveLocale();
			} catch (IOException ee) {
				ee.printStackTrace();
			}
		}

		try {
			String sJson =
					new ReaderFromResouce("localization/" + currentLocale + ".json").getString();
			data = new JSONObject(sJson);
		} catch (IOException e) {
			e.printStackTrace();
			data = new JSONObject("{}");
		}
	}

	public static DataContainer getInstance() {
		if (null == instance) {
			instance = new DataContainer();
		}
		return instance;
	}

	/**
	 * Set newLocale of app. If locale does not implemented then throw exception; Example:
	 * newLoacale = "en"
	 */
	public void changeLocaleTo(String newLocale) throws IllegalArgumentException {
		try {
			String sJson = new ReaderFromResouce(String.format(PATTERN, newLocale)).getString();
			currentLocale = newLocale;
			data = new JSONObject(sJson);
		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalArgumentException(String.format("No such locale as %s", newLocale));
		}
	}

	/**
	 * path separator: "/"; Example: path = "menu/view/title";
	 */
	public String getContentOf(String path) throws NoSuchElementException {
		String[] splited = path.split("/");
		String[] names = Arrays.copyOfRange(splited, 0, splited.length - 1);
		String end = splited[splited.length - 1];

		JSONObject obj = data;
		try {
			for (String name : names) {
				obj = obj.getJSONObject(name);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			throw new NoSuchElementException("path");
		}
		return obj.getString(end);
	}

	public String getContentNoException(String path) {
		try {
			return getContentOf(path);
		} catch (Exception e) {
			e.printStackTrace();
			return "Null";
		}
	}

	private void saveLocale() throws IOException {
		new CashWriter(CURRENT_LOCALE_FILEPATH).write(currentLocale);
	}

	public void rememberLocale(String locale) throws IllegalArgumentException {
		try {
			new ReaderFromResouce(String.format(PATTERN, locale)).getString();
			rememberedLocale = locale;
		} catch (IOException e) {
			throw new IllegalArgumentException("No such locale like " + locale);
		}
	}

	public void saveTheRememberedLocale() throws IOException {
		if (null == rememberedLocale) {
			saveLocale();
		} else {
			new CashWriter(CURRENT_LOCALE_FILEPATH).write(rememberedLocale);
		}
	}
}
