package com.example.firstedition01;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SupportActivity extends AppCompatActivity {
  String url = "http://screedwall.ru:3050";
  TextView textViewEr;
  EditText editText1, editText2, editText3, editText4;
  JSONObject jsonObject;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_support);

    editText1 = (EditText) findViewById(R.id.editText1);
    editText2 = (EditText) findViewById(R.id.editText2);
    editText3 = (EditText) findViewById(R.id.editText3);
    editText4 = (EditText) findViewById(R.id.editText4);
    textViewEr = (TextView) findViewById(R.id.textViewEr);
  }

  public class SelectTask extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... params){
      OkHttpClient client = new OkHttpClient();

      RequestBody formBody = new FormBody.Builder()
            .add("mail", params[1])
            .add("persona", params[2])
            .add("subjectP", params[2])
            .add("problem", params[2])
            .build();

      Request request = new Request.Builder()
            .url(params[0])
            .post(formBody)
            .build();
      try{

        Response response = client.newCall(request).execute();
        return response.body().string();
      }catch (IOException e){
        e.printStackTrace();
      }

      return null;
    }
    @Override
    protected void onPostExecute(String res){
      super.onPostExecute(res);
      try{
        jsonObject = new JSONObject(res);
        if(jsonObject.getBoolean("response"))
        textViewEr.setText("sended");
      }catch (JSONException ex){
        textViewEr.setText(ex.getMessage());
      }

    }
  }

  public void sendSupport(View view){
    SelectTask selectTask = new SelectTask();
    selectTask.execute(url,
          editText1.getText().toString(),
          editText2.getText().toString(),
          editText3.getText().toString(),
          editText4.getText().toString());
  }
}
