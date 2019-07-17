package refined_energetic_storage.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import refined_energetic_storage.tile.IRESDevice;
import refined_energetic_storage.tile.TileController;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockController extends BlockContainer {

    public BlockController() {
        super(Material.IRON, MapColor.GRAY);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
        return new TileController();
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, @Nonnull BlockPos pos) {
        boolean result = true;
        for (EnumFacing facing : EnumFacing.values()) {
            BlockPos neighbor = pos.offset(facing);
            TileEntity tile = worldIn.getTileEntity(neighbor);
            if (tile != null) {
                if (tile instanceof TileController) {
                    result = false;
                    break;
                }
                if (tile instanceof IRESDevice) {
                    IRESDevice device = (IRESDevice) tile;
                    result = device.getControllerPos() == null;
                }
            }
        }
        return result || super.canPlaceBlockAt(worldIn, pos);
    }

}
