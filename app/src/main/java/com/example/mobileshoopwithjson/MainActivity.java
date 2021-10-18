package com.example.mobileshoopwithjson;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
ImageView mobileImage;
TextView companyName,Mobilename,screenSize,battery,ram;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mobileImage=findViewById(R.id.imageView);
        companyName=findViewById(R.id.companyNameTv);
        Mobilename=findViewById(R.id.MobilenameTv);
        screenSize=findViewById(R.id.screenSizeTv);
        battery=findViewById(R.id.batteryTv);
        ram=findViewById(R.id.ramTv);

        String api="http://androidtutorialpoint.com/api/MobileJSONObject.json";
        new MobileTask().execute(api);

    }
    public class MobileTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            String s = strings[0];
            try {
                URL url = new URL(s);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(stream);
                BufferedReader bufferedReader = new BufferedReader(reader);
                StringBuilder stringBuilder = new StringBuilder();
                String line="";
                if((line=bufferedReader.readLine())!=null){
                    stringBuilder.append(line);
                }
              return stringBuilder.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONArray jsonArray = new JSONArray(s);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                companyName.setText(jsonObject.get("companyName").toString());
                Mobilename.setText(jsonObject.get("name").toString());
                screenSize.setText(jsonObject.get("screenSize").toString());
               battery.setText(jsonObject.get("battery").toString());
                ram.setText(jsonObject.get("ram").toString());

                new ImageDownload().execute(jsonObject.get("url").toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            super.onPostExecute(s);
        }
    }
    public class ImageDownload extends AsyncTask<String,Void,Bitmap>{

        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
               Bitmap bitmap = BitmapFactory.decodeStream(stream);
               return bitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            mobileImage.setImageBitmap(bitmap);
            super.onPostExecute(bitmap);
        }
    }

}