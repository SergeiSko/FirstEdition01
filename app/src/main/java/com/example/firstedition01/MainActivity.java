package com.example.firstedition01;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.*;

public class MainActivity extends AppCompatActivity {
  String url = "http://screedwall.ru:3000";
  TextView textView;
  EditText editText1, editText2;
  boolean auth  = false;
  JSONArray jsonArray;
  JSONObject jsonObject;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    editText1 = (EditText) findViewById(R.id.editView1);
    editText2 = (EditText) findViewById(R.id.editView2);
    textView = (TextView) findViewById(R.id.textView2);
  }

  public class Authorization2 extends AsyncTask<String, Void, String>{
    OkHttpClient client = new OkHttpClient();
    @Override
    protected String doInBackground(String... params){
      HttpUrl.Builder urlBuilder = HttpUrl.parse(params[0]).newBuilder();
      urlBuilder.addQueryParameter("login", params[1]);
      urlBuilder.addQueryParameter("password", params[2]);
      String url = urlBuilder.build().toString();

      Request request = new Request.Builder()
            .url(url)
            .build();
      try {
        Response response = client.newCall(request).execute();
        return response.body().string();
        // Do something with the response.
      } catch (IOException e) {
        e.printStackTrace();
      }
      return null;
    }
    @Override
    protected void  onPostExecute(String res){
      super.onPostExecute(res);
      textView.setText("res: "+res);
    }
  }

  public class Authorization extends AsyncTask<String, Void, String> {
    OkHttpClient client = new OkHttpClient();

    @Override
    protected String doInBackground(String... params){
      RequestBody formBody = new FormBody.Builder()
            .add("login", params[1])
            .add("password", params[2])
            .build();
      Request request = new Request.Builder()
            .url(params[0])
            .post(formBody)
            .build();

      try {
        Response response = client.newCall(request).execute();
        return response.body().string();
        // Do something with the response.
      } catch (IOException e) {
        e.printStackTrace();
      }
      return null;
    }
    @Override
    protected void  onPostExecute(String res){
      super.onPostExecute(res);
      try{
        jsonObject = new JSONObject(res);
        start(jsonObject.getBoolean("respons"));
      }catch (JSONException ex){
        textView.setText(ex.getMessage());
      }

    }
  }
  public void Auth(View view){
    Authorization authorization = new Authorization();
    authorization.execute(url+"/auth", editText1.getText().toString(), editText2.getText().toString());

  }
  void start(boolean auth){
    if(auth){
      Intent intent = new Intent(this, Client.class);
      startActivity(intent);
    }
    else{
      textView.setText("Login or password uncorrected");
    }
  }
}
