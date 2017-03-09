package com.puddlz;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class Blog extends Activity {
	ProgressWheel progress_bar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_blog);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		progress_bar=(ProgressWheel) findViewById(R.id.text_top);
		progress_bar.setVisibility(View.VISIBLE);

	}

	@Override
	protected void onResume() {
		super.onResume();
		final Context globalContext=this;
		progress_bar=(ProgressWheel) findViewById(R.id.text_top);
		final ListView list1=(ListView) findViewById(R.id.listView1);
		final ArrayList<Details_blog> details=new ArrayList<Details_blog>();	
		list1.setOnScrollListener(new EndlessScrollListener(1) {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
						
				ParseQuery<ParseObject> query = ParseQuery.getQuery("Blog");
				query.setSkip((page-1)*10);
				query.setLimit(10);
				
				query.orderByDescending("createdAt");
				query.findInBackground(new FindCallback<ParseObject>() {
					public void done(List<ParseObject> blog_object, ParseException e) {
						if(e==null)
						{
						int first_visible=list1.getFirstVisiblePosition();

						for(int i=0;i<blog_object.size();i++)
						{

							Details_blog detail=new Details_blog();
							detail.set_title(blog_object.get(i).getString("name"));
							detail.set_url(blog_object.get(i).getString("link"));
							detail.set_intro(blog_object.get(i).getString("intro"));
							detail.set_type(blog_object.get(i).getString("type"));
							detail.set_date(blog_object.get(i).getCreatedAt());
							detail.set_link(blog_object.get(i).getString("actual_link"));
							details.add(detail);
						}
						list1.setAdapter(new BlogAdapter(details , globalContext));
						list1.setSelection(first_visible);				
						progress_bar.setVisibility(View.GONE);
						list1.setOnItemClickListener(new OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

						    	Intent intent = new Intent(globalContext, Blog_View.class);
								Bundle extras = new Bundle();  
								extras.putString("title",details.get(position).get_title());
								extras.putString("link",details.get(position).get_url());
								extras.putString("link_actual",details.get(position).get_link());
								intent.putExtras(extras);
								startActivity(intent);
								((Activity) globalContext).overridePendingTransition(R.animator.animation3,R.animator.animation4);

							}


						});                   
						}
						else
						{
							Toast.makeText(globalContext, "Failed to load!", Toast.LENGTH_SHORT).show();
						}
					}
				});
				

			}});

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
	//	getMenuInflater().inflate(R.menu.blog, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}




public void onBackPressed() {
	super.onBackPressed();
	overridePendingTransition(R.animator.animation7, R.animator.animation8);
}

}