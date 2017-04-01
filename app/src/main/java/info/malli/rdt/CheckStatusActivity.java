package info.malli.rdt;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

public class CheckStatusActivity extends AppCompatActivity {
    private EditText etAadhar;
    private Button btnCheck,btnGoBack;
    private ImageView imStatus;
    private TextView tvStatus;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_status);
        etAadhar = (EditText) findViewById(R.id.et_check_aadhar);
        btnCheck = (Button) findViewById(R.id.btn_check_aadhar);
        btnGoBack = (Button) findViewById(R.id.go_back);

        imStatus = (ImageView) findViewById(R.id.im_status);
        tvStatus = (TextView) findViewById(R.id.tv_status);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CheckStatusActivity.this,MainActivity.class);
                CheckStatusActivity.this.startActivity(i);
            }
        });

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String aadhar = etAadhar.getText().toString();
                if(TextUtils.isEmpty(aadhar) || aadhar.length() != 12) {
                    etAadhar.setError("Enter a valid Aadhar Number");
                    return;
                }
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild("students/" + aadhar + "/status")) {
                            String status = (String) dataSnapshot.child("students").child(aadhar).child("status").getValue();
                            status = status.toLowerCase();
                            if(status.startsWith("g")) {
                                imStatus.setBackgroundResource(R.drawable.tick_mark);
                                tvStatus.setTextColor(Color.parseColor("#00FF00"));
                                tvStatus.setText("Scholarship Granted.Please Contact RDT officer for Proceedings. ");
                            }
                            else if(status.startsWith("p")) {
                                imStatus.setBackgroundResource(R.drawable.processing);
                                tvStatus.setTextColor(Color.parseColor("#DE8737"));
                                tvStatus.setText("Under Processing");
                            }
                            else if(status.startsWith("r")) {
                                tvStatus.setTextColor(Color.parseColor("#FF0000"));
                                imStatus.setBackgroundResource(R.drawable.rejected);
                                tvStatus.setTextColor(Color.RED);
                                tvStatus.setText("Scholarship Rejected");
                            }
                            else {
                                tvStatus.setTextColor(Color.parseColor("#FF0000"));
                                imStatus.setBackgroundResource(R.drawable.admin);
                                tvStatus.setTextColor(Color.RED);
                                tvStatus.setText("Scholarship status Not Detected,Please Contact RDT Officer!!");
                            }
                        }
                        else {
                            Toast.makeText(CheckStatusActivity.this,"Your Application Not Found",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

    }
}
