package cn.thundersoft.codingnight.acitivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

/**
 * @author greenshadow
 */
public abstract class AbsStoragePermissionCheckActivity extends AppCompatActivity {
    private static final int STORAGE_REQUEST_CODE = 0;

    protected void checkAndRequestStoragePermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
            onStoragePermissionGranted();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    STORAGE_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        if (requestCode == STORAGE_REQUEST_CODE && permissions.length == grantResults.length) {
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.READ_EXTERNAL_STORAGE) &&
                        grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    onStoragePermissionGranted();
                    break;
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    abstract protected void onStoragePermissionGranted();
}
