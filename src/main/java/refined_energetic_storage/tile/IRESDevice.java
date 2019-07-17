package refined_energetic_storage.tile;

import net.minecraft.util.math.BlockPos;

public interface IRESDevice {

    BlockPos getControllerPos();

    int distanceToDevice();

}
