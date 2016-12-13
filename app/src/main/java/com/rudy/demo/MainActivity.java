package com.rudy.demo;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rudy.demo.entity.Student;
import com.rudy.demo.greendao.DaoMaster;
import com.rudy.demo.greendao.DaoSession;
import com.rudy.demo.greendao.StudentDao;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btInsert;
    private Button btDelete;
    private Button btQuery;
    private Button btUpdate;

    private EditText etName;
    private EditText etAge;
    private EditText etSex;
    private EditText etGrade;

    private TextView tvState;

    private RecyclerView mRecyclerView;
    private StudentAdapter studentAdapter;

    private DaoMaster.DevOpenHelper openHelper;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private StudentDao studentDao;

    private Dialog deleteDialog;
    private EditText etId;
    private Button btCancle;
    private Button btConfirm;

    private Dialog updateDialog;
    private EditText etUpdateId;
    private EditText etUpdateAge;
    private Button btUpdateCancle;
    private Button btUpdateConfirm;

    //开启线程对数据库进行操作，防止阻塞主线程
    private ExecutorService executorService;

    private final int INSERT = 1;
    private final int DELETE = 2;
    private final int QUERY = 3;
    private final int UPDATE = 4;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case INSERT:
                    tvState.setText("插入数据成功");
                    break;

                case DELETE:
                    Student deleteStudent = (Student) msg.obj;

                    if(deleteStudent != null) {
                        studentDao.deleteByKey(deleteStudent.getId());
                        showToast("删除成功");
                        queryStudent();
                    }else {
                        showToast("该ID不存在");
                    }
                    break;

                case QUERY:
                    List<Student> studentList = (List<Student>) msg.obj;
                    if(studentList == null || studentList.isEmpty()){
                        if(tvState.getVisibility() == View.GONE){
                            tvState.setVisibility(View.VISIBLE);
                        }
                        tvState.setText("暂无相关数据");
                    }else {
                        showToast("查询成功");
                        tvState.setText("查询结果");
                    }
                    studentAdapter.setStudentList(studentList);
                    break;

                case UPDATE:
                    String result = (String) msg.obj;
                    if(result.equals("fail")){
                        showToast("该ID不存在");
                    }else {
                        showToast("更新成功");
                        queryStudent();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDataBase();
        initViews();
        initEvents();
        Log.e("MainThreadId" , "ThreadId = "+Thread.currentThread().getId());
    }

    private void initDataBase() {
        //初始化数据库person，其中person中有FATHER , SON , STUDENT 三张表
        //这里只对STUDENT表进行操作
        openHelper = new DaoMaster.DevOpenHelper(this, "person-db");
        daoMaster = new DaoMaster(openHelper.getWritableDatabase());
        daoSession = daoMaster.newSession();
        studentDao = daoSession.getStudentDao();

        executorService = Executors.newSingleThreadExecutor();
    }

    private void initViews() {
        btInsert = (Button) findViewById(R.id.btInsert);
        btDelete = (Button) findViewById(R.id.btDelete);
        btQuery = (Button) findViewById(R.id.btQuery);
        btUpdate = (Button) findViewById(R.id.btUpdate);
        etName = (EditText) findViewById(R.id.etName);
        etAge = (EditText) findViewById(R.id.etAge);
        etSex = (EditText) findViewById(R.id.etSex);
        etGrade = (EditText) findViewById(R.id.etGrade);
        tvState = (TextView) findViewById(R.id.tvState);
        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        studentAdapter = new StudentAdapter(new ArrayList<Student>() , this);
        mRecyclerView.setAdapter(studentAdapter);
        deleteDialog = new Dialog(this , R.style.customDialog);
        deleteDialog.setContentView(R.layout.dialog_delete);
        etId = (EditText) deleteDialog.findViewById(R.id.etId);
        btCancle = (Button) deleteDialog.findViewById(R.id.btCancle);
        btConfirm = (Button) deleteDialog.findViewById(R.id.btConfirm);
        deleteDialog.setCanceledOnTouchOutside(true);
        deleteDialog.setCancelable(true);
        deleteDialog.getWindow().setGravity(Gravity.CENTER);
        deleteDialog.getWindow().setWindowAnimations(R.style.dialogAnim);

        updateDialog = new Dialog(this , R.style.customDialog);
        updateDialog.setContentView(R.layout.dialog_update);
        etUpdateId = (EditText) updateDialog.findViewById(R.id.etUpdateId);
        etUpdateAge = (EditText) updateDialog.findViewById(R.id.etUpdateAge);
        btUpdateCancle = (Button) updateDialog.findViewById(R.id.btUpdateCancle);
        btUpdateConfirm = (Button) updateDialog.findViewById(R.id.btUpdateConfirm);
        updateDialog.setCanceledOnTouchOutside(true);
        updateDialog.setCancelable(true);
        updateDialog.getWindow().setGravity(Gravity.CENTER);
        updateDialog.getWindow().setWindowAnimations(R.style.dialogAnim);
    }

    private void initEvents() {
        btInsert.setOnClickListener(this);
        btDelete.setOnClickListener(this);
        btQuery.setOnClickListener(this);
        btUpdate.setOnClickListener(this);

        btCancle.setOnClickListener(this);
        btConfirm.setOnClickListener(this);

        btUpdateCancle.setOnClickListener(this);
        btUpdateConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            //插入数据
            case R.id.btInsert:
                if (isEmpty(etName.getText().toString())) {
                    showToast("请输入姓名");
                    return;
                } else if (isEmpty(etAge.getText().toString())) {
                    showToast("请输入年龄");
                    return;
                } else if (isEmpty(etSex.getText().toString())) {
                    showToast("请输入性别");
                    return;
                } else if (isEmpty(etGrade.getText().toString())) {
                    showToast("请输入年级");
                    return;
                }


                final Student student = new Student();
                student.setName(etName.getText().toString());
                student.setAge(Integer.parseInt(etAge.getText().toString()));
                student.setSex(etSex.getText().toString());
                student.setGrade(etGrade.getText().toString());
                if (tvState.getVisibility() == View.GONE) {
                    tvState.setVisibility(View.VISIBLE);
                }
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        studentDao.insert(student);
                        Message message = Message.obtain();
                        message.what = INSERT;
                        handler.sendMessage(message);
                    }
                });
                break;

            //删除数据
            case R.id.btDelete:
                deleteDialog.show();
                break;

            //查询数据
            case R.id.btQuery:
                queryStudent();
                break;

            //更新数据
            case R.id.btUpdate:
                updateDialog.show();
                break;

            case R.id.btCancle:
                deleteDialog.dismiss();
                break;

            case R.id.btConfirm:
                if(isEmpty(etId.getText().toString())){
                    showToast("请输入你要删除的ID");
                    return;
                }
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        //可以直接根据ID删除，studentDao.deleteByKey();
                        //也可以根据其他值获取实体Student,再获取ID进行删除
                        //这里使用第二种方法
                        long studentId = Long.parseLong(etId.getText().toString());
                        Student deleteStudent = studentDao.queryBuilder()
                                .where(StudentDao.Properties.Id.eq(studentId)).build().unique();
                        Message message = Message.obtain();
                        message.what = DELETE;
                        message.obj = deleteStudent;
                        handler.sendMessage(message);
                    }
                });
                deleteDialog.dismiss();
                break;

            case R.id.btUpdateCancle:
                updateDialog.dismiss();
                break;

            case R.id.btUpdateConfirm:
                if(isEmpty(etUpdateId.getText().toString())){
                    showToast("请输入你要删除的ID");
                    return;
                }

                if(isEmpty(etUpdateAge.getText().toString())){
                    showToast("请输入你要修改的年龄");
                    return;
                }

                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        long updateId = Long.parseLong(etUpdateId.getText().toString());
                        //通过查找ID获取实体Student,再进行更新
                        Student updateStudent = studentDao.queryBuilder()
                                .where(StudentDao.Properties.Id.eq(updateId)).build().unique();
                        Message message = Message.obtain();
                       Log.e("RunnableThreadId" , "ThreadId = "+Thread.currentThread().getId());
                        message.what = UPDATE;
                        updateStudent.setAge(Integer.parseInt(etUpdateAge.getText().toString()));
                        if(updateStudent != null) {
                            studentDao.update(updateStudent);
                            message.obj = "success";
                        }else {
                            message.obj = "fail";
                        }
                        handler.sendMessage(message);
                    }
                });
                updateDialog.dismiss();
                break;

            default:
                break;

        }
    }

    private void queryStudent(){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
               List<Student> studentList = studentDao.queryBuilder().build().list();
                Message message = Message.obtain();
                message.what = QUERY;
                message.obj = studentList;
                handler.sendMessage(message);
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private boolean isEmpty(String str) {
        if (str == null || str.length() == 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //移除所有回调，防止内存泄漏
        handler.removeCallbacksAndMessages(null);
        handler = null;
    }
}
