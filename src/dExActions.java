import java.util.ArrayList;
import java.util.HashMap;

/**
* dExchange v1.x
* Copyright (C) 2011 Visual Illusions Entertainment
* @author darkdiplomat <darkdiplomat@visualillusionsent.net>
*
* This file is part of dExchange.
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with dExchange.  If not, see http://www.gnu.org/licenses/gpl.html
*/

public class dExActions {
	dExchange dEx;
	dExData dExD;
	HashMap<Player, String> SAC = new HashMap<Player, String>();
	HashMap<Player, Double> SPC = new HashMap<Player, Double>();
	ArrayList<Player> STS = new ArrayList<Player>();
	ArrayList<Inventory> STSinUse = new ArrayList<Inventory>();
	
	public dExActions(dExchange dEx){
		this.dEx = dEx;
		dExD = dEx.dExD;
	}
	
	public boolean BuyCommand(Player player, String Item, String Amount, boolean stack){
		int[] ID = dExD.getItemId(Item.toUpperCase());
		int a = 0;
		double price = 0;
		double balance = getPlayerBalance(player.getName());
		double globacc = dExD.globalaccountbalance();
		Inventory inv = player.getInventory();
		if(ID[0] == -1){
			return dExD.ErrorMessage(player, 107);
		}
		else if(ID[0] == -2){
			return dExD.ErrorMessage(player, 112);
		}
		try{
			a = Integer.parseInt(Amount);
		}catch (NumberFormatException nfe){
			a = 1;
		}
		if (a < 1){
			a = 1;
		}
		price = dExD.getItemBuyPrice(Item.toUpperCase());
		if(price == -1){
			return dExD.ErrorMessage(player, 105);
		}
		if(price == -2){
			return dExD.ErrorMessage(player, 106);
		}
		if(price == 0){
			return dExD.ErrorMessage(player, 107);
		}
		if(stack){
			if (!hasRoom(inv, ID[0], ID[1], a*64, 36)){
				return dExD.ErrorMessage(player, 121);
			}
			price *= (a*64);
			if(price > balance){
				return dExD.ErrorMessage(player, 122);
			}
			int give = a*64;
			addItem(inv, ID[0], ID[1], give, 36);
			if((globacc != -2) && (globacc != -1)){
				dExD.payglobalaccount(price);
			}
			chargePlayer(player.getName(), price);
			String L1 = dExD.pmessage(207, Amount, Item.toUpperCase());
			String L2 = dExD.pmessage(206, priceForm(price), "");
			player.sendMessage(L1);
			player.sendMessage(L2);
			dExD.logAct(304, player.getName(), "", "", "", "", "", "", "", "", "", "", Item.toUpperCase(), String.valueOf(a), "");
		}
		else{
			if (!hasRoom(inv, ID[0], ID[1], a, 36)){
				return dExD.ErrorMessage(player, 121);
			}
			price *= a;
			if(price > balance){
				return dExD.ErrorMessage(player, 122);
			}
			addItem(inv, ID[0], ID[1], a, 36);
			if((globacc != -2) && (globacc != -1)){
				dExD.payglobalaccount(price);
			}
			chargePlayer(player.getName(), price);
			inv.update();
			String L1 = dExD.pmessage(208, Amount, Item.toUpperCase());
			String L2 = dExD.pmessage(206, priceForm(price), "");
			player.sendMessage(L1);
			player.sendMessage(L2);
			dExD.logAct(305, player.getName(), "", "", "", "", "", "", "", "", "", "", Item.toUpperCase(), String.valueOf(a), "");
		}
		return true;
	}
	
	public boolean SellCommand(Player player, String Item, String Amount, boolean stack){
		int[] ID = dExD.getItemId(Item.toUpperCase());
		int a = 0;
		Inventory inv = player.getInventory();
		int take = 0;
		double price = 0;
		double globacc = dExD.globalaccountbalance();
		if(ID[0] == -1){
			return dExD.ErrorMessage(player, 107);
		}
		else if(ID[0] == -2){
			return dExD.ErrorMessage(player, 112);
		}
		try{
			a = Integer.parseInt(Amount);
		}catch (NumberFormatException nfe){
			a = 1;
		}
		if (a < 1){
			a = 1;
		}
		price = dExD.getItemSellPrice(Item.toUpperCase());
		if(price == -1){
			return dExD.ErrorMessage(player, 105);
		}
		if(price == -2){
			return dExD.ErrorMessage(player, 106);
		}
		if(price == 0){
			return dExD.ErrorMessage(player, 107);
		}
		if(stack){
			int get = a/64;
			if(!hasItem(inv, ID[0], ID[1], get, 36)){
				return dExD.ErrorMessage(player, 123);
			}
			price *= (a*64);
			if((globacc != -2) && (globacc != -1)){
				if (globacc < price){
					return dExD.ErrorMessage(player, 124);
				}
				else{
					dExD.chargeglobalaccount(price);
				}
			}
			take = a*64;
			removeItem(inv, ID[0], ID[1], take, 36);
			payPlayer(player.getName(), price);
			String L1 = dExD.pmessage(209, Amount, Item.toUpperCase());
			String L2 = dExD.pmessage(206, priceForm(price), "");
			player.sendMessage(L1);
			player.sendMessage(L2);
			dExD.logAct(306, player.getName(), "", "", "", "", "", "", "", "", "", "", Item.toUpperCase(), String.valueOf(a), "");
		}
		else{
			if(!hasItem(inv, ID[0], ID[1], a, 36)){
				return dExD.ErrorMessage(player, 123);
			}
			price *= a;
			if((globacc != -2) && (globacc != -1)){
				if (globacc < price){
					return dExD.ErrorMessage(player, 124);
				}
			}
			removeItem(inv, ID[0], ID[1], a, 36);
			payPlayer(player.getName(), price);
			String L1 = dExD.pmessage(210, Amount, Item.toUpperCase());
			String L2 = dExD.pmessage(206, priceForm(price), "");
			player.sendMessage(L1);
			player.sendMessage(L2);
			dExD.logAct(307, player.getName(), "", "", "", "", "", "", "", "", "", "", Item.toUpperCase(), String.valueOf(a), "");
		}
		return true;
	}
	
