package com.puddlz;

import java.util.Arrays;
import java.util.List;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.RefreshCallback;
import com.parse.SaveCallback;

public class MainActivity extends ActionBarActivity {

	private Button loginButton;
	private Dialog progressDialog;
	ParseUser currentUser;
	Context mcontext=this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		PushService.setDefaultPushCallback((Context)this, Notifications.class);
		super.onCreate(savedInstanceState);
		getSupportActionBar().hide();
		Log.d("Entering","oncreate");
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		currentUser = ParseUser.getCurrentUser();
		if(currentUser!=null && currentUser.getNumber("check") == null )
		{
		 /*ParseUser.getCurrentUser().refreshInBackground(new RefreshCallback() {
			  public void done(ParseObject object, ParseException e) {
				    if (e == null) {
				      Log.d("Sucess","data_fetched");      
				    } else {
				      // Failure!
				    	Log.d("Failure","data_not_fetched");
				    }
				  }
				});*/
			
			try {
				ParseUser.getCurrentUser().refresh();
				 Log.d("Sucess","data_fetched");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
			
		// Check if there is a currently logged in user
		// and they are linked to a Facebook account.
		currentUser = ParseUser.getCurrentUser();
	
		if ((currentUser != null) && ParseFacebookUtils.isLinked(currentUser)
				&& currentUser.getNumber("check") != null) {

			// Go to the user info activity

			Session session = ParseFacebookUtils.getSession();
			if (session != null && session.isOpened()) {
				
				 List<String> PERMISSIONS = Arrays.asList("public_profile",
							"user_friends", "email");

	                if(!session.getPermissions().containsAll(PERMISSIONS))
	                { Log.d("contains5", "all-->nope");
	                    fetch_permissions();
                 
	                }
	                else	              
	                {Log.d("tada","going to home");
	                	home();}
			}
			else
			{Log.d("Session","Session is null");
				setContentView(R.layout.activity_main);
			loginButton = (Button) findViewById(R.id.loginButton);
		loginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onLoginButtonClicked();
		//		test();
			}
		});}
			
		}
		else
		{	/*
			Log.d("Figure","Figure what is null");

	
		
		if((currentUser == null))
		{Log.d("Caught", " a null");}
		
		if (currentUser!=null && ParseFacebookUtils.isLinked(currentUser))
		{Log.d("null","nullllllll");
		
		}
		if(currentUser!=null && currentUser.getNumber("check") == null)
			{Log.d("number","null");}
			*/
			
		setContentView(R.layout.activity_main);
			loginButton = (Button) findViewById(R.id.loginButton);
		loginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onLoginButtonClicked();
		//		test();
			}
		});
		}
		

}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
	
	super.onResume();
	Log.d("Entering","onresume");
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
	//	getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	  @Override
	    protected void onDestroy() {
	        dismissProgressDialog();
	        super.onDestroy();
	    }

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d(Integer.toString(requestCode),Integer.toString(resultCode));
		ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
	}

	private void onLoginButtonClicked() {
		MainActivity.this.progressDialog = ProgressDialog.show(
				MainActivity.this, "", "Logging in...", true);
		List<String> permissions = Arrays.asList("public_profile",
				"user_friends", "email");

		ParseFacebookUtils.logIn(permissions, this, new LogInCallback() {
			@Override
			public void done(ParseUser user, ParseException err) {
				if(progressDialog!=null && progressDialog.isShowing())
				{MainActivity.this.progressDialog.dismiss();}
				
				Session session = ParseFacebookUtils.getSession();
				 List<String> PERMISSIONS = Arrays.asList("public_profile",
							"user_friends", "email");

							if (user == null) {
					Log.d(PuddlzApplication.TAG,
							"Uh oh. The user cancelled the Facebook login.");
				} else if (user.isNew()) {
					Log.d(PuddlzApplication.TAG,
							"User signed up and logged in through Facebook!");

					
					if (session != null && session.isOpened()) {
						if (!session.getPermissions().containsAll(PERMISSIONS))
		                { Log.d("contains3", "all--->no");
		                fetch_permissions();}
						else
						{	
						makeMeRequest();}
					}		
					
				} else {
					Log.d(PuddlzApplication.TAG,
							"User logged in through Facebook!");

					
					if (session != null && session.isOpened()) {
						if (!session.getPermissions().containsAll(PERMISSIONS))
		                {   fetch_permissions();
							Log.d("contains4", "all-->nothing");}
										
					else
					{
					
					ParseUser currentUser = ParseUser.getCurrentUser();
					
					if (currentUser != null && currentUser.getNumber("check") == null) {
						Log.d(PuddlzApplication.TAG, "User IN THE GAME"
								+ currentUser.getObjectId());
						
						try {
							ParseUser.getCurrentUser().refresh();
							 Log.d("Sucess","data_fetched");
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if (currentUser.getNumber("check") != null) {
						PuddlzApplication.add_installation_user();
						if (PuddlzApplication.is_first()) {
							Parse.enableLocalDatastore(mcontext);
							PuddlzApplication.set_first(false);
						}
						 SaveData.save_pic_and_list(mcontext);
						//home();
					} else {
						if (session != null && session.isOpened()) {
							makeMeRequest();
						}
						
					}}	}
				}
			}
		});
	}
	private void showUserDetailsActivity() {

		Intent intent = new Intent(this, UserDetailsActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	private void home() {
        Uri targetUri = getIntent().getData();
        Boolean is_blog=false;
        if (targetUri != null) {
            Log.i("YAYY", "Incoming deep link: " + targetUri);
            if(targetUri.toString().contains("blog"))
            {
                Intent intent = new Intent(mcontext, Blog.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.animator.animation3,R.animator.animation4);
                is_blog=true;
                return;
            }
            
             String link=getLink(targetUri.toString());
             Log.d("link-see",link);
             ParseQuery query=new ParseQuery("Questions");
             query.whereEqualTo("objectId",link);
              
             try {
                ParseObject object=query.getFirst();
                if(object!=null)
                {
                    Log.d("reach","here");
                    Intent intent = new Intent(mcontext, Answers.class);
                    Bundle extras = new Bundle();     
                    extras.putString("string",object.getString("question"));
                    extras.putString("object_id",object.getObjectId());
                    intent.putExtras(extras);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    this.finish();
                    overridePendingTransition(R.animator.animation3,R.animator.animation4);
                    return;
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
             
            
        }
        Intent intent = new Intent(this, Home.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.animator.animation9,R.animator.animation10);
    }
	
	private void start_again() {

		Intent intent = new Intent(this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		finish();
		startActivity(intent);
	}
	private void test() {

		Intent intent = new Intent(this, Invite.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		finish();
		startActivity(intent);
	}

	
	private void fetch_permissions() {

		Intent intent = new Intent(this, Permissions.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		finish();
		startActivity(intent);
	}
	
	private void makeMeRequest() {
		Request request = Request.newMeRequest(ParseFacebookUtils.getSession(),
				new Request.GraphUserCallback() {
					@Override
					public void onCompleted(GraphUser user, Response response) {
						if (user != null) {
							ParseUser currentUser = ParseUser
									.getCurrentUser();
							if(currentUser==null)
							{Log.d("why am i", "null");}
							currentUser.put("name", user.getName());
							currentUser.put("facebookId", user.getId());
							currentUser.put("gender",
									(String) user.getProperty("gender"));

							if(user.asMap().get("email")!=null)
							{ currentUser.setEmail((String) user.asMap().get("email") );}
														// Save the user profile info in a user property
						/*	try{
								currentUser.save();
							}catch(ParseException e)
							{ e.printStackTrace();
								Log.d("TAG","Error in saving");
								onLogoutButtonClicked();
							}
							*/
							
							currentUser.saveInBackground(new SaveCallback() {
								
								@Override
								public void done(ParseException arg0) {
									showUserDetailsActivity();		
								}
							});
							

							

						} else if (response.getError() != null) {
							if ((response.getError().getCategory() == FacebookRequestError.Category.AUTHENTICATION_RETRY)
									|| (response.getError().getCategory() == FacebookRequestError.Category.AUTHENTICATION_REOPEN_SESSION)) {
								Log.d(PuddlzApplication.TAG,
										"The facebook session was invalidated.");
								onLogoutButtonClicked();
							} else {
								Log.d(PuddlzApplication.TAG,
										"Some other error: "
												+ response.getError()
														.getErrorMessage());
							}
						}
					}
					
					
				});
		request.executeAsync();

	
	}
	
	private void onLogoutButtonClicked() {
		// Log the user out
		ParseUser.logOut();
		
		if (Session.getActiveSession() != null) {
			Session.getActiveSession().closeAndClearTokenInformation();
			}

			Session.setActiveSession(null);

		// Go to the login view
		start_again();
	}
	
	  private void dismissProgressDialog() {
	        if (progressDialog != null && progressDialog.isShowing()) {
	            progressDialog.dismiss();
	        }
	     
	  }
	  
	  public String getLink(String link) {
	        //String link1=link.replace("http://www.tinkitapp.com/q.php?q=", "");
	        int ind=link.indexOf("q=");
	        
	        if(ind>2)
	        {    
	        return link.substring(ind+2);
	        }
	        else
	            return link;
	         }
	  
	  @Override
		public void onBackPressed() {
			 this.finish();
			 Log.d("main","home");
			 System.exit(0);	
			 super.onBackPressed(); 
			
		}
		
	  
}
