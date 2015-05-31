package vandy.mooc.services;

import java.util.List;

import vandy.mooc.aidl.WeatherCall;
import vandy.mooc.aidl.WeatherData;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

/**
 * @class WeatherDataServiceSync
 * 
 * @brief This class uses synchronous AIDL interactions to check
 *        the weather for a location via an Weather Data Web
 *        service.  The MainActivity that binds to this Service
 *        will receive an IBinder that's an instance of
 *        WeatherCall, which extends IBinder.  The Activity
 *        can then interact with this Service by making two-way
 *        method calls on the WeatherCall object asking this 
 *        Service to lookup the weather for a location, passing
 *        in the location string.  After the lookup is finished,
 *        this Service finishes and sends the Weather results
 *        back to the Activity directly.
 * 
 *        AIDL is an example of the Broker Pattern, in which all
 *        interprocess communication details are hidden behind the
 *        AIDL interfaces.
 */
public class WeatherDataServiceSync extends LifecycleLoggingService {
    /**
     * Factory method that makes an Intent used to start the
     * WeatherDataServiceSync when passed to bindService().
     * 
     * @param context
     *            The context of the calling component.
     */
    public static Intent makeIntent(Context context) {
        return new Intent(context,
        		WeatherDataServiceSync.class);
    }

    /**
     * Called when a client (e.g., MainActivity) calls
     * bindService() with the proper Intent.  Returns the
     * implementation of WeatherCall, which is implicitly cast as
     * an IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mWeatherCallImpl;
    }

    /**
     * The concrete implementation of the AIDL Interface
     * WeatherCall, which extends the Stub class that implements
     * WeatherCall, thereby allowing Android to handle calls across
     * process boundaries.  This method runs in a separate Thread as
     * part of the Android Binder framework.
     * 
     * This implementation plays the role of Invoker in the Broker
     * Pattern.
     */
    WeatherCall.Stub mWeatherCallImpl = new WeatherCall.Stub() {
        /**
         * Implement the AIDL WeatherCall getCurrentWeather()
         * method, which forwards to DownloadUtils getResults() to
         * obtain the results from the Weather Web service and
         * then sends the results back to the Activity.
         */
		@Override
		public List<WeatherData> getCurrentWeather(String Weather)
				throws RemoteException {
			// TODO Auto-generated method stub
			return null;
		}
	};
}
