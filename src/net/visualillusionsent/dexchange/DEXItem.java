package net.visualillusionsent.dexchange;

public class DEXItem {
    private double buyprice, sellprice;
    private int id, amount, damage;
    private String name;
    
    public DEXItem(int id, int amount, int damage){
        this.id = id;
        this.damage = damage;
        this.amount = amount;
    }
    
    public DEXItem(int id, int damage, double buyprice, double sellprice, String name){
        this.id = id;
        this.damage = damage;
        this.buyprice = buyprice;
        this.sellprice = sellprice;
        this.name = name;
    }
    
    public DEXItem(int id, int damage){
        this.id = id;
        this.damage = damage;
    }
    
    public boolean equals(int id, int damage){
        if(id == this.id && damage == this.damage){
            return true;
        }
        return false;
    }
    
    public boolean equals(String name){
        return this.name.equals(name);
    }
    
    public double getBuyPrice(){
        return buyprice;
    }
    
    public double getSellPrice(){
        return sellprice;
    }
    
    public int getId(){
        return id;
    }
    
    public int getAmount(){
        return amount;
    }
    
    public int getDamage(){
        return damage;
    }
    
    public String getName(){
        return name;
    }
    
    public Object getItem(){
        return null;
    }
}
