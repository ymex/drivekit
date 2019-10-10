package cn.ymex.kitx.permission;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import io.reactivex.subjects.PublishSubject;

public class RxPermissionsFragment extends Fragment {

    private static final int PERMISSIONS_REQUEST_CODE = 42;
    private static final int SPECIAL_PERMISSIONS_REQUEST_CODE = 43;

    private static final List<String> SPECIAL_PERMISSIONS = new ArrayList<String>() {
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                add(Manifest.permission.REQUEST_INSTALL_PACKAGES);
            } else {
                add("android.permission.REQUEST_INSTALL_PACKAGES");
            }
            add(Manifest.permission.SYSTEM_ALERT_WINDOW);
        }
    };

    private String[] requestPermissions;
    private Queue<String> permissionQueue = new LinkedList<>();

    // Contains all the current permission requests.
    // Once granted or denied, they are removed from it.
    private Map<String, PublishSubject<Permission>> mSubjects = new HashMap<>();
    private boolean mLogging;

    public RxPermissionsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    private String[] list2arr(List<String> strs) {
        String[] ss = new String[strs.size()];
        for (int i = 0; i < ss.length; i++) {
            ss[i] = strs.get(i);
        }
        return ss;
    }

    @TargetApi(Build.VERSION_CODES.M)
    void requestPermissions(@NonNull String[] permissions) {
        requestPermissions = permissions;
        List<String> ps = new ArrayList<>(Arrays.asList(permissions));
        ps.removeAll(SPECIAL_PERMISSIONS);
        if (ps.size() == permissions.length) {
            requestPermissions(permissions, PERMISSIONS_REQUEST_CODE);
            return;
        }

        ps = new ArrayList<>(Arrays.asList(permissions));
        for (String p : SPECIAL_PERMISSIONS) {
            if (ps.contains(p)) {
                permissionQueue.add(p);
            }
        }
        onActivityResult(SPECIAL_PERMISSIONS_REQUEST_CODE, 0, null);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != SPECIAL_PERMISSIONS_REQUEST_CODE) {
            return;
        }
        if (permissionQueue.isEmpty()) {
            List<String> ps = new ArrayList<>(Arrays.asList(requestPermissions));
            ps.removeAll(SPECIAL_PERMISSIONS);
            if (ps.isEmpty()) {
                onRequestPermissionsResult(PERMISSIONS_REQUEST_CODE, new String[0], new int[0]);
                return;
            }
            requestPermissions(list2arr(ps), PERMISSIONS_REQUEST_CODE);
            return;
        }

        String permission = permissionQueue.poll();

        switch (permission) {
            case Manifest.permission.REQUEST_INSTALL_PACKAGES:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !getContext().getPackageManager().canRequestPackageInstalls()) {
                    startInstallPermissionSettingActivity();
                } else {
                    onActivityResult(SPECIAL_PERMISSIONS_REQUEST_CODE, 0, null);
                }
                break;
            case Manifest.permission.SYSTEM_ALERT_WINDOW:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(getContext())) {
                    startCanDrawOverlaysPermissionSettingActivity();
                } else {
                    onActivityResult(SPECIAL_PERMISSIONS_REQUEST_CODE, 0, null);
                }
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void startCanDrawOverlaysPermissionSettingActivity() {
        Uri packageURI = Uri.parse("package:" + getContext().getPackageName());
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, packageURI);
        startActivityForResult(intent, SPECIAL_PERMISSIONS_REQUEST_CODE);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startInstallPermissionSettingActivity() {
        Uri packageURI = Uri.parse("package:" + getContext().getPackageName());
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
        startActivityForResult(intent, SPECIAL_PERMISSIONS_REQUEST_CODE);
    }

    private int getIndexInArray(String permission, String permissions[]) {
        for (int i = 0; i < permissions.length; i++) {
            if (permission.equals(permissions[i])) {
                return i;
            }
        }
        return -1;
    }


    @TargetApi(Build.VERSION_CODES.M)
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode != PERMISSIONS_REQUEST_CODE) return;

        boolean[] shouldShowRequestPermissionRationale = new boolean[requestPermissions.length];
        int[] _grantResults = new int[requestPermissions.length];
        for (int i = 0; i < requestPermissions.length; i++) {
            int index = getIndexInArray(requestPermissions[i], permissions);
            if (index != -1) {
                shouldShowRequestPermissionRationale[i] = shouldShowRequestPermissionRationale(permissions[index]);
                _grantResults[i] = grantResults[index];
            } else {
                shouldShowRequestPermissionRationale[i] = false;
                switch (requestPermissions[i]) {
                    case Manifest.permission.REQUEST_INSTALL_PACKAGES:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            _grantResults[i] = getContext().getPackageManager().canRequestPackageInstalls() ? PackageManager.PERMISSION_GRANTED : PackageManager.PERMISSION_DENIED;
                        } else {
                            grantResults[i] = PackageManager.PERMISSION_GRANTED;
                        }
                        break;
                    case Manifest.permission.SYSTEM_ALERT_WINDOW:
                        _grantResults[i] = Settings.canDrawOverlays(getContext()) ? PackageManager.PERMISSION_GRANTED : PackageManager.PERMISSION_DENIED;
                        break;
                }
            }
        }

        onRequestPermissionsResult(requestPermissions, _grantResults, shouldShowRequestPermissionRationale);
    }

    void onRequestPermissionsResult(String permissions[], int[] grantResults, boolean[] shouldShowRequestPermissionRationale) {
        for (int i = 0, size = permissions.length; i < size; i++) {
            log("onRequestPermissionsResult  " + permissions[i]);
            // Find the corresponding subject
            PublishSubject<Permission> subject = mSubjects.get(permissions[i]);
            if (subject == null) {
                // No subject found
                Log.e(RxPermissions.TAG, "RxPermissions.onRequestPermissionsResult invoked but didn't find the corresponding permission request.");
                return;
            }
            mSubjects.remove(permissions[i]);
            boolean granted = grantResults[i] == PackageManager.PERMISSION_GRANTED;
            subject.onNext(new Permission(permissions[i], granted, shouldShowRequestPermissionRationale[i]));
            subject.onComplete();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    boolean isGranted(String permission) {
        final FragmentActivity fragmentActivity = getActivity();
        if (fragmentActivity == null) {
            throw new IllegalStateException("This fragment must be attached to an activity.");
        }
        return fragmentActivity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    @TargetApi(Build.VERSION_CODES.M)
    boolean isRevoked(String permission) {
        final FragmentActivity fragmentActivity = getActivity();
        if (fragmentActivity == null) {
            throw new IllegalStateException("This fragment must be attached to an activity.");
        }
        return fragmentActivity.getPackageManager().isPermissionRevokedByPolicy(permission, getActivity().getPackageName());
    }

    public void setLogging(boolean logging) {
        mLogging = logging;
    }

    public PublishSubject<Permission> getSubjectByPermission(@NonNull String permission) {
        return mSubjects.get(permission);
    }

    public boolean containsByPermission(@NonNull String permission) {
        return mSubjects.containsKey(permission);
    }

    public void setSubjectForPermission(@NonNull String permission, @NonNull PublishSubject<Permission> subject) {
        mSubjects.put(permission, subject);
    }

    void log(String message) {
        if (mLogging) {
            Log.d(RxPermissions.TAG, message);
        }
    }

}
