package rg.ent;



import java.math.BigDecimal;

import org.apache.commons.lang3.text.WordUtils;

public class NewSummaryListing {
	
	private java.util.TreeMap<String, String> string_fields;
	private java.util.TreeMap<String, Integer> integer_fields;
	private java.util.TreeMap<String, java.util.Calendar> date_fields;
	private String[] thumbs;
	private BigDecimal askingprice;
	
	
	//private java.sql.Timestamp l_updatedate;
	/*
	 *   	batch_statements.addBatch("update listing set lot_sqft=CASE WHEN lm_dec_11~E'^\\d+$' THEN lm_dec_11::numeric ELSE 0 END");
      		batch_statements.addBatch("update listing set sqft=CASE WHEN lm_dec_7~E'^\\d+$' THEN lm_dec_7::integer ELSE 0 END");
      		batch_statements.addBatch("update listing set frontage=CASE WHEN lm_dec_8~E'^\\d+$' THEN lm_dec_8::integer ELSE 0 END");
	 * 
	 * 
	 */
	
	public NewSummaryListing(){
		setupFields();
	}
	
	private void setupFields(){
	
		 string_fields = new java.util.TreeMap<String, String>();
		 integer_fields = new java.util.TreeMap<String, Integer>();
		 date_fields = new java.util.TreeMap<String, java.util.Calendar>();
		 		 
		 string_fields.put("lmd_mp_latitude","");
		 string_fields.put("lmd_mp_longitude","");
		 string_fields.put("l_address", "");
		 string_fields.put("l_area", "");
		 string_fields.put("l_city", "");
		 string_fields.put("l_state","");
		 string_fields.put("l_addressstreet", "");
		 string_fields.put("l_displayid","");
		 string_fields.put("l_askingprice","");
		 string_fields.put("property_type", "");  // property_type
		 string_fields.put("neighborhood","");  // neighborhood/sub_area
		 string_fields.put("lo1_organizationname", "");
		 string_fields.put("six_forty_path","");
		 string_fields.put("three_twenty_path","");
		 string_fields.put("one_sixty_path","");
		 string_fields.put("postal","");
		 string_fields.put("lr_remarks22","");
		 string_fields.put("lr_remarks33","");
		 string_fields.put("view","");		// view  lm_char100_3
		
		 string_fields.put("lfd_featuresincluded_24",""); // features_included
		 string_fields.put("lfd_siteinfluences_15","");
		 string_fields.put("lfd_styleofhome_1","");
		 string_fields.put("lft_amenities_25","");
		 string_fields.put("lfd_basementarea_6","");

		 string_fields.put("gross_tax",""); 			// lm_dec_16
		 string_fields.put("maint_fee","");  		// maint fee  //lm_dec_22"
		 string_fields.put("lot_depth","");   	// lot depth  lm_char30_28

		 string_fields.put("la1_loginname","");
		 
		 integer_fields.put("l_listingid", 0); 	// l_listingid,	
		 integer_fields.put("sqft", 0);
		 integer_fields.put("bedrooms", 0); 
		 integer_fields.put("full_baths", 0); 
		 integer_fields.put("days_on", 0);
		 integer_fields.put("property_age", 0);
		 integer_fields.put("number_of_pics", 0);
		 integer_fields.put("lot_sqft", 0);
		 integer_fields.put("frontage",0); 
		 
		 java.sql.Date sql_d = java.sql.Date.valueOf("1970-01-20");
		 java.util.Calendar cal = java.util.Calendar.getInstance();
		 
		 cal.setTime( sql_d );
		 
		 date_fields.put("l_last_photo_updt", cal);
		 date_fields.put("l_listingdate", cal);
		 date_fields.put("l_updatedate", cal);
	}
	
	public int getIntValue(String field_name){
		return integer_fields.get(field_name);
	}
	public String getStringValue(String field_name){
		return string_fields.get(field_name);
	}
	
	

	public void setInt_field(String field_name, int i){
		if(integer_fields.containsKey(field_name)){
			integer_fields.put(field_name, i);
		}
	}
	public void setString_field(String field_name,  String value){
		if(string_fields.containsKey(field_name)){
			string_fields.put(field_name, value);
		}
	}
	public void setDate_field(String field_name, java.util.Calendar cal){
		if(date_fields.containsKey(field_name)){
			date_fields.put("field_name", cal);
		}
	}
	

	public BigDecimal getAskingprice() {
		return askingprice;
	}
	public String[] getThumbs() {
		return thumbs;
	}
	
	
	public void setAskingprice(BigDecimal askingprice) {
		this.askingprice = askingprice;
	}
	public void setThumbs(String[] thumbs) {
		this.thumbs = thumbs;
	}
	
	
	
