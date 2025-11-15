package com.example.lab4;

import android.os.Bundle;

import android.os.AsyncTask;

import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;


import java.io.IOException;
import java.net.URI;
import okhttp3.*;


public class MainActivity extends AppCompatActivity {

    private TextView resultTextView;
    private TextView statusText;
    private final OkHttpClient client = new OkHttpClient();
    private static final String BASE_URL = "http://192.168.181.130:8080/employees";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultTextView = findViewById(R.id.result_text);
        statusText = findViewById(R.id.status_text);
        Button getButton = findViewById(R.id.get_button);
        Button postButton = findViewById(R.id.post_button);
        Button putButton = findViewById(R.id.put_button);
        Button deleteButton = findViewById(R.id.delete_button);

        getButton.setOnClickListener(v -> new GetTask().execute());
        postButton.setOnClickListener(v -> new PostTask().execute());
        putButton.setOnClickListener(v -> new PutTask().execute());
        deleteButton.setOnClickListener(v -> new DeleteTask().execute());
    }

    // ========= GET =========
    private class GetTask extends AsyncTask<Void, Void, String> {
        @Override protected String doInBackground(Void... voids) {
            Request request = new Request.Builder().url(BASE_URL).build();
            try (Response response = client.newCall(request).execute()) {
                return response.isSuccessful() ? response.body().string() : "Erro: " + response.code();
            } catch (IOException e) { return "Erro de rede: " + e.getMessage(); }
        }
        @Override protected void onPostExecute(String result) {
            resultTextView.setText(result);
            statusText.setText("GET concluído");
            Toast.makeText(MainActivity.this, "Lista atualizada!", Toast.LENGTH_LONG).show();
        }
    }

    // ========= POST =========
    private class PostTask extends AsyncTask<Void, Void, String> {
        @Override protected String doInBackground(Void... voids) {
            String json = "{\"firstName\":\"Aragorn\",\"lastName\":\"King\",\"role\":\"ranger\"}";
            RequestBody body = RequestBody.create(json, MediaType.get("application/json"));
            Request request = new Request.Builder().url(BASE_URL).post(body).build();
            try (Response response = client.newCall(request).execute()) {
                return response.isSuccessful() ? "NOVO FUNCIONÁRIO CRIADO!\n" + response.body().string()
                        : "Falha: " + response.code();
            } catch (IOException e) { return "Erro: " + e.getMessage(); }
        }
        @Override protected void onPostExecute(String result) {
            resultTextView.setText(result);
            statusText.setText("POST - Funcionário criado");
            Toast.makeText(MainActivity.this, "Criado com sucesso!", Toast.LENGTH_LONG).show();
        }
    }

    // ========= PUT (atualiza ID 1) =========
    private class PutTask extends AsyncTask<Void, Void, String> {
        @Override protected String doInBackground(Void... voids) {
            String json = "{\"firstName\":\"Gandalf\",\"lastName\":\"The White\",\"role\":\"wizard\"}";
            RequestBody body = RequestBody.create(json, MediaType.get("application/json"));
            Request request = new Request.Builder().url(BASE_URL + "/1").put(body).build();
            try (Response response = client.newCall(request).execute()) {
                return response.isSuccessful() ? "ATUALIZADO COM SUCESSO!\n" + response.body().string()
                        : "Falha no PUT: " + response.code();
            } catch (IOException e) { return "Erro: " + e.getMessage(); }
        }
        @Override protected void onPostExecute(String result) {
            resultTextView.setText(result);
            statusText.setText("PUT - Funcionário ID 1 atualizado");
            Toast.makeText(MainActivity.this, "Atualizado!", Toast.LENGTH_LONG).show();
        }
    }

    // ========= DELETE (apaga ID 1) =========
    private class DeleteTask extends AsyncTask<Void, Void, String> {
        @Override protected String doInBackground(Void... voids) {
            Request request = new Request.Builder().url(BASE_URL + "/1").delete().build();
            try (Response response = client.newCall(request).execute()) {
                return response.isSuccessful() ? "FUNCIONÁRIO ID 1 APAGADO COM SUCESSO!"
                        : "Falha no DELETE: " + response.code();
            } catch (IOException e) { return "Erro: " + e.getMessage(); }
        }
        @Override protected void onPostExecute(String result) {
            resultTextView.setText(result);
            statusText.setText("DELETE - Funcionário ID 1 removido");
            Toast.makeText(MainActivity.this, "Apagado!", Toast.LENGTH_LONG).show();
            new GetTask().execute(); // atualiza a lista automaticamente
        }
    }
}