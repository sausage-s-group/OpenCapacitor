package open_capacitor.util;

public enum UnitPrefix {
	NONE(""),
	// SI prefixes
	kilo,
	Mega,
	Giga,
	Tera,
	Peta,
	Exa,
	Zetta,
	Yotta,
	// non-SI prefixes
	Bronto,
	Nona,
	Dogga,
	Corydon,
	Xero;
	final String abbr;

	UnitPrefix() {
		abbr = String.valueOf(name().charAt(0));
	}

	UnitPrefix(String abbr) {
		this.abbr = abbr;
	}

	//10^n instead of 2^n
	public int exponent() {
		return 3 * ordinal();
	}

	@Override
	public String toString() {
		return this == NONE ? "" : name();
	}


}
