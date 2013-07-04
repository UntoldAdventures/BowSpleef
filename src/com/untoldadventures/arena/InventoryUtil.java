package com.untoldadventures.arena;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.math.BigInteger;

import net.minecraft.server.v1_6_R1.NBTBase;
import net.minecraft.server.v1_6_R1.NBTTagCompound;
import net.minecraft.server.v1_6_R1.NBTTagList;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_6_R1.inventory.CraftInventoryCustom;
import org.bukkit.craftbukkit.v1_6_R1.inventory.CraftItemStack;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class InventoryUtil
{

	public static String inventoryToString(final Inventory inventory)
	{
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		final DataOutputStream dataOutput = new DataOutputStream(outputStream);
		final NBTTagList itemList = new NBTTagList();
		for (int i = 0; i < inventory.getSize(); i++)
		{
			final NBTTagCompound outputObject = new NBTTagCompound();
			net.minecraft.server.v1_6_R1.ItemStack craft = null;
			final org.bukkit.inventory.ItemStack is = inventory.getItem(i);
			if (is != null)
			{
				craft = CraftItemStack.asNMSCopy(is);
			}
			else
			{
				craft = null;
			}
			if (craft != null)
			{
				craft.save(outputObject);
			}
			itemList.add(outputObject);
		}
		NBTBase.a(itemList, dataOutput);
		return new BigInteger(1, outputStream.toByteArray()).toString(32);
	}

	public static Inventory stringToInventory(final String data, final String name)
	{
		final ByteArrayInputStream inputStream = new ByteArrayInputStream(new BigInteger(data, 32).toByteArray());
		final NBTTagList itemList = (NBTTagList) NBTBase.a(new DataInputStream(inputStream));
		final Inventory inventory = Bukkit.createInventory(null, 27, name);
		for (int i = 0; i < itemList.size(); i++)
		{
			final NBTTagCompound inputObject = (NBTTagCompound) itemList.get(i);
			if (!inputObject.isEmpty())
			{
				inventory.setItem(i, CraftItemStack.asBukkitCopy(net.minecraft.server.v1_6_R1.ItemStack.createStack(inputObject)));
			}
		}
		return inventory;
	}

	public static Inventory getArmorInventory(PlayerInventory inventory)
	{
		ItemStack[] armor = inventory.getArmorContents();
		CraftInventoryCustom storage = new CraftInventoryCustom(null, armor.length);

		for (int i = 0; i < armor.length; i++)
			storage.setItem(i, armor[i]);

		return storage;
	}

	public static Inventory getContentInventory(PlayerInventory inventory)
	{
		ItemStack[] content = inventory.getContents();
		CraftInventoryCustom storage = new CraftInventoryCustom(null, content.length);

		for (int i = 0; i < content.length; i++)
			storage.setItem(i, content[i]);

		return storage;
	}

	public static String toBase64(Inventory inventory)
	{
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		DataOutputStream dataOutput = new DataOutputStream(outputStream);
		NBTTagList itemList = new NBTTagList();
		// Save every element in the list
		for (int i = 0; i < inventory.getSize(); i++)
		{
			NBTTagCompound outputObject = new NBTTagCompound();
			net.minecraft.server.v1_6_R1.ItemStack craft = getCraftVersion(inventory.getItem(i));

			// Convert the item stack to a NBT compound
			if (craft != null) craft.save(outputObject);

			itemList.add(outputObject);
		}

		// Now save the list
		NBTBase.a(itemList, dataOutput);

		// Serialize that array
		return new BigInteger(1, outputStream.toByteArray()).toString(32);
		// return encodeBase64(outputStream.toByteArray());
	}

	public static Inventory fromBase64(String data)
	{
		ByteArrayInputStream inputStream = new ByteArrayInputStream(new BigInteger(data, 32).toByteArray());
		// ByteArrayInputStream inputStream = new
		// ByteArrayInputStream(decodeBase64(data));
		// NBTTagList itemList = (NBTTagList) NBTBase.b(new
		// DataInputStream(inputStream));
		NBTTagList itemList = (NBTTagList) NBTBase.a(new DataInputStream(inputStream));
		Inventory inventory = new CraftInventoryCustom(null, itemList.size());

		for (int i = 0; i < itemList.size(); i++)
		{
			NBTTagCompound inputObject = (NBTTagCompound) itemList.get(i);
			// IsEmpty
			if (!inputObject.isEmpty())
			{
				inventory.setItem(i, CraftItemStack.asBukkitCopy(net.minecraft.server.v1_6_R1.ItemStack.createStack(inputObject)));
			}
		}
		// Serialize that array
		return inventory;
	}

	private static net.minecraft.server.v1_6_R1.ItemStack getCraftVersion(ItemStack stack)
	{
		if (stack != null) return CraftItemStack.asNMSCopy(stack);

		return null;
	}
}