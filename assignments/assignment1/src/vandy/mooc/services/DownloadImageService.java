package vandy.mooc.services;

import vandy.mooc.utils.Utils;
import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

/**
 * An IntentService that downloads an image requested via data in an
 * intent, stores the image in a local file on the local device, and
 * returns the image file's URI back to the MainActivity via the
 * Messenger passed with the intent.
 */
public class DownloadImageService extends IntentService {
    /**
     * Debugging tag used by the Android logger.
     */
    private final String TAG = getClass().getSimpleName();
    // http://i0.kym-cdn.com/photos/images/original/000/961/263/80e.jpg
    // http://i3.kym-cdn.com/photos/images/original/000/919/578/c2f.jpg
    // http://i0.kym-cdn.com/photos/images/original/000/957/232/df2.jpg
    // http://www.john-partridge.info/1.jpg
    // http://www.john-partridge.info/2.jpg
    // http://www.john-partridge.info/3.jpg

    /**
     * String constant used to extract the Messenger "extra" from an
     * intent.
     */
    private static final String MESSENGER = "MESSENGER";

    /**
     * String constant used to extract the pathname to a downloaded
     * image from a Bundle.
     */
    private static final String IMAGE_PATHNAME = "IMAGE_PATHNAME";

    /**
     * String constant used to extract the request code.
     */
    private static final String REQUEST_CODE = "REQUEST_CODE";

    /**
     * String constant used to extract the URL to an image from a
     * Bundle.
     */
    private static final String IMAGE_URL = "IMAGE_URL";

    /**
     * String constant used to extract the directory pathname to use
     * to store a downloaded image.
     */
    private static final String DIRECTORY_PATHNAME = "DIRECTORY_PATHNAME";
    
    public DownloadImageService() {
    	super("DownloadImageService");
    }

    /**
     * Factory method that returns an Intent for downloading an image.
     */
    public static Intent makeIntent(Context context,
                                    int requestCode, 
                                    Uri url,
                                    String directoryPathname,
                                    Handler downloadHandler) {
        // Create an intent that will download the image from the web.
    	// TODONE -- you fill in here, replacing "null" with the proper
    	// code, which involves (1) setting the URL as "data" to the
    	// intent, (2) putting the request code as an "extra" to the
    	// intent, (3) creating and putting a Messenger as an "extra"
    	// to the intent so the DownloadImageService can send the path
    	// to the image file back to the MainActivity, and (3) putting
    	// the directory pathname as an "extra" to the intent
        // to tell the Service where to place the image within
        // external storage.
    	Intent downloadImage = new Intent(context, DownloadImageService.class);
    	downloadImage.setData(url);
    	downloadImage.putExtra(REQUEST_CODE, requestCode);
    	downloadImage.putExtra(MESSENGER, new Messenger(downloadHandler));
    	downloadImage.putExtra(DIRECTORY_PATHNAME, directoryPathname);
        return downloadImage;
    }

    /**
     * Helper method that returns the path to the image file if it is
     * download successfully.
     */
    public static String getImagePathname(Bundle data) {
        // Extract the path to the image file from the Bundle, which
        // should be stored using the IMAGE_PATHNAME key.
        return data.getString(IMAGE_PATHNAME);
    }

    public static int getResultCode(Message message) {
      // Check to see if the download succeeded.
      return message.arg1;
    }
    
    /**
     * Helper method that returns the request code associated with
     * the @a message.
     */
    public static int getRequestCode(Message message) {
        // Extract the data from Message, which is in the form of a
        // Bundle that can be passed across processes.
        Bundle data = message.getData();

        // Extract the request code.
        return data.getInt(REQUEST_CODE);
    }

    /**
     * Helper method that returns the URL to the image file.
     */
    public static String getImageURL(Bundle data) {
        // Extract the path to the image file from the Bundle, which
        // should be stored using the IMAGE_URL key.
        return data.getString(IMAGE_URL);
    }

    /**
     * Hook method dispatched by the IntentService framework to
     * download the image requested via data in an intent, store the
     * image in a local file on the local device, and return the image
     * file's URI back to the MainActivity via the Messenger passed
     * with the intent.
     */
    @Override
    public void onHandleIntent(Intent intent) {
    	Log.d(TAG, "Entered onHandleIntent()");
        // Get the URL associated with the Intent data.
        // @@ TODONE -- you fill in here.
    	Uri url = intent.getData();
    	Log.d(TAG, "Image to download " + url.toString());

        // Get the directory pathname where the image will be stored.
        // @@ TODONE -- you fill in here.
    	String imageDirectory = intent.getStringExtra(DIRECTORY_PATHNAME);
    	Log.d(TAG, "Directory to save image to " + imageDirectory);

        // Download the requested image.
        // @@ TODONE -- you fill in here.
    	Uri image = Utils.downloadImage(this, url, imageDirectory);
    	Log.d(TAG, "Downloaded image to " + image.toString());

        // Extract the Messenger stored as an extra in the
        // intent under the key MESSENGER.
        // @@ TODONE -- you fill in here.
    	Messenger messenger = (Messenger) intent.getParcelableExtra(MESSENGER);

        // Send the path to the image file back to the
        // MainActivity via the messenger.
        // @@ TODONE -- you fill in here.
    	sendPath(messenger, image, url);
    }

    /**
     * Send the pathname back to the MainActivity via the
     * messenger.
     */
    private void sendPath(Messenger messenger, 
                          Uri pathToImageFile,
                          Uri url) {
    	Log.d(TAG, "Entered sendPath()");
        // Call the makeReplyMessage() factory method to create
        // Message.
        // @@ TODONE -- you fill in here.
        Message message = makeReplyMessage(pathToImageFile, url);
        
        // Send the path to the image file back to the
        // MainActivity.
        // @@ TODONE -- you fill in here.
        try {
			messenger.send(message);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
    }

    /**
     * A factory method that creates a Message to return to the
     * MainActivity with the pathname of the downloaded image.
     */
    private Message makeReplyMessage(Uri pathToImageFile,
                                     Uri url) {
    	Log.d(TAG, "Entered makeReplyMessage()");
        // Get a message via the obtain() factory method.
        Message message = Message.obtain();

        // Create a new Bundle to handle the result.
        // @@ TODONE -- you fill in here.
        Bundle bundle = new Bundle();

        // Put the URL to the image file into the Bundle via the
        // IMAGE_URL key.
        // @@ TODONE -- you fill in here.
        bundle.putString(IMAGE_URL, url.toString());
    	Log.d(TAG, "Image downloaded " + url.toString());

        // Return the result to indicate whether the download
        // succeeded or failed.
        // @@ TODONE -- you fill in here.
        int resultCode = pathToImageFile == null
        		? Activity.RESULT_CANCELED
				: Activity.RESULT_OK; 
    	Log.d(TAG, "Result code is " + resultCode);

        // Put the path to the image file into the Bundle via the
        // IMAGE_PATHNAME key only if the download succeeded.
        // @@ TODONE -- you fill in here.
		if (resultCode == Activity.RESULT_OK) {
			bundle.putString(IMAGE_PATHNAME, pathToImageFile.toString());
	    	Log.d(TAG, "Image saved to " + pathToImageFile.toString());
		}

        // Set the Bundle to be the data in the message.
        // @@ TODONE -- you fill in here.
        message.setData(bundle);
        message.arg1 = resultCode;

        return message;
    }
}
