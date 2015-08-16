package com.goatsoft.magicwars;

import net.minecraft.inventory.Container;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.world.World;
import tconstruct.tools.logic.CraftingStationLogic;

public class FilteredCraftingStationLogic extends CraftingStationLogic
{
   @Override
   public Container getGuiContainer(InventoryPlayer inv, World world, int x, int y, int z)
   {
      super.getGuiContainer(inv, world, x, y, z);
      return new FilteredCraftingStationContainer(inv, this, x, y, z);
   }
}

