
public class dExMessages {
	dExData dExD;
	PropertiesFile Mess;
	String 
	/*Error Messages*/
	E101 = "You do not have permission to place that!",
	E102 = "You do not have permission to use that!",
	E103 = "You did not specify an Item to sell!",
	E104 = "You did not specify an Amount to sell!",
	E105 = "Price not found!",
	E106 = "The was an issue getting the price!",
	E107 = "The Item is currently not for sell!",
	E108 = "You entered an invalid amount!",
	E109 = "Please link you other sign before making a new one!",
	E110 = "You did not specified a price!",
	E111 = "You did not fill in Item:Amount fully!",
	E112 = "The Item could not be found!",
	E113 = "You entered an invalid Item ID!",
	E114 = "You entered an invalid Item Damage value!",
	E115 = "That Item is banned!",
	E116 = "You entered an invalid price!",
	E117 = "This sign is missing it's chest!",
	E118 = "ACCESS DENIED!",
	E119 = "You can not break a sign you do not own!",
	E120 = "You can not break a chest you do not own!",
	E121 = "You do not have enough room in your inventory for the items!",
	E122 = "You do not have enough money to buy that!",
	E123 = "You do not have that much to sell!",
	E124 = "The Global Account does not have the funds for this transaction!",
	E125 = "There was not enough room in the chest!",
	E126 = "There was not enough in the chest!",
	E127 = "The owner does not have enough money to pay!",
	E128 = "This sign is not accepting that item!",
	E129 = "You are already set to change a sign's price!",
	E130 = "You are already set to change a sign's amount!",
	E131 = "You can not set a P-SHOP amount to '*'!",
	E132 = "You can not change a sign you do not own!",
	E133 = "This sign cannot take anymore items!",
	E134 = "That Item is already listed!",
	E135 = "You have maxed your sign limit already!",
	E136 = "You cannot attach a chest to one not owned!",
	E137 = "This chest is protected and you do not own it!",
	E138 = "This chest is part of a link you do not own!",
	E139 = "Not all items were sold and have been returned to you.",
	E140 = "This chest is already in use!",
	E141 = "S-Trade chests cannot be Double Chests!",

	/*Regular Messages*/
	M201 = "<lightblue>Global Shop Sign Created!",
	M202 = "<lightblue>Global Trade Sign Created!",
	M203 = "<lightblue>You now need to place/left click a chest to complete the link.",
	M204 = "<lightblue>Link Created!",
	M205 = "<rose>Link Canceled!",
	M206 = "<lightblue>for <yellow><A> <M>",
	M207 = "<lightblue>You have purchased <yellow><A> Stacks<lightblue> of <gold><I>",
	M208 = "<lightblue>You have purchased <yellow><A><lightblue> of <gold><I>",
	M209 = "<lightblue>You have sold <yellow><A> Stacks<lightblue> of <gold><I>",
	M210 = "<lightblue>You have sold <yellow><A><lightblue> of <gold><I>",
	M211 = "<rose>Link Broken!",
	M212 = "<lightblue>Your chest is located at <lightgray>X:<white><X> <lightgray>Y:<white><Y> <lightgray>Z:<white><Z> <lightgray>W:<white><W>",
	M213 = "<lightblue>and has room for <yellow><A><lightblue> more.",
	M214 = "<lightblue>and has <yellow><A><lightblue> of <gold><I>",
	M215 = "<gold><X> <lightblue>BuyPrice = <yellow><Y> <lightblue>SellPrice = <yellow><Z>",
	M216 = "<lightblue>ID = <yellow><X> <lightblue>Damage = <yellow><Y>",
	M217 = "<lightblue>LeftClick sign to set new price.",
	M218 = "<lightblue>New price set.",
	M219 = "<lightblue>LeftClick sign to set new Amount",
	M220 = "<lightblue>New amount set.",
	M221 = "<rose>Amount Change Canceled.",
	M222 = "<rose>Price Change Canceled.",
	M223 = "<lightblue> Item Added!",
	M224 = "<rose> Item Removed!",
	M225 = "<lightblue> Item Buy Price Updated!",
	M226 = "<lightblue> Item Sell Price Updated!",
	M227 = "<lightblue> Your items sold for a total of <lightgreen><A> <M>",
	M228 = "<lightblue> Place a chest to make a S-TRADE Chest",

