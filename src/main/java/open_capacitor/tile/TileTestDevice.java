package open_capacitor.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import sausage_core.api.core.tile.TileBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileTestDevice extends TileBase implements IDevice, ITickable {

    private BlockPos controllerPos = null;
    private boolean needUpdate = false;

    public TileTestDevice() {
        this.network = true;
    }

    @Nullable
    @Override
    public BlockPos getControllerPos() {
        return controllerPos;
    }

    @Override
    public void setControllerPos(@Nullable BlockPos pos) {
        controllerPos = pos;
        needUpdate = true;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("controller")) {
            controllerPos = NBTUtil.getPosFromTag(compound.getCompoundTag("controller"));
        }
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        if (controllerPos != null) {
            compound.setTag("controller", NBTUtil.createPosTag(controllerPos));
        } else {
            compound.removeTag("controller");
        }
        return compound;
    }

    @Override
    public void update() {
        if (!world.isRemote && needUpdate) {
            this.notifyClient();
            needUpdate = false;
        }
    }
}
