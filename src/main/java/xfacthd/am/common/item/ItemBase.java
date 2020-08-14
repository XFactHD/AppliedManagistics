package xfacthd.am.common.item;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import xfacthd.am.AppliedManagistics;
import xfacthd.am.Reference;

import java.util.Collections;
import java.util.List;

@SuppressWarnings("ConstantConditions")
public class ItemBase extends Item
{
    private List<String> subnames;

    public ItemBase(String name, int maxStackSize)
    {
        this(name, maxStackSize, Collections.emptyList());
    }

    public ItemBase(String name, int maxStackSize, List<String> subnames)
    {
        setRegistryName(Reference.MOD_ID, name);
        setUnlocalizedName(getRegistryName().toString());
        setMaxStackSize(maxStackSize);
        setHasSubtypes(!subnames.isEmpty());
        setCreativeTab(AppliedManagistics.INSTANCE.creativeTab);
        this.subnames = subnames;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
        if (subnames.isEmpty()) { super.getSubItems(tab, items); }
        else if (isInCreativeTab(tab))
        {
            for (int i = 0; i < subnames.size(); i++) { items.add(new ItemStack(this, 1, i)); }
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        if (subnames.isEmpty() || stack.getMetadata() >= subnames.size()) { return getUnlocalizedName(); }
        return getUnlocalizedName() + "_" + subnames.get(stack.getMetadata());
    }

    public void registerItemModels(ModelRegistryEvent event)
    {
        if (subnames.isEmpty())
        {
            ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
        }
        else
        {
            for (int i = 0; i < subnames.size(); i++)
            {
                ModelResourceLocation mrl = new ModelResourceLocation(getRegistryName(), subnames.get(i));
                ModelLoader.setCustomModelResourceLocation(this, i, mrl);
            }
        }
    }
}