package vandy.mooc.utils;

import java.util.List;

import vandy.mooc.R;
import vandy.mooc.aidl.WeatherData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class WeatherDataArrayAdapter extends ArrayAdapter<WeatherData> {

	public WeatherDataArrayAdapter(Context context) {
		super(context, R.layout.weather_data_row);
	}
	
	public WeatherDataArrayAdapter(Context context, 
			List<WeatherData> objects) {
		super(context, R.layout.weather_data_row, objects);
	}
	
    @Override
    public View getView(int position,
                        View convertView,
                        ViewGroup parent) {
    	WeatherData data = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
            		R.layout.weather_data_row, parent, false);
        }

        TextView locationTV =
            (TextView) convertView.findViewById(R.id.locationName);
        TextView temperatureTV = 
            (TextView) convertView.findViewById(R.id.temperature);
        TextView humidityTV =
            (TextView) convertView.findViewById(R.id.humidity);
        TextView sunriseTV =
                (TextView) convertView.findViewById(R.id.sunrise);
        TextView sunsetTV =
                (TextView) convertView.findViewById(R.id.sunset);
        TextView windTV =
                (TextView) convertView.findViewById(R.id.wind);

        locationTV.setText(data.getLocation());
        temperatureTV.setText("" + data.getTempurature());
        humidityTV.setText("" + data.getHumidity());
        sunriseTV.setText("" + data.getSunrise());
        sunsetTV.setText("" + data.getSunset());
        windTV.setText("" + data.getWind());

        return convertView;
    }	
}
