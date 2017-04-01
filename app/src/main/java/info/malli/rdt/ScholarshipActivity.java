package info.malli.rdt;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ipaulpro.afilechooser.utils.FileUtils;

import java.io.File;
import android.widget.AutoCompleteTextView;

public class ScholarshipActivity extends AppCompatActivity {
    private static final int REQUEST_PERMISSION_BANK_UPLOAD_STORAGE = 124;
    private static final int REQUEST_PERMISSION_AADHAR_UPLOAD_STORAGE = 125;
    private static final int REQUEST_PERMISSION_ACK_UPLOAD_STORAGE = 126;
    private static final int REQUEST_PERMISSION_BONAFIDE_UPLOAD_STORAGE = 127;
    private static final int REQUEST_PERMISSION_FEE_UPLOAD_STORAGE = 128;
    private static final int REQUEST_PERMISSION_MARKS1_UPLOAD_STORAGE = 129;
    private static final int REQUEST_PERMISSION_MARKS2_UPLOAD_STORAGE = 130;
    private Button btnBankAccountUpload, btnApplyNow, btnAadharUpload,btnAckUpload,btnBonafideUpload,btnFeeUpload,btnMarks1Upload,btnMarks2Upload;
    private EditText etStuName,etFatherName,etRollNo,etGender,etAddr,etMobile,etEmail,etIncome,etCollegeName,etCourse,etYear,etPreviousYearPercentage,etAadhar,etBankName,etIFSC,etBankAccountNumber;
    private File bankFile,aadharFile,ackFile,bonafideFile,feeFile,marks1File,marks2File;
    private Intent data;
    private ScrollView scrollView;
    private StorageReference storageRef;
    private DatabaseReference databaseRef;
    private int scrollX=0,scrollY=0;
    private String[] Gender= {"MALE","FEMALE"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schorlarship);
        scrollView = (ScrollView) findViewById(R.id.activity_schorlarship);
        btnBankAccountUpload = (Button) findViewById(R.id.btn_bank_account_upload);
        btnAadharUpload = (Button) findViewById(R.id.btn_aadhar_upload);
        btnAckUpload = (Button) findViewById(R.id.btn_ack_upload);
        btnApplyNow = (Button) findViewById(R.id.btn_apply);
        btnBonafideUpload = (Button) findViewById(R.id.btn_bonafide_upload);
        btnFeeUpload = (Button) findViewById(R.id.btn_fee_upload);
        btnMarks1Upload = (Button) findViewById(R.id.btn_marks1_upload);
        btnMarks2Upload = (Button) findViewById(R.id.btn_marks2_upload);

        etStuName = (EditText) findViewById(R.id.et_stuname);
        etFatherName = (EditText) findViewById(R.id.et_father_name);
        etRollNo = (EditText) findViewById(R.id.et_rollno);
        etGender = (AutoCompleteTextView) findViewById(R.id.et_gender);
        etAddr = (EditText) findViewById(R.id.et_addr);
        etMobile = (EditText) findViewById(R.id.et_mobile);
        etEmail = (EditText) findViewById(R.id.et_email);
        etIncome = (EditText) findViewById(R.id.et_income);
        etCollegeName = (EditText) findViewById(R.id.et_college_name);
        etCourse = (EditText) findViewById(R.id.et_course);
        etYear = (EditText) findViewById(R.id.et_year);
        etPreviousYearPercentage = (EditText) findViewById(R.id.et_prev_year_percent);
        etAadhar = (EditText) findViewById(R.id.et_aadhar);
        etBankName = (EditText) findViewById(R.id.et_bank_name);
        etIFSC = (EditText) findViewById(R.id.et_ifsc);
        etBankAccountNumber = (EditText) findViewById(R.id.et_bank_acct_number);

        storageRef = FirebaseStorage.getInstance().getReference();
        databaseRef = FirebaseDatabase.getInstance().getReference();

        btnBankAccountUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genericUploadClickListener(REQUEST_PERMISSION_BANK_UPLOAD_STORAGE);
            }
        });
        btnAadharUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genericUploadClickListener(REQUEST_PERMISSION_AADHAR_UPLOAD_STORAGE);
            }
        });
        btnAckUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genericUploadClickListener(REQUEST_PERMISSION_ACK_UPLOAD_STORAGE);
            }
        });
        btnBonafideUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genericUploadClickListener(REQUEST_PERMISSION_BONAFIDE_UPLOAD_STORAGE);
            }
        });
        btnFeeUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genericUploadClickListener(REQUEST_PERMISSION_FEE_UPLOAD_STORAGE);
            }
        });
        btnMarks1Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genericUploadClickListener(REQUEST_PERMISSION_MARKS1_UPLOAD_STORAGE);
            }
        });
        btnMarks2Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genericUploadClickListener(REQUEST_PERMISSION_MARKS2_UPLOAD_STORAGE);
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, Gender);
        //Getting the instance of AutoCompleteTextView
        AutoCompleteTextView actv = (AutoCompleteTextView) findViewById(R.id.et_gender);
        actv.setThreshold(1);//will start working from first character
        actv.setAdapter(adapter);
        btnApplyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stuName,fatherName,rollNo,gender,addr,mobile,email,income,collegeName,course,year,previousYearPercentage,aadhar,bankName,ifsc,bankAccountNumber;
                stuName = etStuName.getText().toString();
                fatherName = etFatherName.getText().toString();
                rollNo = etRollNo.getText().toString();
                gender = etGender.getText().toString();
                addr = etAddr.getText().toString();
                mobile = etMobile.getText().toString();
                email = etEmail.getText().toString();
                income = etIncome.getText().toString();
                collegeName = etCollegeName.getText().toString();
                course = etCourse.getText().toString();
                year = etYear.getText().toString();
                previousYearPercentage = etPreviousYearPercentage.getText().toString();
                aadhar = etAadhar.getText().toString();
                bankName = etBankName.getText().toString();
                ifsc = etIFSC.getText().toString();
                bankAccountNumber = etBankAccountNumber.getText().toString();

                if(TextUtils.isEmpty(stuName)) {
                    etStuName.setError("Student Name can't be empty");
                    return;
                }
                if(TextUtils.isEmpty(fatherName)) {
                    etFatherName.setError("Father Name can't be empty");
                    return;
                }
                if(TextUtils.isEmpty(rollNo)) {
                    etRollNo.setError("Roll No can't be empty");
                    return;
                }
                if(TextUtils.isEmpty(gender)) {
                    etGender.setError("Gender can't be empty");
                    return;
                }
                if(TextUtils.isEmpty(addr)) {
                    etAddr.setError("Address can't be empty");
                    return;
                }
                if(TextUtils.isEmpty(mobile)) {
                    etMobile.setError("Mobile can't be empty");
                    return;
                }
                if(TextUtils.isEmpty(email)) {
                    etEmail.setError("Email can't be empty");
                    return;
                }
                if(TextUtils.isEmpty(income)) {
                    etIncome.setError("Income can't be empty");
                    return;
                }
                if(TextUtils.isEmpty(collegeName)) {
                    etCollegeName.setError("College Name can't be empty");
                    return;
                }
                if(TextUtils.isEmpty(course)) {
                    etCourse.setError("Course can't be empty");
                    return;
                }
                if(TextUtils.isEmpty(year)) {
                    etYear.setError("Year can't be empty");
                    return;
                }
                if(TextUtils.isEmpty(previousYearPercentage)) {
                    etPreviousYearPercentage.setError("Percentage can't be empty");
                    return;
                }
                if(TextUtils.isEmpty(aadhar)) {
                    etAadhar.setError("Aadhar can't be empty");
                    return;
                }
                if(TextUtils.isEmpty(bankName)) {
                    etBankName.setError("Bank Name can't be empty");
                    return;
                }
                if(TextUtils.isEmpty(ifsc)) {
                    etIFSC.setError("IFSC Code can't be empty");
                    return;
                }
                if(TextUtils.isEmpty(bankAccountNumber)) {
                    etBankAccountNumber.setError("Bank Account Number can't be empty");
                    return;
                }

                if(bankFile == null) {
                    Toast.makeText(ScholarshipActivity.this,"Please Upload Bank  file",Toast.LENGTH_LONG).show();
                    return;
                }
                if(aadharFile == null) {
                    Toast.makeText(ScholarshipActivity.this,"Please Upload Aadhar file",Toast.LENGTH_LONG).show();
                    return;
                }
                if(ackFile == null) {
                    Toast.makeText(ScholarshipActivity.this,"Please Upload Acknowledgement file",Toast.LENGTH_LONG).show();
                    return;
                }
                if(bonafideFile == null) {
                    Toast.makeText(ScholarshipActivity.this,"Please Upload Bonafide  file",Toast.LENGTH_LONG).show();
                    return;
                }
                if(feeFile == null) {
                    Toast.makeText(ScholarshipActivity.this,"Please Upload Fee_Receipt file",Toast.LENGTH_LONG).show();
                    return;
                }
                if(marks1File == null) {
                    Toast.makeText(ScholarshipActivity.this,"Please Upload Sem1 Marks file",Toast.LENGTH_LONG).show();
                    return;
                }
                if(marks2File == null) {
                    Toast.makeText(ScholarshipActivity.this,"Please Upload Sem2 Marks file",Toast.LENGTH_LONG).show();
                    return;
                }
                String directoryName = aadhar;
                uploadFile(bankFile,directoryName,"bank_account");
                uploadFile(aadharFile,directoryName,"aadhar");
                uploadFile(ackFile,directoryName,"acknowledgment_letter");
                uploadFile(bonafideFile,directoryName,"bonafide_college");
                uploadFile(feeFile,directoryName,"fee_receipt");
                uploadFile(marks1File,directoryName,"marks_memo_sem1");
                uploadFile(marks2File,directoryName,"marks_memo_sem2");
                databaseRef.child("students").child(aadhar).removeValue();
                Student s = new Student(stuName,fatherName,rollNo,gender,addr,mobile,email,income,collegeName,course,year,previousYearPercentage,aadhar,bankName,ifsc,bankAccountNumber,"processing");
                databaseRef.child("students").child(aadhar).setValue(s);
            }
        });
    }
    private void uploadFile(File f, String directoryName,String newname) {
        //if there is a file to upload
        if (f != null) {
            final String filename = f.getName();
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(this);
            final File final_file = f;
            progressDialog.setTitle("Uploading " + filename);
            progressDialog.show();
            String[] fileNameSplits = filename.split("\\.");
            String newName = newname + "." + fileNameSplits[fileNameSplits.length-1];
            StorageReference riversRef = storageRef.child(directoryName + "/" +newName);
            riversRef.putFile(Uri.fromFile(f))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying a success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_SHORT).show();
                            if(final_file == marks2File) {
                                Toast.makeText(ScholarshipActivity.this,"Applied Successfully",Toast.LENGTH_LONG).show();
                                Intent i = new Intent(ScholarshipActivity.this,MainActivity.class);
                                ScholarshipActivity.this.startActivity(i);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();
                            if(final_file == marks2File) {
                                Toast.makeText(ScholarshipActivity.this,"Failed to Upload Files",Toast.LENGTH_LONG).show();
                                Intent i = new Intent(ScholarshipActivity.this,MainActivity.class);
                                ScholarshipActivity.this.startActivity(i);
                            }

                            //and displaying error message
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploading: " + filename + " " + ((int) progress) + "%...");
                        }
                    });
        }
        //if there is not any file
        else {
            //you can display an error toast
        }
    }
    private void genericUploadClickListener(int r) {
        int hasReadPermission = ContextCompat.checkSelfPermission(ScholarshipActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE);
        if(hasReadPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ScholarshipActivity.this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, r);
            return;
        }
        startFileChooserIntent(r);
    }

    private void startFileChooserIntent(int r) {
        Intent getContentIntent = FileUtils.createGetContentIntent();

        Intent intent = Intent.createChooser(getContentIntent, "Select a file");
        startActivityForResult(intent, r);
    }
    private void getFile(Intent data,int type) {
        final Uri uri = data.getData();

        // Get the File path from the Uri
        String path = FileUtils.getPath(this, uri);
        // Alternatively, use FileUtils.getFile(Context, Uri)
        if (path != null && FileUtils.isLocal(path)) {
            String filename=path.substring(path.lastIndexOf("/")+1);
            File file = new File(path);
            switch(type) {
                case REQUEST_PERMISSION_BANK_UPLOAD_STORAGE:
                    bankFile = file;
                    setReplaceTextToButton(btnBankAccountUpload,"Replace Bank ( " + filename + " )");
                    break;
                case REQUEST_PERMISSION_AADHAR_UPLOAD_STORAGE:
                    aadharFile = file;
                    setReplaceTextToButton(btnAadharUpload,"Replace Aadhar ( " + filename + " )");
                    break;
                case REQUEST_PERMISSION_ACK_UPLOAD_STORAGE:
                    ackFile = file;
                    setReplaceTextToButton(btnAckUpload,"Replace Acknowledgment ( " + filename + " )");
                    break;
                case REQUEST_PERMISSION_BONAFIDE_UPLOAD_STORAGE:
                    bonafideFile = file;
                    setReplaceTextToButton(btnBonafideUpload,"Replace Bonafide ( " + filename + " )");
                    break;
                case REQUEST_PERMISSION_FEE_UPLOAD_STORAGE:
                    feeFile = file;
                    setReplaceTextToButton(btnFeeUpload,"Replace Fee ( " + filename + " )");
                    break;
                case REQUEST_PERMISSION_MARKS1_UPLOAD_STORAGE:
                    marks1File = file;
                    setReplaceTextToButton(btnMarks1Upload,"Replace Marks-1 ( " + filename + " )");
                    break;
                case REQUEST_PERMISSION_MARKS2_UPLOAD_STORAGE:
                    marks2File = file;
                    setReplaceTextToButton(btnMarks2Upload,"Replace Marks-2 ( " + filename + " )");
                    break;
                default:
                    break;
            }
        }
    }
    private void setReplaceTextToButton(Button b,String s) {
        b.setText(s);
        b.setTextSize(12);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_PERMISSION_BANK_UPLOAD_STORAGE:
            case REQUEST_PERMISSION_AADHAR_UPLOAD_STORAGE:
            case REQUEST_PERMISSION_ACK_UPLOAD_STORAGE:
            case REQUEST_PERMISSION_BONAFIDE_UPLOAD_STORAGE:
            case REQUEST_PERMISSION_FEE_UPLOAD_STORAGE:
            case REQUEST_PERMISSION_MARKS1_UPLOAD_STORAGE:
            case REQUEST_PERMISSION_MARKS2_UPLOAD_STORAGE:
                if (resultCode == RESULT_OK) {
                    getFile(data, requestCode);
                }
                break;
            default:
                break;
        }
        scrollView.post(new Runnable() {
            public void run() {
                Log.e("test",scrollX + " " + scrollY);
                scrollView.scrollTo(scrollX,scrollY);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case REQUEST_PERMISSION_BANK_UPLOAD_STORAGE:
            case REQUEST_PERMISSION_AADHAR_UPLOAD_STORAGE:
            case REQUEST_PERMISSION_ACK_UPLOAD_STORAGE:
            case REQUEST_PERMISSION_BONAFIDE_UPLOAD_STORAGE:
            case REQUEST_PERMISSION_FEE_UPLOAD_STORAGE:
            case REQUEST_PERMISSION_MARKS1_UPLOAD_STORAGE:
            case REQUEST_PERMISSION_MARKS2_UPLOAD_STORAGE:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startFileChooserIntent(requestCode);
                }
                else {
                    Toast.makeText(this,"Unable to Acces Internal Storage",Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        scrollX = scrollView.getScrollX();
        scrollY = scrollView.getChildAt(0).getHeight();
    }

}

class Student {
    String stuName,fatherName,rollNo,gender,addr,mobile,email,income,collegeName,course,year,previousYearPercentage,aadhar,bankName,ifsc,bankAccountNumber,status;

    public Student() {

    }
    public Student(String stuName,String fatherName,String rollNo,String gender,String addr,String mobile,String email,String income,String collegeName,String course,String year,String previousYearPercentage,String aadhar,String bankName,String ifsc,String bankAccountNumber,String status) {
        this.stuName = stuName;
        this.fatherName = fatherName;
        this.rollNo = rollNo;
        this.gender = gender;
        this.addr = addr;
        this.mobile = mobile;
        this.email = email;
        this.income = income;
        this.collegeName = collegeName;
        this.course = course;
        this.year = year;
        this.previousYearPercentage = previousYearPercentage;
        this.aadhar = aadhar;
        this.bankName = bankName;
        this.ifsc = ifsc;
        this.bankAccountNumber = bankAccountNumber;
        this.status = status;

    }
}