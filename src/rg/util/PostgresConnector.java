package rg.util;






import java.sql.DriverManager;



import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;

//import rg.ctm.CustomSearch;
import rg.ent.NewSummaryListing;

//import rg.sch.SearchResultsContainer;

public class PostgresConnector {
	//private static StringBuilder messages;
	private static java.sql.Connection connection;
	private static PostgresConnector pgc = new PostgresConnector( );
	private PostgresConnector(){ }
	
	public static PostgresConnector getInstance( ) {      return pgc;}
	public static java.sql.Connection getConnection(){ 
		try {
			if(connection==null || connection.isClosed()){ connection=init();}
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return connection;
	}

	
	public static void updateVisitorMLS(java.util.TreeMap<String, String> mls_to_save, java.util.TreeSet<String> mls_to_delete, int vis_id){
		try{
		
			/****
			 * First, SELECT from the visitor's save listings any MLS numbers 
			 * matching the list to be saved   (mls_to_save)
			 */
			int counter = 1;
			StringBuffer sb = new StringBuffer();
			if(!mls_to_save.isEmpty()){
				sb.append("select vis_id, mls_num from visitor_mls where mls_num in (");
				java.util.Set<String> key_set = mls_to_save.keySet();
				java.util.Iterator<String> key_it = key_set.iterator();
				
				while(key_it.hasNext()){
					if(counter>1){	sb.append(",");			}
					key_it.next();
					sb.append("?");
					counter=counter+1;
				}
				sb.append(") and vis_id=? ");
				connection = getConnection();
	
				PreparedStatement stmt = connection.prepareStatement(sb.toString());
				key_it = key_set.iterator();
				counter=1;  String key_placeholder ="";
				while(key_it.hasNext()){
					key_placeholder = key_it.next();
					stmt.setString(counter, key_placeholder);
					counter= counter+1;	
				}
				//========================================
				/***
				 * next REMOVE all MLS numbers from the update which are found
				 *  in the visitor's save Listings table
				 *  (you dont need to save mls numbers that are already saved )
				 */
				stmt.setInt(counter, vis_id);	
				ResultSet rs = stmt.executeQuery();
				while(rs.next()){
					mls_to_save.remove(rs.getString("mls_num"));
				}
			//}
				//========================================
			/****
			 *  after we have eliminated possible duplicates, insert MLS numbers 
			 *  from the list "mls_to_be_saved" 
			 */
			//if(!mls_to_save.isEmpty()){
				PreparedStatement insert_stmt = connection.prepareStatement("insert into visitor_mls (vis_id, mls_num, street_addr ) values (?,?,?)");
				connection.setAutoCommit(false);
				key_set = mls_to_save.keySet();
				key_it = key_set.iterator();
				key_placeholder = "";
				while(key_it.hasNext()){
					key_placeholder = key_it.next();
					insert_stmt.setInt(1, vis_id);
					insert_stmt.setString(2, key_placeholder);
					insert_stmt.setString(3, mls_to_save.get(key_placeholder));
					insert_stmt.addBatch();
				}		
				insert_stmt.executeBatch();
				connection.commit();
			}
			//=========================================
			/***
			 *  Finally, mls numbers to be removed from the visitor's saved mls-number table
			 */
			if(!mls_to_delete.isEmpty()){
				int sze = mls_to_delete.size();
				sb = new StringBuffer();
				sb.append("delete from visitor_mls where mls_num in (");
				for(int i=0; i<sze; i++){
					sb.append("?");
					if(i<sze-1){
						sb.append(",");
					}
				}
				sb.append(")");
				sb.append(" and vis_id=? and vis_id>0");	
		
				if(connection==null){connection = getConnection(); }
				PreparedStatement del_stmt = connection.prepareStatement(sb.toString());
				connection.setAutoCommit(true);
				java.util.Iterator<String> it = mls_to_delete.iterator();
				counter = 1;  String to_delete = "";
				while(it.hasNext()){
					to_delete = it.next();
					del_stmt.setString(counter, to_delete);
					counter = counter+1;
				}
				del_stmt.setInt(counter, vis_id);
				del_stmt.execute();
			}	
			connection.close();
			connection=null;
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	
	
	
	
	
	
	
	public static void updateVisitorSearchInterface(int search_interface, int visitor_id){
		try{
		
			connection = getConnection();
			PreparedStatement stmt = connection.prepareStatement("update visitor_profile set search_interface=? where visitor_id=?");
			stmt.setInt(1, search_interface);
			stmt.setInt(2, visitor_id);
			
			stmt.execute();
			
			connection.close();
			connection=null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	

	
	public static String tryConnection(){
		StringBuffer sb = new StringBuffer();
		try{
			connection = getConnection();
			PreparedStatement stmt = connection.prepareStatement("select count (visitor_id) num from visitor_profile ");
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				sb.append(rs.getInt("num"));
			}
			connection.close();
			connection=null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	

	
		
	
	
	
		
	
	

	
	
	
	
	
	public static void  updateListingRealtor(String sql, String num, String bool){
		try{	
			connection = getConnection();
			PreparedStatement prepStmt = connection.prepareStatement(sql);
			
			//System.out.println(sql);
			//System.out.println(num);
			//System.out.println(bool);
					
			prepStmt.setBoolean(1, Boolean.parseBoolean(bool));
			prepStmt.setInt(2, Integer.parseInt(num));

			prepStmt.execute();
			connection.close();
			connection = null;
			
		}catch(SQLException e){
			System.out.println("PostGresConnector.storeNewContentItem: "+e );
		}
	}
	
		
	private static java.sql.Connection init(){
		connection = null;
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFoundException "+e+"<br />");
			System.out.println("Where is your PostgreSQL JDBC Driver? Include in your library path!<br />");
		}
		try {	
			connection = DriverManager.getConnection(		
			"jdbc:postgresql://aa1w2fh7xu35vbl.cngclcqagnfu.us-west-2.rds.amazonaws.com:5432/ebdb?user=rgrsiteuser&password=shadrach99"
				);
			/*
					"jdbc:postgresql://127.0.0.1:5432/property_listings", 
					"postgres",
					"Saturn5");
			 */
			//System.out.println("connection succeeded <br />");
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console<br />");

			e.printStackTrace();
		}
		if (connection == null) {
			System.out.println("Failed to make connection!\n");
		}
		return connection;
	}


		
	
	public static void updatememberVisibility(int mem_id, boolean visible){
		try{
			connection = getConnection();		
			PreparedStatement prepstmt = connection.prepareStatement("update member set visible=? where m_id=?"); 
			prepstmt.setBoolean(1, visible);
			prepstmt.setInt(2, mem_id);
			prepstmt.executeUpdate();  
			connection.close();
			connection=null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


		

	
	public static java.util.TreeSet<String> loadListingBrokerages(){
		java.util.TreeSet<String> listing_brokerages = new java.util.TreeSet<String>();
		try{
			connection = getConnection();		
			PreparedStatement prepstmt = connection.prepareStatement("select distinct lo1_organizationname  as org_name from listing_realtor order by lo1_organizationname"); 
			ResultSet rs = prepstmt.executeQuery();
			while(rs.next()){
				listing_brokerages.add(rs.getString("org_name"));
			}
			connection.close();
			connection=null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listing_brokerages;
	}
	
	public static java.util.TreeMap<String, rg.ent.NewUser> loadAgentsbyListings(java.util.Set<String> l_display_ids){
		rg.ent.NewUser usr= null;
		java.util.TreeMap<String, rg.ent.NewUser> users_arr = new java.util.TreeMap<String, rg.ent.NewUser>();
		try{
			connection = getConnection();	
			StringBuffer sb = new StringBuffer();
			sb.append("select m_id,img_id,first_name, last_name, email, pass_hash, role, areas_serviced, description, languages, salesforce_id,phone, visible, mem_login, datecreated, web_site, link_text,admin_level, thumb_320 from member where visible='true' ");
			sb.append(" and mem_login in (");  
			java.util.Iterator<String> string_it = l_display_ids.iterator();
			int counter = 0;
			while(string_it.hasNext()){
				string_it.next();
				if(counter>0){
					sb.append(","); 
				}
				sb.append("?");
				counter++;
			}
			sb.append(") order by first_name "); 
			
			System.out.println(sb.toString());
			
			
			PreparedStatement prepstmt = connection.prepareStatement(sb.toString());	
			string_it = l_display_ids.iterator();
			counter = 1;
			while(string_it.hasNext()){
				
				prepstmt.setString(counter, string_it.next());
				
				counter++;
			}
			
			//S
			//prepstmt.setString(1, key_field);
			ResultSet rs = prepstmt.executeQuery();
			while(rs.next()){
				
				usr = new rg.ent.NewUser();	
				usr.setId(rs.getInt("m_id"));
				usr.setAdmin_level(rs.getInt("admin_level"));
				usr.setString_field("first_name",rs.getString("first_name"));
				usr.setString_field("last_name",rs.getString("last_name"));
				usr.setString_field("user_email",rs.getString("email"));
				usr.setString_field("phone",rs.getString("phone"));
				usr.setString_field("role",rs.getString("role"));
				
				
				usr.setString_field("areas_serviced", rs.getString("areas_serviced"));
				usr.setString_field("description", rs.getString("description")); 
				usr.setString_field("languages", rs.getString("languages"));
				//rs.getString("phone"); 
				//rs.getBoolean("visible"); 
				usr.setString_field("mem_login",rs.getString("mem_login"));
				
				usr.setString_field("web_site", rs.getString("web_site")); 
				usr.setString_field("link_text", rs.getString("link_text"));
				usr.setAdmin_level(rs.getInt("admin_level")); 
				usr.setString_field("thumb_320", rs.getString("thumb_320")); 
				
				
				if(rs.getString("thumb_320")!=null && rs.getString("thumb_320").length()>0){
					usr.setString_field("thumb",rs.getString("thumb_320"));
				}
				users_arr.put(usr.getStringValue("first_name"), usr);	
			}
			
			connection.close();
			connection=null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return users_arr;
	}
	
	
	/*
	public static java.util.HashMap<String, rg.lst.Member> loadMembers(){
		rg.lst.Member mem= null;
		java.util.HashMap<String, rg.lst.Member> members = new java.util.HashMap<String, rg.lst.Member>();
		try{
			connection = getConnection();		
			PreparedStatement prepstmt = connection.prepareStatement("select m_id,img_id,first_name, last_name, email, pass_hash, role, areas_serviced, description, languages, salesforce_id,phone, visible, mem_login, datecreated, web_site, link_text,admin_level, thumb_320 from member where visible='true'"); 
		
			ResultSet rs = prepstmt.executeQuery();
			while(rs.next()){
				
					mem = new rg.lst.Member();
					mem.setM_id(rs.getInt("m_id"));
					mem.setFirst_name(rs.getString("first_name"));
					mem.setLast_name(rs.getString("last_name"));
					mem.setMem_login(rs.getString("mem_login"));
					mem.setEmail(rs.getString("email"));
					mem.setPhone(rs.getString("phone"));
					mem.setAreas_serviced(rs.getString("areas_serviced"));
					mem.setLanguages(rs.getString("languages"));
					mem.setDescription(rs.getString("description"));
					
					mem.setVisible(rs.getBoolean("visible"));
					mem.setWeb_site(rs.getString("web_site"));
					mem.setLink_text(rs.getString("link_text"));
					mem.setThumb_320(rs.getString("thumb_320"));
							
					members.put(mem.getTextKey(), mem);
			}
			
			connection.close();
			connection=null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return members;
	}
	*/
	
	/* public static java.util.ArrayList<String> loadSubAreas(){
		java.util.ArrayList<String> sub_areas = new java.util.ArrayList<String>();
		try{
			connection = getConnection();		
			PreparedStatement prepstmt = connection.prepareStatement("select distinct lm_char10_5 from listing order by lm_char10_5");
			ResultSet rs = prepstmt.executeQuery();
			while(rs.next()){	sub_areas.add(rs.getString("lm_char10_5"));	}
			connection.close();
			connection=null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return sub_areas;
	}	*/
	
	
	
	public static java.util.ArrayList<String> loadSubAreas(){
	
		java.util.ArrayList<String> sub_areas = new java.util.ArrayList<String>();
		try{
			connection = getConnection();		
			PreparedStatement prepstmt = connection.prepareStatement("select distinct lm_char10_5 from listing order by lm_char10_5"); 
		
			ResultSet rs = prepstmt.executeQuery();
			while(rs.next()){
				
				sub_areas.add(rs.getString("lm_char10_5"));
			
			}
			
			connection.close();
			connection=null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return sub_areas;
	}



	public static void FlagAllToDelete(boolean tokeep){
		try{
			connection = getConnection();
			PreparedStatement prep_stmt = connection.prepareStatement("update Listing set tokeep=?");
			connection.setAutoCommit(false);
			prep_stmt.setBoolean(1, tokeep);	
			prep_stmt.execute();
			connection.commit();
			connection.close();
			connection=null;
		} catch (SQLException e) {
			System.out.println(e);
			System.out.println(e.getNextException());
		}
	}
	

	
/*
	private static int findCategory(){
		int last_id = 0;
		try{
			connection = getConnection();
			PreparedStatement prepstmt = connection.prepareStatement("select * from listing where lr_remarks33 LIKE '%tile%' limit 10");   //.createPreparedStatement("");
			ResultSet rs=  prepstmt.executeQuery();
			while(rs.next()){
				//System.out.println("["+rs.getString("l_displayid")+"]");
				//System.out.println(rs.getString("lr_remarks22"));
				
				//System.out.println(" found record");
			}
			connection.close();
			
		} catch (SQLException e) {
			System.out.println(e);
			System.out.println(e.getNextException());
		}
		//System.out.println("getlastrecordid returning "+last_id);
	 return last_id;
	}
	*/
	
	
	
	
	private static int getLastRecordID(String prop_type){
		int last_id = 0;
		try{
			connection = getConnection();
			PreparedStatement prepstmt = connection.prepareStatement("select l_listingid from listing where lm_char1_36=? order by l_listingid desc limit 1");   //.createPreparedStatement("");
			prepstmt.setString(1, prop_type);
			ResultSet rs=  prepstmt.executeQuery();
			while(rs.next()){
				last_id = rs.getInt("l_listingid");
			}
			connection.close();
			
		} catch (SQLException e) {
			System.out.println(e);
			System.out.println(e.getNextException());
		}
		System.out.println("getlastrecordid returning "+last_id);
	 return last_id;
	}
	/*
	public static SearchResultsContainer loadbyMls(String mls_num){
		
		SearchResultsContainer slc = new SearchResultsContainer();
		NewSummaryListing sl = null;
		try{
			connection = getConnection();  //lfd_featuresincluded_24, lfd_siteinfluences_15,
			StringBuilder sb = new StringBuilder();
			sb.append(" select l_listingid,l_displayid,l_askingprice,lm_dec_1,lm_int1_4,lm_int1_18,lmd_mp_latitude,lmd_mp_longitude,");
			sb.append(" l_address,l_area, l_city, lm_char10_11,lm_char10_12, lm_char10_5, sqft, l_zip, lr_remarks22,lr_remarks33,cast(l_picturecount as integer) picturecount,l_state, lo1_organizationname,");
			sb.append(" l_listingdate, cast(DATE_PART('day', CURRENT_DATE::timestamp -  L_LISTINGDATE::timestamp) as integer ) days_on, CAST( (date_part('year', CURRENT_DATE) - cast(lm_int2_2 as integer)) AS INTEGER) prop_age, lm_char100_3,  lm_int2_2,six_forty_path, three_twenty_path, one_sixty_path, ");
			sb.append(" lm_dec_16 gros_tax, lm_dec_22 maint_fee, lot_sqft, frontage, lm_char30_28 lot_depth, la1_loginname ");
			sb.append(" from Listing where l_displayid=?");
			//System.out.println(sb.toString());
			//System.out.println(mls_num);
			
			PreparedStatement prepstmt = connection.prepareStatement(sb.toString()); 
			prepstmt.setString(1,mls_num);
			ResultSet rs = prepstmt.executeQuery();
			//String zip_param = "";
			while(rs.next()){
				System.out.println("found listing");
				sl =new NewSummaryListing();
	
						sl.setAskingprice(rs.getBigDecimal("l_askingprice"));
						
						sl.setInt_field("l_listingid", rs.getInt("l_listingid"));
						sl.setInt_field("sqft",rs.getInt("sqft"));   //sl.setSquare_feet(rs.getInt("sqft"));  // square feet
						sl.setInt_field("bedrooms", rs.getInt("lm_int1_4"));			//sl.setBedrooms(rs.getInt("lm_int1_4"));  //bedrooms
						sl.setInt_field("full_baths", rs.getInt("lm_int1_18"));  //bathrooms
						sl.setInt_field("days_on",rs.getInt("days_on"));
						sl.setInt_field("property_age",rs.getInt("prop_age"));
						sl.setInt_field("number_of_pics", rs.getInt("picturecount"));
						sl.setInt_field("lot_sqft", rs.getInt("lot_sqft"));
						sl.setInt_field("frontage",rs.getInt("frontage"));
						
						sl.setString_field("l_displayid", rs.getString("l_displayid") );  //sl.setL_displayid(rs.getString("l_displayid"));			
						sl.setString_field("lmd_mp_latitude",rs.getString("lmd_mp_latitude"));
						sl.setString_field("lmd_mp_longitude",rs.getString("lmd_mp_longitude"));
						sl.setString_field("l_address",rg.util.StringCleaner.cleanStreetAddress(rs.getString("l_address")));
						sl.setString_field("l_area",rs.getString("l_area"));
						sl.setString_field("l_city", rs.getString("l_city"));
						sl.setString_field("l_state", rs.getString("l_state"));
						sl.setString_field("property_type", rs.getString("lm_char10_11"));
						sl.setString_field("neighborhood",rs.getString("lm_char10_5"));
						sl.setString_field("lo1_organizationname", rs.getString("lo1_organizationname"));	
						sl.setString_field("six_forty_path", rs.getString("six_forty_path"));
						sl.setString_field("three_twenty_path", rs.getString("three_twenty_path"));
						sl.setString_field("one_sixty_path", rs.getString("one_sixty_path"));
						sl.setString_field("postal",rs.getString("l_zip"));	
						sl.setString_field("lr_remarks22",rs.getString("lr_remarks22"));
						sl.setString_field("lr_remarks33",rs.getString("lr_remarks33"));
						sl.setString_field("view",rs.getString("lm_char100_3"));
						
						//sl.setString_field("lfd_featuresincluded_24",""); 
						//sl.setString_field("lfd_siteinfluences_15","");
						//sl.setString_field("lfd_styleofhome_1","");
						//sl.setString_field("lft_amenities_25","");
						//sl.setString_field("lfd_basementarea_6","");
						 sl.setString_field("gross_tax",rs.getString("gros_tax"));  //lm_dec_16
						 sl.setString_field("maint_fee",rs.getString("maint_fee"));  //lm_dec_22 maint_fee
						 sl.setString_field("lot_depth",rs.getString("lot_depth"));
						 sl.setString_field("la1_loginname", rs.getString("la1_loginname"));
					
						//date_fields.put("l_last_photo_updt", cal);
						 sl.setDate_field("l_listingdate", rg.util.DateUtil.sqlDateToCalendar(rs.getDate("l_listingdate")));
						 //date_fields.put("l_updatedate", cal);
					
						 int count=0;
		
						java.util.ArrayList<String> thumbs_640 = null;
						
						if(sl.getIntValue("number_of_pics")>0){				
							PreparedStatement prepstmt_0 = connection.prepareStatement("select l_objectid, host_domain, host_folder,six_forty_path,three_twenty_path, one_sixty_path from flat_image  where l_listingid=? order by l_objectid asc");
							thumbs_640 = new java.util.ArrayList<String>();
							prepstmt_0.setInt(1,sl.getIntValue("l_listingid"));
							ResultSet rs0 = prepstmt_0.executeQuery();
							while(rs0.next()){
								count++;
								thumbs_640.add(rs0.getString("six_forty_path"));
							}
						}
						if(count>0){ 
							String[] arr = thumbs_640.toArray(new String[thumbs_640.size()]);
							sl.setThumbs(arr);
						}else{
							sl.setThumbs(new String[0]); 
						}
	
						System.out.println("line 851 PostgresConnector reached");
						slc.setSingleListing(sl);
			}
			connection.close();
			connection =null;
		} catch  (SQLException e) {
			System.out.println(" one one");
			System.out.println(e);
			System.out.println(e.getNextException());
		}
	return slc;
	}
	*/
	
	public static void saveTextSearch(String search){
		
			try{
			connection = getConnection();
		
			PreparedStatement prepstmt = connection.prepareStatement("insert into text_search (ts_text,ts_time) values (?, now())"); 
			prepstmt.setString(1, search);
			prepstmt.execute();
			connection.close();
			connection=null;
			}catch(SQLException e){
				System.out.println(" saveTextSearch " +e);
				e.printStackTrace();
			}	

		}



/*	public static java.util.TreeMap<String, rg.ent.ListingAddress> loadAddressList(String s ){
		java.util.TreeMap<String, rg.ent.ListingAddress> address_list = new java.util.TreeMap<String, rg.ent.ListingAddress>();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT distinct street_address, city,postal,building_name,latitude, longitude, ts_rank(text_address_search, to_tsquery(?) ) ");
		sb.append(" AS rank from address_list where ts_rank(text_address_search, to_tsquery(?) ) >0.001 order by rank desc limit 10");
		try{
			
			connection = getConnection();
			rg.ent.ListingAddress la = null;
			PreparedStatement prepstmt = connection.prepareStatement(sb.toString()); 	
			prepstmt.setString(1, s);
			prepstmt.setString(2, s);
	
			ResultSet rs=  prepstmt.executeQuery();
			
			while(rs.next()){	
				la= new rg.ent.ListingAddress();
				la.setStreet_address(rs.getString("street_address")); 
				la.setCity(rs.getString("city"));
				la.setPostal(rs.getString("postal"));
				la.setBuilding_name(rs.getString("building_name"));
				la.setLatitude(rs.getString("latitude")); 
				la.setLongitude(rs.getString("longitude"));
				address_list.put(la.getStreet_address(), la);
	
			}
			connection.close();
			connection=null;
			}catch(SQLException e){
				System.out.println("hey hey doListingsSearch_b " +e);
				e.printStackTrace();
			}
		return address_list;
	}
	*/
	
	public static java.util.TreeMap<String, NewSummaryListing>  loadRoyaltyListings(){
		
		java.util.TreeMap<String, NewSummaryListing> search_results = new java.util.TreeMap<String, NewSummaryListing>();
		try{
		connection = getConnection();
	
		StringBuffer sb = new StringBuffer();
		sb.append("select "); 
		
		/*
		 * 
		 *  l_listingid, l_displayid, l_askingprice, sqft, lot_sqft, lm_int1_4, lm_int1_18, lmd_mp_latitude, lmd_mp_longitude, l_address, l_addressstreet, l_city, l_zip, lm_char10_5, lm_char1_36, lm_char10_11, lo1_organizationname,l_listingdate,
		 *  cast(DATE_PART('day', CURRENT_DATE::timestamp -  L_LISTINGDATE::timestamp) as integer ) days_on, six_forty_path,three_twenty_path,one_sixty_path, lr_remarks22,
		 *   frontage
		 * 
		 */
		
		sb.append("lmd_mp_latitude, lmd_mp_longitude, l_address, l_area, l_city, l_state, l_addressstreet,l_displayid, ");
		sb.append("l_askingprice, lm_char10_11, "); //property_type, 
		sb.append(" lm_char10_5, "); // neighborhood, 
		sb.append(" lo1_organizationname, six_forty_path, three_twenty_path, one_sixty_path, ");
		//sb.append("l_zip postal,");
		sb.append("l_zip, ");
		sb.append(" lr_remarks22, lr_remarks33, lm_char100_3 the_view, lfd_featuresincluded_24, lfd_siteinfluences_15, lfd_styleofhome_1, lfd_amenities_25, lfd_basementarea_6,");

		sb.append("lm_dec_16 gross_tax, lm_dec_22 maint_fee, lm_char30_28 lot_depth,la1_loginname,l_listingid,sqft,");
	  
		sb.append("lm_int1_4 , ");  //bedrooms
		sb.append("lm_int1_18,  ");  // full_baths
		sb.append(" cast(DATE_PART('day', CURRENT_DATE::timestamp -  L_LISTINGDATE::timestamp) as integer ) days_on, l_listingdate,");
		
	  
		sb.append("l_picturecount number_of_pics,lot_sqft,frontage ");
		sb.append(" from listing where l_listoffice1 in ('2901','2832')");
		
		System.out.println(sb.toString());
		
		PreparedStatement prepstmt = connection.prepareStatement(sb.toString()); 
		ResultSet rs=  prepstmt.executeQuery();
		NewSummaryListing sl = null;
		while(rs.next()){	
			
			sl = getListing(rs);
			search_results.put(sl.getStringValue("l_displayid"),sl);   
		}
		connection.close();
		connection=null;
		}catch(SQLException e){
			System.out.println("hey hey doListingsSearch_b " +e);
			e.printStackTrace();
		}	
		return search_results;
	}

	
	
	
	
	private static NewSummaryListing getListing(java.sql.ResultSet rs){
		NewSummaryListing sl = new NewSummaryListing();
		
		try {

		sl.setInt_field("l_listingid",rs.getInt("l_listingid"));
		
		sl.setString_field("l_displayid",rs.getString("l_displayid"));
		sl.setString_field("la1_loginname", rs.getString("la1_loginname"));
		
		sl.setAskingprice(rs.getBigDecimal("l_askingprice"));
		sl.setInt_field("sqft",rs.getInt("sqft"));  // square feet ( interior )
		sl.setInt_field("lot_sqft",rs.getInt("lot_sqft"));   // square foot (of lot )
		
		sl.setInt_field("bedrooms",rs.getInt("lm_int1_4"));  //bedrooms
		sl.setInt_field("full_baths",rs.getInt("lm_int1_18"));  //bathrooms
		sl.setString_field("lmd_mp_latitude",rs.getString("lmd_mp_latitude"));
		sl.setString_field("lmd_mp_longitude",rs.getString("lmd_mp_longitude"));
		sl.setString_field("l_address",rg.util.StringCleaner.cleanStreetAddress(rs.getString("l_address")));
		sl.setString_field("l_addressstreet",rs.getString("l_addressstreet"));
		sl.setString_field("l_city",rs.getString("l_city"));
		sl.setString_field("property_type",rs.getString("lm_char10_11")); // property type
		sl.setString_field("lo1_organizationname",rs.getString("lo1_organizationname"));		
		sl.setDate_field("l_listingdate",rg.util.DateUtil.sqlDateToCalendar(rs.getDate("l_listingdate")));
		sl.setInt_field("days_on",rs.getInt("days_on"));
		sl.setString_field("six_forty_path",rs.getString("six_forty_path"));
		sl.setString_field("three_twenty_path",rs.getString("three_twenty_path"));
		sl.setString_field("one_sixty_path",rs.getString("one_sixty_path"));
		sl.setString_field("postal",rs.getString("l_zip"));
		sl.setString_field("lr_remarks22",rs.getString("lr_remarks22"));
	
		} catch (SQLException e) {
			
			System.out.println("  line 1143 ");
			e.printStackTrace();
		}	
		
		return sl;
		
	}
	
	
	
	
	

	/*
	
	public static java.util.HashMap<Integer, rg.ent.Apartment>  loadList_of_apartments(){
		java.util.HashMap<Integer, rg.ent.Apartment> list = new java.util.HashMap<Integer, rg.ent.Apartment>();
		try{
			connection = getConnection();
			StringBuffer sb = new StringBuffer();
			sb.append("select a_id, show_apartment, building_id, management_company_id,");
			sb.append("member_id,contact_phone, contact_email, show_phone,show_email,unit_number, ");
			sb.append("street_address, city, neighborhood, province, postal, square_feet, facing_nsew, ");
			sb.append("levels, floor_level, has_yard, appliances, bathrooms, bedrooms, type_of_building, ");
			sb.append("num_floors_in_building, building_construction_type, parking_spaces_num, features_in_apartment, ");
			sb.append("features_included_in_building, date_available, restrictions, fire_sprinklers, animals_restricted,");
			sb.append("smoking_restricted, furnished, age_restricted, price_per_month, sublet_restricted, ");
			sb.append("security_deposit, photo_1, photo_2, photo_3, photo_4, photo_5, photo_6, photo_7, photo_8,");
			sb.append("photo_9, photo_1_thumb, photo_2_thumb, photo_3_thumb, photo_4_thumb, photo_5_thumb,");
			sb.append("photo_6_thumb, photo_7_thumb, photo_8_thumb, photo_9_thumb from apartment");
			
			PreparedStatement prepStmt = connection.prepareStatement(sb.toString());  
				
			ResultSet rs = prepStmt.executeQuery();
			rg.ent.Apartment apt = null;
			while(rs.next()){
				apt = new rg.ent.Apartment();
				apt.setIntValue("a_id",rs.getInt("a_id"));
				apt.setBoolValue("show_apartment",rs.getBoolean("show_apartment"));
				apt.setIntValue("building_id",rs.getInt("building_id"));
				apt.setIntValue("management_company_id",rs.getInt("management_company_id"));
				apt.setIntValue("member_id",rs.getInt("member_id"));
				apt.setStringValue("contact_phone",rs.getString("contact_phone"));
				apt.setStringValue("contact_email",rs.getString("contact_email"));
				apt.setBoolValue("show_email",rs.getBoolean("show_email"));
				apt.setBoolValue("show_phone",rs.getBoolean("show_phone"));
				apt.setStringValue("unit_number",rs.getString("unit_number"));
				
				apt.setStringValue("street_address",rs.getString("street_address"));
				apt.setStringValue("city",rs.getString("city"));
				apt.setStringValue("neighborhood",rs.getString("neighborhood"));
				apt.setStringValue("province",rs.getString("province"));
				apt.setStringValue("postal",rs.getString("postal"));
				apt.setIntValue("square_feet",rs.getInt("square_feet"));
				apt.setStringValue("facing_NSEW",rs.getString("facing_nsew"));
				apt.setIntValue("levels",rs.getInt("levels"));
				apt.setIntValue("floor_level",rs.getInt("floor_level")); 
				apt.setBoolValue("has_yard",rs.getBoolean("has_yard"));
				apt.setStringValue("appliances",rs.getString("appliances")); 
				apt.setIntValue("bathrooms",rs.getInt("bathrooms"));
				apt.setIntValue("bedrooms",rs.getInt("bedrooms"));
				apt.setStringValue("type_of_building",rs.getString("type_of_building")); 
				apt.setIntValue("num_floors_in_building",rs.getInt("num_floors_in_building"));
				apt.setStringValue("building_construction_type",rs.getString("building_construction_type")); 
				apt.setIntValue("parking_spaces_num",rs.getInt("parking_spaces_num"));
				apt.setStringValue("features_in_apartment",rs.getString("features_in_apartment")); 
				apt.setStringValue("features_included_in_building",rs.getString("features_included_in_building"));
				apt.setDate_available(rg.util.DateValidator.makeGregorian(rs.getDate("date_available")));
				apt.setStringValue("restrictions",rs.getString("restrictions"));
				apt.setBoolValue("fire_sprinklers",rs.getBoolean("fire_sprinklers")); 
				apt.setBoolValue("animals_restricted",rs.getBoolean("animals_restricted"));
				apt.setBoolValue("smoking_restricted",rs.getBoolean("smoking_restricted"));
				apt.setBoolValue("furnished",rs.getBoolean("furnished"));
				apt.setBoolValue("age_restricted",rs.getBoolean("age_restricted")); 
				apt.setBigDecimalValue("price_per_month",rs.getBigDecimal("price_per_month"));
				apt.setBoolValue("sublet_restricted",rs.getBoolean("sublet_restricted"));
				apt.setBigDecimalValue("security_deposit",rs.getBigDecimal("security_deposit"));
				apt.setStringValue("photo_1",rs.getString("photo_1"));
				apt.setStringValue("photo_2",rs.getString("photo_2"));
				apt.setStringValue("photo_3",rs.getString("photo_3"));
				apt.setStringValue("photo_4",rs.getString("photo_4"));
				apt.setStringValue("photo_5",rs.getString("photo_5"));
				apt.setStringValue("photo_6",rs.getString("photo_6"));
				apt.setStringValue("photo_7",rs.getString("photo_7"));
				apt.setStringValue("photo_8",rs.getString("photo_8"));
				apt.setStringValue("photo_9",rs.getString("photo_9"));
				apt.setStringValue("photo_1_thumb",rs.getString("photo_1_thumb")); 
				apt.setStringValue("photo_2_thumb",rs.getString("photo_2_thumb"));
				apt.setStringValue("photo_3_thumb",rs.getString("photo_3_thumb"));
				apt.setStringValue("photo_4_thumb",rs.getString("photo_4_thumb"));
				apt.setStringValue("photo_5_thumb",rs.getString("photo_5_thumb"));
				apt.setStringValue("photo_6_thumb",rs.getString("photo_6_thumb"));
				apt.setStringValue("photo_7_thumb",rs.getString("photo_7_thumb"));
				apt.setStringValue("photo_8_thumb",rs.getString("photo_8_thumb"));
				apt.setStringValue("photo_9_thumb",rs.getString("photo_9_thumb"));
				
				list.put(apt.getA_id(), apt);
			}
			
			connection.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return list;
	}
*/
	
	/* Required : mls_num, first_name, email, phone  */
	/*private static void storeNewLead(
			String mls_num,
			String first_name, 
			String last_name, 
			String street_address, 
			String email,
			String comments,
			String phone_number){
		
		try{
			StringBuffer sb = new StringBuffer();
			sb.append("insert into Lead (");
			 sb.append("frm_subject, frm_name, phone_number, email ");
		
			if(last_name!=null){
				sb.append(",last_name");
			}
			if(street_address!=null){
				sb.append(",street_address");
			}
			if(comments!=null){
				sb.append(",comments");
			}
			sb.append("   ) values (");
			sb.append("?,?,?,?");   // phone_number
			
			if(last_name!=null){
				sb.append(",?");  // last_name
			}
			if(street_address!=null){
				sb.append(",?");  // street_address
			}
			if(comments!=null){
				sb.append(",?");
			}
		
			sb.append(")");
	
			connection  = getConnection();
			int num_of_strings = 1;
			PreparedStatement prepStmt = connection.prepareStatement(sb.toString());
			if(mls_num!=null){	
				prepStmt.setString(num_of_strings, mls_num);	
				num_of_strings = num_of_strings+1;	
			}
			if(email!=null){	
				prepStmt.setString(num_of_strings, first_name);	
				num_of_strings = num_of_strings+1;	
			}
			if(phone_number!=null){	
				prepStmt.setString(num_of_strings, phone_number);
				num_of_strings = num_of_strings+1;	
			}
			if(first_name!=null){	
				prepStmt.setString(num_of_strings, email);
				num_of_strings = num_of_strings+1;	
			}
			if(last_name!=null){	
				prepStmt.setString(num_of_strings, last_name);	
				num_of_strings = num_of_strings +1;
			}
			if(street_address!=null){	
				prepStmt.setString(num_of_strings, street_address);		
				num_of_strings = num_of_strings+1;	
			}			
			if(comments!=null){	
				prepStmt.setString(num_of_strings, comments);		
				num_of_strings = num_of_strings+1;	
			}
			prepStmt.execute();
			connection.close();
			connection=null;
			
		}catch(Exception e){
			System.out.println("PostGresConnector.storeNewContentItem: "+e );
		}
	}
	
	*/
	
	
	
	public static boolean checkMemberExists(String fname, String lname){
		boolean exists = false;
		try{
			connection = getConnection();
			PreparedStatement ps = connection.prepareStatement("select first_name,last_name from member where first_name=? and last_name=?");
			ps.setString(1, fname);
			ps.setString(2, lname);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				exists=true;
			}
			ps.close();
			connection.close();
			connection=null;
		}catch(Exception e){
			System.out.println(e);
		}
		return exists;
	}
	
	public static boolean checkEmailInUse(String eml){
		boolean exists = false;
		try{
			connection = getConnection();
			PreparedStatement ps = connection.prepareStatement("select email from member where email=?");
			ps.setString(1, eml);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				exists=true;
			}
			ps.close();
			connection.close();
			connection=null;
		}catch(Exception e){
			System.out.println(e);
		}
		return exists;
	}
	
/*
		
	public static java.util.ArrayList<UploadedImage> loadThumbsArray(java.util.ArrayList<UploadedImage> theImages, int howmany, int load_from){		
		java.util.ArrayList<UploadedImage> theUpImages = new java.util.ArrayList<UploadedImage>();
        try {
            connection =  getConnection();
            PreparedStatement ps = null;
           
            ps = connection.prepareStatement("SELECT id,filename,width,height,t_width,t_height,type,host_address,host_folder, thumbname FROM uploaded_image where id>0 order by id desc limit ?");	
            ps.setInt(1, howmany);
           
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
           
            		new UploadedImage(
            				rs.getInt("id"),
            				rs.getString("filename"),
            				rs.getString("width"),
            				rs.getString("height"),
            				rs.getString("t_width"),
            				rs.getString("t_height"),
            				rs.getString("type"),
            				rs.getString("host_address"),
            				rs.getString("host_folder"),
            				rs.getString("thumbname"));
            		
            }
            if(theUpImages.size()==0){
            	theUpImages = theImages;
            }
            
            rs.close();
            ps.close();
            connection=null;
        } catch (Exception e) {
        	System.out.println(e);
        }
		return theUpImages;
	}
	*/
	/*
	private static void storeNewImgContentItem( String page_label,String c_body, int img_id){
		try{
			
			connection = getConnection();
			PreparedStatement prepStmt = connection.prepareStatement("INSERT INTO content (c_bytes,c_visible,page_label,c_type,c_body,c_heading,c_heading_visible) SELECT imgdata,'true',?,'text/image','<img src=\"InlineImage\" />',?,? FROM uploaded_image WHERE uploaded_image.id=?");
			
			prepStmt.setString(1, page_label);
			prepStmt.setString(2, "New Picture");	// set c_heading
			prepStmt.setBoolean(3, false);  // set c_heading_visible
			prepStmt.setInt(4, img_id);
			
			prepStmt.execute();
			connection.close();
			connection = null;
		}catch(SQLException e){
			System.out.println("PostGresConnector.storeNewContentItem: "+e );
		}
	}
	*/
	
	public static boolean CreateNewUser(String first_name, String last_name, String email, String pass_hash){
		boolean is = false;
		try{
			connection = getConnection();
			PreparedStatement prepStmt = connection.prepareStatement("insert into Member (first_name,last_name,email,pass_hash,datecreated) values (?,?,?,?,now())");		
			prepStmt.setString(1, first_name);
			prepStmt.setString(2, last_name);
			prepStmt.setString(3, email);
			prepStmt.setString(4, pass_hash);
			is = prepStmt.execute();
			connection.close();
			connection=null;
		}catch(SQLException e){
			is=false;
			System.out.println(e);
		}
		return is;
	}
	
	
	/*private static java.util.ArrayList<SummaryListing> loadNewListings(){
		java.util.ArrayList<SummaryListing> search_results = new java.util.ArrayList<SummaryListing>();
		try{
			SummaryListing sl = null;
			connection = getConnection();
			PreparedStatement stmt = connection.prepareStatement("select l_listingid,l_displayid,l_askingprice,lm_dec_7,lm_int1_4,lm_int1_18,lmd_mp_latitude,l_state, l_zip, lmd_mp_longitude,l_address,l_city,lm_char10_11,lo1_organizationname, l_updatedate,l_listingdate,cast(DATE_PART('day', CURRENT_DATE::timestamp -  L_LISTINGDATE::timestamp) as integer ) days_on, CAST(extract(hour from l_updatedate) AS INTEGER) t_hour,  CAST(extract(minute from l_updatedate) AS INTEGER) t_minute, six_forty_path, three_twenty_path, one_sixty_path from listing where one_sixty_path not in ('') order by l_listingdate desc limit 10");
			
			ResultSet rs=  stmt.executeQuery();
			while(rs.next()){
		
				sl = new SummaryListing();
				sl.setL_listingid(rs.getInt("l_listingid"));
				sl.setL_displayid(rs.getString("l_displayid"));
				sl.setDecimalPrice(rs.getBigDecimal("l_askingprice"));
				sl.setSquare_feet(rs.getInt("sqft"));  // square feet
				sl.setBedrooms(rs.getInt("lm_int1_4"));  //bedrooms
				sl.setBathrooms(rs.getInt("lm_int1_18"));  //bathrooms
				sl.setLatitude(rs.getString("lmd_mp_latitude"));
				sl.setLongitude(rs.getString("lmd_mp_longitude"));
				sl.setStreet_address(rs.getString("l_address"));
				sl.setL_city(rs.getString("l_city"));
				sl.setPostal(rs.getString("l_zip"));
				sl.setL_state(rs.getString("l_state"));
				sl.setProp_type(rs.getString("lm_char10_11")); // property type
				sl.setDays_on(rs.getInt("days_on")+1);
				sl.setOrg_name(rs.getString("lo1_organizationname"));
				sl.setListingDate(rs.getDate("l_listingdate"));
				sl.setListingUpdated(rs.getDate("l_updatedate"), rs.getInt("t_hour"), rs.getInt("t_minute"));
				sl.setSix_forty(rs.getString("six_forty_path"));
				sl.setThreeTwentyPath(rs.getString("three_twenty_path"));
				sl.setOne_sixty(rs.getString("one_sixty_path"));
				
	
				search_results.add(sl);
			}
			connection.close();
			connection=null;
		}catch(SQLException e){
			e.printStackTrace();
		}
	return search_results;
}
	*/

}