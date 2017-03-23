package rg.ent;







/*

create table users (
id serial primary key not null,
first_name character varying(256) not null default '',
last_name character varying(256) not null default '',
email character varying(256) not null default '',
phone character varying(20) not null default '',
pass_hash character varying(256) not null default '',
roll character varying(20) not null default '',
datecreated date default '1970-01-20',
visible boolean default 'false'
)

*/
public class NewUser {
	
	
	

	private java.util.TreeMap<String, String>	string_fields;
	private java.util.TreeMap<String, Integer>	integer_fields;
	
	public NewUser(){
		setupFields();
	}
	
	
	
	private void setupFields(){
		string_fields =new java.util.TreeMap<String, String>();
		integer_fields =new java.util.TreeMap<String, Integer>();
		string_fields.put("first_name", "");
		string_fields.put("last_name", "");
		string_fields.put("user_email", "");
		string_fields.put("phone", "");
		string_fields.put("datecreated", "");
		string_fields.put("role", "");
		string_fields.put("thumb", "http://www.royalty.ca/images/default-realtor.jpg");
		string_fields.put("areas_serviced","");
		string_fields.put("description","");
		string_fields.put("languages",""); 
		string_fields.put("mem_login",""); 
		string_fields.put("web_site","");
		string_fields.put("link_text","");
		string_fields.put("thumb","");
		string_fields.put("pass","");
		string_fields.put("pass_hash","");
		
		integer_fields.put("id", 0);
		integer_fields.put("admin_level", 0);
	}
	
	
	public int getId() {
		return integer_fields.get("id");
	}
	public int getIntValue(String field_name){
		return integer_fields.get(field_name);
	}
	
	public void setString_field(String field_name, String value){
		if(string_fields.containsKey(field_name) && value!=null){
			string_fields.put(field_name, value);
		}
	}

	public void setId(int id) {
		 integer_fields.put("id", id);
	}
	public void setAdmin_level(int i) {
		integer_fields.put("admin_level", i);
	}
	

	public String getAgentSummary(boolean show_data){
		StringBuffer sb = new StringBuffer();
		sb.append("<table><tr>");
		if(integer_fields.size()>0){
			String key = null;
			java.util.Set<String> int_keys = integer_fields.keySet();
			java.util.Iterator<String> ik_it = int_keys.iterator();
			while(ik_it.hasNext()){
				key = ik_it.next();
				sb.append("<td>");
				if(show_data){
					sb.append(key);
				}else{
					sb.append(integer_fields.get(key));
				}
				sb.append("</td>");
			}
		}
		
		if(string_fields.size()>0){
			String key = null;
			java.util.Set<String> int_keys = string_fields.keySet();
			java.util.Iterator<String> sk_it = int_keys.iterator();
			while(sk_it.hasNext()){
				key = sk_it.next();
				sb.append("<td>");
				if(show_data){
					sb.append(key);
				}else{
					sb.append(string_fields.get(key));
				}
				sb.append("</td>");
			}
		}
		
		
		sb.append("</tr></table>");
		return sb.toString();
	}
	

	

	public String getStringValue(String field_name){
		return string_fields.get(field_name);
	}
	
	

}

