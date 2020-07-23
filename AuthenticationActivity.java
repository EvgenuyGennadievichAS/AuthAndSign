package com.rdv.slcard.Authentication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.rdv.slcard.API.API;
import com.rdv.slcard.API.ResultLogin;
import com.rdv.slcard.API.ResultRegister;
import com.rdv.slcard.MainActivity;
import com.rdv.slcard.R;
import com.rdv.slcard.StateSave;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthenticationActivity extends AppCompatActivity {
    private Retrofit retrofit;
    private Gson gson;
    private Button btnSignIn;
    private Button btnRegister;
    private RelativeLayout root;
    private SharedPreferences sharedPreferences;
    private API api;
    private  final String URL = "https://ccardrus.ru";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);


        setContentView(R.layout.activity_authentication);
        init();
        root = findViewById(R.id.root_element);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegisterWindow();
            }


        });
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignInWindow();
            }


        });
    }

    private void init() {
        btnSignIn=findViewById(R.id.btn_sign_in);
        btnRegister = findViewById(R.id.btn_register);
        retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api =retrofit.create(API.class);
    }

    private void showSignInWindow(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Войти");
        dialog.setMessage("Введите данные для входа");

        LayoutInflater inflater = LayoutInflater.from(this);
        View signInWindow = inflater.inflate(R.layout.sign_in_window,null);
        dialog.setView(signInWindow);

        final MaterialEditText email = signInWindow.findViewById(R.id.loginField);
        final MaterialEditText password = signInWindow.findViewById(R.id.passField);


        dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });
        dialog.setPositiveButton("Войти", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

                // вход пользователя
                Map<String,String> loginUser = new HashMap<>();
                loginUser.put("login",email.getText().toString());
                loginUser.put("password",password.getText().toString());
                Call<ResultLogin> resultLoginCall = api.inSignBack(loginUser);
                resultLoginCall.enqueue(new Callback<ResultLogin>() {
                    @Override
                    public void onResponse(Call<ResultLogin> call, Response<ResultLogin> response) {
                        if (response.body().getState().equals("success")){
                            String login =  String.valueOf(response.body().getUser().get("login").toString());
                            //сохранение состояния
                            sharedPreferences = getSharedPreferences(StateSave.getAppPreferences(),Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(StateSave.getAppPreferencesAuthEmailCounter(),email.getText().toString());
                            editor.putBoolean(StateSave.getAppPreferencesAuthBooleanSignIn(),true);
                            editor.putString(StateSave.getAppReferencesCityIdCard(),login);
                            editor.apply();

                            //StateApp.setEmail( response.body().getUser().get("email"));
                            startActivity(new Intent(AuthenticationActivity.this, MainActivity.class));
                            Snackbar.make(root,"Вы успешно авторизировались",Snackbar.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Snackbar.make(root,response.body().getErrors().get("password"),Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultLogin> call, Throwable throwable) {
                        Snackbar.make(root,"Ошибка сервера,зайдите позже",Snackbar.LENGTH_SHORT).show();

                    }
                });


            }
        });
        dialog.show();
    }

    private void showRegisterWindow() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Зарегистрироваться");
        dialog.setMessage("Введите данные для регистрации");

        LayoutInflater inflater = LayoutInflater.from(this);
        View registerWindow = inflater.inflate(R.layout.register_window,null);
        dialog.setView(registerWindow);

        final MaterialEditText card = registerWindow.findViewById(R.id.cardField1);
        final MaterialEditText email = registerWindow.findViewById(R.id.emailField1);
        final MaterialEditText city = registerWindow.findViewById(R.id.сityField1);
        final MaterialEditText name = registerWindow.findViewById(R.id.nameField1);
        final MaterialEditText suname = registerWindow.findViewById(R.id.sunameField1);

        dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });
        dialog.setPositiveButton("Зарегистрироваться", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if(TextUtils.isEmpty(card.getText().toString())){
                    Snackbar.make(root,"Введите card",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(email.getText().toString())){
                    Snackbar.make(root,"Введите email",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                // Регистрация пользователя
                Map<String,String> registerUser = new HashMap<>();
                registerUser.put("card",card.getText().toString());
                registerUser.put("email",email.getText().toString());
                registerUser.put("city",city.getText().toString());
                registerUser.put("debug","0");
                Call<ResultRegister> resultRegisterCall =api.registerBack(registerUser);
                resultRegisterCall.enqueue(new Callback<ResultRegister>() {
                    @Override
                    public void onResponse(Call<ResultRegister> call, Response<ResultRegister> response) {
                        if(response.body().getErrors().isEmpty()){
                            Snackbar.make(root,"Добро пожаловать! Войдите в свой аккаунт",Snackbar.LENGTH_SHORT).show();
                            //сохранение состояния
                          //  sharedPreferences = getSharedPreferences(StateApp.getAppPreferences(),Context.MODE_PRIVATE);
                            //SharedPreferences.Editor editor = sharedPreferences.edit();
                          //  editor.putString(StateApp.getAppPreferencesAuthEmailCounter(),email.getText().toString());


                           // editor.putBoolean(StateApp.getAppPreferencesAuthBooleanSignIn(),true);
                          //  editor.apply();

                        }else
                        {
                            if(response.body().getErrors().get("code") !=null){
                                Snackbar.make(root,response.body().getErrors().get("code"),Snackbar.LENGTH_SHORT).show();
                            }
                            else if(response.body().getErrors().get("email") !=null){
                                Snackbar.make(root,response.body().getErrors().get("email"),Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultRegister> call, Throwable throwable) {
                        Snackbar.make(root,"Ошибка сервера,зайдите позже",Snackbar.LENGTH_SHORT).show();
                    }
                });

            }



                });



        dialog.show();
    }


    public static boolean isOnline(Context context)
    {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
        {
            return true;
        }
        return false;
    }

    public void onClick(View view) {
        sharedPreferences = getSharedPreferences(StateSave.getAppPreferences(),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(StateSave.getAppPreferencesAuthEmailCounter(),"Гость");
        editor.putBoolean(StateSave.getAppPreferencesAuthBooleanSignIn(),false);
        editor.apply();
        startActivity(new Intent(AuthenticationActivity.this, MainActivity.class));
        finish();
    }
    @Override
    protected void onStart() {
        super.onStart();
        loadState();

    }
    void loadState(){
        sharedPreferences = getSharedPreferences(StateSave.getAppPreferences(),Context.MODE_PRIVATE);

        boolean auth =sharedPreferences.getBoolean(StateSave.getAppPreferencesAuthBooleanCounter(),false);


        if(auth){
            startActivity(new Intent(AuthenticationActivity.this,MainActivity.class));
        }
    }

}
