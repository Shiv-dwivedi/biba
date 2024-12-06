package com.AttendanceManger.attendancemanager.viewholders;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

//import com.AttendanceManger.attendancemanager.R;
import com.AttendanceManger.attendancemanager.R;
import com.AttendanceManger.attendancemanager.realm.Attendance_Students_List;

import io.realm.Realm;
import io.realm.RealmResults;


public class ViewHolder_reports_detail extends RecyclerView.ViewHolder {

    public TextView namE;
    public TextView regNo;
    public TextView status;
    public CardView circle;
    private Activity mActivity;
    private RealmResults<Attendance_Students_List> mList;
    private Realm realm;

    public ViewHolder_reports_detail(@NonNull final View itemView, Activity MainActivity, RealmResults<Attendance_Students_List> list) {
        super(itemView);

        namE = itemView.findViewById(R.id.student_name_report_detail_adapter);
        regNo = itemView.findViewById(R.id.student_regNo_report_detail_adapter);
        status = itemView.findViewById(R.id.status_report_detail_adapter);
        circle = itemView.findViewById(R.id.cardView_report_detail_adapter);

        mActivity = MainActivity;
        mList = list;
        realm = Realm.getDefaultInstance();

        // Set the click listener for the CardView
        circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleAttendanceStatus(getAbsoluteAdapterPosition());
            }
        });
    }

    private void toggleAttendanceStatus(int position) {
        Attendance_Students_List student = mList.get(position);
        if (student != null) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    if ("Present".equals(student.getAttendance())) {
                        student.setAttendance("Absent");
                        status.setText("A");
                        circle.setCardBackgroundColor(mActivity.getResources().getColor(R.color.red_new));
                    } else {
                        student.setAttendance("Present");
                        status.setText("P");
                        circle.setCardBackgroundColor(mActivity.getResources().getColor(R.color.green_new));
                    }
                }
            });
        }
    }
}