	/*Logging Messages*/
	L301 = "<P1> created <T> at X:<SX> Y:<SY> Z:<SZ> W:<SW>",
	L302 = "<P1> completed link with chest at X:<CX> Y:<CY> Z: <CZ> W:<CW>",
	L303 = "<P1> broke link between chest at X:<CX> Y:<CY> Z: <CZ> W:<CW> and sign at X:<SX> Y:<SY> Z:<SZ> W:<SW> by breaking the <T>",
	L304 = "<P1> used /dex buy to purchase <A> stacks of <I>",
	L305 = "<P1> used /dex buy to purchase <A> of <I>",
	L306 = "<P1> used /dex sell to sell <A> stacks of <I>",
	L307 = "<P1> used /dex sell to sell <A> of <I>",
	L308 = "<P1> used G-TRADE sign at X:<SX> Y:<SY> Z:<SZ> W:<SW> to sell <A> of <I>",
	L309 = "<P1> used G-SHOP sign at X:<SX> Y:<SY> Z:<SZ> W:<SW> to purchase <A> of <I>",
	L310 = "<P1> used /dex <I> to check prices on <I>",
	L311 = "<P1> used P-SHOP sign at X:<SX> Y:<SY> Z:<SZ> W:<SW> to purchase <A> of <I> from <P2>",
	L312 = "<P1> used P-TRADE sign at X:<SX> Y:<SY> Z:<SZ> W:<SW> to sell <A> of <I> to <P2>",
	L313 = "<P1> changed price on <T> sign at X:<SX> Y:<SY> Z:<SZ> W:<SW> from <PR> to <A>",
	L314 = "<P1> changed amount on <T> sign at X:<SX> Y:<SY> Z:<SZ> W:<SW> from <PR> to <A>",
	L315 = "<P1> added Name:<T> ID:<I> Damage:<A> BuyPrice:<PR> SellPrice:<P2> to Global Lists",
	L316 = "<P1> removed Name:<T> from Global Lists",
	L317 = "<P1> updated Buy Price for Name:<T> to <PR>",
	L318 = "<P1> updated Sell Price for Name:<T> to <PR>",
	L319 = "<P1> used S-SHOP sign at X:<SX> Y:<SY> Z:<SZ> W:<SW> to purchase <A> of <I>",
	L320 = "<P1> used S-TRADE chest at X:<CX> Y:<CY> Z:<CZ> W:<CW> to sell <I>";
		
	public dExMessages(dExData dExD, PropertiesFile Mess){
		this.dExD = dExD;
		this.Mess = Mess;
		LoadMessages();
	}
	
