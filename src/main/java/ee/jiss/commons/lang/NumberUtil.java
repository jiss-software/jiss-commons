package ee.jiss.commons.lang;

import static ee.jiss.commons.lang.ExceptionUtils.wrap;

public class NumberUtil
{
	public static Integer parseInt(String value) {
		return value == null ? null : wrap(() -> Integer.parseInt(value), null);
	}

	public static Integer parseInt(String value, Integer or) {
		return value == null ? or : wrap(() -> Integer.parseInt(value), or);
	}

	public static Long parseLong(String value) {
		return value == null ? null : wrap(() -> Long.parseLong(value), null);
	}

	public static Long parseLong(String value, Long or) {
		return value == null ? or : wrap(() -> Long.parseLong(value), or);
	}

	public static Float parseFloat(String value) {
		return value == null ? null : wrap(() -> Float.parseFloat(value), null);
	}

	public static Float parseFloat(String value, Float or) {
		return value == null ? or : wrap(() -> Float.parseFloat(value), or);
	}

	public static Double parseDouble(String value) {
		return value == null ? null : wrap(() -> Double.parseDouble(value), null);
	}

	public static Double parseDouble(String value, Double or) {
		return value == null ? or : wrap(() -> Double.parseDouble(value), or);
	}
}
