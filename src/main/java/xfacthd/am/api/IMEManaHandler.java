package xfacthd.am.api;

import appeng.api.AEApi;
import appeng.api.storage.IMEInventoryHandler;
import appeng.api.storage.IStorageChannel;

public interface IMEManaHandler extends IMEInventoryHandler<IAEManaStack>
{
    @Override
    default IStorageChannel<IAEManaStack> getChannel()
    {
        return AEApi.instance().storage().getStorageChannel(IManaStorageChannel.class);
    }

    @Override
    default boolean isPrioritized(IAEManaStack stack)
    {
        return false;
    }

    @Override
    default int getSlot()
    {
        return 0;
    }

    @Override
    default boolean validForPass(int pass)
    {
        return true;
    }
}