	public boolean GobalSignSell(Player player, Item item, Sign sign){
		if (item == null){ return true; }
		String name = dExD.reverseItemLookUp(item.getItemId(), item.getDamage());
		Inventory inv = player.getInventory();
		if (name == null){
			return dExD.ErrorMessage(player, 112);
		}
		int amount = item.getAmount();
		double price = dExD.getItemSellPrice(name);
		double globacc = dExD.globalaccountbalance();
		if(price == -1){
			return dExD.ErrorMessage(player, 105);
		}
		if(price == -2){
			return dExD.ErrorMessage(player, 106);
		}
		if(price == 0){
			return dExD.ErrorMessage(player, 107);
		}
		price *= amount;
		if((globacc != -2) && (globacc != -1)){
			if (globacc < price){
				return dExD.ErrorMessage(player, 124);
			}
			else{
				dExD.chargeglobalaccount(price);
			}
		}
		inv.removeItem(item.getSlot());
		inv.update();
		payPlayer(player.getName(), price);
		String L1 = dExD.pmessage(210, String.valueOf(amount), name);
		String L2 = dExD.pmessage(206, priceForm(price), "");
		player.sendMessage(L1);
		player.sendMessage(L2);
		dExD.logAct(308, player.getName(), "", "", String.valueOf(sign.getX()), String.valueOf(sign.getY()), String.valueOf(sign.getZ()), String.valueOf(sign.getWorld().getType().getId()), "", "", "", "", name, String.valueOf(amount), "");
		return true;
	}
	
	public boolean gobalBuySign(Player player, Sign sign){
		String Item = sign.getText(1);
		int[] ID = new int[2];
		int check = 0;
		Inventory inv = player.getInventory();
		try{
			check = Integer.parseInt(sign.getText(1));
		}catch (NumberFormatException nfe){
			if (Item.contains(":")){
				String[] its = Item.split(":");
				Item = dExD.reverseItemLookUp(Integer.valueOf(its[0]), Integer.valueOf(its[1]));
				check = 0;
			}
			else{
				check = 0;
			}
		}
		if(check != 0){
			Item = dExD.reverseItemLookUp(check, 0);
		}
		ID = dExD.getItemId(Item.toUpperCase());
		int a = 0;
		double price = 0;
		double balance = getPlayerBalance(player.getName());
		double globacc = dExD.globalaccountbalance();
		if(ID[0] < 1){
			return dExD.ErrorMessage(player, 112);
		}
		a = Integer.valueOf(sign.getText(2));
		price = dExD.getItemBuyPrice(Item.toUpperCase());
		if(price == -1){
			return dExD.ErrorMessage(player, 105);
		}
		if(price == -2){
			return dExD.ErrorMessage(player, 106);
		}
		if(price == 0){
			return dExD.ErrorMessage(player, 107);
		}
		price *= a;
		if(sign.getText(3) != priceForm(price)){
			sign.setText(3, priceForm(price));
		}
		if(price > balance){
			return dExD.ErrorMessage(player, 122);
		}
		if (!hasRoom(inv, ID[0], ID[1], a, 36)){
			return dExD.ErrorMessage(player, 121);
		}
		if((globacc != -2) && (globacc != -1)){
			dExD.payglobalaccount(price);
		}
		addItem(inv, ID[0], ID[1], a, 36);
		chargePlayer(player.getName(), price);
		String L1 = dExD.pmessage(208, String.valueOf(a), Item.toUpperCase());
		String L2 = dExD.pmessage(206, priceForm(price), "");
		player.sendMessage(L1);
		player.sendMessage(L2);
		dExD.logAct(309, player.getName(), "", "", String.valueOf(sign.getX()), String.valueOf(sign.getY()), String.valueOf(sign.getZ()), String.valueOf(sign.getWorld().getType().getId()), "", "", "", "", Item, String.valueOf(a), "");
		return true; 
	}
	
	public boolean priceCheck(Player player, String Item){
		double buyprice = 0;
		double sellprice = 0;
		String bp = "N/A";
		String sp = "N/A";
		int[] ID = dExD.getItemId(Item.toUpperCase());
		if(ID[0] < 1){
			return dExD.ErrorMessage(player, 112);
		}
		buyprice = dExD.getItemBuyPrice(Item.toUpperCase());
		if(buyprice > 0.009){
			bp = priceForm(buyprice);
		}
		sellprice = dExD.getItemSellPrice(Item.toUpperCase());
		if(sellprice > 0.009){
			sp = priceForm(sellprice);
		}
		String L1 = dExD.SpecM(215, Item.toUpperCase(), bp, sp, "");
		String L2 = dExD.SpecM(216, String.valueOf(ID[0]), String.valueOf(ID[1]), "", "");
		player.sendMessage(L1);
		player.sendMessage(L2);
		dExD.logAct(310, player.getName(),"","","","","","","","","","",Item,"","");
		return true;
	}
	
	public boolean PlayerBuySign(Player player, Sign sign, Chest chest){
		String pname = sign.getText(3);
		if(dExD.checkNameFix(sign.getText(3))){
			pname = dExD.NameFix(sign.getText(3));
		}
		if(pname.equals(player.getName())){
			String[] IDA = sign.getText(1).split(":");
			int[] IDD = new int[]{0,0};
			try{
				IDD[0] = Integer.parseInt(IDA[0]);
				if(IDA.length == 3){
					IDD[1] = Integer.valueOf(IDA[1]);
				}
			}catch(NumberFormatException nfe){
				IDD = dExD.getItemId(IDA[0].toUpperCase());
			}
			int invam = 0;
			Inventory invC = null;
			if(chest.findAttachedChest() != null){
				DoubleChest dchest = (DoubleChest)player.getWorld().getComplexBlock(chest.getX(), chest.getY(), chest.getZ());
				invC = (Inventory)dchest;
				invam = hasItemAmount(invC, IDD[0], IDD[1], invC.getContentsSize());
			}
			else{
				invC = (Inventory)chest;
				invam = hasItemAmount(invC, IDD[0], IDD[1], invC.getContentsSize());
			}
			String Item = dExD.reverseItemLookUp(IDD[0], IDD[1]);
			String L1 = dExD.SpecM(212, String.valueOf(chest.getX()), String.valueOf(chest.getY()), String.valueOf(chest.getZ()), String.valueOf(chest.getBlock().getWorld().getType().getId()));
			String L2 = dExD.pmessage(214, String.valueOf(invam), Item);
			player.sendMessage(L1);
			player.sendMessage(L2);
		}
		else{
			String[] IDA = sign.getText(1).split(":");
			int[] IDD = new int[]{0,0};
			try{
				IDD[0] = Integer.parseInt(IDA[0]);
				if(IDA.length == 3){
					IDD[1] = Integer.valueOf(IDA[1]);
				}
			}catch(NumberFormatException nfe){
				IDD = dExD.getItemId(IDA[0].toUpperCase());
			}
			int Amount = 0;
			Inventory invP = player.getInventory();
			Inventory invC = (Inventory)chest;
			double price = Double.valueOf(sign.getText(2));
			if(IDA.length == 2){
				Amount = Integer.valueOf(IDA[1]);
			}
			else{
				Amount = Integer.valueOf(IDA[2]);
			}
			if(chest.findAttachedChest() != null){
				DoubleChest dchest = (DoubleChest)player.getWorld().getComplexBlock(chest.getX(), chest.getY(), chest.getZ());
				invC = (Inventory)dchest;
				if (!hasItem(invC, IDD[0], IDD[1], Amount, invC.getContentsSize())){
					return dExD.ErrorMessage(player, 126);
				}
				if(!hasRoom(invP, IDD[0], IDD[1], Amount, invP.getContentsSize())){
					return dExD.ErrorMessage(player, 121);
				}
				if (price > getPlayerBalance(player.getName())){
					return dExD.ErrorMessage(player, 122);
				}
				removeItem(invC, IDD[0], IDD[1], Amount, invC.getContentsSize());
				addItem(invP, IDD[0], IDD[1], Amount, 36);
			}
			else{
				if (!hasItem(invC, IDD[0], IDD[1], Amount, invC.getContentsSize())){
					return dExD.ErrorMessage(player, 126);
				}
				if (price > getPlayerBalance(player.getName())){
					return dExD.ErrorMessage(player, 121);
				}
				removeItem(invC, IDD[0], IDD[1], Amount, invC.getContentsSize());
				addItem(invP, IDD[0], IDD[1], Amount, 36);
			}
			chargePlayer(player.getName(), price);
			payPlayer(pname, price);
			String Item = dExD.reverseItemLookUp(IDD[0], IDD[1]);
			String L1 = dExD.pmessage(208, String.valueOf(Amount), Item.toUpperCase());
			String L2 = dExD.pmessage(206, priceForm(price), "");
			player.sendMessage(L1);
			player.sendMessage(L2);
			dExD.logAct(311, player.getName(), pname, "", String.valueOf(sign.getX()), String.valueOf(sign.getY()), String.valueOf(sign.getZ()), String.valueOf(sign.getWorld().getType().getId()), "", "", "", "", Item, String.valueOf(Amount), "");
		}
		return true;
	}
	
