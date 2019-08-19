package open_capacitor.util;

import net.minecraft.nbt.NBTTagCompound;
import sausage_core.api.util.math.UFMBigInt;
import sausage_core.api.util.nbt.NBTs;

public class OCEnergyStorage {
    public final EnergySize size;
    public final UFMBigInt limit; // public but use with care
    public final UFMBigInt energy; // public but use with care

    public OCEnergyStorage(EnergySize size) {
        this.size = size;
        limit = size.prefix.get();
        limit.assign(size.limit());
        energy = size.prefix.get();
    }

    private OCEnergyStorage(NBTTagCompound nbt) {
        size = EnergySize.fromNBT(nbt.getCompoundTag("size"));
        limit = size.prefix.get();
        limit.assign(size.limit());
        energy = UFMBigInt.fromLongs(NBTs.getLongArray(nbt, "energy"));
    }

    public UFMBigInt fill(UFMBigInt other) {
        return energy.fill(other, limit);
    }

    public UFMBigInt drain(UFMBigInt other) {
        return energy.drain(other);
    }

    public NBTTagCompound toNBT() {
        return NBTs.of("size", size.toNBT(), "energy", energy.toNBT());
    }

    public static OCEnergyStorage fromNBT(NBTTagCompound nbt) {
        return new OCEnergyStorage(nbt);
    }
}