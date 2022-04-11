package portfolio.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import portfolio.Portfolio;

public class Utils {
	
	public static final String APIWS_DATE_FORMAT = "yyyy-MM-dd";
	
	/**
	 * @param dateStr
	 * @return
	 */
	public static Date convertStrToDate(String dateStr) {

		SimpleDateFormat textFormat = new SimpleDateFormat(APIWS_DATE_FORMAT);
		Date date = null;
		try {
			date = textFormat.parse(dateStr);
		} catch (ParseException ex) {
			System.err.println("ERROR :: Invalid date format. Try again");
		}

		return date;
	}
	
	public static String convertDateToStr(Date date, String format) {
		SimpleDateFormat txtFormat = new SimpleDateFormat(format);
		return txtFormat.format(date);
	}
	
	/**
	 * Reads a Date String in format APIWS_DATE_FORMAT in console
	 * and validate if this is valid over max 3 tries before return it.
	 * @param message String message to show in console
	 * @return Date object
	 */
	public static Date requestDate(final String message) {
		int tries = 1;
		
		while (tries <= 3) {
			System.out.print(message);
			final Date searchDate = convertStrToDate(Portfolio.getInstance().getSc().nextLine());
			if (Objects.nonNull(searchDate)) {
				return searchDate;
			} else
				tries++;
		}
		System.exit(2);
		return null;
	}
}