	public boolean PlayerSellSign(Player player, Sign sign, Chest chest, Item iih){
		String pname = sign.getText(3);
		if(dExD.checkNameFix(sign.getText(3))){
			pname = dExD.NameFix(sign.getText(3));
		}
		if(pname.equals(player.getName())){
			String[] IDA = sign.getText(1).split(":");
			int[] IDD = new int[]{0,0};
			int Amount = 0;
			try{
				IDD[0] = Integer.parseInt(IDA[0]);
				if(IDA.length == 3){
					IDD[1] = Integer.valueOf(IDA[1]);
				}
			}catch(NumberFormatException nfe){
				IDD = dExD.getItemId(IDA[0].toUpperCase());
			}
			Inventory invC = (Inventory)chest;
			if(chest.findAttachedChest() != null){
				DoubleChest dchest = chest.findAttachedChest();
				invC = (Inventory)dchest;
			}
			if(IDA.length == 2){
				try{
					Amount = Integer.valueOf(IDA[1]);
				}catch (NumberFormatException nfe){
					Amount = 0;
				}
			}
			else{
				try{
					Amount = Integer.valueOf(IDA[2]);
				}catch (NumberFormatException nfe){
					Amount = 0;
				}
			}
			int room = hasRoomChest(invC, IDD[0], IDD[1], Amount, invC.getContentsSize());
			String L1 = dExD.SpecM(212, String.valueOf(chest.getX()), String.valueOf(chest.getY()), String.valueOf(chest.getZ()), String.valueOf(chest.getBlock().getWorld().getType().getId()));
			String L2 = dExD.pmessage(213, String.valueOf(room), "");
			player.sendMessage(L1);
			player.sendMessage(L2);
		}
		else{
			Inventory invP = player.getInventory();
			Inventory invC = (Inventory) chest;
			String[] IDA = sign.getText(1).split(":");
			String SignText = null;
			int[] IDD = new int[]{0,0};
			try{
				IDD[0] = Integer.parseInt(IDA[0]);
				if(IDA.length == 3){
					IDD[1] = Integer.valueOf(IDA[1]);
				}
				SignText = IDD[0]+":"+IDD[1];
			}catch(NumberFormatException nfe){
				IDD = dExD.getItemId(IDA[0].toUpperCase());
				SignText = IDA[0].toUpperCase();
			}
			if(iih != null){
				if (iih.getItemId() != IDD[0]){
					return dExD.ErrorMessage(player, 128); 
				} 
			}
			else{ 
				//no item in hand  stop here
				return true; 
			}
			int Amount = 0;
			int room = 0;
			int pAm = 0;
			double price = Double.valueOf(sign.getText(2).replace(",", "."));
			if(IDA.length == 2){
				try{
					Amount = Integer.valueOf(IDA[1]);
				}catch(NumberFormatException nfe){
					if(IDA[1].equals("*")){
						Amount = 999999999;
					}
				}
			}
			else{
				try{
					Amount = Integer.valueOf(IDA[2]);
				}catch(NumberFormatException nfe){
					if(IDA[2].equals("*")){
						Amount = 999999999;
					}
				}
			}
			if (Amount < 1){ dExD.ErrorMessage(player, 133); return true; }
			pAm = iih.getAmount();
			if (pAm < Amount){
				room = pAm;
			}
			else{
				room = Amount;
			}
			price *= room;
			if (price > getPlayerBalance(pname)){
				return dExD.ErrorMessage(player, 127);
			}
			if(chest.findAttachedChest() != null){
				DoubleChest dchest = chest.findAttachedChest();
				invC = (Inventory)dchest;
			}
			if(!hasRoom(invC, IDD[0], IDD[1], room, chest.getContentsSize())){
				return dExD.ErrorMessage(player, 125);
			}
			invP.removeItem(iih.getSlot());
			invP.update();
			addItem(invC, IDD[0], IDD[1], room, invC.getContentsSize());
			chargePlayer(pname, price);
			payPlayer(player.getName(), price);
			String Item = dExD.reverseItemLookUp(IDD[0], IDD[1]);
			String L1 = dExD.pmessage(210, String.valueOf(room), Item.toUpperCase());
			String L2 = dExD.pmessage(206, priceForm(price), "");
			player.sendMessage(L1);
			player.sendMessage(L2);
			if(Amount != 999999999){
				sign.setText(1, SignText+":"+(Amount-room));
				sign.update();
			}
			dExD.logAct(312, player.getName(), pname, "", String.valueOf(sign.getX()), String.valueOf(sign.getY()), String.valueOf(sign.getZ()), String.valueOf(sign.getWorld().getType().getId()), "", "", "", "", Item, String.valueOf(room), "");
		}
		return true;
	}
	
