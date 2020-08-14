package xfacthd.am.common.grid;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import xfacthd.am.api.IAEManaStack;

import java.io.IOException;

public class AEManaStack implements IAEManaStack, Comparable<IAEManaStack>
{
    private long stackSize = 0;
    private long countRequestable = 0;
    private boolean isCraftable = false;

    public AEManaStack(long amount)
    {
        this.setStackSize(amount);
        this.setCraftable(false);
        this.setCountRequestable(0);
        //this.hash = 0;
    }

    private AEManaStack(AEManaStack stack)
    {
        this.setStackSize(stack.getStackSize());
        this.setCraftable(false);
        this.setCountRequestable(0);
        //this.hash = stack.hash;
    }

    @Override
    public void add(IAEManaStack stack)
    {
        if (stack == null) { return; }

        this.incStackSize(stack.getStackSize());
        this.setCountRequestable(this.getCountRequestable() + stack.getCountRequestable());
        this.setCraftable(this.isCraftable() || stack.isCraftable());
    }

    @Override
    public long getStackSize()
    {
        return stackSize;
    }

    @Override
    public IAEManaStack setStackSize(long size)
    {
        stackSize = size;
        return this;
    }

    @Override
    public long getCountRequestable()
    {
        return countRequestable;
    }

    @Override
    public IAEManaStack setCountRequestable(long count)
    {
        countRequestable = count;
        return this;
    }

    @Override
    public IAEManaStack setCraftable(boolean craftable)
    {
        isCraftable = craftable;
        return this;
    }

    @Override
    public IAEManaStack reset()
    {
        setStackSize(0);
        setCountRequestable(0);
        setCraftable(false);
        return this;
    }

    @Override
    public boolean isMeaningful()
    {
        return (this.getStackSize() != 0) || this.countRequestable > 0 || this.isCraftable;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        nbt.setLong("mana", stackSize);
        nbt.setLong("requestable", countRequestable);
    }

    @Override
    public void writeToPacket(ByteBuf buf) throws IOException
    {
        NBTTagCompound nbt = new NBTTagCompound();
        writeToNBT(nbt);
        ByteBufUtils.writeTag(buf, nbt);
    }

    @Override
    public IAEManaStack copy()
    {
        return new AEManaStack(this);
    }

    @Override
    public IAEManaStack empty()
    {
        IAEManaStack copy = copy();
        copy.reset();
        return copy;
    }

    public static AEManaStack fromNBT(NBTTagCompound nbt)
    {
        AEManaStack stack = new AEManaStack(nbt.getLong("mana"));
        stack.setCountRequestable(nbt.getLong("requestable"));
        return stack;
    }

    public static AEManaStack fromPacket(ByteBuf buf) throws IOException
    {
        return fromNBT(ByteBufUtils.readTag(buf));
    }

    @Override
    public int compareTo(IAEManaStack o)
    {
        return 0;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof IAEManaStack)
        {
            return true;
        }
        return false;
    }
}