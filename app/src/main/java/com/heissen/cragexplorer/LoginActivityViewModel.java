package com.heissen.cragexplorer;

import static android.content.Context.WIFI_SERVICE;
import static android.provider.Settings.System.getString;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.InputType;
import android.text.format.Formatter;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.GoogleAuthProvider;
import com.heissen.cragexplorer.models.Usuario;
import com.heissen.cragexplorer.request.ApiService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivityViewModel extends AndroidViewModel {


    private MutableLiveData<String> mCorreo;


    HashMap<String, Object> map;
    private FirebaseAuth auth;
    private MutableLiveData<GoogleSignInClient> mGoogleSignInClient;
    private MutableLiveData<HashMap<String, Object>> mUserData;
    private MutableLiveData<Usuario> mUsuario;
    public static final int RC_SIGN_IN = 20;


    public LoginActivityViewModel(@NonNull Application application) {
        super(application);
        mCorreo = new MutableLiveData<>();
        mUsuario = new MutableLiveData<>();
        mUserData = new MutableLiveData<>();
        mGoogleSignInClient = new MutableLiveData<>();
        map = new HashMap<>();
    }

    public LiveData<GoogleSignInClient> getmGoogleSignInClient() {
        return mGoogleSignInClient;
    }


    public LiveData<String> getmCorreo() {
        return mCorreo;
    }




    public void login(String correo, String clave) {
        ApiService.ApiInterface apiService = ApiService.getApiInferface();

        if (!correo.contains("@")) {
            Toast.makeText(getApplication(), "Debe brindar una direccion de correo valida", Toast.LENGTH_SHORT).show();
        } else {
            if (clave.isEmpty()) {
                Toast.makeText(getApplication(), "Debe ingresar una contraseña", Toast.LENGTH_SHORT).show();
            } else {

                Call<String> llamada = apiService.login(correo, clave);
                llamada.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            ApiService.guardarToken(getApplication(), "Bearer " + response.body());
                            Intent intent = new Intent(getApplication(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            getApplication().startActivity(intent);
                        } else {
                            Toast.makeText(getApplication(), "Correo y/o contraseña incorrecta", Toast.LENGTH_SHORT).show();
                            Log.d("salida", response.raw().message());
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d("salida", t.getMessage());
                    }
                });
            }
        }
    }





    /////////////////////////////////////

    public void firebaseAuth(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        ApiService.ApiInterface apiService = ApiService.getApiInferface();
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            Log.d("salida",user.getDisplayName());
                            if (user != null) {
                                String fullName = user.getDisplayName(); // User's display name

                                // Split the full name into first name and last name
                                String[] nameParts = fullName.split(" ");
                                String firstName = "";
                                String lastName = "";
                                String googleId=user.getUid();

                                if (nameParts.length > 0) {
                                    firstName = nameParts[0]; // First name
                                    if (nameParts.length > 1) {
                                        lastName = nameParts[1]; // Last name
                                    }
                                }

                                String email = user.getEmail(); // User's email
                                Uri photoUri = user.getPhotoUrl(); // User's profile photo URL
                                Usuario usuario=new Usuario(firstName,lastName,email,googleId,photoUri.toString(),true);
                                Call<String> llamada = apiService.loginGoogle(usuario);
                                llamada.enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        if (response.isSuccessful()) {
                                            Log.d("salida","google: "+response.body());
                                            ApiService.guardarToken(getApplication(), "Bearer " + response.body());
                                            Intent intent = new Intent(getApplication(), MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            getApplication().startActivity(intent);
                                        } else {

                                            Log.d("salida", "google: "+response.raw().message());
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        Log.d("salida", "google: "+t.getMessage());
                                    }
                                });

                            }

                            Intent intent = new Intent(getApplication(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            getApplication().startActivity(intent);
                        } else {
                            Log.d("salida", "Error al autenticar con Firebase: " + task.getException().getMessage());
                        }
                    }
                });
    }


    public void signOutGoogle() {
        GoogleSignInClient googleSignInClient = mGoogleSignInClient.getValue();
        if (googleSignInClient != null) {
            googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        // Cierre de sesión de Google exitoso
                        // Puedes manejarlo aquí, como mostrar un mensaje al usuario
                    } else {
                        // Ocurrió un error durante el cierre de sesión de Google
                        // Puedes manejarlo aquí, como mostrar un mensaje de error al usuario
                    }
                }
            });
        }
    }

    public void init() {
        auth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getApplication().getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient.setValue(GoogleSignIn.getClient(getApplication(), gso));
    }


}