	private void LoadMessages(){
		E101 = LoadStringCheck(Mess, E101, "101-PlaceNoPermission");
		E102 = LoadStringCheck(Mess, E102, "102-UseNoPermission");
		E103 = LoadStringCheck(Mess, E103, "103-ItemNotSpecified");
		E104 = LoadStringCheck(Mess, E104, "104-AmountNotSpecified");
		E105 = LoadStringCheck(Mess, E105, "105-PriceNotFound");
		E106 = LoadStringCheck(Mess, E106, "106-PriceIssues");
		E107 = LoadStringCheck(Mess, E107, "107-NotForSell");
		E108 = LoadStringCheck(Mess, E108, "108-AmountInvalid");
		E109 = LoadStringCheck(Mess, E109, "109-LinkInProgress");
		E110 = LoadStringCheck(Mess, E110, "110-PriceNotSpecified");
		E111 = LoadStringCheck(Mess, E111, "111-SignNotCorrect");
		E112 = LoadStringCheck(Mess, E112, "112-ItemNotFound");
		E113 = LoadStringCheck(Mess, E113, "113-InvalidItemID");
		E114 = LoadStringCheck(Mess, E114, "114-InvalidItemDamage");
		E115 = LoadStringCheck(Mess, E115, "115-ItemBanned");
		E116 = LoadStringCheck(Mess, E116, "116-InvalidPrice");
		E117 = LoadStringCheck(Mess, E117, "117-MissingChest");
		E118 = LoadStringCheck(Mess, E118, "118-OpenChestDenied");
		E119 = LoadStringCheck(Mess, E119, "119-BreakSignDenied");
		E120 = LoadStringCheck(Mess, E120, "120-BreakChestDenied");
		E121 = LoadStringCheck(Mess, E121, "121-InventoryPlayerFull");
	    E122 = LoadStringCheck(Mess, E122, "122-NotEnoughMoney");
	    E123 = LoadStringCheck(Mess, E123, "123-InventoryPlayerNone");
	    E124 = LoadStringCheck(Mess, E124, "124-GlobalNoMoney");
	    E125 = LoadStringCheck(Mess, E125, "125-InventoryChestFull");
	    E126 = LoadStringCheck(Mess, E126, "126-InventoryChestNone");
	    E127 = LoadStringCheck(Mess, E127, "127-PlayerOtherNoMoney");
	    E128 = LoadStringCheck(Mess, E128, "128-PTRADEIncorrectItem");
	    E129 = LoadStringCheck(Mess, E129, "129-SetPriceEngaged");
	    E130 = LoadStringCheck(Mess, E130, "130-SetAmountEngage");
	    E131 = LoadStringCheck(Mess, E131, "131-CannotSetPSHOPAmount");
	    E132 = LoadStringCheck(Mess, E132, "132-CannotChangeSignNotOwned");
	    E133 = LoadStringCheck(Mess, E133, "133-SignNotTakingItems");
	    E134 = LoadStringCheck(Mess, E134, "134-ItemAlreadyListed");
	    E135 = LoadStringCheck(Mess, E135, "135-SignLimitReached");
	    E136 = LoadStringCheck(Mess, E136, "136-CannotAttachChest");
	    E137 = LoadStringCheck(Mess, E137, "137-ChestProtected");
	    E138 = LoadStringCheck(Mess, E138, "138-ChestLinkNotOwned");
	    E139 = LoadStringCheck(Mess, E139, "139-STSNotAllSold");
	    E140 = LoadStringCheck(Mess, E140, "140-STSInUse");
	    E141 = LoadStringCheck(Mess, E141, "141-STSDouble");
		
		M201 = LoadStringCheck(Mess, M201,"201-GSHOPCreate");
		M202 = LoadStringCheck(Mess, M202,"202-GTRADECreate");
		M203 = LoadStringCheck(Mess, M203,"203-PSignLink");
		M204 = LoadStringCheck(Mess, M204,"204-LinkCreate");
		M205 = LoadStringCheck(Mess, M205,"205-LinkCancel");
		M206 = LoadStringCheck(Mess, M206,"206-GBuySell");
		M207 = LoadStringCheck(Mess, M207,"207-BuySTLine1");
		M208 = LoadStringCheck(Mess, M208,"208-BuyLine1");
		M209 = LoadStringCheck(Mess, M209,"209-SellSTLine1");
		M210 = LoadStringCheck(Mess, M210,"210-SellLine1");
		M211 = LoadStringCheck(Mess, M211,"211-PSignLinkBroken");
		M212 = LoadStringCheck(Mess, M212,"212-ChestLoc");
		M213 = LoadStringCheck(Mess, M213,"213-ChestRoom");
		M214 = LoadStringCheck(Mess, M214,"214-ChestAmount");
		M215 = LoadStringCheck(Mess, M215,"215-PriceCheckL1");
		M216 = LoadStringCheck(Mess, M216,"216-PriceCheckL2");
		M217 = LoadStringCheck(Mess, M217,"217-SetSignPrice1");
		M218 = LoadStringCheck(Mess, M218,"218-SetSignPrice2");
	    M219 = LoadStringCheck(Mess, M219,"219-SetSignAmount1");
	    M220 = LoadStringCheck(Mess, M220,"220-SetSignAmount2");
	    M221 = LoadStringCheck(Mess, M221,"221-SignAmountChangeCancel");
	    M222 = LoadStringCheck(Mess, M222,"222-SignPriceChangeCancel");
	    M223 = LoadStringCheck(Mess, M223,"223-ItemAddedToList");
	    M224 = LoadStringCheck(Mess, M224,"224-ItemRemovedFromList");
	    M225 = LoadStringCheck(Mess, M225,"225-ItemBuyPriceUpdate");
	    M226 = LoadStringCheck(Mess, M226,"226-ItemSellPriceUpdate");
	    M227 = LoadStringCheck(Mess, M227,"227-STSItemsSold");
	    M228 = LoadStringCheck(Mess, M228,"228-STSPlaceChest");
		
		L301 = LoadStringCheck(Mess, L301,"301-SignCreate");
		L302 = LoadStringCheck(Mess, L302,"302-SignLinked");
		L303 = LoadStringCheck(Mess, L303,"303-LinkBroken");
		L304 = LoadStringCheck(Mess, L304,"304-DEXBUYS");
		L305 = LoadStringCheck(Mess, L305,"305-DEXBUY");
		L306 = LoadStringCheck(Mess, L306,"306-DEXSELLS");
		L307 = LoadStringCheck(Mess, L307,"307-DEXSELL");
		L308 = LoadStringCheck(Mess, L308,"308-GTUsed");
		L309 = LoadStringCheck(Mess, L309,"309-GSUsed");
		L310 = LoadStringCheck(Mess, L310,"310-PriceCheck");
		L311 = LoadStringCheck(Mess, L311,"311-PSUsed");
		L312 = LoadStringCheck(Mess, L312,"312-PTUsed");
		L313 = LoadStringCheck(Mess, L313,"313-PSignPriceUpdate");
	    L314 = LoadStringCheck(Mess, L314,"314-PSignAmountUpdate");
		L315 = LoadStringCheck(Mess, L315,"315-ItemAdded");
	    L316 = LoadStringCheck(Mess, L316,"316-ItemRemoved");
	    L317 = LoadStringCheck(Mess, L317,"317-BPriceUpdate");
	    L318 = LoadStringCheck(Mess, L318,"318-SPriceUpdate");
	    L319 = LoadStringCheck(Mess, L319,"319-SSUsed");
	    L320 = LoadStringCheck(Mess, L320,"320-STUsed");
	    
	}

