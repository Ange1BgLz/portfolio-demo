package portfolio;

import com.crazzyghost.alphavantage.AlphaVantage;
import com.crazzyghost.alphavantage.Config;
import com.crazzyghost.alphavantage.Fetcher.FailureCallback;
import com.crazzyghost.alphavantage.Fetcher.SuccessCallback;
import com.crazzyghost.alphavantage.timeseries.response.TimeSeriesResponse;

public class MainTest {

	public static void main(String[] args) {

		final Config cfg = Config.builder()
		    .key("CFVZEWRC2O9VP4BM")
		    .timeOut(10)
		    .build();
		
		AlphaVantage.api().init(cfg);
		
		AlphaVantage.api()
		    .timeSeries()
		    .daily()
		    .forSymbol("USDMXN")
			.onSuccess(handle)
		    .onFailure(error)
		    .fetch();
	}
	
	private static SuccessCallback<TimeSeriesResponse> handle = response -> {
		System.out.println(response.getStockUnits());
	};
	
	private static FailureCallback error = e -> {
		System.out.println(e);
	};

}
