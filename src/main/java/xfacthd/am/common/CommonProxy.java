package xfacthd.am.common;

import appeng.api.AEApi;
import xfacthd.am.api.IManaStorageChannel;
import xfacthd.am.common.grid.ManaStorageChannel;

public class CommonProxy
{
    public void preInit()
    {
        AEApi.instance().storage().registerStorageChannel(IManaStorageChannel.class, new ManaStorageChannel());
    }

    public void init()
    {
        Content.registerRecipes();
    }

    public void postInit()
    {

    }
}