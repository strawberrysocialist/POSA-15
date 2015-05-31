package vandy.mooc.operations;

import java.lang.ref.WeakReference;
import java.util.List;

import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import vandy.mooc.activities.MainActivity;
import vandy.mooc.aidl.WeatherCall;
import vandy.mooc.aidl.WeatherData;
import vandy.mooc.aidl.WeatherRequest;
import vandy.mooc.utils.GenericServiceConnection;
import vandy.mooc.utils.WeatherDataArrayAdapter;

public class WeatherDataOpsImpl implements WeatherDataOps {
    /**
     * Debugging tag used by the Android logger.
     */
    protected final String TAG = getClass().getSimpleName();

    /**
     * Used to enable garbage collection.
     */
    protected WeakReference<MainActivity> mActivity;
    	
    /**
     * The ListView that will display the results to the user.
     */
    protected WeakReference<ListView> mListView;

    /**
     * Acronym entered by the user.
     */
    protected WeakReference<EditText> mEditText;

    /**
     * List of results to display (if any).
     */
    protected List<WeatherData> mResults;

    /**
     * A custom ArrayAdapter used to display the list of AcronymData
     * objects.
     */
    protected WeakReference<WeatherDataArrayAdapter> mAdapter;

    /**
     * This GenericServiceConnection is used to receive results after
     * binding to the AcronymServiceSync Service using bindService().
     */
    private GenericServiceConnection<WeatherCall> mServiceConnectionSync;

    /**
     * This GenericServiceConnection is used to receive results after
     * binding to the AcronymServiceAsync Service using bindService().
     */
    private GenericServiceConnection<WeatherRequest> mServiceConnectionAsync;

	public WeatherDataOpsImpl() {
		// TODO Auto-generated constructor stub
	}
	

	@Override
	public void bindService() {
		// TODO Auto-generated method stub
	}
	

	@Override
	public void unbindService() {
		// TODO Auto-generated method stub
	}
	

	@Override
	public void expandAcronymSync(View v) {
		// TODO Auto-generated method stub
	}
	

	@Override
	public void expandAcronymAsync(View v) {
		// TODO Auto-generated method stub
	}
	

	@Override
	public void onConfigurationChange(MainActivity activity) {
		// TODO Auto-generated method stub
	}
}
