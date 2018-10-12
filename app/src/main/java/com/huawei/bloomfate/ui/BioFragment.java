package com.huawei.bloomfate.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huawei.bloomfate.R;
import com.huawei.bloomfate.model.Edu;
import com.huawei.bloomfate.model.Job;
import com.huawei.bloomfate.model.Person;
import com.huawei.bloomfate.model.PersonBasic;
import com.huawei.bloomfate.model.Value;
import com.huawei.bloomfate.util.FabricService;
import com.huawei.bloomfate.util.SafeAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BioFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BioFragment extends Fragment {
    private RatingBar general_measure;
    private RatingBar photo_measure;
    private RatingBar edu_measure;
    private RatingBar job_measure;

    private TextView name;
    private TextView sex;
    private TextView age;
    private TextView phone;
    private TextView wechat;
    private TextView city;
    private TextView education;
    private TextView school;
    private TextView company;
    private TextView salary;


    private String userId;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public BioFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BioFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BioFragment newInstance(String param1, String param2) {
        BioFragment fragment = new BioFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bio, container, false);

        name = view.findViewById(R.id.name);
        sex = view.findViewById(R.id.sex);
        age = view.findViewById(R.id.age);
        phone = view.findViewById(R.id.phone);
        wechat = view.findViewById(R.id.wechat);
        city = view.findViewById(R.id.city);
        education = view.findViewById(R.id.education);
        school = view.findViewById(R.id.school);
        company = view.findViewById(R.id.company);
        salary = view.findViewById(R.id.salary);

        general_measure = view.findViewById(R.id.general_measure);
        photo_measure = view.findViewById(R.id.photo_measure);
        edu_measure = view.findViewById(R.id.edu_measure);
        job_measure = view.findViewById(R.id.job_measure);


        //显示页面，初始值设置为5颗星
        general_measure.setRating(5);
        general_measure.setIsIndicator(true); //不可点击

        photo_measure.setRating(5);
        photo_measure.setIsIndicator(true);

        edu_measure.setRating(5);
        edu_measure.setIsIndicator(true);

        job_measure.setRating(5);
        job_measure.setIsIndicator(true);

        refreshBio();

        return view;


    }

    public void SetStar (List<JSONObject> obj) {

        //显示页面，评分暂设为5颗星
        general_measure.setRating(Float.parseFloat(obj.get(0).optString("general")));
        general_measure.setIsIndicator(true); //不可点击

        photo_measure.setRating(Float.parseFloat(obj.get(1).optString("photo")));
        photo_measure.setIsIndicator(true);

        edu_measure.setRating(Float.parseFloat(obj.get(2).optString("edution")));
        edu_measure.setIsIndicator(true);

        job_measure.setRating(Float.parseFloat(obj.get(2).optString("occupation")));
        job_measure.setIsIndicator(true);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    private String getJsonArg() {
        PersonBasic basic = new PersonBasic();
        basic.setUserId(userId);
        basic.setName(name.getText().toString());
        basic.setAge(age.getText().toString());
        basic.setSex(sex.getText().toString());
        basic.setLocation(city.getText().toString());
        basic.setPhotoHash("");
        basic.setPhotoFormat("");
        basic.setPhone(phone.getText().toString());
        basic.setEmail(wechat.getText().toString());

        Edu edu = new Edu();
        edu.setDegree(education.getText().toString());
        edu.setSchool(school.getText().toString());
        edu.setEncryptedKey("");
        edu.setSignature("");

        Job job = new Job();
        job.setCompany(company.getText().toString());
        job.setJob("");
        job.setSalary(salary.getText().toString());
        job.setEncryptedKey("");
        job.setSignature("");

        Person person = new Person();
        person.setBasic(basic);
        person.setEducation(edu);
        person.setOccupation(job);

        general_measure.setIsIndicator(false); //可点击
        photo_measure.setIsIndicator(false);
        edu_measure.setIsIndicator(false);
        job_measure.setIsIndicator(false);

        Value value = new Value();
        value.setGeneral(general_measure.getRating());
        value.setPhoto(photo_measure.getRating());
        value.setEdu(edu_measure.getRating());
        value.setJob(job_measure.getRating());

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(person);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }
    public void upload() {
        BioUploadTask up_task = new BioUploadTask(this, getJsonArg());
        up_task.execute();
    }

    private static final class BioUploadTask extends SafeAsyncTask<BioFragment, Void, Void, Boolean> {

        private String args;

        public BioUploadTask(BioFragment reference, String args) {
            super(reference);
            this.args = args;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return FabricService.getConnection().invoke("measureCredit", args);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (!checkWeakReference()) {
                return;
            }
            Context context = getReference().getContext();
            if (context == null) {
                return;
            }
            if (success) {
                Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, SquareActivity.class);
                context.startActivity(intent);
                return;
            }
            Toast.makeText(context, "上传评分失败", Toast.LENGTH_SHORT).show();
        }
    }


    public void refreshBio() {
        RefreshBioTask task = new RefreshBioTask(this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        task.execute("queryCredit", jsonObject.toString());
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

        private static final class RefreshBioTask extends SafeAsyncTask<BioFragment, String, Void, String> {

            public RefreshBioTask(BioFragment reference) {
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
                BioFragment fragment = getReference();
                fragment.SetStar(item);
                Toast.makeText(getReference().getContext(), "个人评分更新成功", Toast.LENGTH_SHORT).show();

            }


    }
}
