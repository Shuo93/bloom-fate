package com.huawei.bloomfate.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.huawei.bloomfate.R;
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
public class LikerFragment extends Fragment implements Refreshable {

    private static final String TAG = "LikerFragment";

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    private List<LikerItem> likers;
    private MyLikerRecyclerViewAdapter adapter;
    private String userId;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LikerFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static LikerFragment newInstance(int columnCount) {
        LikerFragment fragment = new LikerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_liker_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            likers = new ArrayList<>();
            adapter = new MyLikerRecyclerViewAdapter(likers, mListener);
            recyclerView.setAdapter(adapter);
        }
        return view;
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

    @Override
    public void refresh() {
        queryLikeList();
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
        void onListFragmentInteraction(String item);
    }

    static class LikerItem {

        private String name;

        private String time;

        private String userId;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }


    public void queryLikeList() {
        RefreshTask task = new RefreshTask(this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        task.execute("queryLikeList", jsonObject.toString());
    }


    private static final class RefreshTask extends SafeAsyncTask<LikerFragment, String, Void, String> {

        public RefreshTask(LikerFragment reference) {
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
            LikerFragment fragment = getReference();
            if (response.equals(FabricService.ERROR)) {
                Toast.makeText(fragment.getContext(), "后台发生错误", Toast.LENGTH_SHORT).show();
                return;
            }
            if (response.equals(FabricService.NO_DATA)) {
                Toast.makeText(fragment.getContext(), "无记录", Toast.LENGTH_SHORT).show();
                return;
            }
            fragment.likers.clear();
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    LikerItem person = new LikerItem();
                    person.setUserId(jsonObject.getString("liker_id"));
                    person.setName(jsonObject.getString("likername"));
                    person.setTime(jsonObject.getString("created_time"));
                    fragment.likers.add(person);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            fragment.adapter.notifyDataSetChanged();
            Toast.makeText(fragment.getContext(), fragment.likers.isEmpty() ? "数据解析错误" : "列表更新成功", Toast.LENGTH_SHORT).show();
        }
    }
}
