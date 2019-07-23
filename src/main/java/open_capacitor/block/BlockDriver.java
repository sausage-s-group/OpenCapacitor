package open_capacitor.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import open_capacitor.tile.TileDeviceBase;
import open_capacitor.tile.TileDriver;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static sausage_core.api.util.common.SausageUtils.nonnull;

public class BlockDriver extends BlockDeviceBase {
    @Nullable
    @Override
    public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
        return new TileDriver();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            playerIn.sendMessage(new TextComponentString("S: " + nonnull((TileDeviceBase) worldIn.getTileEntity(pos)).getController().toString()));
        } else {
            playerIn.sendMessage(new TextComponentString("C: " + nonnull((TileDeviceBase) worldIn.getTileEntity(pos)).getController().toString()));
        }
        return true;
    }
}
