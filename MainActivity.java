package com.example.top10downloader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    //using a tag to let you know what class you're logging from 
    private static final String TAG = "MainActivity";
    //Declaring ListView object
    private ListView listApps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initializing listView to the ListView widget
        listApps = (ListView) findViewById(R.id.xmlListView);

        Log.d(TAG, "onCreate: starting AsyncTask");
        DownloadData downloadData = new DownloadData();
        //URL goes into the  parameter of the "execute" method in order to start the download
        downloadData.execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml");
        Log.d(TAG, "onCreate: done");


    }

    /**
     * AsyncTask can only be used with a subclass.
     * @ param String the type of parameter sent to the test when execute method is called
     * @ param Void don't want to show the progress on the download
     * @ param String type of result of the background computation will be passed to onPostExecutes
     */
    private class DownloadData extends AsyncTask<String, Void, String> {
        private static final String TAG = "DownloadData";

        @Override
        //Takes in the first params that contains the String url after execute call
        //the background computation. The result will be passed into the onPostExecute method.
        //Ellipses(...) can take in multiple strings and store the value in a string array.
        //first value would be String[0], 2nd would be String[1]
        protected String doInBackground(String... strings) {
            Log.d(TAG, "doInBackground: starts with " + strings[0]);
            //element 0 = first parameter of the URL passed in to the downloadXML method and
            //getting the return value from it
            String rrsFeed = downloadXML(strings[0]);
            if (rrsFeed == null) {
                Log.e(TAG, "doInBackground: Error loading");
            }
            //returns to the on onPostExecute
            return rrsFeed;
        }

        @Override
        //takes in the result from the doInBackground after the computation from it in done
        //display result on the UI thread
        //@param s is the xml
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d(TAG, "onPostExecute: parameter is " + s);
            //creating the parse object after finishing downloading the xml data.
            ParseApplications parseApplications = new ParseApplications();
            //passing the xml data into the method for parsing
            //The parse() method will finish parsing
            parseApplications.parse(s);

            /**
             *  Creating an ArrayAdapter to store all the FeedEntry object in the ArrayList
             *
             *  @ param Current Context
             *  @ param The TextView that the ArrayAdapter will use to put the data into
             *  @ param The list of FeedEntry object that will be displayed
             */

//            ArrayAdapter <FeedEntry> arrayAdapter = new ArrayAdapter<FeedEntry>(
//                    MainActivity.this, R.layout.list_item, parseApplications.getApplications());
//            //Connecting the ListView widget so it takes data from the arrayAdapter
//            listApps.setAdapter(arrayAdapter);

            FeedAdapter feedAdapter = new FeedAdapter(MainActivity.this, R.layout.list_record,
                    parseApplications.getApplications());
            //Connecting the ListView widget so it takes data from the feedAdapter
            //display in the activity_main xml that contains the ListView
            listApps.setAdapter(feedAdapter);


        }
    }

        //@param takes in the URL from doInBackground method
        private String downloadXML(String urlPath) {
            StringBuilder xmlResult = new StringBuilder();

            try {
                //creating a ref to urlPath to see it actually exist
                URL url = new URL(urlPath);
                //open the Http connection the server sends back a response codes
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                //storing the response codes
                int response = connection.getResponseCode();
                Log.d(TAG, "downloadXML: The response code was " + response);

                //1).getting the data from http connection as inputStream
                //2).Translate InputStream to character with InputStreamReader
                //3).Use a BufferedReader to read from InputStreamReader. BufferedReader can
                //only read character and can't read from inputStream directly.
                //reader use to read from the stream
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                int charRead;
                //set for reading 500 characters at a time
                char[] inputBuffer = new char[500];
                while (true) {
                    //have to call read method to get it to read
                    //read character and put into the array and return an int of how many of how many
                    //read(inputBuffer) return the number of character that was read and -1 if none was read
                    charRead = reader.read(inputBuffer);
                    //ran out of things to read
                    if (charRead < 0) {
                        break;
                    }
                    //number of character read was greater than 0
                    if (charRead > 0) {
                        //Returns the string that contains character of the specific  of the charArray
                        //String.copyValue(char[] , offset (wher to start), maximum number of character/length
                        xmlResult.append(String.copyValueOf(inputBuffer, 0, charRead));
                    }
                }
                reader.close();
                return xmlResult.toString();

            } catch (MalformedURLException e) {
                System.out.println("Invalid URL error " + e.getMessage());
            } catch (IOException e) {
                System.out.println("Error reading data " + e.getMessage());
            } catch (SecurityException e) {
                System.out.println("security needs permission " + e.getMessage());

            }
            return null;
        }

    }

