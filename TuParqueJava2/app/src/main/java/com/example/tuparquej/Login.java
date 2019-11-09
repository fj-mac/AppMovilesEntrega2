package com.example.tuparquej;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login extends AppCompatActivity {

    private static final int RC_SIGN_IN = 100;

    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton login;
    private Button login2;
    private Button signIn;
    private FirebaseAuth mAuth;
    private EditText correo;
    private EditText clave;
    private EditText clave2;
    public static ArrayList<Integer> listaFavoritos;
    public static FirebaseUser user=null;
    private FirebaseFirestore db= FirebaseFirestore.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login=findViewById(R.id.googleSignIn);
        login2=findViewById(R.id.button3);
        signIn=findViewById(R.id.buttonSignUp);
        correo=findViewById(R.id.editText3);
        clave=findViewById(R.id.editText2);
        clave2=findViewById(R.id.editText4);



        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                googleLogin();
            }
        });
        login2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                normalLogin();
            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                clave2.setVisibility(View.VISIBLE);
                normalSignUp();
            }
        });
        listaFavoritos=new ArrayList<>();



        mAuth = FirebaseAuth.getInstance();
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient= GoogleSignIn.getClient(this,gso);
    }
    private void googleLogin(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(this.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }
        else{
            //Snackbar snackbar = new Snackbar.make(findViewById(android.R.id.content), "Para realizar el LogIn debe tener conexion a internet. Verifique e intente mas tarde", Snackbar.LENGTH_INDEFINITE);
            //Snackbar snackbar =new Snackbar.make(this,"a",Snackbar.LENGTH_LONG);
            Toast.makeText(this, "Para iniciar sesión debe tener conexión a internet. Verifique e intente más tarde", Toast.LENGTH_LONG).show();
        }

    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(getCurrentFocus()!=null)
        {
            InputMethodManager imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
        return super.dispatchTouchEvent(ev);
    }
    private void normalSignUp(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(this.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            String email=correo.getText().toString();
            String password=clave.getText().toString();
            String password2=clave2.getText().toString();
            if(password.equals(password2))
            {
                if (isValidPassword(password))
                {
                    if(password.length()>=6)
                    {
                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            //Log.d(TAG, "createUserWithEmail:success");
                                            user = mAuth.getCurrentUser();
                                            //updateUI(user);
                                            finish();
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Log.w("OJO", "createUserWithEmail:failure", task.getException());
                                            Toast.makeText(Login.this, "Ocurrio un error en nuestros servidores. Por favor intentelo mas tarde", Toast.LENGTH_LONG).show();
                                            //updateUI(null);
                                        }

                                        // ...
                                    }
                                });
                    }
                    else
                    {
                        Toast.makeText(Login.this, "La contraseña debe contener al menos 6 caracteres", Toast.LENGTH_LONG).show();
                    }

                }
                else {
                    Toast.makeText(this, "La contraseña debe ser menor a 20 caracteres y contener al menos 1 letra mayuscula, un caracter especial y un numero", Toast.LENGTH_LONG).show();

                }

            }
            else
            {
                Toast.makeText(this, "Las contraseñas no coinciden. Por favor verifiquelas e intente nuevamente", Toast.LENGTH_SHORT).show();
            }


        }
        else{
            //Snackbar snackbar = new Snackbar.make(findViewById(android.R.id.content), "Para realizar el LogIn debe tener conexion a internet. Verifique e intente mas tarde", Snackbar.LENGTH_INDEFINITE);
            //Snackbar snackbar =new Snackbar.make(this,"a",Snackbar.LENGTH_LONG);
            Toast.makeText(this, "Para iniciar sesión debe tener conexión a internet. Verifique e intente más tarde", Toast.LENGTH_LONG).show();
        }

    }
    private void normalLogin() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(this.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            String email=correo.getText().toString();
            String password=clave.getText().toString();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                //Log.d(TAG, "signInWithEmail:success");
                                user = mAuth.getCurrentUser();
                                new Thread(new Runnable() {
                                    public void run() {
                                        verificarFavoritos();
                                    }
                                }).start();
                                finish();
                                //updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                //Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(Login.this, "Ha ingresado un usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }

                            // ...
                        }
                    });
        }
        else{
            //Snackbar snackbar = new Snackbar.make(findViewById(android.R.id.content), "Para realizar el LogIn debe tener conexion a internet. Verifique e intente mas tarde", Snackbar.LENGTH_INDEFINITE);
            //Snackbar snackbar =new Snackbar.make(this,"a",Snackbar.LENGTH_LONG);
            Toast.makeText(this, "Para iniciar sesión debe tener conexión a internet. Verifique e intente más tarde", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately

                Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                // ...
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            user = mAuth.getCurrentUser();

                            new Thread(new Runnable() {
                                public void run() {
                                    verificarFavoritos();
                                }
                            }).start();
                            Toast.makeText(Login.this, "Se ha iniciado con el correo: "+user.getDisplayName(), Toast.LENGTH_SHORT).show();
                            finish();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Login.this, "Falla el login", Toast.LENGTH_SHORT).show();
                            //Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Login.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean verificar(){
        return true;
    }
    private boolean verificarSignUp(){
        return true;
    }
    public void verificarFavoritos(){
        final DocumentReference favoritos=db.collection("Usuarios").document(user.getUid());
        favoritos.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot ds, @Nullable FirebaseFirestoreException e) {
                if(e !=null){
                    return;
                }
                Map<String, Object> map = ds.getData();
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                        int parque=Integer.parseInt(entry.getValue().toString());
                       listaFavoritos.add(parque);
                        Log.d("Guardando","Lo que esta guardando es: "+parque);

                }
            }
        });
    }
    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }
}
