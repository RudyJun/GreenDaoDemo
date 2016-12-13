package com.rudy.demo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rudy.demo.entity.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RudyJun on 2016/12/13.
 */

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private List<Student> studentList;
    private Context context;

    public StudentAdapter(List<Student> studentList, Context context) {
        this.studentList = studentList;
        this.context = context;
    }

    @Override
    public StudentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_student_item, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StudentViewHolder holder, int position) {
        Student student = studentList.get(position);
        holder.tvName.setText("姓名：" + student.getName());
        holder.tvAge.setText("年龄：" + student.getAge());
        holder.tvSex.setText("性别：" + student.getSex());
        holder.tvGrade.setText("年级：" + student.getGrade());
        holder.tvId.setText("ID：" + student.getId());

    }

    @Override
    public int getItemCount() {
        if (studentList == null) {
            return 0;
        } else {
            return studentList.size();
        }
    }

    public void setStudentList(List<Student> list) {
        if (studentList != null) {
            studentList = new ArrayList<>();
        }
        studentList.clear();
        if (list != null && !list.isEmpty()) {
            studentList.addAll(list);
            notifyDataSetChanged();
        }
    }

    class StudentViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private TextView tvAge;
        private TextView tvSex;
        private TextView tvGrade;
        private TextView tvId;

        public StudentViewHolder(View itemView) {
            super(itemView);
            tvId = (TextView) itemView.findViewById(R.id.tvId);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvAge = (TextView) itemView.findViewById(R.id.tvAge);
            tvSex = (TextView) itemView.findViewById(R.id.tvSex);
            tvGrade = (TextView) itemView.findViewById(R.id.tvGrade);
        }
    }
}
