package com.udg.envio_de_fotos.Vistas;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.udg.envio_de_fotos.Camara.CameraFragment;
import com.udg.envio_de_fotos.R;

import java.io.ByteArrayOutputStream;


public class CameraActivity extends AppCompatActivity {

    public static final String TAG = CameraActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.squarecamera__CameraFullScreenTheme);
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.squarecamera__activity_camera);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, CameraFragment.newInstance(), CameraFragment.TAG)
                    .commit();
        }
    }

    public void returnPhotoUri(Bitmap bitmap ){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Intent data = new Intent();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, stream);
        byte[] byteArray = stream.toByteArray();
        data.putExtra("data", byteArray);

        //data.
        if (getParent() == null) {
            setResult(RESULT_OK, data);
            finish();
        } else {
            getParent().setResult(RESULT_OK, data);
            finish();
        }

    }

    public void onCancel(View view) {
        getSupportFragmentManager().popBackStack();
    }
}
