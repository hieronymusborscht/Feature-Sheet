package rg.util;



import java.io.ByteArrayInputStream;

//import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

public class PostgresConnector
{
  private static Connection connection;
  private static PostgresConnector pgc = new PostgresConnector();
  
  public static PostgresConnector getInstance()
  {
    return pgc;
  }
  
  public static Connection getConnection()
  {
    if (connection == null) {
      connection = init();
    }
    return connection;
  }
  
  private static Connection init()
  {
    connection = null;
    try
    {
      Class.forName("org.postgresql.Driver");
    }
    catch (ClassNotFoundException e)
    {
      System.out.println("ClassNotFoundException " + e + "<br />");
      System.out.println("Where is your PostgreSQL JDBC Driver? Include in your library path!<br />");
      e.printStackTrace();
    }
    try
    {
      connection = DriverManager.getConnection(
        "jdbc:postgresql://aa1w2fh7xu35vbl.cngclcqagnfu.us-west-2.rds.amazonaws.com:5432/ebdb?user=rgrsiteuser&password=shadrach99");
    }
    catch (SQLException e)
    {
      System.out.println("Connection Failed! Check output console<br />");
      e.printStackTrace();
    }
    if (connection == null) {
      System.out.println("Failed to make connection!\n");
    }
    return connection;
  }
  
  public static ArrayList<String> flagDeadListingImagesToDelete()
  {
    ArrayList<String> to_delete = new ArrayList();
    StringBuilder sb = new StringBuilder();
    sb.append("update flat_image set tokeep='false' where flat_image.l_listingid not in (select l_listingid from listing) limit 2");
    try
    {
      connection = getConnection();
      PreparedStatement prepstmt = connection.prepareStatement(sb.toString());
      ResultSet rs = prepstmt.executeQuery();
      while (rs.next())
      {
        to_delete.add(rs.getString("six_forty_path"));
        to_delete.add(rs.getString("three_twenty_path"));
        to_delete.add(rs.getString("one_sixty_path"));
      }
      connection.close();
      connection = null;
    }
    catch (SQLException e)
    {
      System.out.println("Connection Failed! Check output console<br />");
      e.printStackTrace();
    }
    return to_delete;
  }
  
  public static java.util.TreeMap<String, rg.ent.ListingObject> loadLostingsBasic()
  {
	  java.util.TreeMap<String, rg.ent.ListingObject> listings  = new java.util.TreeMap<String, rg.ent.ListingObject>();
    try
    {
      StringBuilder sb = new StringBuilder();
      rg.ent.ListingObject lo = null;
      //sb.append("select l_ from listing where lo1_shortname='V003843'");
      sb.append("select l_listingid, l_displayid, l_area,l_city,l_address,l_listingdate, l_listagent1,l_picturecount, l_state, lfd_styleofhome_1, lo1_phonenumber1,la1_char100_1 as list_agent, la1_loginname, la1_webpage, lfd_amenities_25, lm_char10_5 sub_area from listing where lo1_shortname='V003843'");
      connection = getConnection();
      PreparedStatement ps = connection.prepareStatement(sb.toString());
      ResultSet rs = ps.executeQuery();
      
      while(rs.next()){
    	  lo = new rg.ent.ListingObject();
    	  lo.setListingId(rs.getInt("l_listingid"));
    	  lo.setMls_number(rs.getString("l_displayid"));
    	  //rs.getString("l_area")
    	  lo.setCity(rs.getString("l_city"));
    	  lo.setStreet_address(rs.getString("l_address"));
    	  lo.setListingDate(rs.getDate("l_listingdate"));
    	  //rs.getString("l_listagent1")
    	  lo.setPicture_count(rs.getInt("l_picturecount") );
    	  lo.setState(rs.getString("l_state"));
    	 // rs.getString("lfd_styleofhome_1") 
    	  lo.setOffice_phone(rs.getString("lo1_phonenumber1"));
    	  lo.setRealtor_vnumber(rs.getString("a1_loginname"));
    	 lo.setRealtor_web(rs.getString("la1_webpage"));
    	  lo.setAmmenities(rs.getString("lfd_amenities_25") );
    	  lo.setNeighborhood(rs.getString("sub_area"));
    	  
    	  
    	  
    	  
    	  
    	  listings.put(lo.getMls_number(), lo);
      }
      

 
    }
    catch (SQLException e)
    {
      System.out.println("Connection Failed! Check output console<br />");
      e.printStackTrace();
    }
    
    return listings;
  }
  
