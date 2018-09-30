package com.huawei.bloomfate.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;
import android.widget.TextView;

import com.huawei.bloomfate.R;

public class PermissionActivity extends AppCompatActivity {

    private static final String TAG = "PermissionActivity";

    private TextView mTextMessage;

    private int currentPosition = 0;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
//                    mTextMessage.setText(R.string.title_home);
                    if (currentPosition == 0) {
                        return true;
                    }
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment, PermissionFragmentManger.getInstance()
                                    .getFragmentInstance(0))
                            .commit();
                    currentPosition = 0;
                    return true;
                case R.id.navigation_dashboard:
//                    mTextMessage.setText(R.string.title_dashboard);
                    if (currentPosition == 1) {
                        return true;
                    }
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment, PermissionFragmentManger.getInstance()
                                    .getFragmentInstance(1))
                            .commit();
                    currentPosition = 1;
                    return true;
                case R.id.navigation_notifications:
//                    mTextMessage.setText(R.string.title_notifications);
                    if (currentPosition == 2) {
                        return true;
                    }
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment, PermissionFragmentManger.getInstance()
                                    .getFragmentInstance(1))
                            .commit();
                    currentPosition = 2;
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.fragment, PermissionFragmentManger.getInstance().getFragmentInstance(0)).commit();
        }
        
    }

    private static class PermissionFragmentManger {

        private SparseArray<Fragment> fragmentArr = new SparseArray<>(3);

        private PermissionFragmentManger() {}

        public static PermissionFragmentManger getInstance() {
            return Holder.INSTANCE;
        }

        private static class Holder {
            private static final PermissionFragmentManger INSTANCE = new PermissionFragmentManger();
        }

        public Fragment getFragmentInstance(int position) {
            if (fragmentArr.size() <= position) {
                switch (position) {
                    case 0:
                        fragmentArr.put(position, PersonFragment.newInstance(PersonFragment.Type.ALL));
                        break;
                    case 1:
                        fragmentArr.put(position, PermissionFragment.newInstance(PermissionFragment.Type.SEND));
                        break;
                    case 2:
                        fragmentArr.put(position, PermissionFragment.newInstance(PermissionFragment.Type.RECEIVE));
                        break;
                    default:
                        Log.e(TAG, "position shouldn't more than 3");
                        return null;
                }
            }
            return fragmentArr.get(position);
        }

    }
}
