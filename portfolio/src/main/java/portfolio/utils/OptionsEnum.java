package portfolio.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum OptionsEnum {
	
	GETPRICE(1, "Check a price for a Stock with given date", "Get Price"),
	PROFIT(2, "Calculate profit between two dates", "Calculate Profit"),
	EXIT(3, "Exit Portfolio", "Exit"),
	;
	
	@Getter
	private int id;
	@Getter
	private String description;
	@Getter
	private String shortDesc;
	
	public static OptionsEnum findOption(final int id) {
		for (final OptionsEnum option : values()) {
			if (id == option.getId())
				return option;
		}
		return null;
	}

}
