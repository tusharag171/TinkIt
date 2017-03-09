package com.puddlz;

import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

public class Permissions extends ActionBarActivity {

	private Button permissionButton;
	private Button continueButton;
	private TextView fb_oops_text;
	Session session = Session.getActiveSession();
	List<String> PERMISSIONS = Arrays.asList("public_profile",
			"user_friends", "email");
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_permissions);
		getSupportActionBar().hide();
		permissionButton = (Button) findViewById(R.id.fetch_permissions);
		continueButton = (Button) findViewById(R.id.continu);
		fb_oops_text = (TextView) findViewById(R.id.fb_perm_text);
		permissionButton.setVisibility(View.GONE);
		continueButton.setVisibility(View.GONE);
			}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		if (!session.getPermissions().containsAll(PERMISSIONS))
		{permissionButton.setVisibility(View.VISIBLE);
		continueButton.setVisibility(View.GONE);	
		}  		 
		else
		{
			permissionButton.setVisibility(View.GONE);
			continueButton.setVisibility(View.VISIBLE);
			fb_oops_text.setText("You are all set to go. Press the continue button to verify your profile");
		}

		
		permissionButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try{
				fetch_permissions();
				}catch(UnsupportedOperationException e)
				{
					Log.d("UnsupportedOperation","error");
				}
			}
		});
		
		continueButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ParseUser currentUser = ParseUser
						.getCurrentUser();
				if (currentUser!=null){
					makeMeRequest();
				}
				else
				{
					onLogoutButtonClicked();
				}
			
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.permissions, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		
		return super.onOptionsItemSelected(item);
	}

	
	public void fetch_permissions()
	{
		Session session = Session.getActiveSession();

		
				if (session != null && session.isOpened()) {
			
			final Session.StatusCallback statusCallback =   new SessionStatusCallback();

			
              if (!session.getPermissions().containsAll(PERMISSIONS))
               { Log.d("contains3", "all");
               
                   Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(Permissions.this, PERMISSIONS).setCallback(statusCallback);
                   session.requestNewReadPermissions(newPermissionsRequest);
              //       session =Session.getActiveSession();
                     Log.d("contains4", "all");
               }
				}}
		
	private class SessionStatusCallback implements Session.StatusCallback {
	    @Override
	    public void call(Session session, SessionState state, Exception exception) {
	            // Respond to session state changes, ex: updating the view
	    }
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d(Integer.toString(requestCode),Integer.toString(resultCode));
		 session.onActivityResult(this, requestCode, resultCode, data);
		ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
	}
	
	private void makeMeRequest() {
		Request request = Request.newMeRequest(ParseFacebookUtils.getSession(),
				new Request.GraphUserCallback() {
					@Override
					public void onCompleted(GraphUser user, Response response) {
						if (user != null) {
							ParseUser currentUser = ParseUser
									.getCurrentUser();
							currentUser.put("name", user.getName());
							currentUser.put("facebookId", user.getId());
							currentUser.put("gender",
									(String) user.getProperty("gender"));

							if(user.asMap().get("email")!=null)
							{ currentUser.setEmail((String) user.asMap().get("email") );}
														// Save the user profile info in a user property
							try{
								currentUser.save();
							}catch(ParseException e)
							{
								Log.d("TAG","Error in saving");
								
							}
							showUserDetailsActivity();

							

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
	
	private void showUserDetailsActivity() {

		Intent intent = new Intent(this, UserDetailsActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	private void start_again() {

		Intent intent = new Intent(this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		finish();
		startActivity(intent);
	}

	
}
