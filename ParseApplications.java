package com.example.top10downloader;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by Rathtana on 1/20/17.
 */

public class ParseApplications {
    private static final String TAG = "ParseApplications";
    //Declaring an ArrayList
    //Storing all the FeedEntry object in the ArrayList
    private ArrayList<FeedEntry> applications;

    public ParseApplications() {
        //Initializing the ArrayList when an object of this class is created
        this.applications = new ArrayList<>();

    }
    //getting for the application
    public ArrayList<FeedEntry> getApplications() {
        //returning the the applications in the ArrayList
        return applications;
    }
    //parsing is to read the XML and create a for the program to use the XML
    public boolean parse(String xmlData) {
        boolean status = true;
        FeedEntry currentRecord = null;
        //inside the Entry of the xml data that we want
        boolean inEntry = false;
        String textValue = "";

        try {
            /**
             * creating an instance(object) for XmlPullParserFactor to read the XML and getting its
              contents according to the structure. It gives you a way to read the XML while only
             getting the data that you want from it.
             */
            //The key word Factory is used because this is a singleton class. Only one object
            //can exist for this class. Therefor, you can't use the keyword "new" when calling the
            //constructor.
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();

            //Specifies that the parser produced by this factory will provide support for XML namespaces.
            //allow you to parse xml that uses name space
            //Cannot be change during parsing
            factory.setNamespaceAware(true);

            //We can't just create an instance of PullParser object; therefore, the XmlPullParserFactory
            //provides us a factory that will produce a PullParser object.
            XmlPullParser xpp = factory.newPullParser();

            /**
             * StreamReader is reading the xmlData
             * Set the input source for parser to the given reader and reset the parser by
             * starting again from the beginning of the inputStream
             * @param Reader object
             */
            xpp.setInput(new StringReader(xmlData));

            //getting the event type to see where were at
            int eventType = xpp.getEventType();

            //check the event to make sure we haven't reach the end of the document/xml
            while(eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = xpp.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        Log.d(TAG, "parse: Starting tag for " + tagName);
                        //checking to see if the tag matches what we're looking for
                        if ("entry".equalsIgnoreCase(tagName)) {
                            //Change status to true since we're in Entry now
                            inEntry = true;
                            //Create a Feed Entry object
                            currentRecord = new FeedEntry();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        Log.d(TAG, "parse: Ending tag for " + tagName);
                        if(inEntry) {
                            if("entry".equalsIgnoreCase(tagName)) {
                                //add the FeedEntry object to the ArrayList to save it's add
                                applications.add(currentRecord);
                                //exiting out the entry
                                inEntry = false;
                            }
                            else if("name".equalsIgnoreCase(tagName)) {
                                //add the text value of the tag to the object instance variable
                                currentRecord.setName(textValue);
                            }
                            else if ("artist".equalsIgnoreCase(tagName)) {
                                currentRecord.setArtist(textValue);
                            }
                            else if ("releaseDate".equalsIgnoreCase(tagName)){
                                currentRecord.setReleaseDate(textValue);
                            }
                            else if ("summary".equalsIgnoreCase(tagName)) {
                                currentRecord.setSummary(textValue);
                            }
                            //use get attributeValue method in the future to avoid overriding
                            //Since there might be more than one image in a single entry 
                            else if ("image".equalsIgnoreCase(tagName)) {
                                currentRecord.setImageURl(textValue);
                            }

                        }
                        break;
                }
                eventType = xpp.next();
            }
            //checking to see if all of our 10 apps were found and stored
            for(FeedEntry app: applications) {
                Log.d(TAG, "*************************");
                Log.d(TAG, app.toString());
            }
        } catch (Exception e) {   //catching all exception
            status = false;
            e.printStackTrace(); //track back to where the error occured
        }
        return status;

    }

}

