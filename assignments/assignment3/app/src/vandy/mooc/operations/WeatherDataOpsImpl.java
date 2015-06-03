package vandy.mooc.operations;

import java.lang.ref.WeakReference;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import vandy.mooc.R;
import vandy.mooc.activities.MainActivity;
import vandy.mooc.aidl.WeatherCall;
import vandy.mooc.aidl.WeatherData;
import vandy.mooc.aidl.WeatherRequest;
import vandy.mooc.aidl.WeatherResults;
import vandy.mooc.services.WeatherServiceAsync;
import vandy.mooc.services.WeatherServiceSync;
import vandy.mooc.utils.GenericServiceConnection;
import vandy.mooc.utils.Utils;
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

	public WeatherDataOpsImpl(MainActivity activity) {
		mActivity = new WeakReference<>(activity);
		initializeViewFields();
		initializeNonViewFields();
	}

    /**
     * Initialize the View fields, which are all stored as
     * WeakReferences to enable garbage collection.
     */
    private void initializeViewFields() {
        // Get references to the UI components.
        mActivity.get().setContentView(R.layout.main_activity);

        // Store the EditText that holds the urls entered by the user
        // (if any).
        mEditText = new WeakReference<>
            ((EditText) mActivity.get().findViewById(R.id.locationName));

        // Store the ListView for displaying the results entered.
        mListView = new WeakReference<>
            ((ListView) mActivity.get().findViewById(R.id.weatherResults));

        // Create a local instance of our custom Adapter for our
        // ListView.
        mAdapter = new WeakReference<>
            (new WeatherDataArrayAdapter(mActivity.get()));

        // Set the adapter to the ListView.
        mListView.get().setAdapter(mAdapter.get());

        // Display results if any (due to runtime configuration change).
        if (mResults != null)
            displayResults(mResults);
    }

    /**
     * (Re)initialize the non-view fields (e.g.,
     * GenericServiceConnection objects).
     */
    private void initializeNonViewFields() {
        mServiceConnectionSync = 
            new GenericServiceConnection<WeatherCall>(WeatherCall.class);

        mServiceConnectionAsync =
            new GenericServiceConnection<WeatherRequest>(WeatherRequest.class);
    }

	public void bindService() {
        Log.d(TAG, "calling bindService()");

        // Launch the Weather Bound Services if they aren't already
        // running via a call to bindService(), which binds this
        // activity to the WeatherService* if they aren't already
        // bound.
        if (mServiceConnectionSync.getInterface() == null) 
            mActivity.get().getApplicationContext().bindService
                (WeatherServiceSync.makeIntent(mActivity.get()),
                 mServiceConnectionSync,
                 Context.BIND_AUTO_CREATE);

        if (mServiceConnectionAsync.getInterface() == null) 
            mActivity.get().getApplicationContext().bindService
                (WeatherServiceAsync.makeIntent(mActivity.get()),
                 mServiceConnectionAsync,
                 Context.BIND_AUTO_CREATE);
	}
	
	public void unbindService() {
        if (mActivity.get().isChangingConfigurations()) 
            Log.d(TAG,
                  "just a configuration change - unbindService() not called");
        else {
            Log.d(TAG,
                  "calling unbindService()");

            // Unbind the Async Service if it is connected.
            if (mServiceConnectionAsync.getInterface() != null)
                mActivity.get().getApplicationContext().unbindService
                    (mServiceConnectionAsync);

            // Unbind the Sync Service if it is connected.
            if (mServiceConnectionSync.getInterface() != null)
                mActivity.get().getApplicationContext().unbindService
                    (mServiceConnectionSync);
        }
	}
	
	public void getWeatherSync(View v) {
        final WeatherCall weatherCall = 
                mServiceConnectionSync.getInterface();

            if (weatherCall != null) {
                // Get the acronym entered by the user.
                final String location =
                    mEditText.get().getText().toString();

                resetDisplay();

                // Use an anonymous AsyncTask to download the Acronym data
                // in a separate thread and then display any results in
                // the UI thread.
                new AsyncTask<String, Void, List<WeatherData>> () {
                    /**
                     * Location for which we are trying to get the weather.
                     */
                    private String mLocation;

                    /**
                     * Retrieve the expanded acronym results via a
                     * synchronous two-way method call, which runs in a
                     * background thread to avoid blocking the UI thread.
                     */
                    protected List<WeatherData> doInBackground(String... locations) {
                        try {
                            mLocation = locations[0];
                            return weatherCall.getCurrentWeather(mLocation);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    /**
                     * Display the results in the UI Thread.
                     */
                    protected void onPostExecute(List<WeatherData> weatherList) {
                        if (weatherList.size() > 0)
                            displayResults(weatherList);
                        else 
                            Utils.showShortToast(mActivity.get(),
                                            "Unable to retreive the weather for "
                                            + mLocation);
                    }
                    // Execute the AsyncTask to expand the acronym without
                    // blocking the caller.
                }.execute(location);
            } else {
                Log.d(TAG, "mWeatherCall was null.");
            }
	}
	
	public void getWeatherAsync(View v) {
        WeatherRequest weatherRequest = 
                mServiceConnectionAsync.getInterface();

            if (weatherRequest != null) {
                // Get the location entered by the user.
                final String location =
                    mEditText.get().getText().toString();

                resetDisplay();

                try {
                    // Invoke a one-way AIDL call, which does not block
                    // the client.  The results are returned via the
                    // sendResults() method of the mWeatherResults
                    // callback object, which runs in a Thread from the
                    // Thread pool managed by the Binder framework.
                	weatherRequest.getCurrentWeather(location,
                                                 mWeatherResults);
                } catch (RemoteException e) {
                    Log.e(TAG,
                          "RemoteException:" 
                          + e.getMessage());
                }
            } else {
                Log.d(TAG, "weatherRequest was null.");
            }
	}
	
	public void onConfigurationChange(MainActivity activity) {
        Log.d(TAG,
                "onConfigurationChange() called");

          // Reset the mActivity WeakReference.
          mActivity = new WeakReference<>(activity);

          // (Re)initialize all the View fields.
          initializeViewFields();
	}

    /**
     * The implementation of the WeatherResults AIDL Interface, which
     * will be passed to the Weather Web service using the
     * WeatherRequest.getCurrentWeather() method.
     * 
     * This implementation of WeatherResults.Stub plays the role of
     * Invoker in the Broker Pattern since it dispatches the upcall to
     * sendResults().
     */
    private WeatherResults.Stub mWeatherResults = new WeatherResults.Stub() {
            /**
             * This method is invoked by the WeatherServiceAsync to
             * return the results back to the MainActivity.
             */
            @Override
            public void sendResults(final List<WeatherData> weatherResults)
                throws RemoteException {
                // Since the Android Binder framework dispatches this
                // method in a background Thread we need to explicitly
                // post a runnable containing the results to the UI
                // Thread, where it's displayed.
                mActivity.get().runOnUiThread(new Runnable() {
                        public void run() {
                            final String location = 
                            		mEditText.get().getText().toString();
                        	if (weatherResults.size() > 0)
                            	displayResults(weatherResults);
                            else 
                                Utils.showShortToast(mActivity.get(),
                                		"Unable to retreive the weather for "
                        				+ location);
                        }
                    });
            }
	};

    /**
     * Display the results to the screen.
     * 
     * @param results
     *            List of Results to be displayed.
     */
    private void displayResults(List<WeatherData> results) {
        mResults = results;

        // Set/change data set.
        mAdapter.get().clear();
        mAdapter.get().addAll(mResults);
        mAdapter.get().notifyDataSetChanged();
    }

    /**
     * Reset the display prior to attempting to get the 
     * weather for a new location.
     */
    private void resetDisplay() {
        Utils.hideKeyboard(mActivity.get(),
                           mEditText.get().getWindowToken());
        mResults = null;
        mAdapter.get().clear();
        mAdapter.get().notifyDataSetChanged();
    }
}
