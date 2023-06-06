package com.nipa.chatgptbyapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nipa.chatgptbyapi.adaptor.ChatAdaptor;
import com.nipa.chatgptbyapi.databinding.ActivityChatBinding;
import com.nipa.chatgptbyapi.model.ChatModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    private ActivityChatBinding binding;
    private static final String TAG = "MainActivity";
   private static final String API_KEY = "YOUR API KEY";

    private static final String API_URL = "https://api.openai.com/v1/completions";
    List<ChatModel> chatList=new ArrayList<ChatModel>();
    ChatAdaptor chatAdaptor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_chat);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
       // View view = binding.getRoot();
        setContentView(binding.getRoot());


        // Example usage: sending a message and receiving a response
       // sendMessage("Hello, GPT!");
        binding.ivSend.setOnClickListener((View v)->{
           String urComments= binding.tvComments.getText().toString();
            chatAdded(urComments,true);
            binding.tvComments.setText("");
        });
    }
    private void chatAdded(String comments,Boolean isUrcomments){

        ChatModel newCommetns=new ChatModel();
        newCommetns.setMessage(comments);
        newCommetns.setChatByMe(isUrcomments);
        chatList.add(newCommetns);
        if(isUrcomments){
            sendMessage(comments);
        }

        refreshAdaptor();
    }
    private void refreshAdaptor(){
        if(chatAdaptor==null){
            chatAdaptor=new ChatAdaptor(this,chatList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setReverseLayout(true);
            linearLayoutManager.setStackFromEnd(true);
            binding.recyclerView.setLayoutManager(linearLayoutManager);
            binding.recyclerView.setAdapter(chatAdaptor);
        }else {
            chatAdaptor.notifyDataSetChanged();

        }
    }
    private void sendMessage(String message) {
        JSONObject jsonBody=new JSONObject();
        try {
            jsonBody.put("model","text-davinci-003");
            jsonBody.put("prompt",message);
            jsonBody.put("max_tokens",100);
            jsonBody.put("temperature",0);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, API_URL, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject stringResponse) {
                        //Log.d("onResponse"," : "+stringResponse.toString());
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(stringResponse.toString());
                            JSONArray choicesArray = jsonObject.getJSONArray("choices");
                            String generatedMessage = choicesArray.getJSONObject(0).getString("text");
                            Toast.makeText(ChatActivity.this, generatedMessage, Toast.LENGTH_SHORT).show();
                            Log.d("onResponse"," : "+generatedMessage);
                            chatAdded(generatedMessage,false);

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("onResponse : ",""+error.toString());

            }
        }) {


            // Passing some request headers
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization", "Bearer "+API_KEY);

                return headers;
            }

        };

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        Volley.newRequestQueue(this).add(jsonObjReq);


    }
}