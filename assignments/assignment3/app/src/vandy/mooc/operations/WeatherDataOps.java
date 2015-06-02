package vandy.mooc.operations;

import vandy.mooc.activities.MainActivity;
import android.view.View;

/**
 * This class defines all the weather-related operations.
 */
public interface WeatherDataOps {
    /**
     * Initiate the service binding protocol.
     */
    public void bindService();

    /**
     * Initiate the service unbinding protocol.
     */
    public void unbindService();

    /*
     * Initiate the synchronous weather retrieval when the user presses
     * the "Look Up Sync" button.
     */
    public void getWeatherSync(View v);

    /*
     * Initiate the asynchronous weather retrieval when the user presses
     * the "Look Up Async" button.
     */
    public void getWeatherAsync(View v);

    /**
     * Called after a runtime configuration change occurs to finish
     * the initialization steps.
     */
    public void onConfigurationChange(MainActivity activity);
}
