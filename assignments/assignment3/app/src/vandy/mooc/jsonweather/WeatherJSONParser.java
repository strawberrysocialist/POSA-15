package vandy.mooc.jsonweather;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

/**
 * Parses the Json weather data returned from the Weather Services API
 * and returns a List of JsonWeather objects that contain this data.
 */
public class WeatherJSONParser {
    /**
     * Used for logging purposes.
     */
    private final String TAG = this.getClass().getCanonicalName();

    /**
     * Parse the @a inputStream and convert it into a List of JsonWeather
     * objects.
     */
    public List<JsonWeather> parseJsonStream(InputStream inputStream)
    		throws IOException {
        // TODONE -- you fill in here.
        JsonReader reader = new JsonReader(
        		new InputStreamReader(inputStream, "UTF-8"));
        reader.setLenient(true);
        try {
			Log.d(TAG, "Starting parseJsonStreamResults().");
        	return parseJsonStreamResults(reader);
        } finally {
          reader.close();
        }
    }

    /**
     * Parse the Json results stream and convert it into a list of
     * JsonWeather objects.
     */
    public List<JsonWeather> parseJsonStreamResults(JsonReader reader)
    		throws IOException {
		List<JsonWeather> weather = null;
        try {
        	if (reader.peek() == JsonToken.END_DOCUMENT) {
    			Log.d(TAG, "END_DOCUMENT at beginning.");
        		return null;
        	}
        	
        	JsonToken firstValue = reader.peek();
			Log.d(TAG, "First token in results object is " + firstValue.toString());
    		weather = new ArrayList<JsonWeather>();
    		Log.d(TAG, "Launching parseJsonStreamSingle().");
        	weather.add(parseJsonStreamSingle(reader));
        	/**
        	reader.beginObject();
			Log.d(TAG, "root: BEGIN_OBJECT");
        	if (reader.peek() == JsonToken.END_OBJECT) {
    			Log.d(TAG, "END_OBJECT reached right after beginning.");
        		return null;
        	}
        	
        	JsonToken firstValue = reader.peek();
			Log.d(TAG, "First token in results object is " + firstValue.toString());
        	if (firstValue == JsonToken.BEGIN_OBJECT) {
        		weather = new ArrayList<JsonWeather>();
        		Log.d(TAG, "Launching parseJsonStreamSingle().");
            	weather.add(parseJsonStreamSingle(reader));
        	} else {
    			Log.d(TAG, "Multiple results...");
        		while (reader.hasNext()) {
                    String name = reader.nextName();
        			Log.d(TAG, "nextName is " + name);
            		if (name.equals(JsonWeather.list_JSON)) {
                		Log.d(TAG, "Launching parseJsonWeatherArray().");
            			weather = parseJsonWeatherArray(reader);
            		}
        		}
        	}
        	*/
        } finally {
			//Log.d(TAG, "root: END_OBJECT");
        	//reader.endObject();
        }
        return weather;
    }

    /**
     * Parse a single Json stream and convert it into a JsonWeather
     * object.
     */
    public JsonWeather parseJsonStreamSingle(JsonReader reader)
    		throws IOException {
        // TODONE -- you fill in here.
		Log.d(TAG, "Starting parseJsonWeather().");
    	return parseJsonWeather(reader);
    }

    /**
     * Parse a Json stream and convert it into a List of JsonWeather
     * objects.
     */
    public List<JsonWeather> parseJsonWeatherArray(JsonReader reader)
    		throws IOException {
        // TODONE -- you fill in here.
    	List<JsonWeather> weather = null;
        try {
			Log.d(TAG, "WeatherArray: BEGIN_ARRAY");
        	reader.beginArray();
        	weather = new ArrayList<JsonWeather>();
        	
			while (reader.hasNext()) {
				Log.d(TAG, "Beginning parseJsonWeather().");
	        	weather.add(parseJsonWeather(reader));
			}
        } finally {
			Log.d(TAG, "WeatherArray: END_ARRAY");
        	reader.endArray();
        }
        return weather;
    }

    /**
     * Parse a Json stream and return a JsonWeather object.
     */
    public JsonWeather parseJsonWeather(JsonReader reader) 
    		throws IOException {
        // TODONE -- you fill in here.
    	JsonWeather weather = null;
    	try {
			Log.d(TAG, "Weather: BEGIN_OBJECT");
    		reader.beginObject();
    		weather = new JsonWeather();
        	Log.d(TAG, "Next token is " + reader.peek().name());
    		while (reader.hasNext()) {
                String name = reader.nextName();
    			Log.d(TAG, "nextName: " + name);
                switch (name) {
                case JsonWeather.sys_JSON:
        			Log.d(TAG, "Starting parseSys().");
                	weather.setSys(parseSys(reader));
                	break;
                case JsonWeather.weather_JSON:
        			Log.d(TAG, "Starting parseWeathers().");
                	weather.setWeather(parseWeathers(reader));
                	break;
                case JsonWeather.base_JSON:
                	weather.setBase(reader.nextString());
                	break;
                case JsonWeather.main_JSON:
        			Log.d(TAG, "Starting parseMain().");
                	weather.setMain(parseMain(reader));
                	break;
                case JsonWeather.wind_JSON:
        			Log.d(TAG, "Starting parseWind().");
                	weather.setWind(parseWind(reader));
                	break;
                case JsonWeather.dt_JSON:
                	weather.setDt(reader.nextLong());
                	break;
                case JsonWeather.id_JSON:
                	weather.setId(reader.nextLong());
                	break;
                case JsonWeather.name_JSON:
                	weather.setName(reader.nextString());
                	break;
                case JsonWeather.cod_JSON:
                	weather.setCod(reader.nextLong());
                	break;
                default:
                	reader.skipValue();
                	Log.d(TAG, "Skipping field " + name + " as not used.");
                	break;
                }
    		}
    	} finally {
			Log.d(TAG, "Weather: END_OBJECT");
    		reader.endObject();
    	}
    	return weather;
    }
    
