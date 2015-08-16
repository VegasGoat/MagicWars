package com.goatsoft.magicwars;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import tconstruct.tools.inventory.CraftingStationContainer;
import tconstruct.tools.logic.CraftingStationLogic;

public class FilteredCraftingStationContainer extends CraftingStationContainer
{
   private EntityPlayer player;

   public FilteredCraftingStationContainer(
      InventoryPlayer inv, CraftingStationLogic logic, int x, int y, int z)
   {
      super(inv, logic, x, y, z);
      this.player = inv.player;
      onCraftMatrixChanged(this.craftMatrix);
   }

   @Override
   public boolean canInteractWith(EntityPlayer player)
   {
      return true;
   }

   @Override
   public void onCraftMatrixChanged(IInventory par1IInventory)
   {
      // ignore call from base class constructor
      if(this.player == null) return;

      ItemStack tool = modifyItem();
      if(tool != null)
         this.craftResult.setInventorySlotContents(0, tool);
      else
         FilterCommon.filterCrafting(this.craftMatrix, this.player, this.craftResult);
   }
}