	public boolean ServerBuySign(Player player, Sign sign, Chest chest){
		String Item = sign.getText(1);
		int[] ID = new int[2];
		int check = 0;
		try{
			check = Integer.parseInt(Item);
		}catch (NumberFormatException nfe){
			if (Item.contains(":")){
				String[] its = Item.split(":");
				Item = dExD.reverseItemLookUp(Integer.valueOf(its[0]), Integer.valueOf(its[1]));
				check = 0;
			}
			else{
				check = 0;
			}
		}
		if(check != 0){
			Item = dExD.reverseItemLookUp(check, 0);
		}
		ID = dExD.getItemId(Item.toUpperCase());
		int a = 0;
		double price = 0;
		double globacc = dExD.globalaccountbalance();
		if(ID[0] < 1){
			return dExD.ErrorMessage(player, 112);
		}
		a = Integer.valueOf(sign.getText(2));
		price = dExD.getItemBuyPrice(Item.toUpperCase());
		if(price == -1){
			return dExD.ErrorMessage(player, 105);
		}
		if(price == -2){
			return dExD.ErrorMessage(player, 106);
		}
		if(price == 0){
			return dExD.ErrorMessage(player, 107);
		}
		price *= a;
		Inventory invC = chest;
		Inventory invP = player.getInventory();
		if(chest.findAttachedChest() != null){
			DoubleChest dchest = (DoubleChest)player.getWorld().getComplexBlock(chest.getX(), chest.getY(), chest.getZ());
			invC = (Inventory)dchest;
			if (!hasItem(invC, ID[0], ID[1], a, invC.getContentsSize())){
				return dExD.ErrorMessage(player, 126);
			}
			if(!hasRoom(invP, ID[0], ID[1], a, 36)){
				return dExD.ErrorMessage(player, 121);
			}
			if (price > getPlayerBalance(player.getName())){
				return dExD.ErrorMessage(player, 122);
			}
			if((globacc != -2) && (globacc != -1)){
				dExD.payglobalaccount(price);
			}
			removeItem(invC, ID[0], ID[1], a, invC.getContentsSize());
			addItem(invP, ID[0], ID[1], a, 36);
		}
		else{
			if (!hasItem(invC, ID[0], ID[1], a, invC.getContentsSize())){
				return dExD.ErrorMessage(player, 126);
			}
			if(!hasRoom(invP, ID[0], ID[1], a, 36)){
				return dExD.ErrorMessage(player, 121);
			}
			if (price > getPlayerBalance(player.getName())){
				return dExD.ErrorMessage(player, 122);
			}
			if((globacc != -2) && (globacc != -1)){
				dExD.payglobalaccount(price);
			}
			removeItem(invC, ID[0], ID[1], a, invC.getContentsSize());
			addItem(invP, ID[0], ID[1], a, 36);
		}
		chargePlayer(player.getName(), price);
		String L1 = dExD.pmessage(208, String.valueOf(a), Item.toUpperCase());
		String L2 = dExD.pmessage(206, priceForm(price), "");
		player.sendMessage(L1);
		player.sendMessage(L2);
		dExD.logAct(319, player.getName(), "", "", String.valueOf(sign.getX()), String.valueOf(sign.getY()), String.valueOf(sign.getZ()), String.valueOf(sign.getWorld().getType().getId()), "", "", "", "", Item, String.valueOf(a), "");
		return true; 
	}
	
	public boolean hasRoom(Inventory inv, int ID, int Damage, int amount, int size){
		int room = 0;
		for (int i = 0; i < size; i++){
			Item item = inv.getItemFromSlot(i);
			if (item != null){
				if (item.getItemId() == ID){
					if (item.getDamage() == Damage){
						int am = 64 - item.getAmount();
						if (am > 0){
							room += am;
						}
					}
				}
			}
			else{
				room += 64;
			}
		}
		if(room >= amount){ 
			return true;
		}
		return false;
	}
	
	public int hasRoomChest(Inventory inv, int ID, int Damage, int amount, int size){
		int room = 0;
		for (int i = 0; i < size; i++){
			Item item = inv.getItemFromSlot(i);
			if (item != null){
				if (item.getItemId() == ID){
					if (item.getDamage() == Damage){
						int am = 64 - item.getAmount();
						if (am > 0){
							room += am;
						}
					}
				}
			}
			else{
				room += 64;
			}
		}
		return room;
	}
	
	private void addItem(Inventory inv, int ID, int Damage, int amount, int size){
		for (int i = 0; i < size; i++){
			if (amount > 0){
				Item item = inv.getItemFromSlot(i);
				if (item != null){
					int iam = item.getAmount();
					if (item.getItemId() == ID){
						if (item.getDamage() == Damage){
							if (amount > 64){
								if(iam < amount){
									item.setAmount(64);
									amount -= (64 - iam);
								}
								else{
									if(iam < amount){
										item.setAmount(iam+amount);
										amount -= (64 - iam);
									}
								}
							}
							else{
								if(iam < 64 && (iam+amount < 64)){
									item.setAmount(iam+amount);
									amount -= (64 - iam);
								}
								else{
									item.setAmount(64);
									amount -= (64-iam);
								}
							}
						}
					}
				}
				else{
					if (amount > 64){
						inv.setSlot(ID, 64, Damage, i);
						amount -= 64;
					}
					else{
						inv.setSlot(ID, amount, Damage, i);
						amount = 0;
					}
				}
			}
			else{
				break;
			}
		}
	}
	
	public void removeItem(Inventory inv, int ID, int damage, int amount, int size){
		for (int i = 0; i < size; i++){
			if (amount > 0){
				Item item = inv.getItemFromSlot(i);
				if(item != null){
					int ia = item.getAmount();
					if(item.getItemId() == ID){
						if(item.getDamage() == damage){
							if(ia <= amount){
								inv.removeItem(i);
								inv.update();
								amount -= ia;
							}
							else{
								item.setAmount((ia-amount));
								inv.update();
								amount -= ia;
							}
						}
					}
				}
			}
			else{
				break;
			}
		}
	}
	
	public boolean hasItem(Inventory inv, int ID, int damage, int amount, int size){
		int iam = 0;
		for (int i = 0; i < size; i++){
			Item item = inv.getItemFromSlot(i);
			if (item != null){
				if (item.getItemId() == ID){
					if (item.getDamage() == damage){
						iam += item.getAmount();
					}
				}
			}
		}
		if(iam >= amount){ 
			return true;
		}
		return false;
	}
	
