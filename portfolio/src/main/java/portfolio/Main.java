/**
 * 
 */
package portfolio;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import portfolio.data.Stock;
import portfolio.utils.Utils;

/**
 * @author Angel
 *
 */
public class Main {

	final static Scanner sc = new Scanner(System.in);
	
	public static void main(String[] args) {
		System.out.println("*** WELCOME TO PORTFOLIO PROFIT CALCULATOR ***");
		System.out.println("Current date: " + Utils.convertDateToStr(Portfolio.getInstance().getCurrentDate(), Utils.APIWS_DATE_FORMAT));
		System.out.println();
		System.out.println();
		preparePortfolioItems(1);
	}
	
	private static void preparePortfolioItems(int tries) {
		Integer stocks = null;
		System.out.print("Which Stocks quantities do you want to add to Portfolio? Type a number only: ");
		try {
			stocks = sc.nextInt();
			if (stocks < 0) {
				stocks = null;
				throw new NumberFormatException();
			}
		} catch (Exception e) {
			tries++;
			System.err.println("ERROR :: Invalid number. Try again.");
		} finally {
			sc.nextLine();
			if (Objects.isNull(stocks))
				if (tries <= 3)
					preparePortfolioItems(tries);
				else
					System.exit(2);
		}
		System.out.println("Type one by one (ex. \"USDMXN\", \"EURGBP\", \"USDCLP\", \"USDCHF\")");
		
		final List<Stock> list = new ArrayList<Stock>();
		for (int i=1; i<=stocks; i++) {
				System.out.print("--> ");
				final String name = sc.nextLine();
				list.add(new Stock(name.toUpperCase().trim()));
		}
		Portfolio.getInstance().selectStocks(list);
	}

}
