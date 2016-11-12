package kku.tangprasert.komkeao.easykku;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

// Ctrl +Alt + L = ReformatCode
public class MainActivity extends AppCompatActivity {
    //Explicite ต้องทำการประกาศตัวแปลก่อน เพื่อจะรู้ว่าแรมพอไหม
    private Button signInButton, signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Bind Widget
        signInButton = (Button) findViewById(R.id.button);//alt+Enter2
        signUpButton = (Button) findViewById(R.id.button2);

        //Sign Up Controller
        signUpButton.setOnClickListener(new View.OnClickListener() {//Type new OnClick Listener
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,SignUpActivity.class));
            }
        });
    }
    //Main Method
}
//Main Class นี่คือคลาสหลัก