	public int hasItemAmount(Inventory inv, int ID, int damage, int size){
		int am = 0;
		for (int i = 0; i < size; i++){
			Item item = inv.getItemFromSlot(i);
			if (item != null){
				if (item.getItemId() == ID){
					if (item.getDamage() == damage){
						am += item.getAmount();
					}
				}
			}
		}
		return am;
	}
	
	public boolean displayList(Player player, String pagenum){
		ArrayList<String> list = dExD.ListLine();
		int total = 0, c = 1;
		for (int pl = list.size(); pl > 5; pl -= 5){
			total++;
			c = pl;
		}
		if (c > 0) total++;
		int show = 0;
		try{
			show = Integer.parseInt(pagenum);
		}catch (NumberFormatException nfe){
			show = 1;
		}
		if (show > total || show < 1){
			show = 1;
		}
		int page = 5 * show;
		int start = 5 * show - 5;
		player.sendMessage("§aList of §6ITEMS §a Page §e" + show + "§a of §e" + total);
		for (int i = start; i < page && i < list.size(); i++){
			String WholeLine = list.get(i);
			String[] linesplitname = WholeLine.split("=");
			String[] IDPRICE = linesplitname[1].split(",");
			String[] IDDA = IDPRICE[0].split(":");
			player.sendMessage("§aName: §6"+linesplitname[0]+" §aID: §6" + IDDA[0] + " §aDamage: §6" + IDDA[1]);
			player.sendMessage("§aBuyPrice: §e" + IDPRICE[1] + " §aSellPrice: §e" + IDPRICE[2]);
		}
		return true;
	}
	
	public double getPlayerBalance(String PName){
		double balance = (Double)etc.getLoader().callCustomHook("dCBalance", new Object[]{"Player-Balance", PName});
		return balance;
	}
	
	public void chargePlayer(String PName, double amount){
		etc.getLoader().callCustomHook("dCBalance", new Object[]{"Player-Charge", PName, amount});
	}
	
	public void payPlayer(String PName, double amount){
		etc.getLoader().callCustomHook("dCBalance", new Object[]{"Player-Pay", PName, amount});
	}
	
	public boolean makeGShop(Player player, Sign sign){
		if(GSfailchecks(player, sign)){ 
			return true;
		}
		double price = dExD.getItemBuyPrice(sign.getText(1).toUpperCase());
		int amount = Integer.valueOf(sign.getText(2));
		sign.setText(0, "§1[G-SHOP]");
		sign.setText(1, sign.getText(1).toUpperCase());
		sign.setText(3, priceForm(price*amount));
		String mess = dExD.pmessage(201, "", "");
		player.sendMessage(mess);
		dExD.logAct(301, player.getName(), "", "G-SHOP", String.valueOf(sign.getX()), String.valueOf(sign.getY()), String.valueOf(sign.getZ()), String.valueOf(sign.getWorld().getType().getId()), "", "", "", "", "", "", "");
		return false;
	}
	
	public boolean makeGTrade(Player player, Sign sign){
		if((!player.canUseCommand("/dexpgts"))&&(!player.canUseCommand("/dexadmin"))&&(!player.canUseCommand("/dexall"))){
			return dExD.ErrorMessage(player, 101);
		}
		else{
			sign.setText(0, "§2[G-TRADE]");
			sign.setText(1, "Ready to");
			sign.setText(2, "take items!");
			sign.setText(3, "");
			String mess = dExD.pmessage(202, "", "");
			player.sendMessage(mess);
			dExD.logAct(301, player.getName(),"", "G-TRADE", String.valueOf(sign.getX()), String.valueOf(sign.getY()), String.valueOf(sign.getZ()), String.valueOf(sign.getWorld().getType().getId()), "", "", "", "", "", "", "");
			return false;
		}
	}
	
	public boolean makePShop(Player player, Sign sign){
		if(Pfailchecks(player, sign, false)){
			return true;
		}
		double price = Double.valueOf(sign.getText(2).replace(",", "."));
		sign.setText(0, "§5[P-SHOP]");
		sign.setText(1, sign.getText(1).toUpperCase());
		sign.setText(2, priceForm(price));
		String pname = player.getName();
		if (player.getName().length() > 15){
			pname = player.getName().substring(0, 15);
			dExD.setNameFix(player.getName(), pname);
		}
		sign.setText(3, pname);
		dExD.openlink(player, sign);
		String mess = dExD.pmessage(203, "", "");
		player.sendMessage(mess);
		dExD.logAct(301, player.getName(),"", "P-SHOP", String.valueOf(sign.getX()), String.valueOf(sign.getY()), String.valueOf(sign.getZ()), String.valueOf(sign.getWorld().getType().getId()), "", "", "", "", "", "", "");
		dExD.addPlayerSignTotal(player.getName());
		return false;
	}
	
	public boolean makePTrade(Player player, Sign sign){
		if(Pfailchecks(player, sign, true)){
			return true;
		}
		double price = Double.valueOf(sign.getText(2).replace(",", "."));
		sign.setText(0, "§9[P-TRADE]");
		sign.setText(1, sign.getText(1).toUpperCase());
		sign.setText(2, priceForm(price));
		String pname = player.getName();
		if (player.getName().length() > 15){
			pname = player.getName().substring(0, 15);
			dExD.setNameFix(player.getName(), pname);
		}
		sign.setText(3, pname);
		dExD.openlink(player, sign);
		String mess = dExD.pmessage(203, "", "");
		player.sendMessage(mess);
		dExD.logAct(301, player.getName(),"", "P-TRADE", String.valueOf(sign.getX()), String.valueOf(sign.getY()), String.valueOf(sign.getZ()), String.valueOf(sign.getWorld().getType().getId()), "", "", "", "", "", "", "");
		dExD.addPlayerSignTotal(player.getName());
		return false;
	}
	
	public boolean makeSShop(Player player, Sign sign){
		if(GSfailchecks(player, sign)){ 
			return true;
		}
		double price = dExD.getItemBuyPrice(sign.getText(1).toUpperCase());
		int amount = Integer.valueOf(sign.getText(2));
		sign.setText(0, "§7[S-SHOP]");
		sign.setText(1, sign.getText(1).toUpperCase());
		sign.setText(3, priceForm(price*amount));
		dExD.openlink(player, sign);
		String mess = dExD.pmessage(203, "", "");
		player.sendMessage(mess);
		dExD.logAct(301, player.getName(),"", "S-SHOP", String.valueOf(sign.getX()), String.valueOf(sign.getY()), String.valueOf(sign.getZ()), String.valueOf(sign.getWorld().getType().getId()), "", "", "", "", "", "", "");
		return false;
	}
	
