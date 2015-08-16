package com.goatsoft.magicwars;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWorkbench;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.ShapedOreRecipe;
import tconstruct.tools.TinkerTools;
import java.util.Iterator;

@Mod(modid = MagicWars.MODID, version = MagicWars.VERSION,
     dependencies = "required-after:Thaumcraft;required-after:AWWayofTime;required-after:witchery;required-after:TConstruct")
public class MagicWars
{
   public static MagicWars instance;
   public static SimpleNetworkWrapper network;
   public static final String MODID = "MagicWars";
   public static final String VERSION = "1.0";

   @EventHandler
   public void init(FMLInitializationEvent event)
   {
      instance = this;
      network = NetworkRegistry.INSTANCE.newSimpleChannel("MODID");

      // remove recipes we'll be replacing
      removeRecipes();

      // add our own replacements for the various crafting tables
      addCraftingTables();

      // register gui handler
      NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());

      // modifies players when added to the world
      PlayerModifier playerMod = new PlayerModifier();
      FMLCommonHandler.instance().bus().register(playerMod);
      MinecraftForge.EVENT_BUS.register(playerMod);

      // network packets
      network.registerMessage(StatusSyncMessage.Handler.class, StatusSyncMessage.class, 0, Side.CLIENT);
   }

   @EventHandler
   public void serverStart(FMLServerStartingEvent event)
   {
      event.registerServerCommand(new MagicWarsCommand());
   }

   private void removeRecipes()
   {
      Iterator iter = CraftingManager.getInstance().getRecipeList().iterator();
      while(iter.hasNext())
      {
         Object obj = iter.next();
         if(obj instanceof IRecipe)
         {
            IRecipe recipe = (IRecipe) obj;
            ItemStack output = recipe.getRecipeOutput();
            if((output != null) && (output.getItem() instanceof ItemBlock))
            {
               ItemBlock block = (ItemBlock) output.getItem();
               if(block.field_150939_a.getClass().equals(BlockWorkbench.class))
               {
                  iter.remove();
               }
            }
         }
      }
   }

   private void addCraftingTables()
   {
      // register replacement crafting table
      FilteredBlockWorkbench mwBench = new FilteredBlockWorkbench();
      mwBench.setHardness(2.5F);
      mwBench.setStepSound(Block.soundTypeWood);
      mwBench.setBlockName("workbench");
      mwBench.setBlockTextureName("crafting_table");
      GameRegistry.registerBlock(mwBench, "mw_crafting_table");

      // add recipe for replacement crafting table
      GameRegistry.addRecipe(new ShapedOreRecipe(mwBench, "##", "##", '#', "plankWood"));

      // register replacement crafting tile entity
      GameRegistry.registerTileEntity(FilteredCraftingStationLogic.class, "mw_crafting_logic");

      // register replacement crafting station
      FilteredCraftingStation mwStation = new FilteredCraftingStation();
      mwStation.setBlockName("CraftingStation");
      GameRegistry.registerBlock(mwStation, "mw_crafting_station");

      // add recipe for replacement crafting station
      GameRegistry.addShapelessRecipe(new ItemStack(mwStation), new ItemStack(mwBench));

      // register replacement crafting slab
      FilteredCraftingSlab mwCraftSlab = new FilteredCraftingSlab();
      mwCraftSlab.setBlockName("CraftingStation");
      GameRegistry.registerBlock(mwCraftSlab, "mw_crafting_slab");

      // add recipes for replacement crafting slab
      GameRegistry.addRecipe(new ItemStack(mwCraftSlab), "#", '#', new ItemStack(mwStation));
      GameRegistry.addRecipe(new ItemStack(mwCraftSlab, 6), "###", '#', new ItemStack(mwBench));

      // other recipes that use crafting tables
      GameRegistry.addRecipe(
         new ItemStack(TinkerTools.toolStationWood), "A", "B",
         'A', new ItemStack(TinkerTools.blankPattern),
         'B', new ItemStack(mwBench));
      GameRegistry.addRecipe(
         new ItemStack(TinkerTools.toolStationWood), "A", "B",
         'A', new ItemStack(TinkerTools.blankPattern),
         'B', new ItemStack(mwStation));
      GameRegistry.addRecipe(
         new ItemStack(TinkerTools.toolStationWood), "A", "B",
         'A', new ItemStack(TinkerTools.blankPattern),
         'B', new ItemStack(mwCraftSlab));
      // TODO: (BloodMagic) sigil of compression
   }
}

