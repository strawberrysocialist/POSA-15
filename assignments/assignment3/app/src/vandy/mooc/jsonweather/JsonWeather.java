package vandy.mooc.jsonweather;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

/**
 * This "Plain Ol' Java Object" (POJO) class represents data of
 * interest downloaded in Json from the Weather Service.  We don't
 * care about all the data, just the fields defined in this class.
 */
public class JsonWeather {
    /**
     * Used for logging purposes.
     */
    private final String TAG = this.getClass().getCanonicalName();

    /**
     * Various tags corresponding to data downloaded in Json from the
     * Weather Service.
     */
    final public static String list_JSON = "list";
    final public static String cod_JSON = "cod";
    final public static String name_JSON = "name";
    final public static String id_JSON = "id";
    final public static String dt_JSON = "dt";
    final public static String wind_JSON = "wind";
    final public static String main_JSON = "main";
    final public static String base_JSON = "base";
    final public static String weather_JSON = "weather";
    final public static String sys_JSON = "sys";

    /**
     * Various fields corresponding to data downloaded in Json from
     * the Weather Service.
     */
    private Sys mSys;
    private String mBase;
    private Main mMain;
    private List<Weather> mWeather = new ArrayList<Weather>();
    private Wind mWind;
    private long mDt;
    private long mId;
    private String mName;
    private long mCod;

    /**
     * Constructor that initializes all the fields of interest.
     */
    public JsonWeather(Sys sys,
                       String base,
                       Main main,
                       List<Weather> weather,
                       Wind wind,
                       long dt,
                       long id,
                       String name,
                       long cod) {
        mSys = sys;
        mBase = base;
        mMain = main;
        mWeather = weather;
        mWind = wind;
        mDt = dt;
        mId = id;
        mName = name;
        mCod = cod;
    }

    /**
     * No-op constructor
     */
    public JsonWeather() {
    }

    /**
     * @return The sys
     */
    public Sys getSys() {
        return mSys;
    }

    /**
     * @param sys
     *            The sys
     */
    public void setSys(Sys sys) {
        mSys = sys;
    }

    /**
     * @return The base
     */
    public String getBase() {
        return mBase;
    }

    /**
     * @param base
     *            The base
     */
    public void setBase(String base) {
		Log.d(TAG, "setBase=" + base);
        mBase = base;
    }

    /**
     * @return The main
     */
    public Main getMain() {
        return mMain;
    }

    /**
     * @param main
     *            The main
     */
    public void setMain(Main main) {
        mMain = main;
    }

    
    /**
     * 
     * @return The weather
     */
    public List<Weather> getWeather() {
        return mWeather;
    }

    /**
     * 
     * @param weather
     *            The weather
     */
    public void setWeather(List<Weather> weather) {
        mWeather = weather;
    }


    /**
     * @return The wind
     */
    public Wind getWind() {
        return mWind;
    }

    /**
     * 
     * @param wind
     *            The wind
     */
    public void setWind(Wind wind) {
        mWind = wind;
    }

    /**
     * @return The dt
     */
    public long getDt() {
        return mDt;
    }

    /**
     * @param dt
     *            The dt
     */
    public void setDt(long dt) {
		Log.d(TAG, "setDt=" + dt);
        mDt = dt;
    }

    /**
     * @return The id
     */
    public long getId() {
        return mId;
    }

    /**
     * @param id
     *            The id
     */
    public void setId(long id) {
		Log.d(TAG, "setId=" + id);
        mId = id;
    }

    /**
     * @return The name
     */
    public String getName() {
        return mName;
    }

    /**
     * @param name
     *            The name
     */
    public void setName(String name) {
		Log.d(TAG, "setName=" + name);
        mName = name;
    }

    /**
     * @return The cod
     */
    public long getCod() {
        return mCod;
    }

    /**
     * @param cod
     *            The cod
     */
    public void setCod(long cod) {
		Log.d(TAG, "setCod=" + cod);
        mCod = cod;
    }
    
    public double getWindSpeed() {
    	return mWind == null
    			? 0
				: mWind.getSpeed();
    }
    
    public double getWindDirection() {
    	return mWind == null
    			? 0
				: mWind.getDeg();
    }
    
    public long getHumidity() {
    	return mMain == null
    			? 0
				: mMain.getHumidity();
    }
    
    public long getSunrise() {
    	return mSys == null
    			? 0
				: mSys.getSunrise();
    }
    
    public long getSunset() {
    	return mSys == null
    			? 0
				: mSys.getSunset();
    }
    
    public double getTemp() {
    	return mMain == null
    			? 0
				: mMain.getTemp();
    }
}
