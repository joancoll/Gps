package cat.dam.andy.gps;

import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    //Members
    private final String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION};
    private Button btn_gps;
    private TextView tvLatitude, tvLongitude;
    private GpsTracker gpsTracker;
    private PermissionManager permissionManager;
    private PermissionRequired permissionRequired;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initPermissions();
        initListeners();

        if (permissionManager.hasAllNeededPermissions(this, PERMISSIONS)) {
            //mostra la informació del GPS
            showGPSInfo();
        }
        else {
            permissionManager.hasAllNeededPermissions(this, PERMISSIONS);
        }

    }

    private void initViews() {
        btn_gps = findViewById(R.id.btn_gps);
        tvLatitude = findViewById(R.id.tv_latitude);
        tvLongitude = findViewById(R.id.tv_longitude);
    }

    private void initPermissions() {
        //TO DO: add more permissionsrequires and descriptions if needed
        permissionRequired = new PermissionRequired(PERMISSIONS[0],
                getString(R.string.locationPermissionNeeded),
                "",
                getString(R.string.locationPermissionThanks),
                getString(R.string.locationPermissionSettings));
        //call permission manager
        permissionManager= new PermissionManager(this, permissionRequired);

    }

    private void initListeners() {
        btn_gps.setOnClickListener(v -> {
            if (permissionManager.hasAllNeededPermissions(this, PERMISSIONS))
            {
                showGPSInfo();
            } else {
                permissionManager.hasAllNeededPermissions(this, PERMISSIONS);
            }
        });
    }


    private void showGPSInfo() {
        gpsTracker = new GpsTracker(MainActivity.this);
        if (gpsTracker.canGetLocation()) {
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            tvLatitude.setText(String.format(Locale.getDefault(),"%.4f",latitude));
            tvLongitude.setText(String.format(Locale.getDefault(),"%.4f",longitude));
            Toast.makeText(MainActivity.this, R.string.positionReady,
                    Toast.LENGTH_SHORT).show();
        } else {
            gpsTracker.showSettingsAlert();
        }
    }

}