	private boolean GSfailchecks(Player player, Sign sign){
		int idsign = 0;
		if((!player.canUseCommand("/dexpsss"))&&(!player.canUseCommand("/dexadmin"))&&(!player.canUseCommand("/dexall"))){
			NoPlaceSign(sign);
			return dExD.ErrorMessage(player, 101);
		}
		if (sign.getText(1).equals("")){
			NoPlaceSign(sign);
			return dExD.ErrorMessage(player, 103);
		}
		if(sign.getText(2).equals("")){
			NoPlaceSign(sign);
			return dExD.ErrorMessage(player, 104);
		}
		double price = 0;
		String idcheck = sign.getText(1);
		String[] idsplit = idcheck.split(":");
		String name = sign.getText(1).toUpperCase();
		int amount = 0;
		if (idsplit.length == 1){ idsplit = new String[]{idsplit[0], String.valueOf(0)}; }
		try{
			idsign = Integer.parseInt(idsplit[0]);
		}catch (NumberFormatException nfe){
			idsign = -1;
			int[] ID = dExD.getItemId(sign.getText(1).toUpperCase());
			if(ID[0] == -1){
				NoPlaceSign(sign);
				return dExD.ErrorMessage(player, 112);
			}
		}
		if (idsign != -1){
			name = dExD.reverseItemLookUp(Integer.valueOf(idsplit[0]), Integer.valueOf(idsplit[1]));
			if (name == null){ 
				NoPlaceSign(sign); 
				return dExD.ErrorMessage(player, 112);
			}
			price = dExD.getItemBuyPrice(name);
		}
		else{
			name = sign.getText(1).toUpperCase();
			price = dExD.getItemBuyPrice(sign.getText(1).toUpperCase());
		}
		if(price == -1){
			NoPlaceSign(sign);
			return dExD.ErrorMessage(player, 105);
		}
		if(price == -2){
			NoPlaceSign(sign);
			return dExD.ErrorMessage(player, 106);
		}
		if(price == 0){
			NoPlaceSign(sign);
			return dExD.ErrorMessage(player, 107);
		}
		try{
			amount = Integer.parseInt(sign.getText(2));
		}catch (NumberFormatException nfe){
			NoPlaceSign(sign);
			return dExD.ErrorMessage(player, 108);
		}
		if (amount < 1){
			NoPlaceSign(sign);
			return dExD.ErrorMessage(player, 108);
		}
		return false;
	}
	
	private boolean Pfailchecks(Player player, Sign sign, boolean isPTrade){
		int idsign = 0;
		if(!dExD.canmakemore(player)){
			NoPlaceSign(sign);
			return dExD.ErrorMessage(player, 135);
		}
		if((!player.canUseCommand("/dexppts"))&&(!player.canUseCommand("/dexadmin"))&&(!player.canUseCommand("/dexall"))){
			NoPlaceSign(sign);
			return dExD.ErrorMessage(player, 101);
		}
		if(dExD.checklink(player)){
			NoPlaceSign(sign);
			return dExD.ErrorMessage(player, 109);
		}
		if(sign.getText(1).equals("")){
			NoPlaceSign(sign);
			return dExD.ErrorMessage(player, 103);
		}
		if(sign.getText(2).equals("")){
			NoPlaceSign(sign);
			return dExD.ErrorMessage(player, 110);
		}
		String IDA = sign.getText(1);
		String[] IDASplit = IDA.split(":");
		String name = IDA;
		int[] IDD = new int[]{0,0};
		int amount = 0;
		double price = 0;
		if (IDASplit.length < 2){
			NoPlaceSign(sign);
			return dExD.ErrorMessage(player, 111);
		}
		if(IDASplit.length == 2){
			try{
				IDD[0] = Integer.parseInt(IDASplit[0]);
			}catch (NumberFormatException nfe){
				idsign = -1;
				IDD = dExD.getItemId(IDASplit[0].toUpperCase());
				if(IDD[0] == -1){
					NoPlaceSign(sign);
					return dExD.ErrorMessage(player, 112);
				}
			}
			if (idsign != -1){
				name = dExD.reverseItemLookUp(IDD[0],IDD[1]);
				if (name == null){ 
					NoPlaceSign(sign); 
					return dExD.ErrorMessage(player, 112);
				}
			}
			else{
				name = sign.getText(1);
			}
			try{
				amount = Integer.parseInt(IDASplit[1]);
			}catch (NumberFormatException nfe){
				if(IDASplit[2].equals("*") && isPTrade){
					amount = 99999;
				}
				else{
					NoPlaceSign(sign);
					return dExD.ErrorMessage(player, 108);
				}
			}
			if (amount < 1){
				NoPlaceSign(sign);
				return dExD.ErrorMessage(player, 108);
			}
		}
		else{
			try{
				IDD[0] = Integer.parseInt(IDASplit[0]);
			}catch (NumberFormatException nfe){
				NoPlaceSign(sign);
				return dExD.ErrorMessage(player, 113);
			}
			if (IDD[0] < 1){
				NoPlaceSign(sign);
				return dExD.ErrorMessage(player, 113);
			}
			try{
				IDD[1] = Integer.parseInt(IDASplit[1]);
			}catch (NumberFormatException nfe){
				NoPlaceSign(sign);
				return dExD.ErrorMessage(player, 114);
			}
			if (IDD[1] < 0){
				NoPlaceSign(sign);
				return dExD.ErrorMessage(player, 114);
			}
			try{
				amount = Integer.parseInt(IDASplit[2]);
			}catch (NumberFormatException nfe){
				if(IDASplit[2].equals("*") && isPTrade){
					amount = 99999;
				}
				else{
					NoPlaceSign(sign);
					return dExD.ErrorMessage(player, 108);
				}
			}
			if (amount < 1){
				NoPlaceSign(sign);
				return dExD.ErrorMessage(player, 108);
			}
		}
		if(dExD.blacklisteditem(IDD[0])){
			return dExD.ErrorMessage(player, 115);
		}
		try{
			price = Double.parseDouble(sign.getText(2).replace(",", "."));
		}catch(NumberFormatException nfe){
			return dExD.ErrorMessage(player, 116);
		}
		if(price < 0.01){
			return dExD.ErrorMessage(player, 116);
		}
		return false;
	}
	
	public boolean setSignPrice(Player player, String price){
		if(SPC.containsKey(player)){ return dExD.ErrorMessage(player, 129); }
		if(SAC.containsKey(player)){ return dExD.ErrorMessage(player, 130); }
		double newprice = 0;
		try{
			newprice = Double.parseDouble(price.replace(",", "."));
		}catch(NumberFormatException nfe){
			return dExD.ErrorMessage(player, 116);
		}
		if(newprice < 0.01){ return dExD.ErrorMessage(player, 116); }
		SPC.put(player, newprice);
		String mess = dExD.pmessage(217, "", "");
		player.sendMessage(mess);
		return true;
	}
	
