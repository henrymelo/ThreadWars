package com.example.threadwars.api.java;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

public class ApiManager {

    private static final String API_URL = "https://jsonplaceholder.typicode.com/posts/1";

    public ApiManager() {
        // Registrar esta instância na EventBus quando a instância for criada
//        EventBus.getDefault().register(this);
    }

    // Método para iniciar o processo de chamada da API
    public void fetchDataFromApi() {
        Timber.w("*****startTime: " + dateTimeString());

        // Criar um cliente OkHttpClient
        OkHttpClient client = new OkHttpClient();

        // Criar uma solicitação HTTP
        Request request = new Request.Builder()
                .url(API_URL)
                .build();

        // Executar a chamada assíncrona
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Timber.e("*****Chamada à API falhou: %s", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Verificar se a resposta é bem-sucedida antes de postar o evento
                if (response.isSuccessful()) {
                    String apiResponse = response.body().string();

                    // Publicar um evento apenas se a chamada à API for bem-sucedida
                    EventBus.getDefault().post(new ApiDataEvent(apiResponse));

                    Timber.w("*****endTime: " + dateTimeString());
                } else {
                    Timber.e("*****Erro na chamada à API: %s", response.message());
                }
            }
        });
    }

    // Método para desregistrar esta instância na EventBus quando não for mais necessária
    public void unregister() {
        EventBus.getDefault().unregister(this);
    }

    // ... (restante do código permanece inalterado)

    // Adicionando método para obter a data e hora atual formatada
    private String dateTimeString() {
        long currentTimeMillis = System.currentTimeMillis();
        Date date = new Date(currentTimeMillis);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
        String formattedTime = sdf.format(date);
        return formattedTime;
    }
}




