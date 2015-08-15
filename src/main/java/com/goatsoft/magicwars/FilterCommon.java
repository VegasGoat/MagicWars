package com.goatsoft.magicwars;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import tconstruct.tools.items.MaterialItem;

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

         Item itemObj = result.getItem();
         String className = itemObj.getClass().getName();

         // for block items, get the name of the block's class
         if(itemObj instanceof ItemBlock)
         {
            className = ((ItemBlock) itemObj).field_150939_a.getClass().getName();
         }

         System.out.println("*** FILTER RESULT CLASS " + className);

         boolean matched = false;

         // allow anyone to turn ingots into nuggets
         if(itemObj instanceof MaterialItem)
         {
            String materialName = itemObj.getUnlocalizedName(result);
            if(materialName.endsWith("Nugget"))
            {
               matched = true;
            }
         }

         // must have thaumcraft flag for thaumcraft results
         if((!matched) && className.startsWith("thaumcraft.") && (!hasThaum))
         {
            if(!player.worldObj.isRemote)
            {
               ChatComponentText text = new ChatComponentText("A Thaumaturge would be able to build that.");
               text.getChatStyle().setColor(EnumChatFormatting.GRAY).setItalic(true);
               player.addChatComponentMessage(text);
            }
            matched = true;
            result = null;
         }

         // must have blood magic flag for blood magic results
         if((!matched) && className.startsWith("WayofTime.alchemicalWizardry.") && (!hasBlood))
         {
            if(!player.worldObj.isRemote)
            {
               ChatComponentText text = new ChatComponentText("A Blood Mage would be able to build that.");
               text.getChatStyle().setColor(EnumChatFormatting.GRAY).setItalic(true);
               player.addChatComponentMessage(text);
            }
            matched = true;
            result = null;
         }

         // must have witchery flag for witchery results
         if((!matched) && className.startsWith("com.emoniph.witchery.") && (!hasWitch))
         {
            if(!player.worldObj.isRemote)
            {
               ChatComponentText text = new ChatComponentText("A Witch would be able to build that.");
               text.getChatStyle().setColor(EnumChatFormatting.GRAY).setItalic(true);
               player.addChatComponentMessage(text);
            }
            matched = true;
            result = null;
         }

         // must have tinkers flag for tinker's construct results
         if((!matched) && className.startsWith("tconstruct.") && (!hasTinker))
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

