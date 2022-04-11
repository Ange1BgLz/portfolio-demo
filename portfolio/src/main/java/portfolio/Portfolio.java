/**
 * 
 */
package portfolio;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import com.crazzyghost.alphavantage.AlphaVantage;
import com.crazzyghost.alphavantage.Config;
import com.crazzyghost.alphavantage.parameters.OutputSize;
import com.crazzyghost.alphavantage.timeseries.TimeSeries.DailyRequestProxy;

import lombok.Getter;
import portfolio.data.Stock;
import portfolio.utils.Callback;
import portfolio.utils.OptionsEnum;
import portfolio.utils.Utils;

/**
 * @author Angel
 *
 */
public class Portfolio {
	
	private static Portfolio INSTANCE = null;
	@Getter
	private Scanner sc = new Scanner(System.in);
	private DailyRequestProxy tsDaily;
	private List<Stock> stocks;
	
	@Getter
	private Date currentDate;
	
	public static Portfolio getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new Portfolio();
		}
		return INSTANCE;
	}
	
	private Portfolio() {
		sc.useDelimiter(System.lineSeparator());
		currentDate = new Date();
		final Config config = Config.builder()
		    .key("CFVZEWRC2O9VP4BM")
		    .timeOut(10)
		    .build();
		
		final AlphaVantage api = AlphaVantage.api();
		api.init(config);
		
		// Prepare the api to select a category and parameters by default
		tsDaily = api.timeSeries().daily().outputSize(OutputSize.FULL);
	}
	
	protected void selectStocks(List<Stock> stocks) {
		this.stocks = stocks;
		System.out.println();
		System.out.println();
		System.out.println("Please wait, gathering Stocks information...");
		int stCount = 0;
		final Stock firstStock = stocks.get(stCount);
		getStockInfo(firstStock, postMenuCallback(stCount));
	}
	
	private Callback postMenuCallback(int count) {
		return c -> {
			if (stocks.size() <= count+1) {
				System.out.println("Done!");
				System.out.println();
				showOptions();
			} else {
				getStockInfo(stocks.get(count+1), postMenuCallback(count+1));
			}
		};
	}

	private void getStockInfo(final Stock stock, final Callback contCallback) {
		tsDaily.forSymbol(stock.getSymbol());
		tsDaily.onSuccess(stock.handle(contCallback));
		tsDaily.onFailure(stock.error);
		tsDaily.fetch();
	}
	
	public void showOptions() {
		System.out.println("Please select an option to perform in your Portfolio: ");
		for (final OptionsEnum option : OptionsEnum.values()) {
			System.out.println(option.getId() + ". " + option.getDescription());
		}
		
		int tries = 1;
		OptionsEnum option = null;
		tries: while (tries <= 3) {
			try {
				System.out.print("--> ");
				option = OptionsEnum.findOption(sc.nextInt());
			} catch (Exception e) {
				tries++;
				System.err.println("Invalid option. Try again.");
			} finally {
				sc.nextLine();
				if (Objects.nonNull(option)) {
					System.out.println();
					System.out.println("Option selected: " + option.getShortDesc());
					switch (option) {
					case GETPRICE:
						selectStock();
						break tries;
					case PROFIT:
						final Date startDate = Utils.requestDate("Please type a start date (" + Utils.APIWS_DATE_FORMAT + "): ");
						final Date finalDate = Utils.requestDate("Please type a final date (" + Utils.APIWS_DATE_FORMAT + "): ");
						profit(startDate, finalDate);
						break tries;
					case EXIT:
						System.out.println("Bye!");
						System.exit(1);
						break;
					
					}
				}
			}
		}
		System.exit(2);
	}
	
	private void selectStock() {
		Stock selected = null;
		int tries = 1;
		
		System.out.println("Please type the number of the Stock do you want to select for: ");
		for (int i=0; i<stocks.size(); i++) {
			System.out.println(i+1 + ". - " + stocks.get(i).getSymbol());
		}
		System.out.print("--> ");
		while (tries <= 3) {
			try {
				selected = stocks.get(sc.nextInt()-1);
				break;
			} catch (Exception e) {
				tries++;
				System.err.println("ERROR :: Invalid number. Try again.");
			} finally {
				sc.nextLine();
			}
		}
		if (Objects.nonNull(selected)) {
			selected.getPrice(Utils.requestDate("Please type a date (" + Utils.APIWS_DATE_FORMAT + "): "), Boolean.TRUE);
			showOptions();
		}
		else
			System.exit(2);
	}
	
	/**
	 * Extract close price from startDate and finalDate
	 * Then, calculate difference (profit) including in percentage
	 * @param startDate
	 * @param finalDate
	 */
	private void profit(final Date startDate, final Date finalDate) {
		for (final Stock stock : stocks) {
			final Double firstPrice = stock.getPrice(startDate, Boolean.FALSE);
			final Double finalPrice = stock.getPrice(finalDate, Boolean.FALSE);
			
			if (Objects.nonNull(firstPrice) && Objects.nonNull(finalPrice)) {
				// Calculate amount profit
				final Double profit = finalPrice - firstPrice;
				
				System.out.println();
				System.out.println("PROFIT FOR \"" + stock.getSymbol() +"\" FROM " +
						Utils.convertDateToStr(startDate, Utils.APIWS_DATE_FORMAT) + " TO " + 
						Utils.convertDateToStr(finalDate, Utils.APIWS_DATE_FORMAT) + ":");
				System.out.println("$" + profit);
				System.out.println("It's " + Double.valueOf(profit / firstPrice) + "% percent since the first date.");
				
			} else
				break;
		}
		
		System.out.println();
		System.out.print("Press any key to back to Portfolio menu...");
		sc.nextLine();
		showOptions();
	}

}
