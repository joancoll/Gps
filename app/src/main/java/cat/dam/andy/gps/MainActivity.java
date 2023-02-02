package cat.dam.andy.gps;

import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    //Members
    String[] PERMISSIONS;
    private Button btn_gps;
    private TextView tvLatitude, tvLongitude;
    private GpsTracker gpsTracker;
    private PermissionManager permissionManager;
    ArrayList<PermissionData> permissionsRequired=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initPermissions();
        initListeners();
        showGPSInfo();
    }

    private void initViews() {
        btn_gps = findViewById(R.id.btn_gps);
        tvLatitude = findViewById(R.id.tv_latitude);
        tvLongitude = findViewById(R.id.tv_longitude);
    }

    private void initPermissions() {
        //TO DO: CONFIGURE ALL NECESSARY PERMISSIONS

        //BEGIN

        permissionsRequired.add(new PermissionData(Manifest.permission.ACCESS_FINE_LOCATION,
                getString(R.string.locationPermissionNeeded),
                "",
                getString(R.string.locationPermissionThanks),
                getString(R.string.locationPermissionSettings)));

        //END
        //DON'T DELETE == call permission manager ==
        permissionManager= new PermissionManager(this, permissionsRequired);

    }

    private void initListeners() {
        btn_gps.setOnClickListener(v -> {
            if (!permissionManager.hasAllNeededPermissions(this, permissionsRequired))
            { //Si manquen permisos els demanem
                permissionManager.askForPermissions(this, permissionManager.getRejectedPermissions(this, permissionsRequired));
            } else {
                //Si ja tenim tots els permisos, mostrem la informació del GPS
                showGPSInfo();
            }
        });
    }


    private void showGPSInfo() {
        if (permissionManager.hasAllNeededPermissions(this, permissionsRequired)) {
            //mostra la informació del GPS
            gpsTracker = new GpsTracker(MainActivity.this);
            if (gpsTracker.canGetLocation()) {
                double latitude = gpsTracker.getLatitude();
                double longitude = gpsTracker.getLongitude();
                tvLatitude.setText(String.format(Locale.getDefault(),"%.4f",latitude));
                tvLongitude.setText(String.format(Locale.getDefault(),"%.4f",longitude));
                Toast.makeText(MainActivity.this, R.string.locationReady,
                        Toast.LENGTH_SHORT).show();
            } else {
                gpsTracker.showSettingsAlert();
            }
        }
    }
}
