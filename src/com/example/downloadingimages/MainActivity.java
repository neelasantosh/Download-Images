package com.example.downloadingimages;

import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends Activity {

	ImageView imageView1;
	ProgressBar progress;
	Button buttonLoad;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		imageView1 = (ImageView) findViewById(R.id.imageView1);
		buttonLoad = (Button) findViewById(R.id.button1);
		progress = (ProgressBar) findViewById(R.id.progressBar1);

		buttonLoad.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isNetworkAvailable()){
				
				String imageUrl = "http://www.jharkhandstatenews.com/wp-content/uploads/2013/03/Amitabh_Bacchan.jpg";
				// download image using AsyncTask
							LoadImageTask task = new LoadImageTask();
				task.execute(imageUrl);
				}
				else{
					Toast.makeText(MainActivity.this, "No Connection", 5).show();
				}
			}
		});
	}// end of onCreate

	boolean isNetworkAvailable() {
		TelephonyManager telM = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
		boolean isData = false;
		if(telM.getDataState() == TelephonyManager.DATA_CONNECTED)
		isData = true;
		
		boolean isWifi = false;
		ConnectivityManager connManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		
		isWifi = mWifi.isConnected();
		
		return (isData || isWifi);
		/*ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();*/
	}

	class LoadImageTask extends AsyncTask<String, Void, Bitmap> {

		@Override
		protected Bitmap doInBackground(String... params) {
			// TODO Auto-generated method stub
			String url = params[0];
			// create GET request
			HttpGet getRequest = new HttpGet(url);
			HttpClient client = new DefaultHttpClient();
			// send request to server and get response
			Bitmap bmp = null;
			try {
				HttpResponse response = client.execute(getRequest);
				InputStream is = response.getEntity().getContent();
				bmp = BitmapFactory.decodeStream(is);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return bmp;
		}// eof doInBackground

		@Override
		protected void onPostExecute(Bitmap result) {

			super.onPostExecute(result);
			imageView1.setImageBitmap(result);
			progress.setVisibility(View.GONE);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			imageView1.setImageResource(R.drawable.ic_launcher);

			progress.setVisibility(View.VISIBLE);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
