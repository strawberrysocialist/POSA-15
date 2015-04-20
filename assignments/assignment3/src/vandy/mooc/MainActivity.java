package vandy.mooc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.Toast;

/**
 * A main Activity that prompts the user for a URL to an image and
 * then uses Intents and other Activities to download the image and
 * view it.
 */
public class MainActivity extends Activity {
    /**
     * Debugging tag used by the Android logger.
     */
    private final String TAG = getClass().getSimpleName();

    /**
     * A value that uniquely identifies the request to download an
     * image.
     */
    private static final int DOWNLOAD_IMAGE_REQUEST = 1;

    /**
     * EditText field for entering the desired URL to an image.
     */
    private EditText mUrlEditText;

    /**
     * URL for the image that's downloaded by default if the user
     * doesn't specify otherwise.
     */
    private Uri mDefaultUrl =
        Uri.parse("http://www.dre.vanderbilt.edu/~schmidt/robot.png");

    /**
     * Hook method called when a new instance of Activity is created.
     * One time initialization code goes here, e.g., UI layout and
     * some class scope variable initialization.
     *
     * @param savedInstanceState object that contains saved state information.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Always call super class for necessary
        // initialization/implementation.
        // @@ FIXED -- you fill in here.
        super.onCreate(savedInstanceState);

		
        if(savedInstanceState != null) {
            // The activity is being re-created. Use the
            // savedInstanceState bundle for initializations either
            // during onCreate or onRestoreInstanceState().
            Log.d(TAG,
                  "onCreate(): activity re-created from savedInstanceState");
						
        } else {
            // Activity is being created anew.  No prior saved
            // instance state information available in Bundle object.
            Log.d(TAG,
                  "onCreate(): activity created anew");
        }

        // Set the default layout.
        // @@ FIXED -- you fill in here.
        this.setContentView(R.layout.main_activity);

        // Cache the EditText that holds the urls entered by the user
        // (if any).
        // @@ FIXED -- you fill in here.
        mUrlEditText = (EditText) this.findViewById(R.id.url);
    }

    /**
     * Hook method called after onCreate() or after onRestart() (when
     * the activity is being restarted from stopped state).  Should
     * re-acquire resources relinquished when activity was stopped
     * (onStop()) or acquire those resources for the first time after
     * onCreate().
     */	
    @Override
    protected void onStart(){
        super.onStart();
        Log.d(TAG,
                "onStart(): starting activity");
    }
	
    /**
     * Hook method called after onRestoreStateInstance(Bundle) only if
     * there is a prior saved instance state in Bundle object.
     * onResume() is called immediately after onStart().  onResume()
     * is called when user resumes activity from paused state
     * (onPause()) User can begin interacting with activity.  Place to
     * start animations, acquire exclusive resources, such as the
     * camera.
     */
    @Override
    protected void onResume(){
        super.onResume();
        Log.d(TAG,
                "onResume(): resuming activity");
    }
	
    /**
     * Hook method called when an Activity loses focus but is still
     * visible in background. May be followed by onStop() or
     * onResume().  Delegate more CPU intensive operation to onStop
     * for seamless transition to next activity.  Save persistent
     * state (onSaveInstanceState()) in case app is killed.  Often
     * used to release exclusive resources.
     */
    @Override
    protected void onPause(){
        super.onPause();
        Log.d(TAG,
                "onPause(): suspending activity");
    }
	
    /**
     * Called when Activity is no longer visible.  Release resources
     * that may cause memory leak. Save instance state
     * (onSaveInstanceState()) in case activity is killed.
     */
    @Override
    protected void onStop(){
        super.onStop();
        Log.d(TAG,
                "onStop(): stopping activity");
    }
	
    /**
     * Hook method called when user restarts a stopped activity.  Is
     * followed by a call to onStart() and onResume().
     */	
    @Override
    protected void onRestart(){
        super.onRestart();
        Log.d(TAG,
                "onRestart(): restarting activity");
    }
	
