package vandy.mooc;

import java.io.InputStream;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * An Activity that downloads an image, stores it in a local file on the local
 * device, and returns a Uri to the image file.
 */
public class DownloadImageActivity extends Activity {
    /**
     * Debugging tag used by the Android logger.
     */
    private final String TAG = getClass().getSimpleName();

    Uri intentUri;
    Uri downloadImageUri;
    Intent intentI;

    /**
     * Hook method called when a new instance of Activity is created. One time
     * initialization code goes here, e.g., UI layout and some class scope
     * variable initialization.
     *
     * @param savedInstanceState
     *            object that contains saved state information.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Always call super class for necessary
        // initialization/implementation.
        // @@ TODONE -- you fill in here.
        super.onCreate(savedInstanceState);

        // Get the URL associated with the Intent data.
        // @@ TODONE -- you fill in here.

        intentI = getIntent();

        intentUri = intentI.getData();

        // Download the image in the background, create an Intent that
        // contains the path to the image file, and set this as the
        // result of the Activity.

        // @@ TODONE -- you fill in here using the Android "HaMeR"
        // concurrency framework. Note that the finish() method
        // should be called in the UI thread, whereas the other
        // methods should be called in the background thread.
        
        dlImage();

    }


    private void dlImage() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                downloadImageUri = DownloadUtils.downloadImage(
                        getApplicationContext(), intentUri);
                
                DownloadImageActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (downloadImageUri != null) {
                            setResult(RESULT_OK, intentI.setData(downloadImageUri));    
                        } else {
                            setResult(RESULT_CANCELED, intentI.setData(downloadImageUri));
                        }
                        
                        finish();
                    }
                });
            }
        }).start();
    }
    
}