	/***************************
	 * METHODS MIGRATED FROM OLD SUMMARYLISTING OBJECT
	 */


	public String toShortString(int i, boolean visitor_logged_in, boolean matches_saved_listing){
		StringBuffer sb = new StringBuffer();
		sb.append("<div class=\"search-result-listing\">");
		sb.append("<div class=\"search-results-no\">"); sb.append(i); sb.append("</div>");
		sb.append("<div class=\"search-results-saved\">"); 
		if(visitor_logged_in && matches_saved_listing){
			sb.append("<input type=\"checkbox\" checked=\"checked\" id=\"chkbx_");
			sb.append(string_fields.get("l_displayid"));
			sb.append("\" onclick=\"check_checkbox('"); sb.append(string_fields.get("l_displayid")); sb.append("');\" >");  //"chkbx_"+param_arr[13]
		}else if(visitor_logged_in && !matches_saved_listing){   
			sb.append("<input type=\"checkbox\" id=\"chkbx_");
			sb.append(string_fields.get("l_displayid"));
			sb.append("\" onclick=\"check_checkbox('"); sb.append(string_fields.get("l_displayid")); sb.append("','");sb.append(getJSStreetAddress());  sb.append("');\" >");  //"chkbx_"+param_arr[13]
		}else{
			sb.append("<a href=\"#\" onclick=\"showloginWindow()\">[+]</a>");
		}
		sb.append("</div>");
		sb.append("<div class=\"search-results-address\">");  
		sb.append("<a ");
		sb.append("onclick=\"showShortSummaryNew('");
		sb.append(rg.util.StringCleaner.formatPrice(askingprice));
		sb.append("', '");
		sb.append(integer_fields.get("sqft"));
		sb.append("', '");
		sb.append(integer_fields.get("lot_sqft"));
		sb.append("', '");
		sb.append(integer_fields.get("days_on"));
		sb.append("', '");
		sb.append(integer_fields.get("bedrooms")); 
		sb.append("', '");
		sb.append(integer_fields.get("full_baths"));
		sb.append("', '");
		sb.append(string_fields.get("three_twenty_path"));
		sb.append("', '");
		sb.append(getJSStreetAddress());
		sb.append("', '");
		sb.append(string_fields.get("l_city"));
		sb.append("', '");
		sb.append(string_fields.get("postal"));
		sb.append("', '");
		sb.append(getJSOrg_name());
		sb.append("', '");
		sb.append(string_fields.get("l_displayid"));
		sb.append("', '");
		sb.append(visitor_logged_in);
		
		sb.append("', '");
		sb.append(getJSRealtor_remarks_22());
		sb.append("', '");
		sb.append(string_fields.get("property_type"));
		sb.append("'); ");
		
		//sb.append("\"");
		
		//sb.append(" onclick=\"");
		sb.append("document.getElementById('gmap').contentWindow.doPop(");
		sb.append(i);
		sb.append("); ");
		sb.append("\" ");
		
		//sb.append(" onclick=\"console.log(\"["+i+"]\")\"");
		
		sb.append(" href=\"#\">");

		sb.append(WordUtils.capitalizeFully(string_fields.get("l_address")));
		sb.append("</a>");
		sb.append("</div>");
		sb.append("<div class=\"search-results-price\">$"); 
		sb.append(getFormattedPrice());
		sb.append("</div>");
		sb.append("<div class=\"search-results-city\">"); 
		//sb.append(sub_area);
		sb.append(" " );
		sb.append(string_fields.get("l_city"));
		sb.append("</div>");
		sb.append("<div class=\"search-results-size\">"); 
		sb.append(integer_fields.get("sqft"));
		sb.append("</div>");
		sb.append("<div class=\"search-results-bedrooms\">"); 
		sb.append(integer_fields.get("bedrooms"));
		sb.append("</div>");
		sb.append("<div class=\"search-results-bathrooms\">"); 
		sb.append(integer_fields.get("full_baths"));
		sb.append("</div>");
		sb.append("<div class=\"search-results-brokerage\">"); sb.append(string_fields.get("lo1_organizationname"));   sb.append("</div>");
		sb.append("</div>");
		return sb.toString();
	}

	public String getFormattedPrice(){ return rg.util.StringCleaner.formatPrice(askingprice); }
	
	
	

