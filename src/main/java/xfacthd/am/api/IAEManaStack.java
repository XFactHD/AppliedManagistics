package xfacthd.am.api;

import appeng.api.AEApi;
import appeng.api.config.FuzzyMode;
import appeng.api.storage.IStorageChannel;
import appeng.api.storage.data.IAEStack;
import net.minecraft.item.ItemStack;

public interface IAEManaStack extends IAEStack<IAEManaStack>
{
    @Override
    default boolean isCraftable()
    {
        return false;
    }

    @Override
    default void incStackSize(long size)
    {
        setStackSize(getStackSize() + size);
    }

    @Override
    default void decStackSize(long size)
    {
        setStackSize(getStackSize() - size);
    }

    @Override
    default void incCountRequestable(long size)
    {
        setCountRequestable(getCountRequestable() + size);
    }

    @Override
    default void decCountRequestable(long size)
    {
        setCountRequestable(getCountRequestable() - size);
    }

    @Override
    default boolean fuzzyComparison(IAEManaStack stack, FuzzyMode mode)
    {
        return true;
    }

    @Override
    default boolean isItem()
    {
        return false;
    }

    @Override
    default boolean isFluid()
    {
        return false;
    }

    @Override
    default IStorageChannel<IAEManaStack> getChannel()
    {
        return AEApi.instance().storage().getStorageChannel(IManaStorageChannel.class);
    }

    @Override
    default ItemStack asItemStackRepresentation()
    {
        return null;
    }
}