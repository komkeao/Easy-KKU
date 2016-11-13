package kku.tangprasert.komkeao.easykku;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.jibble.simpleftp.SimpleFTP;

import java.io.File;

public class SignUpActivity extends AppCompatActivity {
    //Explicite
    private EditText nameEditText, phoneEditText, userEditText, passwordEditText;
    private ImageView imageView;
    private Button button;
    private String nameString, phoneString, userString, passwordString,imagePathString,imageNameString;
    private Uri uri;
    private  boolean aBoolean=true;
    private String urlAddUser="http://swiftcodingthai.com/kku/add_user_master.php";
    private  String urlImagge="http://swiftcodingthai.com/kku/Image";

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode==0)&&(resultCode==RESULT_OK)) { //เชคว่า OK ไหม มีรูปกลับมาไหม
            Log.d("12NovV1","ResultOK");
            uri=data.getData();
            aBoolean=false;
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                imageView.setImageBitmap(bitmap);
            }catch (Exception e){
                e.printStackTrace();
            }
            //Find path Of Image
            imagePathString=myFindPath(uri);
            Log.d("12NovV1","ImagePath >>"+imagePathString)   ;
            //Find Image Path
            imageNameString =imagePathString.substring(imagePathString.lastIndexOf("/"));
            Log.d("12NovV1","ImageName >>"+imageNameString);

        }

    }

    private String myFindPath(Uri uri) {
        String result =null;
        String[] strings={MediaStore.Images.Media.DATA};
        Cursor cursor=getContentResolver().query(uri,strings,null,null,null);
        if (cursor!=null) {
            cursor.moveToFirst();
            int index =cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            result=cursor.getString(index);
        }else {
            result=uri.getPath();
        }


        return result;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //bind Widget
        nameEditText = (EditText) findViewById(R.id.editText10);
        phoneEditText = (EditText) findViewById(R.id.editText9);
        userEditText = (EditText) findViewById(R.id.editText8);
        passwordEditText = (EditText) findViewById(R.id.editText7);
        imageView = (ImageView) findViewById(R.id.imageView2);
        button = (Button) findViewById(R.id.button3);

        //SignUp Controller
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get Values From Edit Text
                nameString=nameEditText.getText().toString().trim();
                phoneString=phoneEditText.getText().toString().trim();
                userString=userEditText.getText().toString().trim();
                passwordString=passwordEditText.getText().toString().trim();

                //Check Space
                if (nameString.equals("")|| phoneString.equals("")|| userString.equals("")||passwordString.equals("")) {  //Shift+ctrl+Enter
                    //Have Space
                    Log.d("12novV1","Have Space");
                    MyAlert myAlert = new MyAlert(SignUpActivity.this,R.drawable.doremon48,"มีช่องว่าง","กรุณากรอกให้ครบทุกช่อง");
                    myAlert.myDialog();
                } else if (aBoolean) {
                    //nonChose Image
                    MyAlert myAlert=new MyAlert(SignUpActivity.this,R.drawable.kon48,"ยังไม่ได้เลือกรูป","กรุณาเลือกรูป");
                    myAlert.myDialog();
                } else {
                    //choose IMage OK
                    upLoadImageToServer();
                    upLoadStringToServer();
                }

            }//Onclick
        });

        //Image Controller
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");//เปิดโปรแกรมที่ดูภาพได้
                startActivityForResult(intent.createChooser(intent,"โปรแกรมเลือกแอฟดูภาพ"),0);

            }
        });



    }//Main Method

    private void upLoadStringToServer() {
        AddNewUser addNewUser=new AddNewUser(SignUpActivity.this);
        addNewUser.execute(urlAddUser);

    }//upload
    //Create Inner Class
    private class AddNewUser extends AsyncTask<String,Void,String>{
    private Context context;

        public AddNewUser(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody=new FormEncodingBuilder()
                        .add("isAdd","true")
                        .add("Name",nameString)
                        .add("Phone",phoneString)
                        .add("User",userString)
                        .add("Password",passwordString)
                        .add("Image",urlImagge+imageNameString)
                        .build();
                Request.Builder builder = new Request.Builder();
                Request request=builder.url(strings[0]).post(requestBody).build();
                Response response =okHttpClient.newCall(request).execute();
                return  response.body().string();
                //เป็นURL ที่ Add มา แต่ละตัว

            }catch (Exception e){
                Log.d("13NovV1","e DoIn ==>"+e.toString());
                return null;
            }



        }//do on back

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("13NovV1","Result==>"+s);
            if(Boolean.parseBoolean(s)){
                Toast.makeText(context,"Upload Success",Toast.LENGTH_SHORT).show();
                finish();
            }else {
                Toast.makeText(context,"Upload Fail",Toast.LENGTH_SHORT).show();

            }
            //ทำงานเมื่อเมธอดหลักทำงานเสร็จแล้ว
        }
    }//Add New User Class

    private void upLoadImageToServer(){
        StrictMode.ThreadPolicy threadPolicy=new StrictMode.ThreadPolicy
                .Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);
        try {
            SimpleFTP simpleFTP=new SimpleFTP();
            simpleFTP.connect("ftp.swiftcodingthai.com",21,"kku@swiftcodingthai.com","Abc12345");
            simpleFTP.bin();
            simpleFTP.cwd("Image");
            simpleFTP.stor(new File(imagePathString));
            simpleFTP.disconnect();
            Log.d("12NovV1","ErrUpload>>>Success");

        }catch (Exception e){
            Log.d("12NovV1","ErrUpload>>>"+e.getMessage());
            e.printStackTrace();
        }

    }
}//Main Class
