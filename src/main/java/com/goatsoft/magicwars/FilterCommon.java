package com.goatsoft.magicwars;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class FilterCommon
{
   public static void filterCrafting(InventoryCrafting craftMatrix, EntityPlayer player, IInventory craftResult)
   {
      // check matching recipe against player's classes
      ItemStack result = CraftingManager.getInstance().findMatchingRecipe(craftMatrix, player.worldObj);
      if(result != null)
      {
         boolean hasThaum = false;
         boolean hasBlood = false;
         boolean hasWitch = false;
         boolean hasTinker = false;
         try
         {
            NBTTagCompound mwData = (NBTTagCompound) player.getEntityData().getTag("MagicWars");
            if(mwData != null)
            {
               hasThaum = mwData.getBoolean("Thaumcraft");
               hasBlood = mwData.getBoolean("BloodMagic");
               hasWitch = mwData.getBoolean("Witchery");
               hasTinker = mwData.getBoolean("TConstruct");
            }
         }
         catch(ClassCastException ex)
         {
            // wrong data type, will be replaced when something is changed
            // assume everything false for now
         }

         // get class name of item, or underlying block for item
         String className = result.getItem().getClass().getName();
         if(result.getItem() instanceof ItemBlock)
         {
            className = ((ItemBlock) result.getItem()).field_150939_a.getClass().getName();
         }

         System.out.println("*** FILTER RESULT CLASS " + className);

         // filter out item recipes from classes the player doesn't have
         if(className.startsWith("thaumcraft.") && (!hasThaum))
         {
            if(!player.worldObj.isRemote)
            {
               ChatComponentText text = new ChatComponentText("A Thaumaturge would be able to build that.");
               text.getChatStyle().setColor(EnumChatFormatting.GRAY).setItalic(true);
               player.addChatComponentMessage(text);
            }
            result = null;
         }
         else if(className.startsWith("WayofTime.alchemicalWizardry.") && (!hasBlood))
         {
            if(!player.worldObj.isRemote)
            {
               ChatComponentText text = new ChatComponentText("A Blood Mage would be able to build that.");
               text.getChatStyle().setColor(EnumChatFormatting.GRAY).setItalic(true);
               player.addChatComponentMessage(text);
            }
            result = null;
         }
         else if(className.startsWith("com.emoniph.witchery.") && (!hasWitch))
         {
            if(!player.worldObj.isRemote)
            {
               ChatComponentText text = new ChatComponentText("A Witch would be able to build that.");
               text.getChatStyle().setColor(EnumChatFormatting.GRAY).setItalic(true);
               player.addChatComponentMessage(text);
            }
            result = null;
         }
         else if(className.startsWith("tconstruct.") && (!hasTinker))
         {
            if(!player.worldObj.isRemote)
            {
               ChatComponentText text = new ChatComponentText("A Tinker would be able to build that.");
               text.getChatStyle().setColor(EnumChatFormatting.GRAY).setItalic(true);
               player.addChatComponentMessage(text);
            }
            result = null;
         }
      }
      craftResult.setInventorySlotContents(0, result);
   }
}

