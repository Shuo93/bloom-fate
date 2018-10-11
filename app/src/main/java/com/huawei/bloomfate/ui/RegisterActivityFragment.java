package com.huawei.bloomfate.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huawei.bloomfate.R;
import com.huawei.bloomfate.model.Edu;
import com.huawei.bloomfate.model.Job;
import com.huawei.bloomfate.model.Person;
import com.huawei.bloomfate.model.PersonBasic;
import com.huawei.bloomfate.util.FabricService;
import com.huawei.bloomfate.util.SafeAsyncTask;
import com.huawei.bloomfate.util.SharedPreferencesHelper;

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

    private String userId;

    public RegisterActivityFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        userId = SharedPreferencesHelper.getUserId(context);
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
        PersonBasic basic = new PersonBasic();
        basic.setUserId(userId);
        basic.setName(nameView.getText().toString());
        basic.setAge(ageView.getText().toString());
        basic.setSex(sex);
        basic.setLocation(cityView.getText().toString());
        basic.setPhotoHash("");
        basic.setPhotoFormat("");
        basic.setPhone(phoneView.getText().toString());
        basic.setEmail(wechatView.getText().toString());

        Edu edu = new Edu();
        edu.setDegree(education);
        edu.setSchool(schoolView.getText().toString());
        edu.setEncryptedKey("");
        edu.setSignature("");

        Job job = new Job();
        job.setCompany(companyView.getText().toString());
        job.setJob("");
        job.setSalary(salaryView.getText().toString());
        job.setEncryptedKey("");
        job.setSignature("");

        Person person = new Person();
        person.setBasic(basic);
        person.setEducation(edu);
        person.setOccupation(job);

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(person);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void upload() {
        RegisterTask task = new RegisterTask(this, getJsonArg());
        task.execute();
    }

    private static final class RegisterTask extends SafeAsyncTask<RegisterActivityFragment, Void, Void, Boolean> {

        private String args;

        public RegisterTask(RegisterActivityFragment reference, String args) {
            super(reference);
            this.args = args;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return FabricService.getConnection().invoke("uploadResume", args);
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
            Toast.makeText(context, "上传信息失败", Toast.LENGTH_SHORT).show();
        }
    }

}
