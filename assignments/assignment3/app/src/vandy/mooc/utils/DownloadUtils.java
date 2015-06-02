package vandy.mooc.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

public class DownloadUtils {

    /**
     * Used for debugging.
     */
    private final static String TAG = "DownloadUtils";
    
    /**
     * Size of each file I/O operation.
     */
    private static final int BUFLEN = 1024;
    
    private static Boolean mGuaranteeUniqueness = null;
    
    /**
     * Download the file located at the provided Internet url using
     * the URL class, store it on the android file system using a
     * FileOutputStream, and return the path to the file on disk.
     *
     * @param context
     *          The context in which to write the file.
     * @param url 
     *          The URL of the file to download.
     * @param directoryPathname 
     *          Pathname of the directory to write the file.
     * 
     * @return 
     *        Absolute path to the downloaded file on the file
     *        system.
     */
    public static Uri downloadFile(Context context,
                                    Uri url,
                                    String directoryPathname) {
    	return DownloadUtils.downloadFile(context, url, 
    			directoryPathname, false);    
    }
        
    /**
     * Download the file located at the provided Internet url using
     * the URL class, store it on the android file system using a
     * FileOutputStream, and return the path to the file on disk.
     *
     * @param context
     *          The context in which to write the file.
     * @param url 
     *          The URL of the file to download.
     * @param directoryPathname 
     *          Pathname of the directory to write the file.
     * @param uniquenessGuarantee 
     *          Boolean indicating the filename uniqueness 
     *          methodology.
     * 
     * @return 
     *        Absolute path to the downloaded file on the file
     *        system.
     */
    public static Uri downloadFile(Context context,
                                    Uri url,
                                    String directoryPathname,
                                    Boolean uniquenessGuarantee) {
    	DownloadUtils.setGuaranteeUniqueness(uniquenessGuarantee);
    	
    	try  {
            if (!isExternalStorageWritable()) {
                Log.d(TAG,
                      "external storage is not writable");
                return null;
            }
    
            // Create an output file and save the image referenced
            // at the URL into it.
            return DownloadUtils.createDirectoryAndSaveFile
                (context,
                 new URL(url.toString()),
                 url.getLastPathSegment(),
                 directoryPathname);
        } catch (Exception e) {
            Log.e(TAG,
                  "Exception while downloading -- returning null."
                  + e.toString());
            return null;
        }
    }
    	
   /**
     * Sets whether the uniqueness is determined by a 
     * precise timestamp or just the file size.
     * 
     * @param guarantee
     *          True to use a precise timestamp;
     *          False to use the file size (default).
     */
    public static void setGuaranteeUniqueness(Boolean guarantee) {
    	mGuaranteeUniqueness = guarantee;
    }
    
    /**
     * Decode an InputStream into a Bitmap and store it in a file on
     * the device.
     *
     * @param context
     *           The context in which to write the file.
     * @param url               
     *           URL to the resource (e.g., local or remote file).
     * @param fileName          
     *           Name of the file.
     * @param directoryPathname
     *           Pathname of the directory to write the file.
     * 
     * @return 
     *     Absolute path to the downloaded file on the file system.
     */
    private static Uri createDirectoryAndSaveFile(Context context,
                                                  URL url,
                                                  String fileName,
                                                  String directoryPathname) {
        try {
            // Bail out of we get an invalid url.
            if (url == null)
                return null;
            
            // Bail if the fileName is null as well.
            if (fileName == null) {
            	return null;
            }

            // Create a directory path.
            File directoryPath = new File(directoryPathname);

            // If the directory doesn't exist already then create it.
            if (!directoryPath.exists()) 
                directoryPath.mkdirs();

            // Create a filePath within the directoryPath.
            File filePath =
                new File(directoryPath,
                         getUniqueFilename(fileName));

            // Delete the file if it already exists.
            if (filePath.exists())
                filePath.delete();
                
            // Pre-validate file.
            // TODO Find a way to generalize by delegating somehow.
            
            // Get the content of the resource at the url and save it
            // to an output file.
            try (InputStream is = (InputStream) url.getContent();
                 OutputStream os = new FileOutputStream(filePath)) {
                 copyFile(is, os);
            } catch (Exception e) {
            	return null; // Indicate a failure.
            }

            // Get the absolute path of the downloaded file.
            String absolutePathToFile = filePath.getAbsolutePath();

            Log.d(TAG,
                  "absolute path to downloaded file is " 
                  + absolutePathToFile);

            // Return the absolute path to the downloaded file.
            return Uri.parse(absolutePathToFile);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * This method checks if we can write image to external storage.
     * 
     * @return true if an image can be written, and false otherwise
     */
    private static boolean isExternalStorageWritable() {
        return Environment.MEDIA_MOUNTED.equals
            (Environment.getExternalStorageState());
    }

    /**
     * Create a filename that uniquely identifies the file.
     * 
     * @param filename
     *          The name of a file that we'd like to make unique.
     * @return 
     *          String containing the unique temporary filename.
     */
    static private String getUniqueFilename(final String filename) {
        if (mGuaranteeUniqueness) {
        	return Base64.encodeToString((filename
                    + System.currentTimeMillis()
                    + Thread.currentThread().getName()).getBytes(),
                    Base64.NO_WRAP);
        } else {
            // Prevent temp files from filling up your file system with.
        	// multiple copies of the same file.
        	return Base64.encodeToString(filename.getBytes(), Base64.NO_WRAP);
        }
    }

    /**
     * Copy the contents of the @a inputStream to the @a outputStream.
     * @throws IOException 
     */
    private static void copyFile(InputStream inputStream,
                                 OutputStream outputStream) 
                        throws IOException {
        byte[] buffer = new byte[BUFLEN];

        for (int n; (n = inputStream.read(buffer)) >= 0; ) 
            outputStream.write(buffer, 0, n);

        outputStream.flush();
    }

    /**
     * Ensure this class is only used as a utility.
     */
    private DownloadUtils() {
        throw new AssertionError(TAG + " can not be instantiated");
    } 
}
