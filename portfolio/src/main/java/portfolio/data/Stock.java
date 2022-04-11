package portfolio.data;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.crazzyghost.alphavantage.Fetcher.FailureCallback;
import com.crazzyghost.alphavantage.Fetcher.SuccessCallback;
import com.crazzyghost.alphavantage.timeseries.response.StockUnit;
import com.crazzyghost.alphavantage.timeseries.response.TimeSeriesResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import portfolio.utils.Callback;
import portfolio.utils.Utils;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stock {
	
	private String symbol;
	private Double currentValue;
	private Date date;
	private List<StockUnit> units;
	

	public Stock(final String symbol) {
		this.symbol = symbol;
	}
	
	public SuccessCallback<TimeSeriesResponse> handle(final Callback callback) {
		return response -> {
			units = response.getStockUnits();
			currentValue = units.get(0).getClose();
			date = Utils.convertStrToDate(units.get(0).getDate());
			callback.callback(Boolean.TRUE);
		};
	}
	
	public FailureCallback error = e -> {
		System.err.println("Oops! An error ocurred while obtaining stocks information.");
		System.err.println("Please check your internet connection or ensure if you typed the Stocks correctly. Detailed info below:");
		System.err.println(e);
		System.exit(3);
	};
	
	/**
	 * Find the close price for the Stock with given date
	 * If there's no info about the given date, it shows a warning in console to indicate user if it's a valid day.
	 * @param date Search date object
	 * @param showLog Indicates if show found price in console or not.
	 * @return The found price
	 */
	public Double getPrice(final Date date, boolean showLog) {
		final String strDate = Utils.convertDateToStr(date, Utils.APIWS_DATE_FORMAT);
		final StockUnit stockUnit = units.stream()
				.filter(s -> s.getDate().equals(strDate)).findFirst().orElse(null);
		
		if (Objects.nonNull(stockUnit)) {
			if (showLog)
				System.out.println("The price for " + this.symbol + " in " + strDate + " was $" + stockUnit.getClose());
			return stockUnit.getClose();
		}
		
		System.out.println("No price found for " + this.symbol + " with given date " + strDate);
		System.out.println("Please ensure that the given date is a valid business day.");
		
		System.out.println();
		return null;
	}
}