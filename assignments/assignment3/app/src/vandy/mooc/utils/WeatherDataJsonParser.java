package vandy.mooc.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import vandy.mooc.aidl.WeatherData;
import vandy.mooc.jsonacronym.JsonAcronym;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

/**
 * Parses the Json acronym data returned from the Acronym Services API
 * and returns a List of JsonAcronym objects that contain this data.
 */
public class WeatherDataJsonParser {
    /**
     * Used for logging purposes.
     */
    private final String TAG = this.getClass().getCanonicalName();

    /**
     * Various tags corresponding to data downloaded in Json from the
     * Weather Data Service.
     */
    final public static String sys_key_JSON = "sys";
    final public static String sys_sunrise_JSON = "sunrise";
    final public static String sys_sunset_JSON = "sunset";
    final public static String main_key_JSON = "main";
    final public static String main_temp_JSON = "temp";
    final public static String main_humidity_JSON = "humidity";
    final public static String wind_key_JSON = "wind";
    final public static String wind_speed_JSON = "speed";
    final public static String wind_direction_JSON = "deg";
    final public static String name_JSON = "name";

    /**
     * Parse the @a inputStream and convert it into a List of JsonAcronym
     * objects.
     */
    public List<WeatherData> parseJsonStream(InputStream inputStream)
        throws IOException {

        // Create a JsonReader for the inputStream.
        try (JsonReader reader =
             new JsonReader(new InputStreamReader(inputStream,
                                                  "UTF-8"))) {
            Log.d(TAG, "Parsing the results returned as an array");

            // Handle the array returned from the Acronym Service.
            return parseAcronymServiceResults(reader);
        }
    }

    /**
     * Parse a Json stream and convert it into a List of JsonAcronym
     * objects.
     */
    public List<WeatherData> parseAcronymServiceResults(JsonReader reader)
        throws IOException {

        reader.beginArray();
        try {
            // If the acronym wasn't expanded return null;
            if (reader.peek() == JsonToken.END_ARRAY)
                return null;

            // Create a JsonAcronym object for each element in the
            // Json array.
            return parseAcronymMessage(reader);
        } finally {
            reader.endArray();
        }
    }

    public List<WeatherData> parseAcronymMessage(JsonReader reader)
        throws IOException {

        List<WeatherData> acronyms = null;
        reader.beginObject();

        try {
            outerloop:
            while (reader.hasNext()) {
                String name = reader.nextName();
                switch (name) {
                case JsonAcronym.sf_JSON:
                    Log.d(TAG, "reading sf field");
                    reader.nextString();
                    break;
                case JsonAcronym.lfs_JSON:
                    Log.d(TAG, "reading lfs field");
                    if (reader.peek() == JsonToken.BEGIN_ARRAY)
                        acronyms = parseAcronymLongFormArray(reader);
                    break outerloop;
                default:
		    reader.skipValue();
                    Log.d(TAG, "weird problem with " + name + " field");
                    break;
                }
            }
        } finally {
                reader.endObject();
        }
        return acronyms;
    }

    /**
     * Parse a Json stream and convert it into a List of JsonAcronym
     * objects.
     */
    public List<WeatherData> parseAcronymLongFormArray(JsonReader reader)
        throws IOException {

        Log.d(TAG, "reading lfs elements");

        reader.beginArray();

        try {
            List<WeatherData> acronyms = new ArrayList<WeatherData>();

            while (reader.hasNext()) 
                acronyms.add(parseAcronym(reader));
            
            return acronyms;
        } finally {
            reader.endArray();
        }
    }

    /**
     * Parse a Json stream and return a JsonAcronym object.
     */
    public WeatherData parseAcronym(JsonReader reader) 
        throws IOException {

        reader.beginObject();

        WeatherData acronym = new WeatherData();
        try {
            while (reader.hasNext()) {
                String name = reader.nextName();
                switch (name) {
                case JsonAcronym.lf_JSON:
                    acronym.setLongForm(reader.nextString());
                    Log.d(TAG, "reading lf " + acronym.getLongForm());
                    break;
                case JsonAcronym.freq_JSON:
                    acronym.setFreq(reader.nextInt());
                    Log.d(TAG, "reading freq " + acronym.getFreq());
                    break;
                case JsonAcronym.since_JSON:
                    acronym.setSince(reader.nextInt());
                    Log.d(TAG, "reading since " + acronym.getSince());
                    break;
                default:
                    reader.skipValue();
                    Log.d(TAG, "ignoring " + name);
                    break;
                }
            } 
        } finally {
                reader.endObject();
        }
        return acronym;
    }
}
