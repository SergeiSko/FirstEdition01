package com.example.firstedition01;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Client extends AppCompatActivity {
  final String url = "http://screedwall.ru:3000";
  private int userId, userType;

  TextView textView1, textView2, textView3, textView4, textView5;
  TextView textViewEr;
  EditText editTextId, editText1;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_client);

    userId = getIntent().getIntExtra("userID", 0);

    textView1 = (TextView) findViewById(R.id.textRow1);
    textView2 = (TextView) findViewById(R.id.textRow2);
    textView3 = (TextView) findViewById(R.id.textRow3);
    textView4 = (TextView) findViewById(R.id.textRow4);
    textView5 = (TextView) findViewById(R.id.textRow5);
    textViewEr = (TextView) findViewById(R.id.textViewEr);
    editTextId = (EditText) findViewById(R.id.editTextId);
    editText1 = (EditText) findViewById(R.id.editText1);

    GetTasks getServer = new GetTasks();
    getServer.execute(url + "/getTasks");
  }

  public class SelectTask extends AsyncTask<String, Void, String>{
    @Override
    protected String doInBackground(String... params){
      OkHttpClient client = new OkHttpClient();

      RequestBody formBody = new FormBody.Builder()
            .add("_id", params[1])
            .add("stateId", params[2])
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
      textViewEr.setText(res);

      GetTasks getServer = new GetTasks();
      getServer.execute(url + "/getTasks");

    }
  }

  public class GetTasks extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params){
      OkHttpClient client = new OkHttpClient();

      Request request = new Request.Builder()
            .url(params[0])
            .get()
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
        JSONArray array = new JSONArray(res);
        if(array!=null){
          textView1.setText("id");
          textView2.setText("Задача");
          textView3.setText("Диспетчер");
          textView4.setText("Работник");
          textView5.setText("Статус");
          for(int i = 0; i<array.length(); i++){
            JSONObject jsonObject = array.getJSONObject(i);
            if(jsonObject != null){
              textView1.append("\n"+jsonObject.getString("_id"));
              textView2.append("\n"+jsonObject.getString("task"));
              textView3.append("\n"+jsonObject.getString("dispatcher"));
              textView4.append("\n"+jsonObject.getString("worker"));
              textView5.append("\n"+jsonObject.getString("state"));
            }
          }
        }
      }catch (Exception ex)
      {
        ex.printStackTrace();
      }
    }
  }

  public void getServer(View view){
    GetTasks getServer = new GetTasks();
    getServer.execute(url + "/getTasks");
  }

  public void selectTask(View view){
    SelectTask selectTask = new SelectTask();
    selectTask.execute(url + "/updateTask", editTextId.getText().toString(), "1");
    textViewEr.setText("Send request");
  }

  public void endTask(View view){
    SelectTask selectTask = new SelectTask();
    selectTask.execute(url + "/updateTask", editTextId.getText().toString(), "2");
  }
}
