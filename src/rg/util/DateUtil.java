package rg.util;

public class DateUtil {
	
	
	public static java.util.Calendar sqlDateToCalendar(java.sql.Date sql_date){
		java.util.Calendar cal = java.util.Calendar.getInstance();
		cal.setTime( sql_date );
		return cal;
	}

}
