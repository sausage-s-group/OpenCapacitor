package open_capacitor.util;

import sausage_core.api.util.math.UFMBigInt;

import java.math.BigInteger;

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
    public final String abbr;

    UnitPrefix() {
        abbr = String.valueOf(name().charAt(0));
    }

    UnitPrefix(String abbr) {
        this.abbr = abbr;
    }

    public int exponent() {
        return 3 * ordinal();
    }

    public UFMBigInt get() {
        return UFMBigInt.zeroOf(ordinal() / 6 + 1);
    }

    public BigInteger maxOf(int size) {
        return BigInteger.valueOf(size).multiply(BigInteger.TEN.pow(exponent()));
    }


    @Override
    public String toString() {
        return this == NONE ? "" : name();
    }


}
