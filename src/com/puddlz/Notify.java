package com.puddlz;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

public class Notify {

	 public static void notify_following(final List<ParseUser> fb_users) {
	        final List<ParseObject> user_not=new ArrayList<ParseObject>();
	        try {
	            final String username = ParseUser.getCurrentUser().getString("name");
	            final JSONObject data = new JSONObject();
	            data.put("title", username);
	            data.put("alert", "Hi! I just started following you.");
	            final List<ParseRelation> relation_not=new ArrayList<ParseRelation>();
	            ParseUser current_user=ParseUser.getCurrentUser();
	            ParseRelation rel=current_user.getRelation("friendsRelation");
	            final List<String> user_s=new ArrayList<String>();
	            ParseQuery<ParseObject> query=ParseQuery.getQuery("user_not");
	            query.whereContainedIn("user_id",fb_users);
	            query.include("user_id");
	            query.findInBackground(new FindCallback<ParseObject>() {

	                @Override
	                public void done(List<ParseObject> scoreList, ParseException arg1) {
	                    // TODO Auto-generated method stub
	                    if(arg1==null)
	                    {
	                        for (int i = 0; i < scoreList.size(); i++) {
	                            //        Log.d("user_not", scoreList.get(i).getObjectId());
	                            user_not.add(scoreList.get(i));
	                            relation_not.add(scoreList.get(i).getRelation("u_not"));
	                            user_s.add(scoreList.get(i).getParseObject("user_id").getObjectId());
	                            //Log.d("Heyi",user_s.get(0));
	                        }
	                        ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
	                        pushQuery.whereContainedIn("user_id", fb_users);
	                        //Log.d("Notifications", fb_users.get(0).getString("name"));
	                        ParsePush push = new ParsePush();
	                        push.setQuery(pushQuery);

	                        push.setData(data);
	                        push.sendInBackground();
	                        int k;
	                        List<ParseObject> list_notifications = new ArrayList<ParseObject>();
	                        for (int i = 0; i < fb_users.size(); i++) {
	                            if(!user_s.contains(fb_users.get(i).getObjectId()))
	                            {
	                                //    Log.d("yrl",fb_users.get(i).getObjectId());
	                                continue;
	                            }
	                            else
	                                k=user_s.indexOf(fb_users.get(i).getObjectId());
	                            //    Log.d("Notifications", fb_users.get(i).getString("name"));
	                            //relation_not.add(user_not.get(k).getRelation("u_not"));
	                            ParseObject new_notification = new ParseObject("notifications");
	                            //new_notification.put("question_object", fb_users.get(i));
	                            new_notification.put("type", "new_friend");
	                            new_notification.put("details", username+ " is now following you.");
	                            new_notification.put("is_read", false);
	                            String p=fb_users.get(i).getObjectId();
	                            ParseObject p1=fb_users.get(i);
	                            new_notification.put("user_id", p1);
	                            new_notification.put("person",ParseUser.getCurrentUser().getObjectId());
	                            new_notification.put("text", username+ " is now following you.");
	                            list_notifications.add(new_notification);
	                        }

	                        try {
	                            ParseObject.saveAll(list_notifications);
	                            for(int i=0;i<relation_not.size();i++)
	                            {
	                                if(list_notifications.size()>i && relation_not.get(i)!=null)
	                                {
	                                    relation_not.get(i).add(list_notifications.get(i));                    
	                                }
	                                else
	                                {
	                                    Log.d("NO","not working.");
	                                }
	                            }
	                            ParseObject.saveAll(user_not);
	                        } catch (Exception e) {
	                            // TODO Auto-generated catch block
	                            //Log.d("NotificationsNotWorking","all not saved");
	                            e.printStackTrace();
	                        }
	                    }
	                }
	            });

	        } catch (JSONException e) {
	            e.printStackTrace();
	        }

	    }
	
	public static void new_question(final String string, final String objectId) {


		final ParseUser current_user=ParseUser.getCurrentUser();
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.whereEqualTo("friendsRelation", current_user);
		query.whereNotEqualTo("objectId", current_user.getObjectId());
		
		query.findInBackground(new FindCallback<ParseUser>() {

			@Override
			public void done(List<ParseUser> arg0, ParseException arg1) {
				if(arg1==null)
				{
					JSONObject data = new JSONObject();
					try {
						
						data.put("alert", current_user.getString("name")+" asked a question!");
						data.put("title", "New question!");
						data.put("text",string);
						data.put("object_id", objectId);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					ParseQuery pushQuery = ParseInstallation
							.getQuery();
					List<String> version_not = new ArrayList<String>();
					version_not.add("1.0");
					version_not.add("1.1");
			
					pushQuery.whereNotContainedIn("appVersion",version_not);
					pushQuery.whereContainedIn("user_id",arg0);
					ParsePush push = new ParsePush();
					push.setQuery(pushQuery);
                    
					push.setData(data);
					push.sendInBackground();
				}
			}

		});


		// TODO Auto-generated method stub

	}



}
