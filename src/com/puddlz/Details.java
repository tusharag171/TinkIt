package com.puddlz;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class Details
{
	String no_answers;
	String question;
	String object_id;
	String name;
	String image_name;
	String location;
	String date;
	
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
			date="Updated "+Integer.toString(diffInDays)+" days ago";
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
	
	public String getno()
	{
		return no_answers;
	}
	public void setno(String no_answers)
	{
		this.no_answers=no_answers;
	}
	public String get_question()
	{
		return question;
	
	}
	public void set_question(String question)
	{
		this.question=question;
	}
	public String get_name()
	{
		return name;
	
	}
	public void set_name(String name)
	{
		this.name=name;
	}
	public void set_objectid(String object_id1)
	{
		this.object_id=object_id1;
	}
	public String get_objectid()
	{
		return object_id;
	}
	public String get_image()
	{
		return this.image_name;
	}
	public void set_image(String image1)
	{
		this.image_name=image1;
	}
	public void set_location(String loc)
	{location=loc;
	
	}
	public String get_location()
	{
		return location;
	}
	
	
}