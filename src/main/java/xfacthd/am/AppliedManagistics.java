package xfacthd.am;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import xfacthd.am.common.CommonProxy;
import xfacthd.am.common.Content;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION, dependencies = Reference.DEPENDENCIES)
public class AppliedManagistics
{
    @Mod.Instance
    public static AppliedManagistics INSTANCE;

    @SidedProxy(serverSide = Reference.SERVER_PROXY, clientSide = Reference.CLIENT_PROXY)
    public static CommonProxy proxy;

    public boolean deobfEnv = false;

    static { MinecraftForge.EVENT_BUS.register(Content.class); }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        deobfEnv = (Boolean)Launch.blackboard.get("fml.deobfuscatedEnvironment");
        proxy.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        proxy.postInit();
    }

    public final CreativeTabs creativeTab = new CreativeTabs("tab_am")
    {
        @Override
        public ItemStack getTabIconItem()
        {
            return new ItemStack(Content.itemManaCell);
        }
    };
}