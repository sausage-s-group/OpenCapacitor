package refined_energetic_storage.tile;

import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public interface IRESDevice {

    @Nullable
    BlockPos getControllerPos();

    void setControllerPos(@Nullable BlockPos pos);

}
