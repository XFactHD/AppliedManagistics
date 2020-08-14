package xfacthd.am.common;

import appeng.api.AEApi;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;
import xfacthd.am.common.item.ItemBase;
import xfacthd.am.common.item.ItemManaCell;
import xfacthd.am.common.item.ItemManaPart;
import xfacthd.am.common.item.ItemMaterial;

public class Content
{
    public static ItemBase itemMaterial;
    public static ItemBase itemManaPart;
    public static ItemBase itemManaCell;

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(itemMaterial = new ItemMaterial());
        event.getRegistry().register(itemManaPart = new ItemManaPart());
        event.getRegistry().register(itemManaCell = new ItemManaCell());
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event)
    {

    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event)
    {
        itemMaterial.registerItemModels(event);
        itemManaPart.registerItemModels(event);
        itemManaCell.registerItemModels(event);
    }

    private static ItemStack certusQuartz;
    private static ItemStack fluixDust;
    private static ItemStack logicPro;
    private static ItemStack calcPro;
    private static ItemStack engPro;
    private static ItemStack terminal;
    private static ItemStack darkPanel;
    private static ItemStack illumPanel;
    private static ItemStack brightPanel;

    private static ItemStack manaSteel;
    private static ItemStack manaPearl;
    private static ItemStack manaPowder;
    private static ItemStack manaQuartz;
    private static ItemStack manaDiamond;
    private static ItemStack manaGlass;
    private static ItemStack alfGlass;

    public static void registerRecipes()
    {
        certusQuartz = AEApi.instance().definitions().materials().certusQuartzCrystal().maybeStack(1).orElse(null);
        fluixDust = AEApi.instance().definitions().materials().fluixDust().maybeStack(1).orElse(null);
        logicPro = AEApi.instance().definitions().materials().logicProcessor().maybeStack(1).orElse(null);
        calcPro = AEApi.instance().definitions().materials().calcProcessor().maybeStack(1).orElse(null);
        engPro = AEApi.instance().definitions().materials().engProcessor().maybeStack(1).orElse(null);
        terminal = AEApi.instance().definitions().parts().terminal().maybeStack(1).orElse(null);
        darkPanel = AEApi.instance().definitions().parts().darkMonitor().maybeStack(1).orElse(null);
        illumPanel = AEApi.instance().definitions().parts().semiDarkMonitor().maybeStack(1).orElse(null);
        brightPanel = AEApi.instance().definitions().parts().monitor().maybeStack(1).orElse(null);

        manaSteel = new ItemStack(ModItems.manaResource, 1, 0);
        manaPearl = new ItemStack(ModItems.manaResource, 1, 1);
        manaPowder = new ItemStack(ModItems.manaResource, 1, 23);
        manaQuartz = new ItemStack(ModItems.quartz, 1, 1);
        manaDiamond = new ItemStack(ModItems.manaResource, 1, 2);
        manaGlass = new ItemStack(ModBlocks.manaGlass);
        alfGlass = new ItemStack(ModBlocks.elfGlass);

        registerInfusionRecipes();
        registerAltarRecipes();
        registerShapedCraftingRecipes();
        registerShapelessCraftingRecipes();
    }

    private static void registerInfusionRecipes()
    {
        BotaniaAPI.registerManaInfusionRecipe(ItemMaterial.Material.MANA_CERTUS_CRYSTAL.getStack(1), certusQuartz, 6000);
    }

    private static void registerAltarRecipes()
    {
        BotaniaAPI.registerRuneAltarRecipe(ItemManaPart.PartTypes.MANA_TERMINAL.getStack(1), 10000,
                logicPro,
                terminal,
                manaDiamond
        );
    }

    private static void registerShapedCraftingRecipes()
    {
        ResourceLocation recipeGroup = new ResourceLocation("am:recipes");

        //Materials
        GameRegistry.addShapedRecipe(new ResourceLocation("am:mana_annihilation_core"), recipeGroup,
                ItemMaterial.Material.MANA_ANNIHILATION_CORE.getStack(2),
                "   ",
                "CFP",
                "   ",
                'C', manaQuartz,
                'F', fluixDust,
                'P', logicPro
        );
        GameRegistry.addShapedRecipe(new ResourceLocation("am:mana_formation_core"), recipeGroup,
                ItemMaterial.Material.MANA_FORMATION_CORE.getStack(2),
                "   ",
                "CFP",
                "   ",
                'C', ItemMaterial.Material.MANA_CERTUS_CRYSTAL.getStack(1),
                'F', fluixDust,
                'P', logicPro
        );
        GameRegistry.addShapedRecipe(new ResourceLocation("am:mana_storage_housing"), recipeGroup,
                ItemMaterial.Material.MANA_STORAGE_HOUSING.getStack(1),
                "GPG",
                "P P",
                "SSS",
                'G', manaGlass,
                'P', manaPowder,
                'S', manaSteel
        );
        GameRegistry.addShapedRecipe(new ResourceLocation("am:mana_storage_component_1k"), recipeGroup,
                ItemMaterial.Material.MANA_STORAGE_COMPONENT_1k.getStack(1),
                "PQP",
                "QLQ",
                "PQP",
                'P', manaPowder,
                'Q', ItemMaterial.Material.MANA_CERTUS_CRYSTAL.getStack(1),
                'L', logicPro
        );
        GameRegistry.addShapedRecipe(new ResourceLocation("am:mana_storage_component_4k"), recipeGroup,
                ItemMaterial.Material.MANA_STORAGE_COMPONENT_4k.getStack(1),
                "PCP",
                "SGS",
                "PSP",
                'P', manaPearl,
                'C', calcPro,
                'S', ItemMaterial.Material.MANA_STORAGE_COMPONENT_1k.getStack(1),
                'G', manaGlass
        );
        GameRegistry.addShapedRecipe(new ResourceLocation("am:mana_storage_component_16k"), recipeGroup,
                ItemMaterial.Material.MANA_STORAGE_COMPONENT_16k.getStack(1),
                "PCP",
                "SGS",
                "PSP",
                'P', manaPearl,
                'C', calcPro,
                'S', ItemMaterial.Material.MANA_STORAGE_COMPONENT_4k.getStack(1),
                'G', manaGlass
        );
        GameRegistry.addShapedRecipe(new ResourceLocation("am:mana_storage_component_64k"), recipeGroup,
                ItemMaterial.Material.MANA_STORAGE_COMPONENT_64k.getStack(1),
                "PCP",
                "SGS",
                "PSP",
                'P', manaPearl,
                'C', calcPro,
                'S', ItemMaterial.Material.MANA_STORAGE_COMPONENT_16k.getStack(1),
                'G', manaGlass
        );
        GameRegistry.addShapedRecipe(new ResourceLocation("am:mana_storage_component_256k"), recipeGroup,
                ItemMaterial.Material.MANA_STORAGE_COMPONENT_256k.getStack(1),
                "DCD",
                "SGS",
                "DSD",
                'D', manaDiamond,
                'C', engPro,
                'S', ItemMaterial.Material.MANA_STORAGE_COMPONENT_64k.getStack(1),
                'G', alfGlass
        );

        GameRegistry.addShapedRecipe(new ResourceLocation("am:mana_storage_component_1024k"), recipeGroup,
                ItemMaterial.Material.MANA_STORAGE_COMPONENT_1024k.getStack(1),
                "DCD",
                "SGS",
                "DSD",
                'D', manaDiamond,
                'C', engPro,
                'S', ItemMaterial.Material.MANA_STORAGE_COMPONENT_256k.getStack(1),
                'G', alfGlass
        );
        GameRegistry.addShapedRecipe(new ResourceLocation("am:mana_storage_component_4096k"), recipeGroup,
                ItemMaterial.Material.MANA_STORAGE_COMPONENT_4096k.getStack(1),
                "DCD",
                "SGS",
                "DSD",
                'D', manaDiamond,
                'C', engPro,
                'S', ItemMaterial.Material.MANA_STORAGE_COMPONENT_1024k.getStack(1),
                'G', alfGlass
        );

        //Storage cells
        GameRegistry.addShapedRecipe(new ResourceLocation("am:mana_storage_cell1k_raw"), recipeGroup,
                ItemManaCell.CellSize.SIZE_1K.getStack(1),
                "GPG",
                "PCP",
                "SSS",
                'G', manaGlass,
                'P', manaPowder,
                'S', manaSteel,
                'C', ItemMaterial.Material.MANA_STORAGE_COMPONENT_1k.getStack(1)
        );
        GameRegistry.addShapedRecipe(new ResourceLocation("am:mana_storage_cell4k_raw"), recipeGroup,
                ItemManaCell.CellSize.SIZE_4K.getStack(1),
                "GPG",
                "PCP",
                "SSS",
                'G', manaGlass,
                'P', manaPowder,
                'S', manaSteel,
                'C', ItemMaterial.Material.MANA_STORAGE_COMPONENT_4k.getStack(1)
        );
        GameRegistry.addShapedRecipe(new ResourceLocation("am:mana_storage_cell16k_raw"), recipeGroup,
                ItemManaCell.CellSize.SIZE_16K.getStack(1),
                "GPG",
                "PCP",
                "SSS",
                'G', manaGlass,
                'P', manaPowder,
                'S', manaSteel,
                'C', ItemMaterial.Material.MANA_STORAGE_COMPONENT_16k.getStack(1)
        );
        GameRegistry.addShapedRecipe(new ResourceLocation("am:mana_storage_cell64k_raw"), recipeGroup,
                ItemManaCell.CellSize.SIZE_64K.getStack(1),
                "GPG",
                "PCP",
                "SSS",
                'G', manaGlass,
                'P', manaPowder,
                'S', manaSteel,
                'C', ItemMaterial.Material.MANA_STORAGE_COMPONENT_64k.getStack(1)
        );
        GameRegistry.addShapedRecipe(new ResourceLocation("am:mana_storage_cell256k_raw"), recipeGroup,
                ItemManaCell.CellSize.SIZE_256K.getStack(1),
                "GPG",
                "PCP",
                "SSS",
                'G', manaGlass,
                'P', manaPowder,
                'S', manaSteel,
                'C', ItemMaterial.Material.MANA_STORAGE_COMPONENT_256k.getStack(1)
        );
        GameRegistry.addShapedRecipe(new ResourceLocation("am:mana_storage_cell1024k_raw"), recipeGroup,
                ItemManaCell.CellSize.SIZE_1024K.getStack(1),
                "GPG",
                "PCP",
                "SSS",
                'G', manaGlass,
                'P', manaPowder,
                'S', manaSteel,
                'C', ItemMaterial.Material.MANA_STORAGE_COMPONENT_1024k.getStack(1)
        );
        GameRegistry.addShapedRecipe(new ResourceLocation("am:mana_storage_cell4096k_raw"), recipeGroup,
                ItemManaCell.CellSize.SIZE_4096K.getStack(1),
                "GPG",
                "PCP",
                "SSS",
                'G', manaGlass,
                'P', manaPowder,
                'S', manaSteel,
                'C', ItemMaterial.Material.MANA_STORAGE_COMPONENT_4096k.getStack(1)
        );

        //Parts
        GameRegistry.addShapedRecipe(new ResourceLocation("am:part_import_bus"), recipeGroup,
                ItemManaPart.PartTypes.MANA_IMPORT_BUS.getStack(1),
                "   ",
                " C ",
                "SPS",
                'C', ItemMaterial.Material.MANA_ANNIHILATION_CORE.getStack(1),
                'S', manaSteel,
                'P', new ItemStack(Blocks.STICKY_PISTON)
        );
        GameRegistry.addShapedRecipe(new ResourceLocation("am:part_export_bus"), recipeGroup,
                ItemManaPart.PartTypes.MANA_EXPORT_BUS.getStack(1),
                "   ",
                "SCS",
                " P ",
                'C', ItemMaterial.Material.MANA_FORMATION_CORE.getStack(1),
                'S', manaSteel,
                'P', new ItemStack(Blocks.PISTON)
        );
        GameRegistry.addShapedRecipe(new ResourceLocation("am:part_interface"), recipeGroup,
                ItemManaPart.PartTypes.MANA_INTERFACE.getStack(1),
                "SGS",
                "A F",
                "SGS",
                'A', ItemMaterial.Material.MANA_ANNIHILATION_CORE.getStack(1),
                'F', ItemMaterial.Material.MANA_FORMATION_CORE.getStack(1),
                'S', manaSteel,
                'G', manaGlass
        );
    }

    private static void registerShapelessCraftingRecipes()
    {
        ResourceLocation recipeGroup = new ResourceLocation("am:recipes");

        //Parts
        GameRegistry.addShapelessRecipe(new ResourceLocation("am:part_storage_bus"), recipeGroup,
                ItemManaPart.PartTypes.MANA_STORAGE_BUS.getStack(1),
                Ingredient.fromStacks(new ItemStack(Blocks.PISTON)),
                Ingredient.fromStacks(new ItemStack(Blocks.STICKY_PISTON)),
                Ingredient.fromStacks(ItemManaPart.PartTypes.MANA_INTERFACE.getStack(1)) //TODO: add interface block when available
        );
        GameRegistry.addShapelessRecipe(new ResourceLocation("am:part_storage_monitor"), recipeGroup,
                ItemManaPart.PartTypes.MANA_STORAGE_MONITOR.getStack(1),
                Ingredient.fromStacks(darkPanel, illumPanel, brightPanel),
                Ingredient.fromStacks(ItemManaPart.PartTypes.MANA_LEVEL_EMITTER.getStack(1))
        );
        GameRegistry.addShapelessRecipe(new ResourceLocation("am:part_level_emitter"), recipeGroup,
                ItemManaPart.PartTypes.MANA_LEVEL_EMITTER.getStack(1),
                Ingredient.fromStacks(calcPro),
                Ingredient.fromStacks(manaQuartz),
                Ingredient.fromStacks(new ItemStack(Blocks.REDSTONE_TORCH))
        );

        //Storage Cells
        GameRegistry.addShapelessRecipe(new ResourceLocation("am:mana_storage_cell1k"), recipeGroup,
                ItemManaCell.CellSize.SIZE_1K.getStack(1),
                Ingredient.fromStacks(ItemMaterial.Material.MANA_STORAGE_HOUSING.getStack(1)),
                Ingredient.fromStacks(ItemMaterial.Material.MANA_STORAGE_COMPONENT_1k.getStack(1))
        );
        GameRegistry.addShapelessRecipe(new ResourceLocation("am:mana_storage_cell4k"), recipeGroup,
                ItemManaCell.CellSize.SIZE_4K.getStack(1),
                Ingredient.fromStacks(ItemMaterial.Material.MANA_STORAGE_HOUSING.getStack(1)),
                Ingredient.fromStacks(ItemMaterial.Material.MANA_STORAGE_COMPONENT_4k.getStack(1))
        );
        GameRegistry.addShapelessRecipe(new ResourceLocation("am:mana_storage_cell16k"), recipeGroup,
                ItemManaCell.CellSize.SIZE_16K.getStack(1),
                Ingredient.fromStacks(ItemMaterial.Material.MANA_STORAGE_HOUSING.getStack(1)),
                Ingredient.fromStacks(ItemMaterial.Material.MANA_STORAGE_COMPONENT_16k.getStack(1))
        );
        GameRegistry.addShapelessRecipe(new ResourceLocation("am:mana_storage_cell64k"), recipeGroup,
                ItemManaCell.CellSize.SIZE_64K.getStack(1),
                Ingredient.fromStacks(ItemMaterial.Material.MANA_STORAGE_HOUSING.getStack(1)),
                Ingredient.fromStacks(ItemMaterial.Material.MANA_STORAGE_COMPONENT_64k.getStack(1))
        );
        GameRegistry.addShapelessRecipe(new ResourceLocation("am:mana_storage_cell256k"), recipeGroup,
                ItemManaCell.CellSize.SIZE_256K.getStack(1),
                Ingredient.fromStacks(ItemMaterial.Material.MANA_STORAGE_HOUSING.getStack(1)),
                Ingredient.fromStacks(ItemMaterial.Material.MANA_STORAGE_COMPONENT_256k.getStack(1))
        );
        GameRegistry.addShapelessRecipe(new ResourceLocation("am:mana_storage_cell1024k"), recipeGroup,
                ItemManaCell.CellSize.SIZE_1024K.getStack(1),
                Ingredient.fromStacks(ItemMaterial.Material.MANA_STORAGE_HOUSING.getStack(1)),
                Ingredient.fromStacks(ItemMaterial.Material.MANA_STORAGE_COMPONENT_1024k.getStack(1))
        );
        GameRegistry.addShapelessRecipe(new ResourceLocation("am:mana_storage_cell4096k"), recipeGroup,
                ItemManaCell.CellSize.SIZE_4096K.getStack(1),
                Ingredient.fromStacks(ItemMaterial.Material.MANA_STORAGE_HOUSING.getStack(1)),
                Ingredient.fromStacks(ItemMaterial.Material.MANA_STORAGE_COMPONENT_4096k.getStack(1))
        );
    }
}