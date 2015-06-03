package vandy.mooc.activities;

import vandy.mooc.R;
import vandy.mooc.operations.WeatherDataOps;
import vandy.mooc.operations.WeatherDataOpsImpl;
import vandy.mooc.utils.RetainedFragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends LifecycleLoggingActivity {

    /**
     * Used to retain the ImageOps state between runtime configuration
     * changes.
     */
    protected final RetainedFragmentManager mRetainedFragmentManager = 
        new RetainedFragmentManager(this.getFragmentManager(),
                                    TAG);

    /**
     * Provides image-related operations.
     */
    private WeatherDataOps mWeatherDataOps;

    /**
     * Hook method called when a new instance of Activity is created.
     * One time initialization code goes here, e.g., UI layout
     * initialization and runtime configuration changes.
     *
     * @param Bundle object that contains saved state information.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Always call super class for necessary
        // initialization/implementation.
        super.onCreate(savedInstanceState);

        // Set the default layout.
        setContentView(R.layout.main_activity);

        // Handle any configuration change.
        handleConfigurationChanges();
    }

    /**
     * Hook method called by Android when this Activity is
     * destroyed.
     */
    @Override
    protected void onDestroy() {
        // Unbind from the Service.
    	mWeatherDataOps.unbindService();

        // Always call super class for necessary operations when an
        // Activity is destroyed.
        super.onDestroy();
    }

    /**
     * Handle hardware reconfigurations, such as rotating the display.
     */
    protected void handleConfigurationChanges() {
        // If this method returns true then this is the first time the
        // Activity has been created.
        if (mRetainedFragmentManager.firstTimeIn()) {
            Log.d(TAG,
                  "First time onCreate() call");

            // Create the ImageOps object one time.
            mWeatherDataOps = new WeatherDataOpsImpl(this);

            // Store the ImageOps into the RetainedFragmentManager.
            mRetainedFragmentManager.put("IMAGE_OPS_STATE",
                                         mWeatherDataOps);
            
            // Initiate the service binding protocol (which may be a
            // no-op, depending on which type of DownloadImages*Service is
            // used).
            mWeatherDataOps.bindService();
        } else {
            Log.d(TAG,
                  "Second or subsequent onCreate() call");

            // The RetainedFragmentManager was previously initialized,
            // which means that a runtime configuration change
            // occurred, so obtain the ImageOps object and inform it
            // that the runtime configuration change has completed.
            mWeatherDataOps = 
                mRetainedFragmentManager.get("IMAGE_OPS_STATE");

            // This check shouldn't be necessary under normal
            // circumstances, but it's better to lose state than to
            // crash!
            if (mWeatherDataOps == null) {
                // Create the ImageOps object one time.
                mWeatherDataOps = new WeatherDataOpsImpl(this);

                // Store the ImageOps into the
                // RetainedFragmentManager.
                mRetainedFragmentManager.put("IMAGE_OPS_STATE",
                                             mWeatherDataOps);
                
                // Initiate the service binding protocol (which may be a
                // no-op, depending on which type of DownloadImages*Service is
                // used).
                mWeatherDataOps.bindService();
            }            
            else 
                // Inform it that the runtime configuration change has
                // completed.
                mWeatherDataOps.onConfigurationChange(this);
        }
    }
    
    /*
     * Initiate the synchronous weather lookup when the user presses
     * the "Look Up Sync" button.
     */
    public void getWeatherSync(View v) {
    	mWeatherDataOps.getWeatherSync(v);
    }

    /*
     * Initiate the asynchronous weather lookup when the user presses
     * the "Look Up Async" button.
     */
    public void getWeatherAsync(View v) {
    	mWeatherDataOps.getWeatherAsync(v);
    }
}
