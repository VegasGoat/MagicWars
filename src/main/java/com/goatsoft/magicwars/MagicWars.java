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

      // add our own replacement for the crafting table
      addCraftingTable();

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

   private void addCraftingTable()
   {
      // register the new table
      FilteredBlockWorkbench mwBench = new FilteredBlockWorkbench();
      mwBench.setHardness(2.5F);
      mwBench.setStepSound(Block.soundTypeWood);
      mwBench.setBlockName("mw_crafting_table");
      mwBench.setBlockTextureName("crafting_table");
      GameRegistry.registerBlock(mwBench, "mw_crafting_table");

      // add recipe to create the table using any type of wood
      GameRegistry.addRecipe(new ShapedOreRecipe(mwBench, "##", "##", '#', "plankWood"));

      // add recipes that use the crafting table
      // TODO: (TConstruct) crafting station
      // TODO: (TConstruct) crafting station slab
      // TODO: (BloodMagic) sigil of compression
   }
}