	public Object getSortingKey(String s){
		Object o=null;
		if("city".equals(s)){
			o= string_fields.get("l_city")+integer_fields.get("l_listingid");
		}
		if("price".equals(s)){
			StringBuilder sb = new StringBuilder();
			String ss = askingprice+""+integer_fields.get("l_listingid");
			int num = 25-ss.length();
			while(num>0){
				sb.append("0");
				num=num-1;
			}
			sb.append(ss);
			o=sb.toString();
		}
		if("sqft".equals(s)){
			o =new Double(integer_fields.get("sqft") +(integer_fields.get("l_listingid")*.00000000001));
		}
		if("beds".equals(s)){
			o= new Double(integer_fields.get("bedrooms") +(integer_fields.get("l_listingid")*.00000000001));
		}
		if("address".equals(s)){		
			o= string_fields.get("l_address")+integer_fields.get("l_listingid");
		}
        if("brok".equals(s)){
        	o= string_fields.get("lo1_organizationname")+integer_fields.get("l_listingid");
        }
        return o;
	}
	
	
	public String getJSRealtor_remarks_leaf_22(){
		String s ="";
		if(string_fields.containsKey("lr_remarks22")){
			s = string_fields.get("lr_remarks22").replace("\"", "");
			s = s.replace("'", "");
			if(s.length()>200){
				s= s.substring(0, 200);
			}
		}
		return s;
	}
	
	public String getJSRealtor_remarks_22(){
		String s ="";
		if(string_fields.containsKey("lr_remarks22")){
			s = string_fields.get("lr_remarks22").replace("\"", "");
			s = s.replace("'", "");
		}
		return s;
	}
	
	public String getJSStreetAddress() { 	
		String s = string_fields.get("l_address");
		if(s.length()>25){
			s = s.substring(0, 25);
		}
	return s;
	}
	
	public String getJSOrg_name(){
		return string_fields.get("lo1_organizationname").replace("'", "");
	}
	
	
	public String toLeafMapArrayString() {
		StringBuffer sb = new StringBuffer();
		sb.append("\"");
		sb.append(string_fields.get("lmd_mp_latitude"));  //0
		sb.append("\",\"");
		sb.append(string_fields.get("lmd_mp_longitude"));   //1
		sb.append("\",\"");
		sb.append(rg.util.StringCleaner.formatPrice(askingprice));  //2
	
		
		sb.append("\",\"");
		sb.append(WordUtils.capitalizeFully(getJSStreetAddress()));  // 3   street address
		sb.append("\",\"");

		sb.append(string_fields.get("l_city"));	   //4
		sb.append("\",\"");
		
	
		sb.append(integer_fields.get("bedrooms"));   //5  
		sb.append("\",\"");
		
		
		sb.append(integer_fields.get("full_baths"));     //  6 bathrooms
		sb.append("\",\"");
		
		
		sb.append(integer_fields.get("sqft"));   //7
		sb.append("\",\"");
		sb.append(integer_fields.get("lot_sqft")); // 8
		sb.append("\",\"");
		
		sb.append(integer_fields.get("days_on"));   // 9
		sb.append("\",\"");
		sb.append(string_fields.get("one_sixty_path"));  //10
		sb.append("\",\"");
		
		sb.append(string_fields.get("three_twenty_path"));   //11
		
		sb.append("\",\"");
		//sb.append(getJSRealtor_remarks_22());    // 12
		
		sb.append(getJSRealtor_remarks_leaf_22());
		
		
		sb.append("\",\"");
		sb.append(string_fields.get("lo1_organizationname"));   // 13   brokerage
		sb.append("\",\"");

	
		//sb.append(string_fields.get("three_twenty_path"));   //  12 image
		//sb.append(",");
		//sb.append(string_fields.get("postal"));       // 13 postal
		//sb.append(",");
		sb.append(string_fields.get("l_displayid")); // 14 MLS_number

		
	
		//sb.append(string_fields.get("property_type"));  // 16
		sb.append("\"\n");
		return sb.toString();
	}
	
	
	public String toArrayString(int i, boolean is_saved) {
		StringBuffer sb = new StringBuffer();
		sb.append("\"");
		sb.append(rg.util.StringCleaner.formatPrice(askingprice));  //0
		sb.append("\",\"");
		sb.append(integer_fields.get("sqft"));   //1
		sb.append("\",");
		sb.append(integer_fields.get("lot_sqft")); // 2
		sb.append(",");
		sb.append(integer_fields.get("bedrooms"));   //3
		sb.append(",");
		sb.append(i);  // 4
		try {
			sb.append(",\"Search?num=");
			sb.append(string_fields.get("l_displayid"));
			sb.append("&adr=");
			sb.append(java.net.URLEncoder.encode(string_fields.get("l_address"), "UTF-8"));
			//sb.append(java.net.URLEncoder.encode(getStreet_address(),"UTF-8"));
			
			sb.append("&cty=");
			sb.append(java.net.URLEncoder.encode(string_fields.get("l_city"), "UTF-8"));  //5  // search string
		
		}catch(Exception e){e.printStackTrace();}
		sb.append("\",");

		sb.append(WordUtils.capitalizeFully(getJSStreetAddress()));  // 6
		sb.append(",");
		sb.append(string_fields.get("l_city"));		//  7   city
		sb.append(",");
		sb.append("no site");   //8
		sb.append(",");
		sb.append(string_fields.get("lo1_organizationname"));   // 9   brokerage
		sb.append(",");
		sb.append(integer_fields.get("days_on"));    // 10    days on market
		sb.append(",");
		sb.append(integer_fields.get("full_baths"));     //  11 bathrooms
		sb.append(",");
		sb.append(string_fields.get("three_twenty_path"));   //  12 image
		sb.append(",");
		sb.append(string_fields.get("postal"));       // 13 postal
		sb.append(",");
		sb.append(string_fields.get("l_displayid")); // 14 MLS_number
		sb.append(",");
		sb.append(is_saved);     //15
		sb.append(",\"");
		sb.append(getJSRealtor_remarks_22());  // 16
		sb.append("\",\"");
		sb.append(string_fields.get("property_type"));  // 17
		sb.append("\"\n");
		return sb.toString();
	}

	
	public String getSliderArray(){
		StringBuffer sb = new StringBuffer();
		if(thumbs!=null && thumbs.length>0){
			for(int i=0; i<thumbs.length; i++){
				sb.append("<div class=\"w-slide\"><img src=\"https://s3-us-west-2.amazonaws.com/royaltygroupimages/");
				sb.append(thumbs[i]);
				sb.append("\"  /> </div>");
			}
		}
		return sb.toString();
	}
	