	private String LoadStringCheck(PropertiesFile props, String defaultvalue, String Property){
		String value;
		if(props.containsKey(Property)){
			value = props.getString(Property);
		}else{
			dExD.log.warning("[dExchange] - Value: "+Property+" not found! Using default of "+String.valueOf(defaultvalue));
			value = defaultvalue;
		}
		return value;
	}
	
	public void logAct(int Code, String P1, String P2, String T, String SX, String SY, String SZ, String SW, String CX, String CY, String CZ, String CW, String I, String A, String PR){
		if(dExD.LogAc){
			String m = "";
			switch(Code){
			case 301: m = "[dEx]" + L301.replace("<P1>", P1).replace("<T>", T).replace("<SX>", SX).replace("<SY>", SY).replace("<SZ>", SZ).replace("<SW>", SW); break;
			case 302: m = "[dEx]" + L302.replace("<P1>", P1).replace("<T>", T).replace("<CX>", CX).replace("<CY>", CY).replace("<CZ>", CZ).replace("<CW>", CW); break;
			case 303: m = "[dEx]" + L303.replace("<P1>", P1).replace("<T>", T).replace("<SX>", SX).replace("<SY>", SY).replace("<SZ>", SZ).replace("<SW>", SW).replace("<CX>", CX).replace("<CY>", CY).replace("<CZ>", CZ).replace("<CW>", CW); break;
			case 304: m = "[dEx]" + L304.replace("<P1>", P1).replace("<A>", A).replace("<I>", I); break;
			case 305: m = "[dEx]" + L305.replace("<P1>", P1).replace("<A>", A).replace("<I>", I); break;
			case 306: m = "[dEx]" + L306.replace("<P1>", P1).replace("<A>", A).replace("<I>", I); break;
			case 307: m = "[dEx]" + L307.replace("<P1>", P1).replace("<A>", A).replace("<I>", I); break;
			case 308: m = "[dEx]" + L308.replace("<P1>", P1).replace("<SX>", SX).replace("<SY>", SY).replace("<SZ>", SZ).replace("<SW>", SW).replace("<A>", A).replace("<I>", I); break;
			case 309: m = "[dEx]" + L309.replace("<P1>", P1).replace("<SX>", SX).replace("<SY>", SY).replace("<SZ>", SZ).replace("<SW>", SW).replace("<A>", A).replace("<I>", I); break;
			case 310: m = "[dEx]" + L310.replace("<P1>", P1).replace("<I>", I); break;
			case 311: m = "[dEx]" + L311.replace("<P1>", P1).replace("<SX>", SX).replace("<SY>", SY).replace("<SZ>", SZ).replace("<SW>", SW).replace("<A>", A).replace("<I>", I).replace("<P2>", P2); break;
			case 312: m = "[dEx]" + L312.replace("<P1>", P1).replace("<SX>", SX).replace("<SY>", SY).replace("<SZ>", SZ).replace("<SW>", SW).replace("<A>", A).replace("<I>", I).replace("<P2>", P2); break;
			case 313: m = "[dEx]" + L313.replace("<P1>", P1).replace("<SX>", SX).replace("<SY>", SY).replace("<SZ>", SZ).replace("<SW>", SW).replace("<A>", A).replace("<T>", T).replace("<PR>", PR); break;
			case 314: m = "[dEx]" + L314.replace("<P1>", P1).replace("<SX>", SX).replace("<SY>", SY).replace("<SZ>", SZ).replace("<SW>", SW).replace("<A>", A).replace("<T>", T).replace("<PR>", PR); break;
			case 315: m = "[dEx]" + L315.replace("<P1>", P1).replace("<T>", T).replace("<I>", I).replace("<A>", A).replace("<PR>", PR).replace("<P2>", P2); break;
			case 316: m = "[dEx]" + L316.replace("<P1>", P1).replace("<T>", T); break;
			case 317: m = "[dEx]" + L317.replace("<P1>", P1).replace("<T>", T).replace("<PR>", PR); break;
			case 318: m = "[dEx]" + L318.replace("<P1>", P1).replace("<T>", T).replace("<PR>", PR); break;
			case 319: m = "[dEx]" + L319.replace("<P1>", P1).replace("<SX>", SX).replace("<SY>", SY).replace("<SZ>", SZ).replace("<SW>", SW).replace("<A>", A).replace("<I>", I); break;
			case 320: m = "[dEx]" + L320.replace("<P1>", P1).replace("<CX>", CX).replace("<CY>", CY).replace("<CZ>", CZ).replace("<CW>", CW).replace("<I>", I); break;
			}
			etc.getLoader().callCustomHook("dCBalance", new Object[]{"log", m});
		}
	}
	
