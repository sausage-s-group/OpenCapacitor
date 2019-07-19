package open_capacitor.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import open_capacitor.tile.TileDriver;

import javax.annotation.Nullable;

public class BlockDriver extends BlockDeviceBase {
    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileDriver();
    }
}