	public String getSummaryCollumnNew(){
		StringBuffer sb = new StringBuffer();
    	sb.append("<table>"); sb.append("<tr ><td>Price</td><td>"); 
    	sb.append(getFormattedPrice()); 
    	sb.append("</td><td style=\" padding:0px;\" rowspan=\"12\">");
    	sb.append("<iframe id=\"singlegmap\"  style=\"margin-right:0px;");
    	sb.append("margin-left: 15px; ");
    	sb.append("margin-top: 6px;");
    	sb.append("margin-bottom:0px;");
    	sb.append("width:450px;");
    	sb.append("height:343px;");
    	sb.append("background-color:orange; ");
    	sb.append("\"  src=\"SingleMap\" frameborder=\"0\"> </iframe>");
    	sb.append("</td></tr>");
		sb.append("<tr><td>Size</td><td>"); sb.append(getIntValue("sqft")); sb.append("SqFt</td></tr>");
		sb.append("<tr><td>Age</td><td>");  sb.append(integer_fields.get("property_age"));  sb.append("</td></tr>");	
		sb.append("<tr><td>Baths</td><td>");    sb.append(integer_fields.get("full_baths")); sb.append("</td></tr>");
		sb.append("<tr><td>Beds</td><td>");  sb.append(integer_fields.get("bedrooms"));  sb.append("</td></tr>");
		sb.append("<tr><td>Lot Area</td><td>");       sb.append(integer_fields.get("lot_sqft"));  sb.append("</td></tr>");
		sb.append("<tr><td>Lot Frontage</td><td>");   sb.append(integer_fields.get("frontage"));   sb.append("</td></tr>");
		sb.append("<tr><td>Lot Depth</td><td>");   sb.append(string_fields.get("lot_depth"));   sb.append("</td></tr>");
		sb.append("<tr><td>Property Tax</td><td>"); sb.append(string_fields.get("gross_tax"));   sb.append("</td></tr>");
		sb.append("<tr><td>Maintenance</td><td>");          sb.append(string_fields.get("maint_fee"));   sb.append("</td></tr>");
		sb.append("<tr><td>Days on Market </td><td>");         sb.append(integer_fields.get("ays_on"));   sb.append("</td></tr>");
		sb.append("<tr><td>MLS </td><td>");         sb.append(string_fields.get("l_displayid"));   sb.append("</td></tr>");
		sb.append("</table>");
        return sb.toString();
	}


	
	
	public String Listing_info_general(){
		StringBuffer sb = new StringBuffer();
		sb.append("<div><strong>");
		sb.append(string_fields.get("l_address"));
		sb.append(", ");
		sb.append(string_fields.get("l_city"));
		sb.append(", ");
		sb.append(string_fields.get("postal")); 
		sb.append("</strong><br>");
        sb.append(string_fields.get("lo1_organizationname"));
        sb.append(" (");
        sb.append(string_fields.get("neighborhood"));
        sb.append(")");
        sb.append("</div>");
		return sb.toString();
	}
	
	
	
	/************************
	 * 
	 * END MIGRATED METHODS
	 * 
	 */

	
	
	

}
