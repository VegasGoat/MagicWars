package com.goatsoft.magicwars;

import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler
{
   @Override
   public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) 
   {
      switch(id)
      {
         // Crafting table override
         case 0:
            return new FilteredContainerWorkbench(player, player.inventory, world, x, y, z);
      }
      return null;
   }

   @Override
   public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
   {
      switch(id)
      {
         // Crafting table override
         case 0:
         {
            GuiCrafting gui = new GuiCrafting(player.inventory, world, x, y, z);
            gui.inventorySlots = new FilteredContainerWorkbench(player, player.inventory, world, x, y, z);
            return gui;
         }
      }
      return null;
   }
}

