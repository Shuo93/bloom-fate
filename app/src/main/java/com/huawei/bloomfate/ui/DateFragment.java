package com.huawei.bloomfate.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.huawei.bloomfate.R;
import com.huawei.bloomfate.model.Date;
import com.huawei.bloomfate.ui.dummy.DummyContent;
import com.huawei.bloomfate.ui.dummy.DummyContent.DummyItem;
import com.huawei.bloomfate.util.FabricService;
import com.huawei.bloomfate.util.SafeAsyncTask;
import com.huawei.bloomfate.util.SharedPreferencesHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class DateFragment extends Fragment {

    private static final String TAG = "DateFragment";

    enum Type {
        SEND,
        RECEIVE
    }

    private static final String ARG_TYPE = "browse-type";
    private Type type;
    private OnListFragmentInteractionListener mListener;

    private List<JSONObject> dateList;
    private MyDateRecyclerViewAdapter adapter;

    private String userId;

//    private Switch switchStatus;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DateFragment() {
    }

    public static DateFragment newInstance(Type type) {
        DateFragment fragment = new DateFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, type.name());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            type = Type.valueOf(getArguments().getString(ARG_TYPE));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_date_list, container, false);

        // Set the adapter
        View view = root.findViewById(R.id.date_list);
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            dateList = new ArrayList<>();
            adapter = new MyDateRecyclerViewAdapter(dateList, mListener, type);
            recyclerView.setAdapter(adapter);
            refresh();
        }
        Log.v(TAG, type.name());
        return root;
    }

    public void refresh() {
        RefreshTask task = new RefreshTask(this);
        JSONObject jsonObject = new JSONObject();
        String userType;
        if (type == Type.SEND) {
            userType = "sender_id";
        } else if (type == Type.RECEIVE) {
            userType = "receiver_id";
        } else {
            userType = "";
        }
        try {
            jsonObject.put("userType", userType);
            jsonObject.put("userId", userId);
            jsonObject.put("status", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        task.execute("queryDate", jsonObject.toString());
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        userId = SharedPreferencesHelper.getUserId(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(JSONObject object);
    }

    private static final class RefreshTask extends SafeAsyncTask<DateFragment, String, Void, String> {

        public RefreshTask(DateFragment reference) {
            super(reference);
        }

        @Override
        protected String doInBackground(String... funcAndParams) {
            String func = funcAndParams[0];
            String args = funcAndParams[1];
            return FabricService.getConnection().query(func, args);
        }

        @Override
        protected void onPostExecute(String response) {
            if (!checkWeakReference()) {
                return;
            }
            List<JSONObject> item = new ArrayList<>();
            try {

                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    item.add(jsonArray.getJSONObject(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            DateFragment fragment = getReference();
            fragment.dateList.clear();
            fragment.dateList.addAll(item);
            fragment.adapter.notifyDataSetChanged();
            Toast.makeText(getReference().getContext(), "约会列表更新成功", Toast.LENGTH_SHORT).show();
        }
    }
}
