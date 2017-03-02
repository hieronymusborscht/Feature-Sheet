package rg.ent;

public class ListingObject {

	
	public int getPicture_count(){
		return picture_count;
	}
	public java.util.Date getListingDate(){
		return listing_date;
	}
	public String getState(){
		return state;
	}
	
	public String getMls_number() {
		return mls_number;
	}
	public void setMls_number(String mls_number) {
		this.mls_number = mls_number;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public Float getPrice_listed() {
		return price_listed;
	}
	public void setPrice_listed(Float price_listed) {
		this.price_listed = price_listed;
	}
	public Float getPrice_sold() {
		return price_sold;
	}
	public void setPrice_sold(Float price_sold) {
		this.price_sold = price_sold;
	}
	public String getStreet_address() {
		return street_address;
	}
	public void setStreet_address(String street_address) {
		this.street_address = street_address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public int getBedrooms() {
		return bedrooms;
	}
	public void setBedrooms(int bedrooms) {
		this.bedrooms = bedrooms;
	}
	public int getBathrooms() {
		return bathrooms;
	}
	public void setBathrooms(int bathrooms) {
		this.bathrooms = bathrooms;
	}
	public String getRealtor_vnumber() {
		return realtor_vnumber;
	}
	public void setRealtor_vnumber(String realtor_vnumber) {
		this.realtor_vnumber = realtor_vnumber;
	}
	public String getRealtor_firstname() {
		return realtor_firstname;
	}
	public void setRealtor_firstname(String realtor_firstname) {
		this.realtor_firstname = realtor_firstname;
	}
	public String getRealtor_lastname() {
		return realtor_lastname;
	}
	public void setRealtor_lastname(String realtor_lastname) {
		this.realtor_lastname = realtor_lastname;
	}
	public String getRealtor_phone() {
		return realtor_phone;
	}
	public void setRealtor_phone(String realtor_phone) {
		this.realtor_phone = realtor_phone;
	}
	public String getRealtor_email() {
		return realtor_email;
	}
	public void setRealtor_email(String realtor_email) {
		this.realtor_email = realtor_email;
	}
	public String getRealtor_web() {
		return realtor_web;
	}
	public void setRealtor_web(String realtor_web) {
		this.realtor_web = realtor_web;
	}
	public java.util.TreeSet<String> getPhotos() {
		return photos;
	}
	public void setPhotos(java.util.TreeSet<String> photos) {
		this.photos = photos;
	}
	public String getRealtor_photo() {
		return realtor_photo;
	}
	public void setRealtor_photo(String realtor_photo) {
		this.realtor_photo = realtor_photo;
	}
	public void setListingDate(java.util.Date ld){
		
		listing_date = ld;
	}
	public void setPicture_count(int i){
		picture_count = i;
	}
	public void setState(String s){
		state = s;
	}
	
	private String office_phone;
	
	public String getOffice_phone() {
		return office_phone;
	}
	public void setOffice_phone(String office_phone) {
		this.office_phone = office_phone;
	}
	public java.util.Date getListing_date() {
		return listing_date;
	}
	public void setListing_date(java.util.Date listing_date) {
		this.listing_date = listing_date;
	}

	
	public String getAmmenities() {
		return ammenities;
	}
	public void setAmmenities(String ammenities) {
		this.ammenities = ammenities;
	}

	public String getNeighborhood() {
		return neighborhood;
	}
	public void setNeighborhood(String neighborhood) {
		this.neighborhood = neighborhood;
	}

	public int getListingId() {
		return listingid;
	}
	public void setListingId(int listingid) {
		this.listingid = listingid;
	}

	private int listingid;
	private String ammenities;
	private String mls_number;
	private String pid;
	private int picture_count;
	private Float price_listed;
	private Float price_sold;
	private String street_address;
	private String city;
	private int bedrooms;
	private int bathrooms;
	private String realtor_vnumber;
	private String realtor_firstname;
	private String realtor_lastname;
	private String realtor_phone;
	private String realtor_email;
	private String realtor_web;
	private java.util.Date listing_date;
	
	private java.util.TreeSet<String> photos;
	private String realtor_photo;
	private String state;
	private String neighborhood;
	
	
}