	public boolean ErrorMessage(Player player, int code){
		switch(code){
		case 101: player.notify(E101); return true;
		case 102: player.notify(E102); return true;
		case 103: player.notify(E103); return true;
		case 104: player.notify(E104); return true;
		case 105: player.notify(E105); return true;
		case 106: player.notify(E106); return true;
		case 107: player.notify(E107); return true;
		case 108: player.notify(E108); return true;
		case 109: player.notify(E109); return true;
		case 110: player.notify(E110); return true;
		case 111: player.notify(E111); return true;
		case 112: player.notify(E112); return true;
		case 113: player.notify(E113); return true;
		case 114: player.notify(E114); return true;
		case 115: player.notify(E115); return true;
		case 116: player.notify(E116); return true;
		case 117: player.notify(E117); return true;
		case 118: player.notify(E118); return true;
		case 119: player.notify(E119); return true;
		case 120: player.notify(E120); return true;
		case 121: player.notify(E121); return true;
		case 122: player.notify(E122); return true;
		case 123: player.notify(E123); return true;
		case 124: player.notify(E124); return true;
		case 125: player.notify(E125); return true;
		case 126: player.notify(E126); return true;
		case 127: player.notify(E127); return true;
		case 128: player.notify(E128); return true;
		case 129: player.notify(E129); return true;
		case 130: player.notify(E130); return true;
		case 131: player.notify(E131); return true;
		case 132: player.notify(E132); return true;
		case 133: player.notify(E133); return true;
		case 134: player.notify(E134); return true;
		case 135: player.notify(E135); return true;
		case 136: player.notify(E136); return true;
		case 137: player.notify(E137); return true;
		case 138: player.notify(E138); return true;
		case 139: player.notify(E139); return true;
		case 140: player.notify(E140); return true;
		case 141: player.notify(E141); return true;
		default: return true;
		}
	}
	
