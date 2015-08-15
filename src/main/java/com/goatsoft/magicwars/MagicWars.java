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
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = MagicWars.MODID, version = MagicWars.VERSION,
     dependencies = "required-after:Thaumcraft;required-after:AWWayofTime")
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

      // add our own replacement for the crafting bench
      FilteredBlockWorkbench mwBench = new FilteredBlockWorkbench();
      mwBench.setHardness(2.5F).setStepSound(Block.soundTypeWood).setBlockName("mw_crafting_table").setBlockTextureName("crafting_table");
      GameRegistry.registerBlock(mwBench, "mw_crafting_table");
      GameRegistry.addRecipe(new ItemStack(mwBench), "##", "##", '#', Blocks.planks);
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
}

