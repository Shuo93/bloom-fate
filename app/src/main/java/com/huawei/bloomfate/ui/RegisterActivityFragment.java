package com.huawei.bloomfate.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.huawei.bloomfate.R;
import com.huawei.bloomfate.util.FabricConnection;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A placeholder fragment containing a simple view.
 */

public class RegisterActivityFragment extends Fragment {

    private Context context;

    private EditText nameView;
    private EditText ageView;
    private RadioGroup sexView;

    private EditText phoneView;
    private EditText wechatView;
    private EditText cityView;

    private RadioGroup educationView;
    private EditText schoolView;

    private EditText companyView;
    private EditText salaryView;

    private Button photoView;

    private String sex;
    private String education;
    private  String photoHash;

    public RegisterActivityFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
//        view.findViewById(R.id.)
        nameView = view.findViewById(R.id.name);
        ageView = view.findViewById(R.id.age);

        //sex
        sexView = view.findViewById(R.id.sex);

        phoneView = view.findViewById(R.id.phone);
        wechatView = view.findViewById(R.id.wechat);
        cityView = view.findViewById(R.id.city);
        //edu
        educationView = view.findViewById(R.id.education);
        schoolView = view.findViewById(R.id.school);

        companyView = view.findViewById(R.id.company);
        salaryView = view.findViewById(R.id.salary);
        photoView = view.findViewById(R.id.photo);

        //get value
        sexView.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                sex = sexView.getCheckedRadioButtonId() == R.id.male ? "male" : "female";
            }

        });

        educationView.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                education = educationView.getCheckedRadioButtonId() == R.id.bachelor ? "bachelor" : (educationView.getCheckedRadioButtonId()==R.id.master ? "master" : "doctor");
            }
        });

       //TODO：上传照片
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                photoHash  =
            }
        });

        return view;
    }

    private String getJsonArg() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", nameView.getText().toString());
            jsonObject.put("age", ageView.getText().toString());
            jsonObject.put("sex", sex);
            jsonObject.put("phone", phoneView.getText().toString());
            jsonObject.put("wechat", wechatView.getText().toString());
            jsonObject.put("city", cityView.getText().toString());
            jsonObject.put("education", education);
            jsonObject.put("school", schoolView.getText().toString());
            jsonObject.put("company", companyView.getText().toString());
            jsonObject.put("salary", salaryView.getText().toString());
            jsonObject.put("photoHash", photoHash);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();

    }

    public void upload() {
        RegisterTask task = new RegisterTask();
        task.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class RegisterTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            String jsonStr = getJsonArg();
            FabricConnection.getInstance().invoke("uploadResume", jsonStr);
            return 200;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
        }
    }

}