	public String pmessage(int code, String A, String I){
		String M = null;
		switch (code){
		case 201: M = Colorize(M201); M = "§2[§6dEx§2]" + M.replace("<A>", A).replace("<I>", I).replace("<M>", dExD.getMN()); return M;
		case 202: M = Colorize(M202); M = "§2[§6dEx§2]" + M.replace("<A>", A).replace("<I>", I).replace("<M>", dExD.getMN()); return M;
		case 203: M = Colorize(M203); M = "§2[§6dEx§2]" + M.replace("<A>", A).replace("<I>", I).replace("<M>", dExD.getMN()); return M;
		case 204: M = Colorize(M204); M = "§2[§6dEx§2]" + M.replace("<A>", A).replace("<I>", I).replace("<M>", dExD.getMN()); return M;
		case 205: M = Colorize(M205); M = "§2[§6dEx§2]" + M.replace("<A>", A).replace("<I>", I).replace("<M>", dExD.getMN()); return M;
		case 206: M = Colorize(M206); M = "§2[§6dEx§2]" + M.replace("<A>", A).replace("<I>", I).replace("<M>", dExD.getMN()); return M;
		case 207: M = Colorize(M207); M = "§2[§6dEx§2]" + M.replace("<A>", A).replace("<I>", I).replace("<M>", dExD.getMN()); return M;
		case 208: M = Colorize(M208); M = "§2[§6dEx§2]" + M.replace("<A>", A).replace("<I>", I).replace("<M>", dExD.getMN()); return M;
		case 209: M = Colorize(M209); M = "§2[§6dEx§2]" + M.replace("<A>", A).replace("<I>", I).replace("<M>", dExD.getMN()); return M;
		case 210: M = Colorize(M210); M = "§2[§6dEx§2]" + M.replace("<A>", A).replace("<I>", I).replace("<M>", dExD.getMN()); return M;
		case 211: M = Colorize(M211); M = "§2[§6dEx§2]" + M.replace("<A>", A).replace("<I>", I).replace("<M>", dExD.getMN()); return M;
		case 213: M = Colorize(M213); M = "§2[§6dEx§2]" + M.replace("<A>", A).replace("<I>", I).replace("<M>", dExD.getMN()); return M;
		case 214: M = Colorize(M214); M = "§2[§6dEx§2]" + M.replace("<A>", A).replace("<I>", I).replace("<M>", dExD.getMN()); return M;
		case 217: M = Colorize(M217); M = "§2[§6dEx§2]" + M.replace("<A>", A).replace("<I>", I).replace("<M>", dExD.getMN()); return M;
		case 218: M = Colorize(M218); M = "§2[§6dEx§2]" + M.replace("<A>", A).replace("<I>", I).replace("<M>", dExD.getMN()); return M;
		case 219: M = Colorize(M219); M = "§2[§6dEx§2]" + M.replace("<A>", A).replace("<I>", I).replace("<M>", dExD.getMN()); return M;
		case 220: M = Colorize(M220); M = "§2[§6dEx§2]" + M.replace("<A>", A).replace("<I>", I).replace("<M>", dExD.getMN()); return M;
		case 221: M = Colorize(M221); M = "§2[§6dEx§2]" + M.replace("<A>", A).replace("<I>", I).replace("<M>", dExD.getMN()); return M;
		case 222: M = Colorize(M222); M = "§2[§6dEx§2]" + M.replace("<A>", A).replace("<I>", I).replace("<M>", dExD.getMN()); return M;
		case 223: M = Colorize(M223); M = "§2[§6dEx§2]" + M.replace("<A>", A).replace("<I>", I).replace("<M>", dExD.getMN()); return M;
		case 224: M = Colorize(M224); M = "§2[§6dEx§2]" + M.replace("<A>", A).replace("<I>", I).replace("<M>", dExD.getMN()); return M;
		case 225: M = Colorize(M225); M = "§2[§6dEx§2]" + M.replace("<A>", A).replace("<I>", I).replace("<M>", dExD.getMN()); return M;
		case 226: M = Colorize(M226); M = "§2[§6dEx§2]" + M.replace("<A>", A).replace("<I>", I).replace("<M>", dExD.getMN()); return M;
		case 227: M = Colorize(M227); M = "§2[§6dEx§2]" + M.replace("<A>", A).replace("<I>", I).replace("<M>", dExD.getMN()); return M;
		case 228: M = Colorize(M228); M = "§2[§6dEx§2]" + M.replace("<A>", A).replace("<I>", I).replace("<M>", dExD.getMN()); return M;
		default: String Missing = "MISSING MESSAGE M:"+code; return Missing;
		}
	}
	
