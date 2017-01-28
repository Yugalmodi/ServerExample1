package com.techpalle.serverexample1;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentOne extends Fragment {
    Button button;
    TextView textView;
    MyAsyncTask myAsynctask;

    public boolean checkInternet(){
        ConnectivityManager manager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if(networkInfo == null || networkInfo.isConnected() == false){
            return false;
        }
        return true;
    }

    public class MyAsyncTask extends AsyncTask<String, Void, String>{
        URL myUrl;
        HttpURLConnection httpURLConnection;
        InputStream inputStream;
        InputStreamReader streamReader;
        BufferedReader bufferedReader;
        String line;
        StringBuilder builder = new StringBuilder();

        @Override
        protected void onPreExecute() {
            textView.setText("\n\n\n\n\n\nPlease KEEP passions, we are loading the data for you only");
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... stringsURL) {
            try {
                myUrl = new URL(stringsURL[0]);
                httpURLConnection = (HttpURLConnection) myUrl.openConnection();
                inputStream = httpURLConnection.getInputStream();
                streamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(streamReader);
                line = bufferedReader.readLine();
                while (line != null){
                    builder.append(line);
                    line = bufferedReader.readLine();
                }
                return builder.toString();
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            catch (SecurityException e) {
                e.printStackTrace();
            }
            return "Some Thing Went Wrong";
        }

        @Override
        protected void onPostExecute(String s) {
            textView.setText(s);
            super.onPostExecute(s);
        }
    }


    public FragmentOne() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fragment_one, container, false);
        textView = (TextView) v.findViewById(R.id.tv1);
        button = (Button) v.findViewById(R.id.button1);
        myAsynctask = new MyAsyncTask();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkInternet()){
                    if(myAsynctask.getStatus() == AsyncTask.Status.RUNNING || myAsynctask.getStatus() == AsyncTask.Status.FINISHED){
                        Toast.makeText(getActivity(), "Already Running, Please Wait......", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    myAsynctask.execute("http://skillgun.com");
                }
                else {
                    Toast.makeText(getActivity(), "NETWORK NOT AVAILABLE", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return v;
    }
}
