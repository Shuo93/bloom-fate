package com.huawei.bloomfate.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.huawei.bloomfate.R;
import com.huawei.bloomfate.model.Person;
import com.huawei.bloomfate.model.PersonBasic;
import com.huawei.bloomfate.util.FabricService;
import com.huawei.bloomfate.util.SafeAsyncTask;
import com.huawei.bloomfate.util.SharedPreferencesHelper;

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
public class PersonFragment extends Fragment {

    private static final String TAG = "PersonFragment";

    enum Type {
        ALL,
        LIKE
    }

    private static final String ARG_TYPE = "browse-type";

    private Type type;

    private String userId;
    private MyPersonRecyclerViewAdapter adapter;
    private List<PersonBasic> personList;

    private OnListFragmentInteractionListener mListener;

    private EditText ageStartTv;
    private EditText ageEndTv;
    private Switch switchLocation;
    private Switch switchSex;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PersonFragment() {
    }

    public static PersonFragment newInstance(Type type) {
        PersonFragment fragment = new PersonFragment();
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
        View root = inflater.inflate(R.layout.fragment_person_list, container, false);

        ageStartTv = root.findViewById(R.id.age_start_tv);
        ageEndTv = root.findViewById(R.id.age_end_tv);
        switchLocation = root.findViewById(R.id.switch_location);
        switchSex = root.findViewById(R.id.switch_sex);

        // Set the adapter
        View view = root.findViewById(R.id.person_list);
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            personList = new ArrayList<>();
            adapter = new MyPersonRecyclerViewAdapter(personList, mListener);
            recyclerView.setAdapter(adapter);
            switch (type) {
                case ALL:
                    queryPersonList();
                    break;
                case LIKE:
                    queryLikeList();
                    break;
                default:
                    Log.e(TAG, "unknown fragment type");
            }
        }
        Log.v(TAG, type.name());
        return root;
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

    public void queryPersonList() {
        RefreshTask task = new RefreshTask(this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", userId);
            jsonObject.put("ageStart", ageStartTv.getText().toString());
            jsonObject.put("ageEnd", ageEndTv.getText().toString());
            jsonObject.put("sex", switchSex.isChecked());
            jsonObject.put("location", switchLocation.isChecked());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        task.execute("queryPersonList", jsonObject.toString());
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
        void onListFragmentInteraction(PersonBasic item);
    }

    private static final class RefreshTask extends SafeAsyncTask<PersonFragment, String, Void, String> {

        public RefreshTask(PersonFragment reference) {
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
            List<PersonBasic> items = FabricService.getConnection().getResults(response, PersonBasic.class);
            PersonFragment fragment = getReference();
            fragment.personList.clear();
            fragment.personList.addAll(items);
            fragment.adapter.notifyDataSetChanged();
            Toast.makeText(getReference().getContext(), "收藏列表更新成功", Toast.LENGTH_SHORT).show();
        }
    }
}
