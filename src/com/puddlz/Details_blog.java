package com.puddlz;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Details_blog {


	String title;
	String url;
	String intro;
    String type;
    String date;
    String link_actual;
    
    String get_link()
	{
		return link_actual;

	}
	
	void set_link(String link_actual)
	{
		this.link_actual=link_actual;
	}

    String get_type()
	{
		return type;

	}
	
	void set_type(String type)
	{
		this.type=type;
	}

	public void set_title(String title)
	{
		this.title=title;
	}
	public void set_url(String url)
	{
		this.url=url;
	}
	public String get_url()
	{
		return url;
	}
	public String get_title()
	{
		return title;
	}
	public String get_intro()
	{
		return intro;
	}
	public void set_intro(String intro)
	{
	  this.intro=intro;
	}
	
	public String get_date()
	{
		return date;
		
	}
	
	public void set_date(java.util.Date date2)
	{
		DateFormat df = new SimpleDateFormat("M");
		Date dt2 = new Date();
		int diffInDays = (int) ((dt2.getTime() - date2.getTime()) / (1000 * 60 * 60 * 24));
		if(diffInDays<=30)
		{
			date="Posted "+Integer.toString(diffInDays)+" days ago";
		}
		else
		{
			Calendar startCalendar = new GregorianCalendar();
			startCalendar.setTime(date2);
			Calendar endCalendar = new GregorianCalendar();
			endCalendar.setTime(dt2);
			int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
			int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
			date="Updated "+Integer.toString(diffMonth)+" months ago";
		}
		
	}
	
}
