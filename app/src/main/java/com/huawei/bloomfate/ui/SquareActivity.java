package com.huawei.bloomfate.ui;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.huawei.bloomfate.R;
import com.huawei.bloomfate.model.PersonBasic;
import com.huawei.bloomfate.ui.dummy.DummyContent;
import com.huawei.bloomfate.util.FabricService;
import com.huawei.bloomfate.util.SafeAsyncTask;
import com.huawei.bloomfate.util.SharedPreferencesHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SquareActivity extends AppCompatActivity implements
        PersonFragment.OnListFragmentInteractionListener,
        DateFragment.OnListFragmentInteractionListener,
        DateDialogFragment.DateDialogListener
{

    private static final String TAG = "SquareActivity";

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_square);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Refreshable) mSectionsPagerAdapter.getItem(mViewPager.getCurrentItem())).refresh();
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_square, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_information) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_permission) {
            Intent intent = new Intent(this, PermissionActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListFragmentInteraction(String userId) {
        Intent intent = new Intent(this, BioActivity.class);
        startActivity(intent);
//        DateDialogFragment dialog = new DateDialogFragment();
//        dialog.setUserId(userId);
//        dialog.show(getSupportFragmentManager(), "date");
    }

    @Override
    public void onLikePersonClick(String userId) {
        LikeTask task = new LikeTask(this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", SharedPreferencesHelper.getUserId(this));
            jsonObject.put("likerId", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        task.execute("like", jsonObject.toString());
    }

    public static final class LikeTask extends SafeAsyncTask<SquareActivity, String, Void, Boolean> {

        public LikeTask(SquareActivity reference) {
            super(reference);
        }

        @Override
        protected Boolean doInBackground(String... funcAndParams) {
            String func = funcAndParams[0];
            String args = funcAndParams[1];
            return FabricService.getConnection().invoke(func, args);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (checkWeakReference()) {
                Toast.makeText(getReference(), "收藏成功", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        dialog.dismiss();


    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    @Override
    public void onListFragmentInteraction(JSONObject object) {
        InvitationDialog dialog = new InvitationDialog();
        dialog.show(getSupportFragmentManager(), "invitation");
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return SquareFragmentManger.getInstance().getFragmentInstance(position);
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }
    }



    private static class SquareFragmentManger {

        private SparseArray<Fragment> fragmentArr = new SparseArray<>(4);

        private SquareFragmentManger() {}

        public static SquareFragmentManger getInstance() {
            return Holder.INSTANCE;
        }

        private static class Holder {
            private static final SquareFragmentManger INSTANCE = new SquareFragmentManger();
        }

        public Fragment getFragmentInstance(int position) {
            if (fragmentArr.size() <= position) {
                switch (position) {
                    case 0:
                        fragmentArr.put(position, PersonFragment.newInstance(PersonFragment.Type.ALL));
                        Log.i(TAG, "Fragment 0 created");
                        break;
                    case 1:
                        fragmentArr.put(position, LikerFragment.newInstance(1));
                        Log.i(TAG, "Fragment 1 created");
                        break;
                    case 2:
                        fragmentArr.put(position, DateFragment.newInstance(DateFragment.Type.SEND));
                        Log.i(TAG, "Fragment 2 created");
                        break;
                    case 3:
                        fragmentArr.put(position, DateFragment.newInstance(DateFragment.Type.RECEIVE));
                        Log.i(TAG, "Fragment 3 created");
                        break;
                    default:
                        Log.e(TAG, "position shouldn't more than 4");
                        return null;
                }
            }
            return fragmentArr.get(position);
        }

    }
}
