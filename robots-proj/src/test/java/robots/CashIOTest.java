package robots;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import robots.data.CashReader;
import robots.data.CashWriter;


@TestMethodOrder(OrderAnnotation.class)
public class CashIOTest {
	private String massage = "testing";
	private String filename = "Test.txt";

	@Test
	@Order(1)
	public void canWrite() throws Exception {
		CashWriter cw = new CashWriter(filename);
		cw.write(massage);
	}

	@Test
	@Order(2)
	public void canRead() throws Exception {
		CashReader  cr = new CashReader(filename);
		String actual = cr.getString();
		Assertions.assertTrue(massage.equals(actual));
	}
}
