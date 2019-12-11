package com.example.barang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import server.ConfigUrl;

public class MainActivity extends AppCompatActivity {
//getdata
  private RequestQueue mRequestQueue;
  private TextView show;


  //inputdata
  private EditText kodebarang, namabarang, harga, jenis;
  private Button input;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mRequestQueue = Volley.newRequestQueue(this);
    //getdata
    show = (TextView) findViewById(R.id.TVtampilkan);

    //inputdata
    kodebarang = (EditText) findViewById(R.id.edtkodebarang);
    namabarang = (EditText) findViewById(R.id.edtnamabarang);
    harga = (EditText) findViewById(R.id.edtharga);
    jenis = (EditText) findViewById(R.id.edtjenis);

    input = (Button) findViewById(R.id.btninput);

    input.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String strkodebarang = kodebarang.getText().toString();
        String strnamabarang = namabarang.getText().toString();
        String strharga = harga.getText().toString();
        String strjenis = jenis.getText().toString();

        if(strkodebarang.isEmpty()){
          Toast.makeText(getApplicationContext(), "Kode barang tidak boleh kosong",
            Toast.LENGTH_LONG).show();
        } else if (strnamabarang.isEmpty()) {
          Toast.makeText(getApplicationContext(), "Nama barang tidak boleh kosong",
            Toast.LENGTH_LONG).show();
        } else if (strharga.isEmpty()) {
          Toast.makeText(getApplicationContext(), "Harga barang tidak boleh kosong",
            Toast.LENGTH_LONG).show();
        } else if (strjenis.isEmpty()) {
          Toast.makeText(getApplicationContext(), "Jenis barang tidak boleh kosong",
            Toast.LENGTH_LONG).show();
        } else{
          inputdata(strkodebarang, strnamabarang, strharga, strjenis);

          Intent a  = new Intent(MainActivity.this, MainActivity.class);
          startActivity(a);
          finish();
        }
      }
    });
  fetchJsonResponse();
  }
  //method getdata
  private void fetchJsonResponse() {
    // Pass second argument as "null" for GET requests
    JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, ConfigUrl.getdata, null,
      new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
          try {
            String result = response.getString("data");
            Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();

            //tambahkan  code
            JSONArray res = new JSONArray(result);
            for ( int i = 0; i < res.length(); i++) {
              JSONObject jObj = res.getJSONObject(i);
              show.append("kodebarang = " + jObj.getString("kodebarang") + "\n" +
                "namabarang = " + jObj.getString("namabarang") + "\n" +
                "harga = " + jObj.getString("harga") + "\n" +
                "jenis = " + jObj.get("jenis") + "\n\n");
              Log.v(" Ini datanya = ", jObj.getString("kodebarang").toString());
            }
          } catch (JSONException e) {
            e.printStackTrace();
          }
        }
      }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        VolleyLog.e("Error: ", error.getMessage());
      }
    });

    /* Add your Requests to the RequestQueue to execute */
    mRequestQueue.add(req);
  }
// method input
  private void inputdata(String kodebarang, String namabarang, String harga, String jenis){
    //final String URL = "/volley/resource/12";
    //post params to be sent to the server
    HashMap<String, String> params = new HashMap<String, String>();
    params.put("kodebarang", kodebarang);
    params.put("namabarang", namabarang);
    params.put("harga", harga);
    params.put("jenis", jenis);

    JsonObjectRequest req = new JsonObjectRequest(ConfigUrl.inputdata, new JSONObject(params),
      new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
          try {
            VolleyLog.v("Response:%n %s", response.toString(4));
          } catch (JSONException e) {
            e.printStackTrace();
          }
        }
      }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        VolleyLog.e("Error: ", error.getMessage());
      }
    });

// add the request object to the queue to be executed
    //ApplicationController.getInstance().addToRequestQueue(req);
    mRequestQueue.add(req);
  }
}
