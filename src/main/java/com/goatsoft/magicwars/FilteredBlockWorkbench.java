package com.goatsoft.magicwars;

import net.minecraft.block.BlockWorkbench;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class FilteredBlockWorkbench extends BlockWorkbench
{
   @Override
   public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int i, float j, float k, float l)
   {
      player.openGui(MagicWars.instance, 0, world, x, y, z);
      return true;
   }
}

