package xfacthd.am.common.item;

import net.minecraft.item.ItemStack;
import xfacthd.am.common.Content;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ItemMaterial extends ItemBase
{
    public ItemMaterial()
    {
        super("item_material", 64, Material.getNames());
    }

    public enum Material
    {
        MANA_CERTUS_CRYSTAL,
        MANA_ANNIHILATION_CORE,
        MANA_FORMATION_CORE,
        MANA_STORAGE_HOUSING,
        MANA_STORAGE_COMPONENT_1k,
        MANA_STORAGE_COMPONENT_4k,
        MANA_STORAGE_COMPONENT_16k,
        MANA_STORAGE_COMPONENT_64k,
        MANA_STORAGE_COMPONENT_256k,
        MANA_STORAGE_COMPONENT_1024k,
        MANA_STORAGE_COMPONENT_4096k;

        public static List<String> getNames()
        {
            List<String> names = new ArrayList<>();
            for (Material material : values())
            {
                names.add(material.toString().toLowerCase(Locale.ENGLISH));
            }
            return names;
        }

        public ItemStack getStack(int size)
        {
            return new ItemStack(Content.itemMaterial, size, ordinal());
        }
    }
}