	public boolean setSignAmount(Player player, String amount){
		if(SPC.containsKey(player)){ return dExD.ErrorMessage(player, 129); }
		if(SAC.containsKey(player)){ return dExD.ErrorMessage(player, 130); }
		if(!amount.equals("*")){
			int a = 0;
			try{
				a = Integer.valueOf(amount);
			}catch (NumberFormatException nfe){
				return dExD.ErrorMessage(player, 108);
			}
			if(a < 1){
				return dExD.ErrorMessage(player, 108);
			}
		}
		SAC.put(player, amount);
		String mess = dExD.pmessage(219, "", "");
		player.sendMessage(mess);
		return true;
	}
	
	public boolean SignChange(Player player, Sign sign){
		if(SPC.containsKey(player)){
			String oldPrice = sign.getText(2);
			sign.setText(2, priceForm(SPC.get(player)));
			sign.update();
			String mess = dExD.pmessage(218, "", "");
			player.sendMessage(mess);
			if(sign.getText(0).equals("§9[P-TRADE]")){
				dExD.logAct(313, player.getName(),"", "P-TRADE", String.valueOf(sign.getX()), String.valueOf(sign.getY()), String.valueOf(sign.getZ()), String.valueOf(sign.getWorld().getType().getId()), "", "", "", "", "", priceForm(SPC.get(player)), oldPrice);
			}
			else{
				dExD.logAct(313, player.getName(),"", "P-SHOP", String.valueOf(sign.getX()), String.valueOf(sign.getY()), String.valueOf(sign.getZ()), String.valueOf(sign.getWorld().getType().getId()), "", "", "", "", "", priceForm(SPC.get(player)), oldPrice);
			}
			SPC.remove(player);
		}
		else if(SAC.containsKey(player)){
			if((SAC.get(player).equals("*")) && (sign.getText(0).equals("§5[P-SHOP]"))){ SAC.remove(player); return dExD.ErrorMessage(player, 131); }
			String[] text1 = sign.getText(1).split(":");
			String oldAmount = null;
			if(text1.length == 2){
				oldAmount = text1[1];
				text1[1] = String.valueOf(SAC.get(player));
				sign.setText(1, text1[0] + ":" + text1[1]);
				sign.update();
			}
			else if(text1.length == 3){
				oldAmount = text1[2];
				text1[2] = String.valueOf(SAC.get(player));
				sign.setText(1, text1[0]+":"+text1[1]+":"+text1[2]);
				sign.update();
			}
			String mess = dExD.pmessage(220, "", "");
			player.sendMessage(mess);
			if(sign.getText(0).equals("§9[P-TRADE]")){
				dExD.logAct(314, player.getName(),"", "P-TRADE", String.valueOf(sign.getX()), String.valueOf(sign.getY()), String.valueOf(sign.getZ()), String.valueOf(sign.getWorld().getType().getId()), "", "", "", "", "", SAC.get(player), oldAmount);
			}
			else{
				dExD.logAct(314, player.getName(),"", "P-SHOP", String.valueOf(sign.getX()), String.valueOf(sign.getY()), String.valueOf(sign.getZ()), String.valueOf(sign.getWorld().getType().getId()), "", "", "", "", "", SAC.get(player), oldAmount);
			}
			SAC.remove(player);
		}
		return true;
	}
	
	public boolean addItemList(Player player, String IName, String ID, String Damage, String BPrice, String SPrice){
		if(dExD.IDD.containsKey(IName.toUpperCase())){ return dExD.ErrorMessage(player, 134); }
		int id = 0, dam = 0;
		double bp = 0, sp = 0;
		try{
			id = Integer.parseInt(ID);
		}catch(NumberFormatException NFE){
			return dExD.ErrorMessage(player, 113);
		}
		if(id <= 0){ return dExD.ErrorMessage(player, 113); }
		try{
			dam = Integer.parseInt(Damage);
		}catch(NumberFormatException NFE){
			return dExD.ErrorMessage(player, 114);
		}
		if(dam < 0){ return dExD.ErrorMessage(player, 114); }
		try{
			bp = Double.parseDouble(BPrice);
		}catch(NumberFormatException NFE){
			return dExD.ErrorMessage(player, 116);
		}
		if(bp < 0){ return dExD.ErrorMessage(player, 116);}
		try{
			sp = Double.parseDouble(SPrice);
		}catch(NumberFormatException NFE){
			return dExD.ErrorMessage(player, 116);
		}
		if(sp < 0){ return dExD.ErrorMessage(player, 116);}
		dExD.addItemToList(IName.toUpperCase(), id, dam, bp, sp);
		String mess = dExD.pmessage(223, "", "");
		player.sendMessage(mess);
		dExD.logAct(315, player.getName(), priceForm(sp), IName.toUpperCase(), "", "", "", "", "", "", "", "", ID, Damage, priceForm(bp));
		return true;
	}
	
	public boolean removeItemFromList(Player player, String IName){
		if(!dExD.IDD.containsKey(IName.toUpperCase())){ return dExD.ErrorMessage(player, 112); }
		dExD.removeItem(IName.toUpperCase());
		String mess = dExD.pmessage(224, "", "");
		player.sendMessage(mess);
		dExD.logAct(316, player.getName(), "", IName.toUpperCase(), "", "", "", "", "", "", "", "", "", "", "");
		return true;
	}
	
	public boolean updateItemBPrice(Player player, String IName, String BPrice){
		if(!dExD.IDD.containsKey(IName.toUpperCase())){ return dExD.ErrorMessage(player, 112); }
		double bp = 0;
		try{
			bp = Double.parseDouble(BPrice.replace(",", "."));
		}catch(NumberFormatException NFE){
			return dExD.ErrorMessage(player, 116);
		}
		if(bp < 0){ return dExD.ErrorMessage(player, 116); }
		dExD.updateItemBuyPrice(IName.toUpperCase(), bp);
		String mess = dExD.pmessage(225, "", "");
		player.sendMessage(mess);
		dExD.logAct(317, player.getName(), "", IName.toUpperCase(), "", "", "", "", "", "", "", "", "", "", priceForm(bp));
		return true;
	}
	
