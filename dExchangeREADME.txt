dExchange v2.x
Copyrighted (c) 2011 Visual Illusions Entertainment
Authored by: darkdiplomat

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see http://www.gnu.org/licenses/gpl.html .

Features:
	Global Shop Signs/Commands
	Item Lists with Global Price Checking
	Player Shops
	Remote chests for Player shops

Permissions:
	/dexall		- use/place all signs
	/dexadmin	- use/place/destory all signs
	/dexpgss	- place G-SHOP Signs
	/dexugss	- use G-SHOP Signs
	/dexpgts	- place G-TRADE Signs
	/dexugts	- use G-TRADE Signs
	/dexppss	- place P-SHOP Signs
	/dexupts	- use P-SHOP Signs
	/dexppts	- place P-TRADE Signs
	/dexupts	- use P-TRADE Signs
	/dexbc		- use /dex buy
	/dexsc		- use /dex sell

Commands:
	/dex buy <ItemName> <amount>	- buy an item from Global
	/dex sell <ItemName> <amount>	- sell an item to Global
	/dex <ItemName> 				- Checks Item ID Damage Buy Price and Sell Price
	/dex list [page#] 				- displays a list of Items and Prices

Sign Formats:

	[G-SHOP] - used to buy items from a global shop
		Line1 = [g-shop]
		Line2 = ItemName
		Line3 = Amount
		Line4 = Price (auto filled in)

	[G-TRADE] - used to sell items to a global shop
		Line1: [g-trade]
		All other lines auto filled

	[P-SHOP] - Player owned Shop
		Line1 = [p-shop]
		Line2 = ID:DAMAGE:AMOUNT or ID:AMOUNT or ITEMNAME:AMOUNT
		Line 3 = Price the player wants to charge for the item
		Line 4 = Player's Name (auto filled in)

		Place or Left click a chest to complete the link

	[P-TRADE] - Player owned sell sign
		Ok a little more info on P-TRADE  this allows players to sell items to the player who owns the sign. 
		Amount is the amount the player wants of an item. and will decrease as the items are sold to the player.

		Line1 = [p-trade]
		Line2 = ID:DAMAGE:AMOUNT or ID:AMOUNT or ITEMNAME:AMOUNT
		Line3 = Price the player wants to charge for the item
		Line4 = Player's Name (auto filled in)

		Place or Left click a chest to complete the link

Configuration Files:
	plugins/config/dExchange/
		dExSettings.ini - Basic Settings
		Global Account= Account to use for Global Actions (takes and gives money to this account) (must be a joint account) (set to N/A to disable)
		BlackListedItems= Items that players are not allowed to sell/buy via P-SHOP or P-TRADE signs
		LogActions= logs actions of dExchange to the dConomy logs
		Use-MySQL= set to true to use MySQL
		Use-CanaryMySQL= set to true to use Canary's MySQL connection settings
		UserName= Username for database
		Password= Password for database
		DataBase= database for where MySQL data is stored

Logging Transactions:
	If turned on it will log transactions through dConomy's Log system (requires logging enabled in dConomy)

MySQL Things:
	There are 2 tables for dExchange which need be created manual before MySQL is turned on
		dExchange - Item Lists for ID Damage BuyPrice(Global) and SellPrice(Global)
		dExchangeChests - Locations of Signs and Chests for P-SHOP and P-TRADE signs
	
FlatFile Things:[/b][/size]
	ItemNameFile - List of Items and Prices
	ChestTrade - List of Sign/Chest locations used for P-SHOP and P-TRADE