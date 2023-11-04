package com.heissen.cragexplorer.ui.home.vias.agregarSesion;

import android.app.Application;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.heissen.cragexplorer.LoginActivity;
import com.heissen.cragexplorer.models.Resenia;
import com.heissen.cragexplorer.models.Sesion;
import com.heissen.cragexplorer.models.Usuario;
import com.heissen.cragexplorer.request.ApiService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.Header;

public class AgregarSesionViewModel extends AndroidViewModel {
    private MutableLiveData<ArrayList<Uri>> mUrliList;
    private MutableLiveData<Uri> mImgUri;
    private ApiService.ApiInterface apiService;

    private MutableLiveData<Integer> mIntentos;

    private MutableLiveData<Integer> mPorcentaje;
    private String token;

    public AgregarSesionViewModel(@NonNull Application application) {
        super(application);
        mUrliList = new MutableLiveData<>();
        mImgUri = new MutableLiveData<>();
        apiService = ApiService.getApiInferface();
        token = ApiService.leerToken(getApplication());
        mPorcentaje = new MutableLiveData<>(10);
        mIntentos = new MutableLiveData<>(1);
    }

    public LiveData<ArrayList<Uri>> getmUrliList() {
        return mUrliList;
    }

    public LiveData<Integer> getmPorcentaje() {
        return mPorcentaje;
    }

    public LiveData<Integer> getmIntentos() {
        return mIntentos;
    }

