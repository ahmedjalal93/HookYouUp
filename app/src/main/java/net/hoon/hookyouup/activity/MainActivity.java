package net.hoon.hookyouup.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.HashMap;

import net.hoon.hookyouup.R;
import net.hoon.hookyouup.helper.SQLiteHandler;
import net.hoon.hookyouup.helper.SessionManager;

public class MainActivity extends Activity {

	private TextView txtName;
	private TextView txtEmail;
	private Button btnLogout;

	private SQLiteHandler db;
	private SessionManager session;

	private CallbackManager callbackManager;
	int fb = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FacebookSdk.sdkInitialize(getApplicationContext());
		setContentView(R.layout.activity_main);
		callbackManager = CallbackManager.Factory.create();
		//Log.e("Im here", "HERE");
		//Toast.makeText(getApplicationContext(), "I made it", Toast.LENGTH_LONG).show();

		Bundle inBundle = getIntent().getExtras();
		 fb = inBundle.get("facebook").hashCode();
		//String name = inBundle.get("name").toString();
		String surname = inBundle.get("surname").toString();
		//String imageUrl = inBundle.get("imageUrl").toString();

//		Button loginButt = (Button) findViewById(R.id.logout_button);
//		loginButt.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				System.out.println("@@@000");
//				logout();
//			}
//
//		});

		TextView nameView = (TextView)findViewById(R.id.nameAndSurname);
		nameView.setText(surname);


		txtName = (TextView) findViewById(R.id.name);
		txtEmail = (TextView) findViewById(R.id.email);
		//if(fb == 0) {
			//System.out.println("@@@111");
			btnLogout = (Button) findViewById(R.id.btnLogout);
		//}

		// SqLite database handler
		db = new SQLiteHandler(getApplicationContext());

		// session manager
		session = new SessionManager(getApplicationContext());

		if (!session.isLoggedIn()) {
			if(fb == 0) {
				System.out.println("@@@111");
				logoutUser();
			}
		}

		// Fetching user details from SQLite
		HashMap<String, String> user = db.getUserDetails();

		String name = user.get("name");
		String email = user.get("email");

		// Displaying the user details on the screen
		txtName.setText(name);
		txtEmail.setText(email);

		// Logout button click event
		btnLogout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				logoutUser();
			}
		});
	}

	public void logout(){
		System.out.println("@@@222");
		LoginManager.getInstance().logOut();
		System.out.println("@@@222111");
		Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
		System.out.println("@@@222222");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		System.out.println("@@@222333");
		startActivity(intent);
		System.out.println("@@@222444");
		finish();
		System.out.println("@@@222555");
	}

	/**
	 * Logging out the user. Will set isLoggedIn flag to false in shared
	 * preferences Clears the user data from sqlite users table
	 * */
	private void logoutUser() {
		session.setLogin(false);

		db.deleteUsers();

		// Launching the login activity
		System.out.println("@@@333");
		Intent intent = new Intent(MainActivity.this, LoginActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	protected void onPause() {
		super.onPause();
		System.out.println("@@@444");
	}

	protected void onStop() {
		super.onStop();
		//finish();
		System.out.println("@@@555");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			logout();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
