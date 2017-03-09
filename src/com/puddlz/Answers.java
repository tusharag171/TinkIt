package com.puddlz;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class Answers extends Activity {
	public ProgressBar progress_bar;
	public String comment_string;
	List<ParseObject> list_people;
	ParseObject question_object = null;
	ProgressDialog progress;
    Context mc;
	View header = null;
	int j = 0;
	
	ParseRelation<ParseObject> relation;// 1 ques to many answers.
	List<ParseRelation> relation_not;// 1 user
																		// to
																		// many
																		// relations.
	String object_id;
	List<ParseObject> user_not = new ArrayList<ParseObject>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_answers);
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mc=this;
		ConnectionDetector cd = new ConnectionDetector(this);
		Boolean isInternet = cd.isConnectingToInternet();

		
		if(isInternet==false)
		{
			Toast.makeText(this, "Couldn't be posted. Please Check your network connection.", Toast.LENGTH_LONG).show();
			Intent intent = new Intent(this, Home.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			return;
		}
		else
		{
		
		list_people = new ArrayList<ParseObject>();
		relation_not = new ArrayList<ParseRelation>();
		if (list_people.isEmpty()) {
		    Log.d("debug1","empty");
		}
		else {
			Log.d("debug1",Integer.toString(list_people.size()));
		}
		
		header = getLayoutInflater().inflate(R.layout.test_layout, null);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		// String str=getIntent().getExtras().getString("string");//get question
		// content.
		progress_bar = (ProgressBar) findViewById(R.id.progressBar1);
		progress_bar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.app_green), Mode.SRC_IN);
		final TextView question_text = (TextView) header
				.findViewById(R.id.question_text);
		final TextView question_user = (TextView) header
				.findViewById(R.id.textView1);
		final TextView loc_user = (TextView) header
				.findViewById(R.id.loc_text);
		final ImageView loc_image = (ImageView) header
				.findViewById(R.id.loc_image);
		// question_text.setText(str);
		object_id = getIntent().getExtras().getString("object_id");
		// above is the object_id of the question.
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Questions");
		query.whereEqualTo("objectId", object_id);
		query.include("post_user");
		query.getFirstInBackground(new GetCallback<ParseObject>() {
			@Override
	    public void done(ParseObject object, ParseException e) {
		  // TODO Auto-generated method stub
				if(e==null)
				{
					question_object=object;
					
					ImageView question_image = (ImageView) header
							.findViewById(R.id.imageView1);
					// BitmapDrawable bitmapDrawable = new
					// BitmapDrawable(getResources(),
					// getFileStreamPath(question_object.getParseObject("post_user").getObjectId()).getAbsolutePath());
					ParseFile file = question_object.getParseObject("post_user")
							.getParseFile("profile_pic");
					byte[] fil;
					try {
						fil = file.getData();
						Bitmap bitmap = BitmapFactory.decodeByteArray(fil, 0, fil.length);
						question_image.setImageBitmap(bitmap);
						question_user.setText(question_object.getParseObject("post_user")
								.getString("name"));

					
					question_text.setText(question_object.getString("question").trim());
					if (question_object != null) {
						Log.d(PuddlzApplication.TAG, "IT IS working!!");
					}
					if (!list_people.contains(question_object.getParseObject("post_user"))) {
						list_people.add(question_object.getParseObject("post_user"));
						Log.d("debug2",question_object.getParseObject("post_user").getObjectId());//user whose ques
					}
					relation = question_object.getRelation("q_ans");// this is a relation to
																	// all the answers of
																	// the question object.
			loc_user.setText(question_object.getString("location").replaceAll("null,",""));
			
			try{
				if(!question_object.getString("location").equals("random"))
				{
					loc_user.setVisibility(View.VISIBLE);
					loc_image.setVisibility(View.VISIBLE);
//			Log.d("Bug",msg.get_question());
				}
				else
				{
					loc_image.setVisibility(View.GONE);
					loc_user.setVisibility(View.GONE);
				}
				} catch(NullPointerException e2)
				{
					Log.d("exception","tada");
				}
				
					refresh_comments();

					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				}
				else
				{
					Intent intent=new Intent(mc,Home.class);
					Log.d("In",":2");
					startActivity(intent);
					overridePendingTransition(R.animator.animation7,R.animator.animation8);
					Toast.makeText(mc, "OOPS! No network access!", Toast.LENGTH_SHORT).show();
					return;	
				}
		
	}
});

		
		

	}
		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.answers, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_share:
			share();
			return true;
		case R.id.action_fb_share:
			publishFeedDialog();
			return true;
			case android.R.id.home:
			onBackPressed();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void share() {
		if(question_object!=null)
		{Intent intent3 = new Intent(Intent.ACTION_SEND);
		intent3.setType("text/plain");
		intent3.putExtra(
		android.content.Intent.EXTRA_TEXT,
		ParseUser.getCurrentUser().getString("name")
		+ " asked you to answer:-\n"
		+ question_object.getString("question")
		+ "\nvia www.tinkitapp.com/q.php?q="+question_object.getObjectId());
		startActivity(intent3);}
		}

	public void write_answer(View view) {
		
		ConnectionDetector cd = new ConnectionDetector(this);
		Boolean isInternet = cd.isConnectingToInternet();
		if (isInternet == false) {
			Toast.makeText(
					this,
					"Couldn't be posted. Please Check your network connection.",
					Toast.LENGTH_LONG).show();
			return;
		}
		
		ParseQuery<ParseObject> query_not = ParseQuery.getQuery("user_not");
		query_not.whereContainedIn("user_id", list_people);
		query_not.findInBackground(new FindCallback<ParseObject>() {
		
			@Override
			public void done(List<ParseObject> arg0, ParseException arg1) {
				// TODO Auto-generated method stub
				if(arg1==null)
				{
				user_not=arg0;
				for(int x=0; x<user_not.size(); x++)
				{ Log.d("debug - not_user list creation ",user_not.get(x).getParseObject("user_id").getObjectId());  }
                write_answer_post();
				}
				else
				{
					Intent intent=new Intent(mc,Home.class);
					Log.d("In",":2");
					startActivity(intent);
					overridePendingTransition(R.animator.animation7,R.animator.animation8);
					Toast.makeText(mc, "OOPS! No network access!", Toast.LENGTH_SHORT).show();
					return;	
			    }
}
		});

		
		
	}
	
	void write_answer_post()
		{
		Log.d("debug 4",Integer.toString(user_not.size()));
		for (int i = 0; i < user_not.size(); i++) {
			
			relation_not.add(user_not.get(i).getRelation("u_not"));
		}
		if (user_not == null) {
			Log.d(PuddlzApplication.TAG, "USER ID IS NULl!!");
		}
		final EditText answer_comment = (EditText) findViewById(R.id.edit_answer);
		
		comment_string = answer_comment.getText().toString();
		final String object_id1 = getIntent().getExtras()
				.getString("object_id");

		if (comment_string.equals("")) {

			Toast toast = Toast.makeText(this, "Can't have blank answer.",
					Toast.LENGTH_SHORT);
			toast.show();
		} else {//change
			
			progress = new ProgressDialog(this);
			progress.setMessage("Posting Reply!");
			progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progress.setIndeterminate(true);
			progress.show();
			answer_comment.setText("");

			final ParseObject new_comment = new ParseObject("answers");
			new_comment.put("comment_user", ParseUser.getCurrentUser());
			new_comment.put("comment", comment_string);
			new_comment.put("parent", object_id1);

			new_comment.saveInBackground(new SaveCallback() {

				public void done(ParseException e) {
					if (e == null) {
						relation.add(new_comment);
						try {
							question_object.save();
						} catch (ParseException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
						if (question_object == null) {
							Log.d(PuddlzApplication.TAG,
									"IT IS working ALL RIGHT BITCHES!!");
						}
						
					   int people_count;
	                   people_count = list_people.size();

						refresh_comments();
					
						if (!list_people.isEmpty()) {
							try {
								JSONObject data = new JSONObject();
								String text1=getIntent()
										.getExtras().getString("string");
								if(text1.contains("New answer/s for -"))
								{
									text1=text1.replaceAll("New answer/s for -","");
								}
								data.put("alert", text1);
								data.put("title", "New reply");
								ParseQuery pushQuery = ParseInstallation
										.getQuery();
								List<ParseObject> old_people=new ArrayList<ParseObject>();
								for(int i=0;i<people_count;i++)
								{
									if(!list_people.get(i).getObjectId().equals(ParseUser.getCurrentUser().getObjectId()))
									{
										old_people.add(list_people.get(i));
										Log.d("myt",list_people.get(i).getObjectId());
									}
								}
						        
								pushQuery.whereContainedIn("user_id",old_people);
								// pushQuery.whereNotEqualTo("user_id",
								// ParseUser.getCurrentUser());
								List<ParseObject> list_notifications = new ArrayList<ParseObject>();

								for (int i = 0; i < people_count; i++) {
									ParseObject new_notification = new ParseObject(
											"notifications");
									new_notification.put("user_id",
											list_people.get(i));
									new_notification.put("type", "new_answer");
									new_notification
											.put("details",
													getIntent()
															.getExtras()
															.getString(
																	"object_id"));
									new_notification.put("is_read", false);
									String text=getIntent()
											.getExtras().getString("string");
									if(text.contains("New answer/s for -"))
									{
										text=text.replaceAll("New answer/s for -","");
									}
									new_notification.put("text", text);
									new_notification.put("question_object",
											object_id);
									list_notifications.add(new_notification);
								}
Log.d("debug 5",Integer.toString(list_people.size()));
Log.d("debug 6",Integer.toString(relation_not.size()));
								try {
									ParseObject.saveAll(list_notifications);
									for (int i = 0; i < people_count; i++) {
										relation_not.get(i).add(list_notifications.get(i));
									}

									ParseObject.saveAll(user_not);

								} catch (Exception e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}

								ParsePush push = new ParsePush();
								push.setQuery(pushQuery);

								push.setData(data);
								push.sendInBackground();

							} catch (JSONException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

						}

					}

				}
			});

		}

	}

	private void refresh_comments() {
		j++;
		progress_bar.setVisibility(View.VISIBLE);
		final ListView mylist1 = (ListView) findViewById(R.id.listView2);
		final Context globalContext = (Context) this;
        Log.d("debug 3",Integer.toString(list_people.size()));
		final ArrayList<Details_answers> details = new ArrayList<Details_answers>();
		ParseQuery<ParseObject> q = relation.getQuery();
		q.include("comment_user");
		q.orderByAscending("createdAt");
		q.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> scoreList, ParseException e) {
				if (e == null) {

					// loop through list of objects
					for (int i = 0; i < scoreList.size(); i++) {

						Details_answers detail = new Details_answers();
						detail.set_comment(scoreList.get(i)
								.getString("comment"));

						ParseObject us = scoreList.get(i).getParseObject(
								"comment_user");
						if (!list_people.contains(us)) {
							list_people.add(us);
							Log.d("added user whose answers are there",us.getObjectId());
						}
						detail.set_name(us.getString("name"));
						detail.set_id(us.getObjectId());
						details.add(detail);

					}
					Log.d("debug 3a",Integer.toString(list_people.size()));
					if (j == 1)
						mylist1.addHeaderView(header);
					progress_bar.setVisibility(View.GONE);
					mylist1.setAdapter(new Adapter_answers(details,
							globalContext));
					if (progress != null) {
						progress.dismiss();
					}
					mylist1.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							if (position > 0) {
								String object_id = details.get(position - 1)
										.get_id();
								Intent intent = new Intent(globalContext,
										ProfilePage.class);
								Bundle extras = new Bundle();
								extras.putString("object_id", object_id);
								intent.putExtras(extras);
								startActivity(intent);
								((Activity) globalContext)
										.overridePendingTransition(
												R.animator.animation3,
												R.animator.animation4);
							} else// header element.
							{
								Intent intent = new Intent(globalContext,
										ProfilePage.class);
								Bundle extras = new Bundle();
								extras.putString("object_id", question_object
										.getParseObject("post_user")
										.getObjectId());
								intent.putExtras(extras);
								startActivity(intent);
								((Activity) globalContext)
										.overridePendingTransition(
												R.animator.animation3,
												R.animator.animation4);

							}
						}
					});

					

				} else {

					Log.d("score", "Error: " + e.getMessage());
				}
			}
		});
	}
	
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.animator.animation7, R.animator.animation8);
	}
	
	private void publishFeedDialog() {
		if(question_object!=null)
		{
	    Bundle params = new Bundle();
	    params.putString("name", question_object.getString("question"));
	    params.putString("caption", "tinkitapp, recommendation made awesome!");
	    params.putString("description", question_object.getParseObject("post_user").getString("name")+" asked this question.\n");
	    params.putString("link", "www.tinkitapp.com/q.php?q="+question_object.getObjectId());
	    if(question_object.getString("topic").equals("Electronics"))
	    {
	    	params.putString("picture", "www.tinkitapp.com/electronic.png");		    
	    }
	    else if(question_object.getString("topic").equals("Apps"))
	    {
	    	params.putString("picture", "www.tinkitapp.com/app.png");	    	
	    }
	    else if(question_object.getString("topic").equals("Restaurant"))
	    {
	    	params.putString("picture", "www.tinkitapp.com/restaurant.png");	    	
	    }
	    else if(question_object.getString("topic").equals("Movies"))
	    {
	    	params.putString("picture", "www.tinkitapp.com/movie.png");	    	
	    }
	    else if(question_object.getString("topic").equals("Other"))
	    {
	    	params.putString("picture", "www.tinkitapp.com/others.png");	    	
	    }
	    else if(question_object.getString("topic").equals("Doctors"))
	    {
	    	params.putString("picture", "www.tinkitapp.com/doctor.png");	    	
	    }
	    params.putString("token",Session.getActiveSession().getAccessToken());
	    WebDialog feedDialog = (
	        new WebDialog.FeedDialogBuilder(this,
	            Session.getActiveSession(),
	            params))
	        .setOnCompleteListener(new OnCompleteListener() {


				@Override
				public void onComplete(Bundle values, FacebookException error) {
					// TODO Auto-generated method stub
	                if (error == null) {
	                    // When the story is posted, echo the success
	                    // and the post Id.
	                    final String postId = values.getString("post_id");
	                    if (postId != null) {
	                        Toast.makeText(mc,
	                            "Posted story, id: "+postId,
	                            Toast.LENGTH_SHORT).show();
	                    } else {
	                        // User clicked the Cancel button
	                        Toast.makeText(mc.getApplicationContext(), 
	                            "Publish cancelled", 
	                            Toast.LENGTH_SHORT).show();
	                    }
	                } else if (error instanceof FacebookOperationCanceledException) {
	                    // User clicked the "x" button
	                    Toast.makeText(mc.getApplicationContext(), 
	                        "Publish cancelled", 
	                        Toast.LENGTH_SHORT).show();
	                } else {
	                    // Generic, ex: network error
	                    Toast.makeText(mc.getApplicationContext(), 
	                        "Error posting story", 
	                        Toast.LENGTH_SHORT).show();
	                }
					
				}

	        })
	        .build();
	    feedDialog.show();
	}
		}

	
	
}
