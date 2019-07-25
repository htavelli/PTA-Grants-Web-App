package com.hillarytavelli.beans;

/**
 * Bean to represent details for a grant item
 * @author Hillary Tavelli
 *
 */
public class Item {
	private String item_desc;
	private String vendor;
	private int quantity;
	private float cost_per_item;
	private String web_link = null;
	
	public String getItem_desc() {
		return item_desc;
	}
	public void setItem_desc(String item_desc) {
		this.item_desc = item_desc;
	}
	public String getVendor() {
		return vendor;
	}
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public float getCost_per_item() {
		return cost_per_item;
	}
	public void setCost_per_item(float cost_per_item) {
		this.cost_per_item = cost_per_item;
	}
	public String getWeb_link() {
		return web_link;
	}
	public void setWeb_link(String web_link) {
		this.web_link = web_link;
	}
	
	
}
