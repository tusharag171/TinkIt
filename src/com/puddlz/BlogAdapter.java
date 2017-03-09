package com.puddlz;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BlogAdapter extends BaseAdapter {

	private ArrayList<Details_blog> data;
    Context c;
   
	
	public BlogAdapter(ArrayList<Details_blog> details, Context globalContext) {
		
    data=details;	
		
	c=globalContext;
	}
	
	@Override
	public int getCount() {
		//how many elements are in the dataset represented by this adapter.
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	//arg0-position:
	//arg1-view jisme populate karna hai
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		View v=arg1;
		if (v == null)
        {		   
           LayoutInflater vi = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
           //layout inflater is used to inflate layout XML files in a particular view.
           v = vi.inflate(R.layout.blog_element, null);
        }
		TextView text_blog=(TextView) v.findViewById(R.id.textView1);
		TextView text_intro=(TextView) v.findViewById(R.id.textView2);
		ImageView image=(ImageView) v.findViewById(R.id.bloglist);
		TextView date_blog = (TextView) v.findViewById(R.id.date_blog);
		Details_blog msg= data.get(arg0);
		Typeface tf = Typeface.createFromAsset(c.getAssets(),
	            "fonts/brushstr.ttf");
		
	    text_blog.setTypeface(tf);
	    text_blog.setTypeface(text_blog.getTypeface(), Typeface.BOLD);
	    
		
		text_blog.setText(msg.get_title());
		text_intro.setText(msg.get_intro());
		date_blog.setText(msg.get_date());
		
		if (msg.get_type().equals("travel"))
			image.setImageResource(R.drawable.travel);
		else if(msg.get_type().equals("apps"))
			image.setImageResource(R.drawable.appsb);
		else if(msg.get_type().equals("food"))
				image.setImageResource(R.drawable.food);
		else if(msg.get_type().equals("medical"))
			image.setImageResource(R.drawable.medical);
		else if(msg.get_type().equals("electronics"))
			image.setImageResource(R.drawable.electronics);
		else if(msg.get_type().equals("movies"))
			image.setImageResource(R.drawable.movies);
		else if(msg.get_type().equals("education"))
			image.setImageResource(R.drawable.education);
		else
			image.setImageResource(R.drawable.other);

		return v;
	}
}
