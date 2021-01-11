package helpers;

//  Imports

import android.os.Build;
import android.widget.TimePicker;

public final class TimePickerHelper {

    public static int GetTimePickerHour(TimePicker timePicker)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            return timePicker.getHour();
        }
        else
        {
            //  For now does nothing
            return  timePicker.getHour();
        }
    }

    public static int GetTimePickerMinute(TimePicker timePicker)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            return timePicker.getMinute();
        }
        else
        {
            return timePicker.getMinute();
        }
    }

}
