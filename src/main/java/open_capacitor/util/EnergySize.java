package open_capacitor.util;

import net.minecraft.nbt.NBTTagCompound;
import sausage_core.api.util.nbt.NBTs;

/**
 * represents size instead of storage
 */
public class EnergySize {
    public final int size;
    public final UnitPrefix prefix;

    public static final EnergySize EMPTY = new EnergySize(0, UnitPrefix.NONE);

    public EnergySize(int size, UnitPrefix prefix) {
        this.size = size;
        this.prefix = prefix;
    }

    @Override
    public String toString() {
        return size + prefix.abbr + EnergyUnit.FE;
    }

    public NBTTagCompound toNBT() {
        return NBTs.of("size", NBTs.of(size), "prefix", NBTs.of(prefix.ordinal()));
    }

    public static EnergySize fromNBT(NBTTagCompound nbt) {
        return new EnergySize(nbt.getInteger("size"), UnitPrefix.values()[nbt.getInteger("prefix")]);
    }
}
