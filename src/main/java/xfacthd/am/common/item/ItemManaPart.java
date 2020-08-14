package xfacthd.am.common.item;

import appeng.api.AEApi;
import appeng.api.config.Upgrades;
import appeng.api.parts.IPart;
import appeng.api.parts.IPartItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import xfacthd.am.AppliedManagistics;
import xfacthd.am.common.Content;
import xfacthd.am.common.part.*;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ItemManaPart extends ItemBase implements IPartItem
{
    public ItemManaPart()
    {
        super("item_mana_part", 64, PartTypes.getNames());

        Upgrades.SPEED.registerItem(new ItemStack(this, 1, PartTypes.MANA_IMPORT_BUS.ordinal()), 4);
        Upgrades.SPEED.registerItem(new ItemStack(this, 1, PartTypes.MANA_EXPORT_BUS.ordinal()), 4);
    }

    @Override
    public EnumActionResult onItemUse(final EntityPlayer player, final World w, final BlockPos pos, final EnumHand hand, final EnumFacing side, final float hitX, final float hitY, final float hitZ )
    {
        ItemStack stack = player.getHeldItem(hand);
        if(stack.isEmpty() || PartTypes.fromMetadata(stack.getMetadata()) == null)
        {
            return EnumActionResult.FAIL;
        }

        return AEApi.instance().partHelper().placeBus(player.getHeldItem(hand), pos, side, player, hand, w);
    }

    @Nullable
    @Override
    public IPart createPartFromItemStack(ItemStack stack)
    {
        PartTypes type = PartTypes.fromMetadata(stack.getMetadata());
        if (type == null) { return null; }

        switch (type)
        {
            case MANA_STORAGE_BUS: return AppliedManagistics.INSTANCE.deobfEnv ? new PartManaStorageBus(stack) : null;
            case MANA_IMPORT_BUS: return new PartManaImportBus(stack);
            case MANA_EXPORT_BUS: return new PartManaExportBus(stack);
            case MANA_TERMINAL: return new PartManaTerminal(stack);
            case MANA_STORAGE_MONITOR: return new PartManaStorageMonitor(stack);
            case MANA_LEVEL_EMITTER: return null;
            case MANA_INTERFACE: return null;
        }
        return null;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag)
    {
        super.addInformation(stack, world, tooltip, flag);
        PartTypes type = PartTypes.fromMetadata(stack.getMetadata());
        if (type == PartTypes.MANA_STORAGE_BUS || type == PartTypes.MANA_INTERFACE || type == PartTypes.MANA_LEVEL_EMITTER)
        {
            tooltip.add(I18n.format("desc.am:wip_msg.name"));
            tooltip.add(I18n.format("desc.am:disabled_msg.name"));
        }
        else if (type == PartTypes.MANA_STORAGE_MONITOR || type == PartTypes.MANA_TERMINAL)
        {
            tooltip.add(I18n.format("desc.am:wip_msg.name"));
            tooltip.add(I18n.format("desc.am:no_func_msg.name"));
        }
    }

    public enum PartTypes
    {
        MANA_STORAGE_BUS,
        MANA_IMPORT_BUS,
        MANA_EXPORT_BUS,
        MANA_TERMINAL,
        MANA_STORAGE_MONITOR,
        MANA_LEVEL_EMITTER,
        MANA_INTERFACE;

        public static List<String> getNames()
        {
            List<String> names = new ArrayList<>();
            for (PartTypes type : values())
            {
                names.add(type.toString().toLowerCase(Locale.ENGLISH));
            }
            return names;
        }

        public static PartTypes fromMetadata(int meta)
        {
            return meta >= values().length ? null : values()[meta];
        }

        public ItemStack getStack(int size)
        {
            return new ItemStack(Content.itemManaPart, size, ordinal());
        }
    }
}