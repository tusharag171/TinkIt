package com.puddlz;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.parse.ParseUser;

public class Blog_View extends Activity {
	private WebView webView;
	String link;
    Context mc;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		mc=this;
		getActionBar().setTitle("Blog");
		setContentView(R.layout.activity_blog__view);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		webView = (WebView) findViewById(R.id.webview);
		link = getIntent().getExtras()
	                .getString("link");
		startWebView(link);
	
	
	}
    private void startWebView(String url) {
        
        //Create new webview Client to show progress dialog
        //When opening a url or click on link
         
        webView.setWebViewClient(new WebViewClient() {     
            ProgressDialog progressDialog;
          
            //If you will not use this method url links are opeen in new brower not in webview
            public boolean shouldOverrideUrlLoading(WebView view, String url) {             
                view.loadUrl(url);
                return true;
            }
        
            //Show loader on url load
            public void onLoadResource (WebView view, String url) {
             /*   if (progressDialog == null) {
                    // in standard case YourActivity.this
                    progressDialog = new ProgressDialog(Blog_View.this);
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();
                }
                */
            	setProgressBarIndeterminateVisibility(true);
            	
            }
            public void onPageFinished(WebView view, String url) {
            	setProgressBarIndeterminateVisibility(false);
            	//  progressDialog.dismiss();
           }
             
        });
          
         // Javascript inabled on webview 
        webView.getSettings().setJavaScriptEnabled(true);
     
                
        //Load url in webview
        webView.loadUrl(url);
          
          
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.blog__view, menu);
		return true;
	}


/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.answers, menu);
        return super.onCreateOptionsMenu(menu);
    }
*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
        case R.id.action_share:
            share();
            return true;
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

void share()
{
     Bundle params = new Bundle();
     params.putString("name", getIntent().getExtras().getString("title"));
     params.putString("caption", "TinkIt, recommendation made awesome!");
     params.putString("description", ParseUser.getCurrentUser().getString("name")+" shared this blog.\n");
    params.putString("link", getIntent().getExtras().getString("link_actual"));
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
                             "Blog Shared!",
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
