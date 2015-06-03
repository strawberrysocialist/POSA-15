package vandy.mooc.jsonweather;

import android.util.Log;

/**
 * This "Plain Ol' Java Object" (POJO) class represents data related
 * to wind downloaded in Json from the Weather Service.
 */
public class Wind {
    /**
     * Used for logging purposes.
     */
    private final String TAG = this.getClass().getCanonicalName();

    /**
     * Various tags corresponding to wind data downloaded in Json from
     * the Weather Service.
     */
    public final static String deg_JSON = "deg";
    public final static String speed_JSON = "speed";

    /**
     * Various fields corresponding to wind data downloaded in Json
     * from the Weather Service.
     */
    private double mSpeed;
    private double mDeg;

    /**
     * @return The speed
     */
    public double getSpeed() {
        return mSpeed;
    }

    /**
     * @param speed
     *            The speed
     */
    public void setSpeed(double speed) {
		Log.d(TAG, "setSpeed=" + speed);
        mSpeed = speed;
    }

    /**
     * @return The deg
     */
    public double getDeg() {
        return mDeg;
    }

    /**
     * @param deg
     *            The deg
     */
    public void setDeg(double deg) {
		Log.d(TAG, "setDeg=" + deg);
        mDeg = deg;
    }
}