	public String SpecM(int code, String X, String Y, String Z, String W){
		String SM = null;
		switch (code){
			case 212: SM = Colorize(M212); SM = "§2[§6dEx§2]" + SM.replace("<X>", String.valueOf(X)).replace("<Y>", String.valueOf(Y)).replace("<Z>", String.valueOf(Z)).replace("<W>", String.valueOf(W)); return SM;
			case 215: SM = Colorize(M215); SM = "§2[§6dEx§2]" + SM.replace("<X>", String.valueOf(X)).replace("<Y>", String.valueOf(Y)).replace("<Z>", String.valueOf(Z)).replace("<W>", String.valueOf(W)); return SM;
			case 216: SM = Colorize(M216); SM = "§2[§6dEx§2]" + SM.replace("<X>", String.valueOf(X)).replace("<Y>", String.valueOf(Y)).replace("<Z>", String.valueOf(Z)).replace("<W>", String.valueOf(W)); return SM;
		}
		return SM;
	}
	
	private String Colorize(String Message){
		Message = Message.replace("<black>", Colors.Black);
		Message = Message.replace("<blue>", Colors.Blue);
		Message = Message.replace("<darkpurple>", Colors.DarkPurple);
		Message = Message.replace("<gold>", Colors.Gold);
		Message = Message.replace("<gray>", Colors.Gray);
		Message = Message.replace("<green>", Colors.Green);
		Message = Message.replace("<lightblue>", Colors.LightBlue);
		Message = Message.replace("<lightgray>", Colors.LightGray);
		Message = Message.replace("<lightgreen>", Colors.LightGreen);
		Message = Message.replace("<lightpurple>", Colors.LightPurple);
		Message = Message.replace("<navy>", Colors.Navy);
		Message = Message.replace("<purple>", Colors.Purple);
		Message = Message.replace("<red>", Colors.Red);
		Message = Message.replace("<rose>", Colors.Rose);
		Message = Message.replace("<white>", Colors.White);
		Message = Message.replace("<yellow>", Colors.Yellow);
		return Message;
	}
}
