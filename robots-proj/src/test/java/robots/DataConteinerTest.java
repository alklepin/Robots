package robots;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import robots.data.CashReader;
import robots.data.DataContainer;

@TestMethodOrder(OrderAnnotation.class)
public class DataConteinerTest {
	@Test
	@Order(1)
	public void canReadJson() throws Exception {
		DataContainer dc = DataContainer.getInstance();
		dc.changeLocaleTo("ru");
		String expected = "Тесты";
		String actual = dc.getContentOf("menu/test/title");
		assert (expected.equals(actual));
	}

	@Test
	@Order(2)
	public void canSaveLocale() throws Exception {
		DataContainer dc = DataContainer.getInstance();
		dc.saveTheRememberedLocale();
		String expected = "ru";
		String actual = new CashReader(DataContainer.CURRENT_LOCALE_FILEPATH).getString();
		Assertions.assertTrue(expected.equals(actual));
	}

}