	public boolean updateItemSPrice(Player player, String IName, String SPrice){
		if(!dExD.IDD.containsKey(IName.toUpperCase())){ return dExD.ErrorMessage(player, 112); }
		double sp = 0;
		try{
			sp = Double.parseDouble(SPrice.replace(",", "."));
		}catch(NumberFormatException NFE){
			return dExD.ErrorMessage(player, 116);
		}
		if(sp < 0){ return dExD.ErrorMessage(player, 116); }
		dExD.updateItemSellPrice(IName.toUpperCase(), sp);
		String mess = dExD.pmessage(226, "", "");
		player.sendMessage(mess);
		dExD.logAct(318, player.getName(), "", IName.toUpperCase(), "", "", "", "", "", "", "", "", "", "", priceForm(sp));
		return true;
	}
	
	public String priceForm(double price){
		String newprice = String.valueOf(price);
		String[] form = newprice.split("\\.");
		if(form[1].length() == 1){
			newprice += "0";
		}
		else{
			newprice = form[0] + "." + form[1].substring(0, 2);
		}
		return newprice;
	}
	
	public boolean cancelSignChange(Player player, boolean isSAC, boolean All){
		if(All){
			if(SAC.containsKey(player)){
				SAC.remove(player);
			}
			if(SPC.containsKey(player)){
				SPC.remove(player);
			}
			return true;
		}
		else{
			if(isSAC){
				if(SAC.containsKey(player)){
					SAC.remove(player);
				}
				String mess = dExD.pmessage(221, "", "");
				player.sendMessage(mess);
			}
			else{
				if(SPC.containsKey(player)){
					SPC.remove(player);
				}
				String mess = dExD.pmessage(222, "", "");
				player.sendMessage(mess);
			}
			return true;
		}
	}
	
	private void NoPlaceSign(Sign sign){
		Location loc = new Location(sign.getWorld(), sign.getX(), sign.getY(), sign.getZ());
		sign.getWorld().dropItem(loc, 323);
		sign.getWorld().setBlockAt(0, sign.getX(), sign.getY(), sign.getZ());
	}
	
	public boolean STSPlayer(Player player){
		return STS.contains(player);
	}
	
	public boolean STSIsInUse(Inventory inv){
		return STSinUse.contains(inv);
	}
	
	public void addSTSInv(Inventory inv){
		if(!STSinUse.contains(inv)){
			STSinUse.add(inv);
		}
	}
	
	public boolean onStartSTrade(Player player){
		if(!player.canUseCommand("/dexpsts")){
			return dExD.ErrorMessage(player, 102);
		}
		if(!STS.contains(player)){
			STS.add(player);
		}
		String mess = dExD.pmessage(228, "", "");
		player.sendMessage(mess);
		return true;
	}
	
	public boolean onSTradePlace(Player player, Block chest){
		float rot = player.getRotation();
		if(rot < 0){ rot *= -1; } //Fix negative rotation
		Block block = new Block();
		block.setType(63);
		block.setX(chest.getX());
		block.setY(chest.getY()+1);
		block.setZ(chest.getZ());
		Sign sign = null;
		if((rot >= 0 && rot < 45) || (rot >= 315 && rot < 361)){
			block.setData(0 | 0x8);
			player.getWorld().setBlock(block);
			sign = (Sign)player.getWorld().getComplexBlock(block);
		}
		else if(rot >= 45 && rot < 115){
			block.setData(0 | 0x4);
			player.getWorld().setBlock(block);
			sign = (Sign)player.getWorld().getComplexBlock(block);
		}
		else if(rot >= 115 && rot < 225){
			block.setData(0 | 0x0);
			player.getWorld().setBlock(block);
			sign = (Sign)player.getWorld().getComplexBlock(block);
		}
		else if(rot >= 225 && rot < 315){
			block.setData(0 | 0xC);
			player.getWorld().setBlock(block);
			sign = (Sign)player.getWorld().getComplexBlock(block);
		}
		if(sign != null){
			sign.setText(0, "§6[S-TRADE]");
			sign.setText(1, "§2Ready To");
			sign.setText(2, "§2Accept");
			sign.setText(3, "~~~~~~~~");
			sign.update();
			STS.remove(player);
			dExD.logAct(301, player.getName(),"", "S-TRADE", String.valueOf(sign.getX()), String.valueOf(sign.getY()), String.valueOf(sign.getZ()), String.valueOf(sign.getWorld().getType().getId()), "", "", "", "", "", "", "");
		}
		else{
			player.notify("An error occurred");
		}
		return false;
	}
	
	public void onSTradeActivate(Player player, Inventory inv){
		Inventory invp = player.getInventory();
		Chest chest = (Chest)inv;
		Block block = player.getWorld().getBlockAt(chest.getX(), chest.getY()+1, chest.getZ());
		if(block.getType() == 63){
			Sign sign = (Sign)player.getWorld().getComplexBlock(block);
			if(sign.getText(0).equals("§6[S-TRADE]")){
				double payout = 0;
				boolean notempty = false, notallsold = false;
				StringBuilder items = new StringBuilder();
				for(Item item : inv.getContents()){
					if(item != null){
						notempty = true;
						int id = item.getItemId(), amount = item.getAmount(), damage = item.getDamage();
						String name = dExD.reverseItemLookUp(id, damage);
						if (name == null){
							notallsold = true;
							addItem(invp, id, damage, amount, 36);
							inv.removeItem(item);
							continue;
						}
						double price = dExD.getItemSellPrice(name);
						if(price == -1){
							notallsold = true;
							addItem(invp, id, damage, amount, 36);
							inv.removeItem(item);
							continue;
						}
						else if(price == -2){
							notallsold = true;
							addItem(invp, id, damage, amount, 36);
							inv.removeItem(item);
							continue;
						}
						else if(price == 0){
							notallsold = true;
							addItem(invp, id, damage, amount, 36);
							inv.removeItem(item);
							continue;
						}
						price *= amount;
						payout += price;
						inv.removeItem(item);
						items.append(name+"("+amount+") ");
					}
				}
				if(notempty){
					payPlayer(player.getName(), payout);
					String mess = dExD.pmessage(227, priceForm(payout), "");
					player.sendMessage(mess);
					if(notallsold){
						dExD.ErrorMessage(player, 139);
					}
					dExD.logAct(318, player.getName(), "", "", "", "", "", "", String.valueOf(chest.getX()), String.valueOf(chest.getY()), String.valueOf(chest.getZ()), String.valueOf(chest.getWorld().getType().getId()), items.toString(), "", "");
				}
			}
			STSinUse.remove(inv);
		}
	}
	
	public boolean onSTradeDestroy(Player player, Block chest){
		if(!player.canUseCommand("/dexpsts") || !player.canUseCommand("/dexadmin")){
			return dExD.ErrorMessage(player, 120);
		}
		Block block = player.getWorld().getBlockAt(chest.getX(), chest.getY()+1, chest.getZ());
		block.setType(0);
		block.update();
		return false;
	}
}
