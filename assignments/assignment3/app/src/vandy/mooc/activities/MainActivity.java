package vandy.mooc.activities;

import vandy.mooc.R;
import vandy.mooc.operations.WebDataOps;
import vandy.mooc.utils.RetainedFragmentManager;
import android.os.Bundle;
import android.util.Log;

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
    private WebDataOps mWebDataOps;

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
     * Handle hardware reconfigurations, such as rotating the display.
     */
    protected void handleConfigurationChanges() {
        // If this method returns true then this is the first time the
        // Activity has been created.
        if (mRetainedFragmentManager.firstTimeIn()) {
            Log.d(TAG,
                  "First time onCreate() call");

            // Create the ImageOps object one time.
            mWebDataOps = new WebDataOps(this);

            // Store the ImageOps into the RetainedFragmentManager.
            mRetainedFragmentManager.put("IMAGE_OPS_STATE",
                                         mWebDataOps);
            
        } else {
            Log.d(TAG,
                  "Second or subsequent onCreate() call");

            // The RetainedFragmentManager was previously initialized,
            // which means that a runtime configuration change
            // occured, so obtain the ImageOps object and inform it
            // that the runtime configuration change has completed.
            mWebDataOps = 
                mRetainedFragmentManager.get("IMAGE_OPS_STATE");

            // This check shouldn't be necessary under normal
            // circumtances, but it's better to lose state than to
            // crash!
            if (mWebDataOps == null) {
                // Create the ImageOps object one time.
                mWebDataOps = new WebDataOps(this);

                // Store the ImageOps into the
                // RetainedFragmentManager.
                mRetainedFragmentManager.put("IMAGE_OPS_STATE",
                                             mWebDataOps);
            }            
            else 
                // Inform it that the runtime configuration change has
                // completed.
                mWebDataOps.onConfigurationChange(this);
        }
    }
}
