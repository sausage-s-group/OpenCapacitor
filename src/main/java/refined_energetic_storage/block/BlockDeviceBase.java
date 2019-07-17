package refined_energetic_storage.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import refined_energetic_storage.tile.IRESDevice;
import refined_energetic_storage.tile.TileController;

import javax.annotation.Nonnull;

public abstract class BlockDeviceBase extends BlockContainer {

    public BlockDeviceBase(Material material) {
        super(material);
    }

    public BlockDeviceBase(Material material, MapColor mapColor) {
        super(material, mapColor);
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, @Nonnull BlockPos pos) {
        boolean result = true;
        boolean firstFound = false;
        for (EnumFacing facing : EnumFacing.values()) {
            BlockPos neighbor = pos.offset(facing);
            TileEntity tile = worldIn.getTileEntity(neighbor);
            if (tile != null) {
                if (tile instanceof IRESDevice) {
                    IRESDevice device = (IRESDevice) tile;
                    if (device.getControllerPos() != null && firstFound) {
                        result = false;
                        break;
                    } else if (device.getControllerPos() != null) {
                        firstFound = true;
                    }
                }
                if (tile instanceof TileController) {
                    if (firstFound) {
                        result = false;
                        break;
                    } else {
                        firstFound = true;
                    }
                }
            }
        }
        return result || super.canPlaceBlockAt(worldIn, pos);
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(world, pos, neighbor);
    }
}
