package rg;



import rg.ent.NewSummaryListing;
import rg.out.WriterOfFiles;

public class RunnableImplementation implements Runnable {

 
    @Override
    public void run() {
        
    	//select m_id,img_id,first_name, last_name, email, pass_hash, role, areas_serviced, description, languages, salesforce_id,phone, visible, mem_login, datecreated, web_site, link_text,admin_level, thumb_320 from member where mem_login not in ('') and  visible='true' order by first_name 

    	 java.util.TreeMap<String, NewSummaryListing> listings = rg.util.PostgresConnector.loadRoyaltyListings();
    	 NewSummaryListing listing = null;
    	 java.util.HashSet<String> member_login_set = new java.util.HashSet<String>();
    	 java.util.Set<String> listing_mls_set = listings.keySet();
    	 java.util.Iterator<String> mls_it = listing_mls_set.iterator();
    	 while(mls_it.hasNext()){
    		 
    		listing = listings.get(mls_it.next());
    		if(!member_login_set.contains(listing.getStringValue("la1_loginname"))){
    			//System.out.println(listing.getStringValue("la1_loginname"));
    			member_login_set.add(listing.getStringValue("la1_loginname"));
    		}	
    	 }
    	 
    	 
    	 java.util.TreeMap<String, rg.ent.NewUser> agents = rg.util.PostgresConnector.loadAgentsbyListings(member_login_set);
    	 
    	 //System.out.println("agents size: " +agents.size());
    	 
    	 java.util.Set<String> agent_keys = agents.keySet();
    	 java.util.Iterator<String> ak_it = agent_keys.iterator();
    	 while(ak_it.hasNext()){
    		 findHighestPrice( listings, agents.get(ak_it.next()));
    		 
    	 }
    	 
    	 //findHighestPrice( listings, agents.get(agents.firstKey()));
    	 
    	 
        for (int i = 0; i < 10; i++) {
        	try {
        		
        		NewSummaryListing sl = null;
        		java.util.Set<String> string_keys  = listings.keySet();
        		java.util.Iterator<String> string_keys_it = string_keys.iterator();
        		
        		
        		while(string_keys_it.hasNext()){
        			sl = listings.get(string_keys_it.next());
        		}
        		
        		
        		//WriteAnHTMLPage("the content here", "shane", "page.html");
				//Thread.sleep(20000);
			} catch (Exception e) {
        	//catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
    }
    
    
    public void findHighestPrice(java.util.TreeMap<String, NewSummaryListing> listings, rg.ent.NewUser agent){
    	java.util.TreeMap<java.math.BigDecimal, NewSummaryListing>  price_sorted_list = new java.util.TreeMap<java.math.BigDecimal, NewSummaryListing>();
    	NewSummaryListing temp_list_a = null;
    	
    	java.util.Set<String> listing_keys = listings.keySet();
    	java.util.Iterator<String> keys_it = listing_keys.iterator();
    	while(keys_it.hasNext()){
    		
    			temp_list_a  = listings.get(keys_it.next());
    			if(agent.getStringValue("mem_login").equals(temp_list_a.getStringValue("la1_loginname"))){
    				System.out.println(temp_list_a.getAskingprice());
    				price_sorted_list.put(temp_list_a.getAskingprice(), temp_list_a);
    			}
    			//if(list.getAskingprice()<)
    		}
    	System.out.println("["+price_sorted_list.lastEntry().getValue().getFormattedPrice()+"]");
    	//return price_sorted_list.lastEntry().getValue();
    	
    }
    
   public static void WriteAnHTMLPage(String the_content, String the_folder, String the_doc){
	   System.out.println("make an html page");
	   WriterOfFiles.writeAFile(rg.out.pageCreator.makeHTML(the_content), the_folder, the_doc);
	   
   }
    
}
