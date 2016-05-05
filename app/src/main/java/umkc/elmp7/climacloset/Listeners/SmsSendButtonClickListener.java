package umkc.elmp7.climacloset.Listeners;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;

import umkc.elmp7.climacloset.ClimaClothes.ClothingItem;

/**
 * Created by Eric on 4/27/16.
 */
public class SmsSendButtonClickListener implements View.OnClickListener{
    Activity thisActivity;
    Button smsButton;

    public SmsSendButtonClickListener(Activity Activity){
        this.thisActivity = Activity;
    }

    @Override
    public void onClick(View view) {
        if (view.getTag() != null) {
            if (ContextCompat.checkSelfPermission(thisActivity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(thisActivity,
                        Manifest.permission.READ_CONTACTS)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(thisActivity,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            0);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }
            if (ContextCompat.checkSelfPermission(thisActivity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED && view.getTag() != null) {
                ClothingItem itemTag = (ClothingItem) view.getTag();
                String result = MediaStore.Images.Media.insertImage(thisActivity.getContentResolver(), itemTag.getPicture(), "", "");
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(result));
                sendIntent.setType("image/png");
                thisActivity.startActivity(sendIntent);
            }
        }
    }
}