  public static void testConnection()
  {
    try
    {
      connection = getConnection();
      PreparedStatement stmt = connection.prepareStatement("select l_listingid from listing limit 1");
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        System.out.println(rs.getInt("l_listingid"));
      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
  }
  
  
  public void loadImages(java.util.TreeMap<String, rg.ent.ListingObject> loaded_listings ){
	  
	  try
	    {
	      connection = getConnection();
	      StringBuffer sb = new StringBuffer();
	  
	     // select six_forty_path from flat_image where l_listingid in ()
	  
	    }catch (Exception e)
	      {
	        System.out.println("PostgresConnector. updateIndexes() :");
	        System.out.println(e.toString());
	      }
	  
  }
  
  
  
  public static void updateIndexes()
  {
    try
    {
      connection = getConnection();
      StringBuilder sb = new StringBuilder();
      Statement batch_statements = connection.createStatement();
      connection.setAutoCommit(false);
      
      sb.append("update listing set phrase=to_tsvector(concat_ws(' ', ");
      sb.append("l_area, ");
      sb.append("l_city, ");
      sb.append("l_address,");
      sb.append("l_state, ");
      sb.append("lfd_featuresincluded_24,");
      sb.append("lfd_siteinfluences_15, ");
      sb.append("lfd_styleofhome_1,");
      sb.append("lr_remarks22, ");
      sb.append("lr_remarks33, ");
      sb.append("lm_char10_5,");
      sb.append(" lm_char10_6))");
      
      sb.append(" where (current_date - l_listingdate) in (0,1,2)  ");
      
      batch_statements.addBatch(sb.toString());
      
      sb = new StringBuilder();
      sb.append("update listing set location=to_tsvector(concat_ws(' ', l_area, ");
      sb.append("l_city, ");
      sb.append("l_address, ");
      sb.append("l_state, ");
      sb.append("lm_char10_5))");
      
      sb.append(" where (current_date - l_listingdate) in (0,1,2)  ");
      batch_statements.addBatch(sb.toString());
      batch_statements.addBatch("update Listing set sqft_int = cast(lm_dec_7 as integer) where lm_dec_7 is not null and sqft_int<1");
      batch_statements.executeBatch();
      connection.commit();
      connection.close();
      connection = null;
    }
    catch (Exception e)
    {
      System.out.println("PostgresConnector. updateIndexes() :");
      System.out.println(e.toString());
    }
  }
  
  public static void updateListingThumbs()
  {
    try
    {
      connection = getConnection();
      StringBuffer sb = new StringBuffer();
      
      sb.append("UPDATE Listing SET ");
      sb.append("six_forty_path=flat_image.six_forty_path, three_twenty_path=flat_image.three_twenty_path, one_sixty_path=flat_image.one_sixty_path ");
      sb.append("FROM flat_image WHERE listing.l_listingid = flat_image.l_listingid ");
      
      sb.append("and listing.six_forty_path in ('') ");
      sb.append("and flat_image.l_objectid='1' ");
      
      PreparedStatement prep_stmt = connection.prepareStatement(sb.toString());
      prep_stmt.execute();
      connection.close();
      connection = null;
    }
    catch (Exception e)
    {
      System.out.println("PostgresConnector.updateListingThumbs: " + e);
    }
  }
  
  public static ArrayList<String> getExistingBeforeInsert(String sql_statement)
  {
    ArrayList<String> dont_insert_list = new ArrayList();
    try
    {
      connection = getConnection();
      Statement stmt = connection.createStatement();
      ResultSet rs = stmt.executeQuery(sql_statement);
      while (rs.next()) {
        dont_insert_list.add(""+rs.getInt("l_listingid"));
      }
      connection.close();
      connection = null;
    }
    catch (SQLException e)
    {
      System.out.println(e);
    }
    return dont_insert_list;
  }
  
  public static void doNewRecordInserts(ArrayList<String> stmts)
  {
    try
    {
      connection = getConnection();
      Statement batch_statements = connection.createStatement();
      connection.setAutoCommit(false);
      for (int i = 0; i < stmts.size(); i++) {
        batch_statements.addBatch((String)stmts.get(i));
      }
      batch_statements.executeBatch();
      connection.commit();
      connection.close();
      connection = null;
    }
    catch (SQLException e)
    {
      System.out.println("PostgresConnector. doNewRecordInserts");
      e.printStackTrace();
      System.out.println(e.getNextException());
    }
  }
  
  public static void FlagAllToDelete(boolean tokeep, String prop_type)
  {
    try
    {
      connection = getConnection();
      PreparedStatement prep_stmt = connection.prepareStatement("update Listing set tokeep=? where lm_char1_36=?");
      connection.setAutoCommit(false);
      prep_stmt.setBoolean(1, tokeep);
      prep_stmt.setString(2, prop_type);
      prep_stmt.execute();
      if (prop_type.equals("Land Only"))
      {
        prep_stmt = connection.prepareStatement("update Image set tokeep=?");
        prep_stmt.execute();
      }
      prep_stmt.execute();
      connection.commit();
      connection.close();
      connection = null;
    }
    catch (SQLException e)
    {
      System.out.println(e);
      System.out.println(e.getNextException());
    }
  }
  
  public static void JpgToDatabaseThumb(ByteArrayInputStream main_640, ByteArrayInputStream thumb_320, ByteArrayInputStream tiny_160, int length_640, int length_320, int length_160, String remote_uri, int listing_id, int object_id, String unique_id, String mime_type)
  {
    try
    {
      connection = getConnection();
      PreparedStatement ps = connection.prepareStatement("INSERT INTO image (remote_uri,l_listingid,l_objectid,l_uniqueid,imgdata, thumb_data, tiny_data) VALUES (?,?,?,?,?,?,?)");
      ps.setString(1, remote_uri);
      ps.setInt(2, listing_id);
      ps.setInt(3, object_id);
      ps.setString(4, unique_id);
      ps.setBinaryStream(5, main_640, length_640);
      ps.setBinaryStream(6, thumb_320, length_320);
      ps.setBinaryStream(7, tiny_160, length_160);
      ps.executeUpdate();
      ps.close();
      main_640.close();
      thumb_320.close();
      tiny_160.close();
      
      connection.close();
      connection = null;
    }
    catch (Exception e)
    {
      System.out.println("JpgToDatabase blew up " + e);
    }
  }
  
  public static void JpgToDatabase(ByteArrayInputStream fin, int mainlength, String remote_uri, int listing_id, int object_id, String unique_id, String mime_type)
  {
    try
    {
      connection = getConnection();
      PreparedStatement ps = connection.prepareStatement("INSERT INTO image (remote_uri,l_listingid,l_objectid,l_uniqueid,imgdata) VALUES (?,?,?,?,?)");
      ps.setString(1, remote_uri);
      ps.setInt(2, listing_id);
      ps.setInt(3, object_id);
      ps.setString(4, unique_id);
      ps.setBinaryStream(5, fin, mainlength);
      ps.executeUpdate();
      ps.close();
      fin.close();
      connection.close();
      connection = null;
    }
    catch (Exception e)
    {
      System.out.println("JpgToDatabase blew up " + e);
    }
  }
  /*
  public static ArrayList<ListingIdWithLatLong> getTodaysListingsToUpdateImages()
  {
    ArrayList<ListingIdWithLatLong> to_update = new ArrayList();
    StringBuffer sb = new StringBuffer();
    sb.append("select l_listingid, l_last_photo_updt, l_picturecount from Listing where six_forty_path in ('') and l_picturecount>'0' and l_listingdate in (current_date-1, current_date) and l_listingid not in (select distinct l_listingid from flat_image) ");
    try
    {
      ListingIdWithLatLong lwll = null;
      connection = getConnection();
      PreparedStatement prepStmt = connection.prepareStatement(sb.toString());
      ResultSet rs = prepStmt.executeQuery();
      while (rs.next())
      {
        lwll = new ListingIdWithLatLong();
        lwll.setL_listingid(rs.getInt("l_listingid"));
        lwll.setLmd_mp_latitude(rs.getString("lmd_mp_latitude"));
        lwll.setLmd_mp_longitude(rs.getString("lmd_mp_longitude"));
        
        to_update.add(lwll);
      }
      connection.close();
      connection = null;
    }
    catch (Exception e)
    {
      System.out.println(e);
      System.out.println("getListingsToUpdateImages" + e.getStackTrace());
    }
    return to_update;
  }
  */
  
  /*
  public static ArrayList<ListingIdWithLatLong> getListingsToUpdateImages()
  {
    ArrayList<ListingIdWithLatLong> to_update = new ArrayList();
    StringBuffer sb = new StringBuffer();
    
    sb.append("select l_listingid, l_last_photo_updt, l_picturecount, lmd_mp_latitude, lmd_mp_longitude from Listing where six_forty_path in ('') and l_picturecount>'0' and l_listingid not in(select distinct l_listingid from flat_image) order by l_last_photo_updt desc limit 10");
    try
    {
      ListingIdWithLatLong lwll = null;
      connection = getConnection();
      PreparedStatement prepStmt = connection.prepareStatement(sb.toString());
      ResultSet rs = prepStmt.executeQuery();
      while (rs.next())
      {
        lwll = new ListingIdWithLatLong();
        lwll.setL_listingid(rs.getInt("l_listingid"));
        lwll.setLmd_mp_latitude(rs.getString("lmd_mp_latitude"));
        lwll.setLmd_mp_longitude(rs.getString("lmd_mp_longitude"));
        
        to_update.add(lwll);
      }
      connection.close();
      connection = null;
    }
    catch (Exception e)
    {
      System.out.println(e);
      System.out.println("getListingsToUpdateImages" + e.getStackTrace());
    }
   // System.out.println("PostgresConnector.getListingsToUpdateImages() returning " + to_update.size());
    return to_update;
  }
  */
  public static int getLastRecordID(String prop_type)
  {
    int last_id = 0;
    try
    {
      connection = getConnection();
      PreparedStatement prepstmt = connection.prepareStatement("select l_listingid from listing where lm_char1_36=? order by l_listingid desc limit 1");
      prepstmt.setString(1, prop_type);
      ResultSet rs = prepstmt.executeQuery();
      while (rs.next()) {
        last_id = rs.getInt("l_listingid");
      }
      connection.close();
      connection = null;
    }
    catch (SQLException e)
    {
      System.out.println(e);
      System.out.println(e.getNextException());
    }
    return last_id;
  }
  /*
  public static String getLastUpdate(String prop_type)
  {
    String last_update = "";
    try
    {
      connection = getConnection();
      PreparedStatement prepstmt = connection.prepareStatement("select l_updatedate from listing where lm_char1_36=? order by l_updatedate desc limit 1");
      prepstmt.setString(1, prop_type);
      ResultSet rs = prepstmt.executeQuery();
      while (rs.next()) {
        last_update = StringUtil.dateToDMQL(rs.getString("l_updatedate"));
      }
      connection.close();
      connection = null;
    }
    catch (SQLException e)
    {
      System.out.println(e);
      System.out.println(e.getNextException());
    }
    return last_update;
  }
  */
  public static void getImagesDataStatusReport()
  {
    StringBuffer sb = new StringBuffer();
    sb.append("REPORT : there are [");
    try
    {
      connection = getConnection();
      Statement stmt = connection.createStatement();
      
      ResultSet rs = stmt.executeQuery("select count(l_listingid) howmany from Listing where l_picturecount>'0' and six_forty_path not in ('')");
      while (rs.next()) {
        sb.append(rs.getInt("howmany"));
      }
      rs = null;
      sb.append("] listings in the database with images \n");
      
      sb.append("there are [");
      rs = stmt.executeQuery("select count(distinct l_listingid) num from flat_image where l_objectid='1'");
      while (rs.next()) {
        sb.append(rs.getInt("num"));
      }
      sb.append("] distinct l_listingid numbers in the flat_image table \n");
      
      connection.close();
      connection = null;
    }
    catch (SQLException e)
    {
      System.out.println("DataConnector.delete_current_images " + e);
    }
    System.out.println(sb.toString());
  }
  
  public static void saveFlatImages(String host_domain, String host_folder, int listing_id, int l_objectid, String six_forty_path, String three_twenty_path, String one_sixty_path)
  {
    try
    {
      connection = getConnection();
      
      String updatestatement = "delete from flat_image where l_listingid=? and l_objectid=?";
      PreparedStatement prepStmt = connection.prepareStatement(updatestatement);
      prepStmt.setInt(1, listing_id);
      prepStmt.setInt(2, l_objectid);
      prepStmt.execute();
      
      updatestatement = "insert into flat_image (l_listingid,l_objectid,host_domain,host_folder, six_forty_path,three_twenty_path,one_sixty_path) values (?,?,?,?,?,?,?)";
      prepStmt = connection.prepareStatement(updatestatement);
      
      prepStmt.setInt(1, listing_id);
      prepStmt.setInt(2, l_objectid);
      prepStmt.setString(3, host_domain);
      prepStmt.setString(4, host_folder);
      prepStmt.setString(5, six_forty_path);
      prepStmt.setString(6, three_twenty_path);
      prepStmt.setString(7, one_sixty_path);
      prepStmt.execute();
      connection.close();
      connection = null;
    }
    catch (SQLException e)
    {
      System.out.println("DataConnector.delete_current_images " + e);
    }
  }
}
