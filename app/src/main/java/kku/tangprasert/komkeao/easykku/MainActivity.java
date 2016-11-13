package kku.tangprasert.komkeao.easykku;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

// Ctrl +Alt + L = ReformatCode
public class MainActivity extends AppCompatActivity {
    //Explicite ต้องทำการประกาศตัวแปลก่อน เพื่อจะรู้ว่าแรมพอไหม
    private Button signInButton, signUpButton;
    private EditText userEditText,passwordEditText;
    private  String userString,passwordString;
    private MyConstant myConstant;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myConstant=new MyConstant();



        //Bind Widget
        signInButton = (Button) findViewById(R.id.button2);//alt+Enter2
        signUpButton = (Button) findViewById(R.id.button);
        userEditText = (EditText) findViewById(R.id.editText);
        passwordEditText = (EditText) findViewById(R.id.editText2);

        //Sign In Controller
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Get Value From Edit Text
                userString=userEditText.getText().toString().trim();
                passwordString=passwordEditText.getText().toString().trim();


                //Check Spase
                if (userString.equals("") || passwordString.equals("")) {
                    MyAlert muMyAlert = new MyAlert(MainActivity.this, R.drawable.bird48, getResources().getString(R.string.title_HaveSpace),
                            getResources().getString(R.string.message_HaveSpace));
                    muMyAlert.myDialog();
                    //Have Space
                } else {
                    //No Space
                    SynUser synUser = new SynUser(MainActivity.this);
                    synUser.execute(myConstant.getUrlGetJSON());

                }
            }//on Click
        });


        //Sign Up Controller
        signUpButton.setOnClickListener(new View.OnClickListener() {//Type new OnClick Listener
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,SignUpActivity.class));
            }
        });
    }
    //Main Method



    private class SynUser extends AsyncTask<String,Void,String>{
        //Explicite
        private Context context;
        private String[] nameStrings,phoneStrings,imageStrings;
        private String truePassword;
        private boolean aBoolean;

        public SynUser(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                OkHttpClient okHttpClient=new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                Request request=builder.url(strings[0]).build();
                Response response=okHttpClient.newCall(request).execute();
                return response.body().string();

            }catch (Exception e){
                Log.d("13NovV1","do In==>"+e.toString());
                return null;
            }
        }//DiInBack

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try{
                JSONArray jsonArray=new JSONArray(s);
                nameStrings=new String[jsonArray.length()];
                phoneStrings=new String[jsonArray.length()];
                imageStrings=new String[jsonArray.length()];
                for (int i=0;i<jsonArray.length();i++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    nameStrings[i]=jsonObject.getString("Name");
                    phoneStrings[i]=jsonObject.getString("Phone");
                    imageStrings[i]=jsonObject.getString("Image");
                    Log.d("13NovV3", "name(" + i + ") = =>" + nameStrings[i]);
                    if(userString.equals(jsonObject.getString("User"))){
                        aBoolean=false;
                        truePassword=jsonObject.getString("Password");

                    }
                    if(aBoolean){
                        //User False
                        MyAlert myAlert=new MyAlert(context,R.drawable.kon48,
                                getResources().getString(R.string.title_UserFalse),
                                getResources().getString(R.string.message_UserFalse));

                    } else if (passwordString.equals(truePassword)) {
                        //Password True
                        Toast.makeText(context,"Welcome",Toast.LENGTH_SHORT).show();

                        //Intent
                        Intent intent = new Intent(MainActivity.this, ServiceActivity.class);
                        intent.putExtra("Name",nameStrings);
                        intent.putExtra("Phone",phoneStrings);
                        intent.putExtra("Image", imageStrings);
                        startActivity(intent);
                        finish();

                    } else {
                        //PasswordFalse
                        MyAlert myAlert=new MyAlert(context,R.drawable.rat48,
                                getResources().getString(R.string.title_PasswordFalse),
                                getResources().getString(R.string.message_PasswordFalse));

                    }

                }//for


            }catch (Exception e){

            }




        }
    }//Syn User
}
//Main Class นี่คือคลาสหลัก
