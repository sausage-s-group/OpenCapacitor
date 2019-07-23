package open_capacitor.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import sausage_core.api.core.tile.TileBase;

import java.util.Optional;

public abstract class TileDeviceBase extends TileBase implements ITickable {
    public static final BlockPos INVALID = BlockPos.ORIGIN.down();
    public BlockPos controller = INVALID;

    public TileDeviceBase() {
        network = true;
    }

    public Optional<TileController> getController() {
        if(!hasController()) return Optional.empty();
        return Optional.ofNullable(world.getTileEntity(controller)).map(TileController.class::cast);
    }

    public boolean hasController() {
        return controller != INVALID;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        controller = NBTUtil.getPosFromTag(nbt.getCompoundTag("controller"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setTag("controller", NBTUtil.createPosTag(controller));
        return super.writeToNBT(nbt);
    }

    @Override
    public void update() {
        this.notifyClient();
    }
}
