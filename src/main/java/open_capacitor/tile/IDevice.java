package open_capacitor.tile;

import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public interface IDevice {

    @Nullable
    BlockPos getControllerPos();

    void setControllerPos(@Nullable BlockPos pos);

}