    public String convertirImgBase64(Uri uri) {
        try {
            ContentResolver contentResolver = getApplication().getContentResolver();
            InputStream inputStream = contentResolver.openInputStream(uri);

            if (inputStream != null) {
                // Decodifica la imagen en un objeto Bitmap
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2; // Puedes ajustar este valor para comprimir más o menos
                Bitmap originalBitmap = BitmapFactory.decodeStream(inputStream, null, options);

                // Comprime la imagen (ajusta la calidad según tus necesidades)
                int quality = 50; // Rango de 0 (más comprimido) a 100 (menos comprimido)
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                originalBitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream);

                // Convierte la imagen comprimida a Base64
                byte[] imageBytes = byteArrayOutputStream.toByteArray();
                return Base64.encodeToString(imageBytes, Base64.DEFAULT);
            } else {
                Log.d("salida", "Error al cargar la imagen");
                return null;
            }
        } catch (IOException e) {
            // Handle error: Ocurrió un error al leer la imagen.
            e.printStackTrace();
            return null;
        }
    }

    public void cargarImg(ActivityResult result) {
        mUrliList.setValue(new ArrayList<>());
        if (result != null && result.getData() != null) {
            ClipData clipData = result.getData().getClipData();

            if (clipData != null) {
                int count = clipData.getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri uri = clipData.getItemAt(i).getUri();
                    if (uri != null) {
                        mUrliList.getValue().add(uri);
                        Log.d("salida", uri.toString());
                    }
                }
            } else {
                // Si no hay ClipData, intenta obtener una sola URI
                Uri uri = result.getData().getData();
                if (uri != null) {
                    mUrliList.getValue().add(uri);
                    Log.d("salida", uri.toString());
                }
            }
        }
    }

    public void setmIntentos(int intentos) {
        this.mIntentos.setValue(intentos);
    }

    public void setmPorcentaje(int porcentaje) {
        this.mPorcentaje.setValue(porcentaje);
    }

    public void agregarFotoVia(ArrayList<Uri> listUris, int idVia) {
        ArrayList<String> imgsBase64 = new ArrayList<>();
        if (listUris != null && !listUris.isEmpty()) {
            listUris.forEach(s -> {
                imgsBase64.add(convertirImgBase64(s));
            });
            Call<String> llamada = apiService.cargarFotosVia(token, imgsBase64, idVia);
            llamada.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {

                        Log.d("salida", "TODO OK");

                    } else {
                        Log.d("salida", "ELSE " + response.raw());

                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.d("salida", "ERROR " + t.getMessage());

                }
            });
        }
    }

    public void plusProcentaje() {
        if (mPorcentaje.getValue() < 100) {
            int porcentaje = mPorcentaje.getValue();
            porcentaje += 10;
            mPorcentaje.setValue(porcentaje);
        }

    }

    public void minusProcentaje() {
        if (mPorcentaje.getValue() > 10) {
            int porcentaje = mPorcentaje.getValue();
            porcentaje -= 10;
            mPorcentaje.setValue(porcentaje);
        }
    }


    public void cargarSesion(Sesion sesion) {
        if (sesion.getIdVia() <= 0) {
            Toast.makeText(getApplication(), "idVia no válido", Toast.LENGTH_SHORT).show();
        }
        if (sesion.getPorcentaje() < 0 || sesion.getPorcentaje() > 100) {
            Toast.makeText(getApplication(), "Porcentaje no válido", Toast.LENGTH_SHORT).show();

        }
        if (sesion.getFecha() == null) {
            Toast.makeText(getApplication(), "La fecha no puede ser nula", Toast.LENGTH_SHORT).show();

        }
        if (sesion.getIdTipo() <= 0) {
            Toast.makeText(getApplication(), "El tipo no puede ser 0", Toast.LENGTH_SHORT).show();

        }
        if (sesion.getIntentos() <= 0) {
            Toast.makeText(getApplication(), "Intentos no válido", Toast.LENGTH_SHORT).show();

        }

        ApiService.ApiInterface apiService = ApiService.getApiInferface();

        Log.d("salida", sesion.toString());

        Call<Sesion> llamada = apiService.agregarSesion(token, sesion);

        llamada.enqueue(new Callback<Sesion>() {
            @Override
            public void onResponse(Call<Sesion> call, Response<Sesion> response) {
                if (response.isSuccessful()) {
                    Log.d("salida", sesion.toString());

                } else {
                    Log.d("salida", "ELSE " + response.raw());

                }
            }

            @Override
            public void onFailure(Call<Sesion> call, Throwable t) {
                Log.d("salida", "ERROR " + t.getMessage());
            }
        });
    }

    public void cargarResenia(Resenia resenia) {
        if (resenia.getIdVia() <= 0) {
            Toast.makeText(getApplication(), "idVia no válido", Toast.LENGTH_SHORT).show();
        }
        if (resenia.getCalificacion() <= 0 || resenia.getCalificacion() > 5) {
            Toast.makeText(getApplication(), "Calificacion no válida", Toast.LENGTH_SHORT).show();

        }
        if (resenia.getFecha() == null) {
            Toast.makeText(getApplication(), "La fecha no puede ser nula", Toast.LENGTH_SHORT).show();

        }

        ApiService.ApiInterface apiService = ApiService.getApiInferface();

        Log.d("salida", "Resenia: "+resenia.toString());

        Call<Resenia> llamada = apiService.agregarResenia(token, resenia);

        llamada.enqueue(new Callback<Resenia>() {
            @Override
            public void onResponse(Call<Resenia> call, Response<Resenia> response) {
                if (response.isSuccessful()) {
                    Log.d("salida", resenia.toString());

                } else {
                    Log.d("salida", "ELSE " + response.raw());
                    Log.d("salida", "ELSE " + response.toString());
                    Log.d("salida", "ELSE " + response.code());
                    Log.d("salida", "ELSE " + response.toString());

                }
            }

            @Override
            public void onFailure(Call<Resenia> call, Throwable t) {
                Log.d("salida", "ERROR " + t.getMessage());
            }
        });
    }


    public void plusIntentos(int tipoIntento) {
        if (mIntentos.getValue() < 99 &&tipoIntento>=3) {
            int intentos = mIntentos.getValue();
            intentos += 1;
            mIntentos.setValue(intentos);
        }
    }


    public void minusIntentos(int tipoIntento) {
        if (mIntentos.getValue() > 1 &&tipoIntento>=4) {
            int intentos = mIntentos.getValue();
            intentos -= 1;
            mIntentos.setValue(intentos);
            Log.d("salida",mIntentos.getValue()+"");
        }
        if (mIntentos.getValue() > 2 && tipoIntento==3) {
            Log.d("salida",mIntentos.getValue()+"");
            int intentos = mIntentos.getValue();
            intentos -= 1;
            mIntentos.setValue(intentos);
        }
    }
}