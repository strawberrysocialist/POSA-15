package vandy.mooc;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;

/**
 * An Activity that downloads an image, stores it in a local file on
 * the local device, and returns a Uri to the image file.
 */
public class DownloadImageActivity extends Activity {
    /**
     * Debugging tag used by the Android logger.
     */
    private final String TAG = getClass().getSimpleName();

   // public static final String DATA_URL ="url";
    public static final String DATA_DOWNLOADED_FILE="data";

    private final Handler handler = new Handler();

    /**
     * Hook method called when a new instance of Activity is created.
     * One time initialization code goes here, e.g., UI layout and
     * some class scope variable initialization.
     *
     * @param savedInstanceState object that contains saved state information.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Always call super class for necessary
        // initialization/implementation.
        // @@ +TODO -- you fill in here.
        Log.d(TAG, "onCreate");
        super.onCreate( savedInstanceState);

        // Get the URL associated with the Intent data.
        // @@ +TODO -- you fill in here.
        Intent intent = getIntent();
        Uri url=intent.getData();

        Log.d(TAG, url.toString());

        // Download the image in the background, create an Intent that
        // contains the path to the image file, and set this as the
        // result of the Activity.

        // @@ TODO -- you fill in here using the Android "HaMeR"
        // concurrency framework.  Note that the finish() method
        // should be called in the UI thread, whereas the other
        // methods should be called in the background thread.  See
        // http://stackoverflow.com/questions/20412871/is-it-safe-to-finish-an-android-activity-from-a-background-thread
        // for more discussion about this topic.
        //Uri downloadedUrl= DownloadUtils.downloadImage(getApplicationContext(),url);

        Thread th= new Thread(new DownloadTask(url));
        th.start();
        //Intent data = new Intent();
        //data.putExtra(DATA_DOWNLOADED_FILE,(downloadedUrl==null?null:downloadedUrl.toString()));
        //setResult(RESULT_OK,data);
        //this.getParent()

    }
  private void doneActivity(Uri downloadedUrl)
  {
      Log.d(TAG, "doneActivity");
      Intent data = new Intent();
      data.putExtra(DATA_DOWNLOADED_FILE,(downloadedUrl==null?null:downloadedUrl.toString()));
      setResult(RESULT_OK,data);
      Log.d(TAG, "call finish()");
      finish();


  }


    private class DownloadTask implements Runnable {

        protected Uri url;
        protected Uri downloadedUrl;

        private DownloadTask(Uri url) {
            this.url = url;
        }
        public void run() {
            Log.d(TAG, "DownloadTask-run");
            downloadedUrl=DownloadUtils.downloadImage(getApplicationContext(),url);
            Log.d(TAG, "downloadedUrl="+downloadedUrl.toString());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    doneActivity(downloadedUrl);
                }
            });

        }





    }



}
