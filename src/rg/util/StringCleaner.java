package rg.util;

import java.text.DecimalFormat;

public class StringCleaner {

	
	public static String cleanQuotes(String s){
		if(s==null){
			s="";
		}
		s = s.replace("\'", "");
		return s.replace("\"", "");
		
	}
	
	public static String formatPrice(java.math.BigDecimal amount){
		   DecimalFormat twoPlaces = new DecimalFormat("#,###");
		   return twoPlaces.format(amount);
	}
		
	
	public static String formatPrice(double amount){
		   DecimalFormat twoPlaces = new DecimalFormat("#,###");
		   return twoPlaces.format(amount);
	}
	
	
	public static String cleanStreetAddress(String s){
		if(s!=null){
			s = s.replace("'", "");
			s = s.replace("\"", "");
			s = org.apache.commons.lang3.text.WordUtils.capitalizeFully(s);
			if(s.length()>24){
				s = abbreviateRoads(s);
			}
		}
		return s;
	}
	
	private static String abbreviateRoads(String the_address){
		String s = the_address;
		s = s.replace("Road", "Rd");
		s = s.replace("Boulevard", "Bvd");
		s = s.replace("Crescent", "Cr");
		s = s.replace("Highway", "Hwy");
		s = s.replace("Street", "St");
		s = s.replace("Avenue", "Av");
		s = s.replace("Drive", "Dv");
		s = s.replace("West", "W");
		s = s.replace("North", "N");
		s = s.replace("East", "E");
		s = s.replace("South", "S");
		s = s.replace("'", "\\'");
	return s;	
	}
	
}
