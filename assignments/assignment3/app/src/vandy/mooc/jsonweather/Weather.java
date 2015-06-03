package vandy.mooc.jsonweather;

import android.util.Log;

/**
 * This "Plain Ol' Java Object" (POJO) class represents data related
 * to weather downloaded in Json from the Weather Service.
 */
public class Weather {
    /**
     * Used for logging purposes.
     */
    private final String TAG = this.getClass().getCanonicalName();

    /**
     * Various tags corresponding to weather data downloaded in Json
     * from the Weather Service.
     */
    public final static String id_JSON = "id";
    public final static String main_JSON = "main";
    public final static String description_JSON = "description";
    public final static String icon_JSON = "icon";

    /**
     * Various fields corresponding to weather data downloaded in Json
     * from the Weather Service.
     */
    private long id;
    private String main;
    private String description;
    private String icon;

    /**
     * 
     * @return The id
     */
    public long getId() {
        return id;
    }

    /**
     * 
     * @param id
     *            The id
     */
    public void setId(long id) {
		Log.d(TAG, "setId=" + id);
        this.id = id;
    }

    /**
     * 
     * @return The main
     */
    public String getMain() {
        return main;
    }

    /**
     * 
     * @param main
     *            The main
     */
    public void setMain(String main) {
		Log.d(TAG, "setMain=" + main);
        this.main = main;
    }

    /**
     * 
     * @return The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     *            The description
     */
    public void setDescription(String description) {
		Log.d(TAG, "setDescription=" + description);
        this.description = description;
    }

    /**
     * 
     * @return The icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * 
     * @param icon
     *            The icon
     */
    public void setIcon(String icon) {
		Log.d(TAG, "setIcon=" + icon);
        this.icon = icon;
    }
}
