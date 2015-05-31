package vandy.mooc.services;

import vandy.mooc.operations.WebDataOps;
import vandy.mooc.services.DownloadService;
import vandy.mooc.services.DownloadServiceBoundAsync;
import vandy.mooc.utils.ActivityUtils;

public class ServiceStrategy {

    /**
     * Debugging tag used by the Android logger.
     */
    protected final String TAG = getClass().getSimpleName();

	private DownloadService mDownloadService = null;
	
	@SuppressWarnings("unused")
	private WebDataOps mCallback = null; 
	
    public enum DownloadStrategy {
    	STARTED,
    	INTENT,
    	BOUND_MESSAGE,
    	BOUND_SYNC,
    	BOUND_ASYNC
    }

	public ServiceStrategy() {
		//throw new AssertionError(TAG + " can not be instantiated.");
	}
    
	public DownloadService getDownloadService(DownloadStrategy downloadStrategy) {
		switch (downloadStrategy) {
		case STARTED:
	        throw new AssertionError(TAG +" " 
	        		+ ActivityUtils.toCapitalCase(downloadStrategy.toString())
	        		+ " not implemented yet.");
			// TODO Create concrete implementation.
			//break;
		case INTENT:
	        throw new AssertionError(TAG +" " 
	        		+ ActivityUtils.toCapitalCase(downloadStrategy.toString())
	        		+ " not implemented yet.");
			// TODO Create concrete implementation.
			//break;
		case BOUND_MESSAGE:
	        throw new AssertionError(TAG +" " 
	        		+ ActivityUtils.toCapitalCase(downloadStrategy.toString())
	        		+ " not implemented yet.");
			// TODO Create concrete implementation.
			//break;
		case BOUND_SYNC:
	        throw new AssertionError(TAG +" " 
	        		+ ActivityUtils.toCapitalCase(downloadStrategy.toString())
	        		+ " not implemented yet.");
			// TODO Create concrete implementation.
			//break;
		case BOUND_ASYNC:
			// TODO Create concrete implementation.
			mDownloadService = new DownloadServiceBoundAsync();
			break;
		}
		return mDownloadService;
	}
}
