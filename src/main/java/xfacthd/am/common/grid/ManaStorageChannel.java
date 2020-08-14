package xfacthd.am.common.grid;

import appeng.api.storage.data.IItemList;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import xfacthd.am.api.IAEManaStack;
import xfacthd.am.api.IManaStorageChannel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;

public class ManaStorageChannel implements IManaStorageChannel
{
    @Nonnull
    @Override
    public IItemList<IAEManaStack> createList()
    {
        return new ManaList();
    }

    @Nullable
    @Override
    public IAEManaStack createStack(@Nonnull Object o)
    {
        if (o instanceof Integer)
        {
            return new AEManaStack(((Integer)o));
        }
        else if (o instanceof IAEManaStack)
        {
            return ((IAEManaStack)o).copy();
        }
        return null;
    }

    @Nullable
    @Override
    public IAEManaStack readFromPacket(@Nonnull ByteBuf buf) throws IOException
    {
        return AEManaStack.fromPacket(buf);
    }

    @Nullable
    @Override
    public IAEManaStack createFromNBT(@Nonnull NBTTagCompound nbt)
    {
        return AEManaStack.fromNBT(nbt);
    }
}