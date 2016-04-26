package umkc.elmp7.climacloset.Listeners;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;

import umkc.elmp7.climacloset.ClimaClothesActivites.MainActivity;

/**
 * Created by Eric on 4/25/16.
 */
public class BackButtonClickListener implements View.OnClickListener {
    Activity context;
    public BackButtonClickListener(Activity Context){
        context = Context;
    }
    @Override
    public void onClick(View view) {
        Intent homeScreen = new Intent(context.getApplicationContext(), MainActivity.class);
        context.startActivity(homeScreen);
    }
}
