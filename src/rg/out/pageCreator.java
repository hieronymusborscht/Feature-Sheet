package rg.out;

public class pageCreator {

	
	
	public static String makeHTML(){
		StringBuffer sb = new StringBuffer();
		
		
		sb.append("<html>\n");


		sb.append("<head>\n");

		sb.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"feature.css\">\n");
		sb.append("</head>\n");

		sb.append("<body>\n");

		sb.append("<div id=\"header\">\n");

		sb.append("</div>\n");

		sb.append("<div id=\"content_container\">\n");

		sb.append("<div id=\"content_pane\">\n");
		sb.append("<table>\n");
					
		sb.append("<tr>\n");
		sb.append("<td>\n");
		sb.append("		<table><tr>\n");
		sb.append("			<td>\n");
		sb.append("				<div class=\"banner_top\"><span id=\"headline\">SOLD IN 8 DAYS</span><br /><span id=\"address_line\">2604 Mainland Street, Vancouver</span></div>\n");
		sb.append("			</td>\n");
		sb.append("		</tr></table>\n");
		sb.append("	</td>\n");
		sb.append("</tr>\n");
		sb.append("<tr>\n");
		sb.append("	<td> \n");
		sb.append("		<table><tr> \n");  
		sb.append("			<td><div id=\"box1\" class=\"squarebox\"  >&nbsp;</div></td><td> <div class=\"squarebox\" id=\"box2\">&nbsp;</div> </td>\n");
		sb.append("		</tr></table>\n");
		sb.append("	 </td>\n");
		sb.append("</tr>\n");
		sb.append("<tr>\n");
		sb.append("	<td> \n");
		sb.append("		<table><tr> \n");  
		sb.append("			<td> <div class=\"squarebox2\" id=\"box3\"></div></td><td><div class=\"squarebox2\" id=\"box4\"> </div></td> <td> <div class=\"squarebox2\" id=\"box5\"></div></td> \n");
		sb.append("		</tr></table> \n");
		sb.append("	</td>\n");
		sb.append("</tr>\n");
		sb.append("<tr>\n");
		sb.append("	<table class=\"box-table\"><tr><td style=\"text-align:left;\">Bedrooms: 2<br />Bathrooms: 3<br />$618,000 </td> <td>&nbsp; </td><td> 778.322.5040<br />\n");
		sb.append("			Saleem@SoldBySaleem.com<br />Saleem Dhalla</td></tr></table>\n");
		sb.append("</tr>\n");
		sb.append("</table>\n");
		sb.append("</div>\n");
		sb.append("</div>\n");


		sb.append("</body>\n");


		sb.append("</html>\n");
		
		
		return sb.toString();
	}
}
