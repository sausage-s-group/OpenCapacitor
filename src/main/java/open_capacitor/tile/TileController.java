package open_capacitor.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;
import sausage_core.api.util.nbt.NBTs;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class TileController extends TileDeviceBase implements ITickable {
    public Set<BlockPos> devices = new HashSet<>();

    public TileController() {
        network = true;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        devices = NBTs.stream(nbt.getTagList("devices", Constants.NBT.TAG_COMPOUND))
                .map(NBTTagCompound.class::cast)
                .map(NBTUtil::getPosFromTag)
                .collect(Collectors.toSet());
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setTag("devices", devices.stream().map(NBTUtil::createPosTag).collect(NBTs.toNBTList()));
        return super.writeToNBT(nbt);
    }
}
