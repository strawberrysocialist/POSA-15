package vandy.mooc.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import vandy.mooc.aidl.WeatherData;
import vandy.mooc.jsonweather.JsonWeather;
import vandy.mooc.jsonweather.WeatherJSONParser;
import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

public class Utils {
    /**
     * Used for debugging.
     */
    private final static String TAG = "Utils";

    @SuppressWarnings("unused")
	private static String sWeather_Service_URL = "http://api.openweathermap.org/data/2.5/weather?q=";
    
    private static String sWeather_Service_Base_URL = "http://api.openweathermap.org/data/2.5/";
    
    private static String sSingle_City = "weather?q=";
    
    private static String sMetric_Units = "&units=metric";
    
    private static String sImperial_Units = "&units=imperial";
    
    private static final double[] DEGREES = {
    		0, 11.25, 33.75, 56.25, 
			78.75, 101.25, 123.75, 146.25, 
			168.75, 191.25, 213.75, 236.25, 
			258.75, 281.25, 303.75, 326.25,
			348.75, 360};

    private static final String[] DIRECTIONS = {
    		"N", "NNE", "NE", "ENE", 
			"E", "ESE", "SE", "SSE", 
			"S", "SSW", "SW", "WSW", 
			"W", "WNW", "NW", "NNW",
			"N"};

    public static List<WeatherData> getWeatherResults(final String location) {
        // Create a List that will return the WeatherData obtained
        // from the Weather Service web service.
        final List<WeatherData> returnList = 
            new ArrayList<WeatherData>();
            
        // A List of JsonWeather objects.
        List<JsonWeather> jsonWeathers = null;

        try {
            // Append the location to create the full URL.
            final URL url = getRequestUrl(location);

            // Opens a connection to the Weather Service.
            HttpURLConnection urlConnection =
                (HttpURLConnection) url.openConnection();
            Log.d(TAG, "Opened connection to " + url.toString());
            
            // Sends the GET request and reads the Json results.
            try (InputStream in =
            		new BufferedInputStream(urlConnection.getInputStream())) {
				// Create the parser.
				final WeatherJSONParser parser =
				     new WeatherJSONParser();

				// Parse the Json results and create JsonWeather data
				// objects.
				Log.d(TAG, "Begin parsing JSON.");
				jsonWeathers = parser.parseJsonStream(in);
				Log.d(TAG, "JSON parsed.");
            } finally {
                urlConnection.disconnect();
                Log.d(TAG, "Closed connection to " + url.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return returnList;
        }

        if (jsonWeathers != null && jsonWeathers.size() > 0) {
            // Convert the JsonWeather data objects to our WeatherData
            // object, which can be passed between processes.
            for (JsonWeather jsonWeather : jsonWeathers)
                returnList.add(new WeatherData(jsonWeather.getName(),
                                               jsonWeather.getWindSpeed(),
                                               jsonWeather.getWindDirection(),
                                               jsonWeather.getTemp(),
                                               jsonWeather.getHumidity(),
                                               jsonWeather.getSunrise(),
                                               jsonWeather.getSunset()));
        }
        // Return the List of WeatherData.
        return returnList;
    }
    
    private static URL getRequestUrl(final String location) {
        URL url = null;
        final String country = Locale.getDefault().getCountry();
        final String uri;
        if (country.equals("US") || 
        		country.equals("LR") || 
        		country.equals("MM")) {
            uri = sWeather_Service_Base_URL +
            		sSingle_City + location + sImperial_Units;
        } else {
            uri = sWeather_Service_Base_URL +
            		sSingle_City + location + sMetric_Units;
        }
        
		try {
			url = new URL(uri);
	        Log.d(TAG, "Checking weather for " + location +
	        		" using " + url.toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
    	return url;
    }
    
    public static String longToTime(long time) {
    	// Convert time expressed in seconds to 
    	//	milliseconds for use by Date functions.
    	final Date date = new Date(time * 1000);
    	Log.d(TAG, "Converted time (" + time +
    			") to date (" + date.toString() + ").");
    	final DateFormat formatter = new SimpleDateFormat(
    			"HH:mm", Locale.getDefault());
    	final String dateFormatted = formatter.format(date);    	
    	Log.d(TAG, "Formatted date (" + date.toString() +
    			") to (" + dateFormatted + ").");
    	return dateFormatted;
    }
    
    /**
     * Convert the wind direction in degrees into compass direction.
     */
    public static String getWindDirection(double windDegrees) {
    	for (int i = 0; i < DEGREES.length - 1; i++) {
    		if (windDegrees >= DEGREES[i] && windDegrees < DEGREES[i + 1]) {
    			return DIRECTIONS[i];
    		}
    	}
    	return null;
    }
    
    /**
     * Show a short toast message.
     */
    public static void showShortToast(Context context,
                             		String message) {
        showToast(context,
               message,
               Toast.LENGTH_SHORT);
    }

    /**
     * Show a long toast message.
     */
    public static void showLongToast(Context context,
    								String message) {
    	showToast(context,
    			message,
    			Toast.LENGTH_LONG);
    }

    /**
     * Show a toast message.
     */
    private static void showToast(Context context,
                                 String message,
                                 int length) {
        Toast.makeText(context,
                       message,
                       length).show();
    }

    /**
     * This method is used to hide a keyboard after a user has
     * finished typing the input.
     */
    public static void hideKeyboard(Activity activity,
                                    IBinder windowToken) {
        InputMethodManager mgr =
            (InputMethodManager) activity.getSystemService
            (Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(windowToken,
                                    0);
    }

    /**
     * Ensure this class is only used as a utility.
     */
    private Utils() {
        throw new AssertionError(TAG + " can not be instantiated");
    } 
}
