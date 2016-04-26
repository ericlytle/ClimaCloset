package umkc.elmp7.climacloset.Listeners;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.view.View;

/**
 * Created by Eric on 4/25/16.
 */
public class TakePhotoButtonClickListener implements View.OnClickListener {
    private static final int CAMERA_REQUEST = 1888;
    private Activity activity;

    public TakePhotoButtonClickListener(Activity Activity){
        activity = Activity;
    }
    @Override
    public void onClick(View view) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        activity.startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }
}
