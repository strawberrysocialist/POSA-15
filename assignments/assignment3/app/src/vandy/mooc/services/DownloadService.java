package vandy.mooc.services;

import vandy.mooc.activities.MainActivity;

public interface DownloadService {

    /**
     * Called by the ImageOps constructor and after a runtime
     * configuration change occurs to finish the initialization steps.
     */
	public void download();
	
    /**
     * Called by the ImageOps constructor and after a runtime
     * configuration change occurs to finish the initialization steps.
     */
	public void getStatus();
	
    /**
     * Called by the ImageOps constructor and after a runtime
     * configuration change occurs to finish the initialization steps.
     */
	public void getResult();
	
    /**
     * Called by the ImageOps constructor and after a runtime
     * configuration change occurs to finish the initialization steps.
     */
    public void onConfigurationChange(MainActivity activity);
}
