package com.AttendanceManger.attendancemanager.BottomSheet;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.AttendanceManger.attendancemanager.R;
import com.AttendanceManger.attendancemanager.realm.Students_List;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class Student_Edit_Sheet extends BottomSheetDialogFragment {

    public String _name, _regNo, _mobNo;
    public EditText name_student, regNo_student;
    public Button updateButton;

    public Student_Edit_Sheet(String stuName, String regNo, String mobileNo) {
        _name = stuName;
        _regNo = regNo;
        _mobNo = mobileNo;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.bottomsheet_student_edit, container, false);

        name_student = v.findViewById(R.id.stu_name_edit);
        regNo_student = v.findViewById(R.id.stu_regNo_edit);
        updateButton = v.findViewById(R.id.updateButton);

        name_student.setText(_name);
        regNo_student.setText(_regNo);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String updatedName = name_student.getText().toString();
                String updatedRegNo = regNo_student.getText().toString();

                updateStudentDetails(updatedName, updatedRegNo);
            }
        });

        return v;
    }

    private void updateStudentDetails(String name, String regNo) {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Updating student details..");
        progressDialog.show();

        Realm.init(getContext());
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm realm = Realm.getInstance(config);

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // Assuming Students_List is your model class and regNo is the primary key
                Students_List student = realm.where(Students_List.class).equalTo("regNo_student", _regNo).findFirst();
                if (student != null) {
                    student.setName_student(name);
                    student.setRegNo_student(regNo);
                }
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Student details updated", Toast.LENGTH_SHORT).show();
                dismiss(); // Close the bottom sheet
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Error updating student details", Toast.LENGTH_SHORT).show();
            }
        });

        realm.close();
    }
}
