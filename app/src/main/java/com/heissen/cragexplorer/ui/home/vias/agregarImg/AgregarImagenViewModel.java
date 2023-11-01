package com.heissen.cragexplorer.ui.home.vias.agregarImg;

import android.app.Application;
import android.content.ClipData;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.heissen.cragexplorer.models.Usuario;
import com.heissen.cragexplorer.request.ApiService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgregarImagenViewModel extends AndroidViewModel {
    private MutableLiveData<ArrayList<Uri>> mUrliList;
    private MutableLiveData<Uri> mImgUri;
    private ApiService.ApiInterface apiService;

    private MutableLiveData<Integer> mPorcentaje;
    private String token;

    public AgregarImagenViewModel(@NonNull Application application) {
        super(application);
        mUrliList = new MutableLiveData<>();
        mImgUri = new MutableLiveData<>();
        apiService = ApiService.getApiInferface();
        token = ApiService.leerToken(getApplication());
        mPorcentaje=new MutableLiveData<>(10);
    }

    public LiveData<ArrayList<Uri>> getmUrliList() {
        return mUrliList;
    }

    public LiveData<Integer> getmPorcentaje() {
        return mPorcentaje;
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

    public void agregarFotoVia(ArrayList<Uri> listUris, int idVia) {
        ArrayList<String> imgsBase64 = new ArrayList<>();

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

    public void plusProcentaje() {
        if (mPorcentaje.getValue()<100){
            int porcentaje=mPorcentaje.getValue();
            porcentaje+=10;
            mPorcentaje.setValue(porcentaje);
        }
    }
    public void minusProcentaje() {
        if (mPorcentaje.getValue()>10){
            int porcentaje=mPorcentaje.getValue();
            porcentaje-=10;
            mPorcentaje.setValue(porcentaje);
        }
    }

}