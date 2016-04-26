package umkc.elmp7.climacloset.Listeners;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import umkc.elmp7.climacloset.ClimaClothesActivites.CatalogTopActivity;

/**
 * Created by Eric on 4/25/16.
 */
public class ActivityButtonClickListener implements View.OnClickListener{
    private Activity activity;
    private Class activityClass;
    public ActivityButtonClickListener(Activity Activity, Class ActivityClass){
        activity = Activity;
        activityClass = ActivityClass;
    }

    @Override
    public void onClick(View view) {
        Intent catalogTopIntent = new Intent(activity.getApplicationContext(), activityClass);
        activity.startActivity(catalogTopIntent);
    }
}