    /**
     * Parse a Json stream and return a List of Weather objects.
     */
    public List<Weather> parseWeathers(JsonReader reader)
    		throws IOException {
        // TODONE -- you fill in here.
    	List<Weather> weathers = null;
    	try {
			Log.d(TAG, "weathers: BEGIN_ARRAY");
    		reader.beginArray();
    		weathers = new ArrayList<Weather>();
    		
    		while (reader.hasNext()) {
    			Log.d(TAG, "hasNext weather");
    			weathers.add(parseWeather(reader));
    		}
    	} finally {
			Log.d(TAG, "weathers: END_ARRAY");
    		reader.endArray();
    	}
    	return weathers;
    }

    /**
     * Parse a Json stream and return a Weather object.
     */
    public Weather parseWeather(JsonReader reader)
    		throws IOException {
        // TODONE -- you fill in here.
    	Weather weather = null;
    	try {
			Log.d(TAG, "weather: BEGIN_OBJECT");
    		reader.beginObject();
    		weather = new Weather();
    		
    		while (reader.hasNext()) {
                String name = reader.nextName();
    			Log.d(TAG, "nextName: " + name);
                switch (name) {
                case Weather.id_JSON:
                	weather.setId(reader.nextLong());
                	break;
                case Weather.main_JSON:
                	weather.setMain(reader.nextString());
                	break;
                case Weather.description_JSON:
                	weather.setDescription(reader.nextString());
                	break;
                case Weather.icon_JSON:
                	weather.setIcon(reader.nextString());
                	break;
                default:
                	reader.skipValue();
                    Log.d(TAG, "Encountered unknown field named " + name);
                	break;
                }
    		}
    	} finally {
			Log.d(TAG, "weather: END_OBJECT");
    		reader.endObject();
    	}
    	return weather;
    }
    
    /**
     * Parse a Json stream and return a Main Object.
     */
    public Main parseMain(JsonReader reader) 
    		throws IOException {
        // TODONE -- you fill in here.
    	Main main = null;
    	try {
			Log.d(TAG, "main: BEGIN_OBJECT");
    		reader.beginObject();
    		main = new Main();
    		
    		while (reader.hasNext()) {
                String name = reader.nextName();
    			Log.d(TAG, "nextName: " + name);
                switch (name) {
                case Main.temp_JSON:
                	main.setTemp(reader.nextDouble());
                	break;
                case Main.tempMin_JSON:
                	main.setTempMin(reader.nextDouble());
                	break;
                case Main.tempMax_JSON:
                	main.setTempMax(reader.nextDouble());
                	break;
                case Main.pressure_JSON:
                	main.setPressure(reader.nextDouble());
                	break;
                case Main.seaLevel_JSON:
                	main.setSeaLevel(reader.nextDouble());
                	break;
                case Main.grndLevel_JSON:
                	main.setGrndLevel(reader.nextDouble());
                	break;
                case Main.humidity_JSON:
                	main.setHumidity(reader.nextLong());
                	break;
                default:
                	reader.skipValue();
                    Log.d(TAG, "Encountered unknown field named " + name);
                	break;
                }
    		}
    	} finally {
			Log.d(TAG, "main: END_OBJECT");
    		reader.endObject();
    	}
    	return main;
    }

    /**
     * Parse a Json stream and return a Wind Object.
     */
    public Wind parseWind(JsonReader reader)
    		throws IOException {
        // TODONE -- you fill in here.
    	Wind wind = null;
    	try {
			Log.d(TAG, "wind: BEGIN_OBJECT");
    		reader.beginObject();
    		wind = new Wind();
    		
    		while (reader.hasNext()) {
                String name = reader.nextName();
    			Log.d(TAG, "nextName: " + name);
                switch (name) {
                case Wind.speed_JSON:
                	wind.setSpeed(reader.nextDouble());
                	break;
                case Wind.deg_JSON:
                	wind.setDeg(reader.nextDouble());
                	break;
                default:
                	reader.skipValue();
                    Log.d(TAG, "Encountered unknown field named " + name);
                	break;
                }
    		}
    	} finally {
			Log.d(TAG, "wind: END_OBJECT");
    		reader.endObject();
    	}
    	return wind;
    }

    /**
     * Parse a Json stream and return a Sys Object.
     */
    public Sys parseSys(JsonReader reader)
    		throws IOException {
        // TODONE -- you fill in here.
    	Sys sys = null;
    	try {
			Log.d(TAG, "sys: BEGIN_OBJECT");
    		reader.beginObject();
    		sys = new Sys();
    		
    		while (reader.hasNext()) {
                String name = reader.nextName();
    			Log.d(TAG, "nextName: " + name);
                switch (name) {
                case Sys.message_JSON:
                	sys.setMessage(reader.nextDouble());
                	break;
                case Sys.country_JSON:
                	sys.setCountry(reader.nextString());
                	break;
                case Sys.sunrise_JSON:
                	sys.setSunrise(reader.nextLong());
                	break;
                case Sys.sunset_JSON:
                	sys.setSunset(reader.nextLong());
                	break;
                default:
                	reader.skipValue();
                    Log.d(TAG, "Encountered unknown field named " + name);
                	break;
                }
    		}
    	} finally {
			Log.d(TAG, "sys: END_OBJECT");
    		reader.endObject();
    	}
    	return sys;
    }
}
