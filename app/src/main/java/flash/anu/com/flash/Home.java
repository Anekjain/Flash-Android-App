package flash.anu.com.flash;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.Image;
import android.preference.DialogPreference;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import java.security.Policy;


public class Home extends ActionBarActivity {
    ImageButton btn_switch;
    public Camera camera;
    Camera.Parameters params;
    boolean isFlashOn;
    boolean hasFlash;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btn_switch = (ImageButton) findViewById(R.id.btn);
        btn_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFlashOn){
                    turnOnFlash();
                }else{
                    turnOffFlash();
                }

            }
        });

        //DETECTING DOES DEVICE SUPPOT FLASH
        hasFlash = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        if(!hasFlash){
            AlertDialog alert = new AlertDialog.Builder(Home.this).create();
            alert.setTitle("ERROR");
            alert.setMessage("SORRY YOUR DEVICE DOESNT SUPPORT FLASH");
            alert.setButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            alert.show();
            return;
        }


    }

    public void getCamera(){
        if(camera == null){
            try{
                camera = Camera.open();
                params = camera.getParameters();
                }catch (Exception e){
                Log.e("Sorry Camera ERROR", e.getMessage());
            }
        }
    }

    public void turnOnFlash(){
        if(!isFlashOn){
            if(camera == null || params == null){
                return;
            }
            params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
            camera.setParameters(params);
            camera.startPreview();
            isFlashOn = true;

            togglebtn();
        }
    }
    public void turnOffFlash(){
        if(isFlashOn){
            if(camera == null || params == null){
                return;
            }
            params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(params);
            camera.stopPreview();
            isFlashOn = false;

            togglebtn();
        }
    }

    public void togglebtn(){
        if(isFlashOn){
            btn_switch.setImageResource(R.drawable.on);
          }else{
            btn_switch.setImageResource(R.drawable.off);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // on pause turn off the flash
        turnOffFlash();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // on resume turn on the flash
        if(hasFlash)
            turnOnFlash();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // on starting the app get the camera params
        getCamera();
    }

    @Override
    protected void onStop() {
        super.onStop();

        // on stop release the camera
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
