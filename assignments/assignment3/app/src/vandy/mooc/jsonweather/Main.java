package vandy.mooc.jsonweather;

import android.util.Log;

/**
 * This "Plain Ol' Java Object" (POJO) class represents data related
 * to temperature, pressure, and humidity downloaded in Json from the
 * Weather Service.
 */
public class Main {
    /**
     * Used for logging purposes.
     */
    private final String TAG = this.getClass().getCanonicalName();

    /**
     * Various tags corresponding to temperature, pressure, and
     * humidity data downloaded in Json from the Weather Service.
     */
    public final static String temp_JSON = "temp";
    public final static String tempMin_JSON = "temp_min";
    public final static String tempMax_JSON = "temp_max";
    public final static String pressure_JSON = "pressure";
    public final static String seaLevel_JSON = "sea_level";
    public final static String grndLevel_JSON = "grnd_level";
    public final static String humidity_JSON = "humidity";

    /**
     * Various fields corresponding to temperature, pressure, and
     * humidity data downloaded in Json from the Weather Service.
     */
    private double mTemp;
    private double mTempMin;
    private double mTempMax;
    private double mPressure;
    private double mSeaLevel;
    private double mGrndLevel;
    private long mHumidity;

    /**
     * @return The temperature
     */
    public double getTemp() {
        return mTemp;
    }

    /**
     * @param temp
     *            The temp
     */
    public void setTemp(double temp) {
		Log.d(TAG, "setTemp=" + temp);
        mTemp = temp;
    }

    /**
     * @return The tempMin
     */
    public double getTempMin() {
        return mTempMin;
    }

    /**
     * @param tempMin
     *            The temp_min
     */
    public void setTempMin(double tempMin) {
		Log.d(TAG, "setTempMin=" + tempMin);
        mTempMin = tempMin;
    }

    /**
     * @return The tempMax
     */
    public double getTempMax() {
        return mTempMax;
    }

    /**
     * @param tempMax
     *            The temp_max
     */
    public void setTempMax(double tempMax) {
		Log.d(TAG, "setTempMax=" + tempMax);
        mTempMax = tempMax;
    }

    /**
     * @return The pressure
     */
    public double getPressure() {
        return mPressure;
    }

    /**
     * @param pressure
     *            The pressure
     */
    public void setPressure(double pressure) {
		Log.d(TAG, "setPressure=" + pressure);
        mPressure = pressure;
    }

    /**
     * @return The seaLevel
     */
    public double getSeaLevel() {
        return mSeaLevel;
    }

    /**
     * @param seaLevel
     *            The sea_level
     */
    public void setSeaLevel(double seaLevel) {
		Log.d(TAG, "setSeaLevel=" + seaLevel);
        mSeaLevel = seaLevel;
    }

    /**
     * @return The grndLevel
     */
    public double getGrndLevel() {
        return mGrndLevel;
    }

    /**
     * @param grndLevel
     *            The grnd_level
     */
    public void setGrndLevel(double grndLevel) {
		Log.d(TAG, "setGrndLevel=" + grndLevel);
        mGrndLevel = grndLevel;
    }

    /**
     * @return The humidity
     */
    public long getHumidity() {
        return mHumidity;
    }

    /**
     * @param humidity
     *            The humidity
     */
    public void setHumidity(long humidity) {
		Log.d(TAG, "setHumidity=" + humidity);
        mHumidity = humidity;
    }
}
