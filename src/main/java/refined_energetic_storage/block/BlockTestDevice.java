package refined_energetic_storage.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import refined_energetic_storage.tile.IRESDevice;
import refined_energetic_storage.tile.TileTestDevice;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class BlockTestDevice extends BlockDeviceBase {

    public BlockTestDevice() {
        super(Material.IRON);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
        return new TileTestDevice();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            playerIn.sendMessage(new TextComponentString("Server Controller: " + ((IRESDevice) Objects.requireNonNull(worldIn.getTileEntity(pos))).getControllerPos()));
        }
        if (worldIn.isRemote) {
            playerIn.sendMessage(new TextComponentString("Client Controller: " + ((IRESDevice) Objects.requireNonNull(worldIn.getTileEntity(pos))).getControllerPos()));
        }
        return true;
    }
}
