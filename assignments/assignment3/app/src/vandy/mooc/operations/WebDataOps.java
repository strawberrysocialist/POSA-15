package vandy.mooc.operations;

import vandy.mooc.activities.MainActivity;
import vandy.mooc.services.DownloadService;
import vandy.mooc.services.ServiceStrategy;
import vandy.mooc.services.ServiceStrategy.DownloadStrategy;

/**
 * This class defines all the image-related operations.  It plays the
 * role of the "Abstraction" in Bridge pattern.
 */
public class WebDataOps {
    /**
     * Reference to the designed Concrete Implementor (i.e., either
     * ImageOpsBoundService or ImageOpsStartedService).
     */
    @SuppressWarnings("unused")
	private DownloadService mDownloadService;
    
    /**
     * Constructor will choose either the Started Service or Bound
     * Service implementation of ImageOps.
     */
    public WebDataOps(MainActivity activity) {
    	this(activity, DownloadStrategy.BOUND_ASYNC);
    }

    /**
     * Constructor will choose either the Started Service or Bound
     * Service implementation of ImageOps.
     */
    public WebDataOps(MainActivity activity,
					DownloadStrategy downloadStrategy) {
    	mDownloadService = new ServiceStrategy()
        		.getDownloadService(downloadStrategy);
    }

    /**
     * Called by the ImageOps constructor and after a runtime
     * configuration change occurs to finish the initialization steps.
     */
    public void onConfigurationChange(MainActivity activity) {
        // TO-DO
    }
}
