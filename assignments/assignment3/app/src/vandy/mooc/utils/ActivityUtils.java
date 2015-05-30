package vandy.mooc.utils;

import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

public class ActivityUtils {

    /**
     * Used for debugging.
     */
    private final static String TAG = "ActivityUtils";
    
    /**
     * Show a short toast message.
     */
    public static void showShortToast(Context context,
                             		String message) {
        showToast(context,
               message,
               Toast.LENGTH_SHORT);
    }

    /**
     * Show a long toast message.
     */
    public static void showLongToast(Context context,
    								String message) {
    	showToast(context,
    			message,
    			Toast.LENGTH_LONG);
    }

    /**
     * Show a toast message.
     */
    private static void showToast(Context context,
                                 String message,
                                 int length) {
        Toast.makeText(context,
                       message,
                       length).show();
    }

    /**
     * This method is used to hide a keyboard after a user has
     * finished typing the input.
     */
    public static void hideKeyboard(Activity activity,
                                    IBinder windowToken) {
        InputMethodManager mgr =
            (InputMethodManager) activity.getSystemService
            (Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(windowToken,
                                    0);
    }

    public static String toCapitalCase(String text) {
    	if (text.contains(" ")) {
    		String capitalized = "";
        	for (String word : text.split(" ")) {
        		String upperCase = capitalFirstLetter(word);
        		capitalized.concat(" " + upperCase);
        	}
        	return capitalized.trim();
    	} else {
    		return capitalFirstLetter(text);
    	}
    }
    
    public static String capitalFirstLetter(String text) {
		if (text != null) {
    		String lowerCase = text.trim().toLowerCase(Locale.getDefault());
	    	String firstLetter = Character.toString(lowerCase.charAt(0));
			return  lowerCase.replaceFirst(firstLetter, 
					firstLetter.toUpperCase(Locale.getDefault()));
		}
		return null;
    }
    
    /**
     * Ensure this class is only used as a utility.
     */
	private ActivityUtils() {
        throw new AssertionError(TAG + " can not be instantiated");
	}
}