    /**
     * Hook method that gives a final chance to release resources and
     * stop spawned threads.  onDestroy() may not always be
     * called-when system kills hosting process
     */
    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d(TAG,
                "onDestroy(): destroying activity");
    }

    /**
     * Called by the Android Activity framework when the user clicks
     * the "Download Image" button.
     *
     * @param view The view.
     */
    public void downloadImage(View view) {
        Log.d(TAG, "Entering downloadImage");
        try {
            // Hide the keyboard.
            hideKeyboard(this,
                         mUrlEditText.getWindowToken());

            // Call the makeDownloadImageIntent() factory method to
            // create a new Intent to an Activity that can download an
            // image from the URL given by the user.  In this case
            // it's an Intent that's implemented by the
            // DownloadImageActivity.
            // @@ FIXED - you fill in here.
            Intent downloadImage = null;
            Uri url = this.getUrl();
            if (null != url) {
                downloadImage = this.makeDownloadImageIntent(url);
            }

            // Start the Activity associated with the Intent, which
            // will download the image and then return the Uri for the
            // downloaded image file via the onActivityResult() hook
            // method.
            // @@ FIXED -- you fill in here.
            if (null != downloadImage) {
                this.startActivityForResult(downloadImage, DOWNLOAD_IMAGE_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Hook method called back by the Android Activity framework when
     * an Activity that's been launched exits, giving the requestCode
     * it was started with, the resultCode it returned, and any
     * additional data from it.
     */
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {
        // Check if the started Activity completed successfully.
        // @@ FIXED -- you fill in here, replacing true with the right
        // code.
        if (resultCode == RESULT_OK) {
            // Check if the request code is what we're expecting.
            // @@ FIXED -- you fill in here, replacing true with the
            // right code.
            if (requestCode == DOWNLOAD_IMAGE_REQUEST) {
                // Call the makeGalleryIntent() factory method to
                // create an Intent that will launch the "Gallery" app
                // by passing in the path to the downloaded image
                // file.
                // @@ FIXED -- you fill in here.
                Intent showImage = this.makeGalleryIntent(data.getDataString());

                // Start the Gallery Activity.
                // @@ FIXED -- you fill in here.
                if (null != showImage) {
                    Log.d(TAG, "Displaying image at " + data.getDataString());
                    this.startActivity(showImage);
                }
                else {
                    Toast.makeText(this, 
                                "Unable to start a Photo Gallery app.",  
                                Toast.LENGTH_SHORT);
                }
            }
        }
        // Check if the started Activity did not complete successfully
        // and inform the user a problem occurred when trying to
        // download contents at the given URL.
        // @@ FIXED -- you fill in here, replacing true with the right
        // code.
        else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, 
                            "Unable to download the desired contents.", 
                            Toast.LENGTH_SHORT).show();
        }
    }    

    /**
     * Factory method that returns an implicit Intent for viewing the
     * downloaded image in the Gallery app.
     */
    private Intent makeGalleryIntent(String pathToImageFile) {
        // Create an intent that will start the Gallery app to view
        // the image.
        // FIXED -- you fill in here, replacing "null" with the proper
        // code.
        Log.d(TAG, "Preparing to show image at " + pathToImageFile);
        Intent showImage = new Intent();
        showImage.setAction(Intent.ACTION_VIEW);
        //showImage.addCategory(Intent.CATEGORY_APP_GALLERY);
        showImage.setDataAndType(Uri.parse("file://" + pathToImageFile), "image/*");
        return showImage;
    }

    /**
     * Factory method that returns an implicit Intent for downloading
     * an image.
     */
    private Intent makeDownloadImageIntent(Uri url) {
        // Create an intent that will download the image from the web.
        // FIXED -- you fill in here, replacing "null" with the proper
        // code.
        //Intent downloadImage = new Intent(this.getApplicationContext(), DownloadImageActivity.class);
        Intent downloadImage = new Intent();
        downloadImage.setAction(Intent.ACTION_WEB_SEARCH);
        downloadImage.addCategory(Intent.CATEGORY_DEFAULT);
        downloadImage.setData(url);
        return downloadImage;
    }

    /**
     * Get the URL to download based on user input.
     */
    protected Uri getUrl() {
        Uri url = null;

        // Get the text the user typed in the edit text (if anything).
        url = Uri.parse(mUrlEditText.getText().toString());

        // If the user didn't provide a URL then use the default.
        String uri = url.toString();
        if (uri == null || uri.equals(""))
            url = mDefaultUrl;

        // Do a sanity check to ensure the URL is valid, popping up a
        // toast if the URL is invalid.
        // @@ FIXED -- you fill in here, replacing "true" with the
        // proper code.
        if (URLUtil.isValidUrl(url.toString()))
            return url;
        else {
            Toast.makeText(this,
                           "Invalid URL",
                           Toast.LENGTH_SHORT).show();
            return null;
        } 
    }

    /**
     * This method is used to hide a keyboard after a user has
     * finished typing the url.
     */
    public void hideKeyboard(Activity activity,
                             IBinder windowToken) {
        InputMethodManager mgr =
            (InputMethodManager) activity.getSystemService
            (Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(windowToken,
                                    0);
    }
}
