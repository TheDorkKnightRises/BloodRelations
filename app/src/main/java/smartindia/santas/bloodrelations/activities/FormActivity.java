package smartindia.santas.bloodrelations.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import smartindia.santas.bloodrelations.Constants;
import smartindia.santas.bloodrelations.R;

public class FormActivity extends AppCompatActivity {

    private EditText ques1_ans;
    private RadioGroup ques2,ques3,ques4,ques5,ques6,ques7,ques8;
    private RadioButton ques2_ans,ques3_ans,ques4_ans,ques5_ans,ques6_ans,ques7_ans,ques8_ans;
    private Button submitButton;
    private String ans1,ans2,ans3,ans4,ans5,ans6,ans7,ans8;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSharedPreferences(Constants.PREFS, MODE_PRIVATE).getBoolean(Constants.DARK_THEME, false))
            setTheme(R.style.AppTheme_Dark);
        setContentView(R.layout.activity_form);


        ques1_ans = (EditText)findViewById(R.id.ques1);
        ques2 = (RadioGroup)findViewById(R.id.ques2);
        ques3 = (RadioGroup)findViewById(R.id.ques3);
        ques4 = (RadioGroup)findViewById(R.id.ques4);
        ques5 = (RadioGroup)findViewById(R.id.ques5);
        ques6 = (RadioGroup)findViewById(R.id.ques6);
        ques7 = (RadioGroup)findViewById(R.id.ques7);
        ques8 = (RadioGroup)findViewById(R.id.ques8);

        submitButton = (Button)findViewById(R.id.submit_form_button);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ques2_ans = (RadioButton)findViewById(ques2.getCheckedRadioButtonId());
                ques3_ans = (RadioButton)findViewById(ques3.getCheckedRadioButtonId());
                ques4_ans = (RadioButton)findViewById(ques4.getCheckedRadioButtonId());
                ques5_ans = (RadioButton)findViewById(ques5.getCheckedRadioButtonId());
                ques6_ans = (RadioButton)findViewById(ques6.getCheckedRadioButtonId());
                ques7_ans = (RadioButton)findViewById(ques7.getCheckedRadioButtonId());
                ques8_ans = (RadioButton)findViewById(ques8.getCheckedRadioButtonId());

                ans1 = ques1_ans.getText().toString();
                ans2 = ques2_ans.getText().toString();
                ans3 = ques3_ans.getText().toString();
                ans4 = ques4_ans.getText().toString();
                ans5 = ques5_ans.getText().toString();
                ans6 = ques6_ans.getText().toString();
                ans7 = ques7_ans.getText().toString();
                ans8 = ques8_ans.getText().toString();

//                databaseReference.push().setValue(new Question("1",ans1));
//                databaseReference.push().setValue(new Question("2",ans2));
//                databaseReference.push().setValue(new Question("3",ans3));
//                databaseReference.push().setValue(new Question("4",ans4));
//                databaseReference.push().setValue(new Question("5",ans5));
//                databaseReference.push().setValue(new Question("6",ans6));
//                databaseReference.push().setValue(new Question("7",ans7));
//                databaseReference.push().setValue(new Question("8",ans8));

                for(int i=1;i<=8;i++){
                    databaseReference = firebaseDatabase.getReference().child("users").child(user.getUid()).child("questions").child("question"+i);
                    switch(i){
                        case 1:
                            databaseReference.setValue(ans1);
                            break;
                        case 2:
                            databaseReference.setValue(ans2);
                            break;
                        case 3:
                            databaseReference.setValue(ans3);
                            Log.v("tag",ans3);
                            break;
                        case 4:
                            databaseReference.setValue(ans4);
                            break;
                        case 5:
                            databaseReference.setValue(ans5);
                            break;
                        case 6:
                            databaseReference.setValue(ans6);
                            break;
                        case 7:
                            databaseReference.setValue(ans7);
                            break;
                        case 8:
                            databaseReference.setValue(ans8);
                            break;
                    }
                }
            }
        });
    }
}
