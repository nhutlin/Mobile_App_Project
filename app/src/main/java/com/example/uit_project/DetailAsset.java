package com.example.uit_project;
import com.example.uit_project.model.Asset;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.uit_project.api.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailAsset extends AppCompatActivity {
    ApiService apiService;
    private String token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJoREkwZ2hyVlJvaE5zVy1wSXpZeDBpT2lHMzNlWjJxV21sRk4wWGE1dWkwIn0.eyJleHAiOjE3MDE1Mzg0MjAsImlhdCI6MTcwMTQ1MjY2MSwiYXV0aF90aW1lIjoxNzAxNDUyMDIwLCJqdGkiOiJmNWE0YTliMC03YTFmLTQzZmEtYmQ5NC05ZTNkNjZkNmU1NGQiLCJpc3MiOiJodHRwczovL3Vpb3QuaXh4Yy5kZXYvYXV0aC9yZWFsbXMvbWFzdGVyIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6IjRlM2E0NDk2LTJmMTktNDgxMy1iZjAwLTA5NDA3ZDFlZThjYiIsInR5cCI6IkJlYXJlciIsImF6cCI6Im9wZW5yZW1vdGUiLCJzZXNzaW9uX3N0YXRlIjoiNTQyZGUwMzctZTllZi00N2U3LTljZTItZjVmMzg2NzFiMzJhIiwiYWNyIjoiMCIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwczovL3Vpb3QuaXh4Yy5kZXYiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbImRlZmF1bHQtcm9sZXMtbWFzdGVyIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7Im9wZW5yZW1vdGUiOnsicm9sZXMiOlsicmVhZDptYXAiLCJyZWFkOnJ1bGVzIiwicmVhZDppbnNpZ2h0cyIsInJlYWQ6YXNzZXRzIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6Im9wZW5pZCBwcm9maWxlIGVtYWlsIiwic2lkIjoiNTQyZGUwMzctZTllZi00N2U3LTljZTItZjVmMzg2NzFiMzJhIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJuYW1lIjoiRmlyc3QgTmFtZSBMYXN0IG5hbWUiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJ1c2VyIiwiZ2l2ZW5fbmFtZSI6IkZpcnN0IE5hbWUiLCJmYW1pbHlfbmFtZSI6Ikxhc3QgbmFtZSIsImVtYWlsIjoidXNlckBpeHhjLmRldiJ9.L713xLglorcTUAwJw15SZUt6GSzDj9X9YuV4bmMDIgQJL4dL9ZMMDe4fTBmHKgiagDLVfgsPoX6I3sL2zjsb1Xe7NGstzMzggR6mPQES21h0VxTuEVyuPDOm6amUwN-NkWVEjClajsRNuu6ENSlUEoGx8KUBaUNrb-VirNvGTVbPX-E4DvcuY0C2jGbROI37LcGOFgNAxVwae3Uczg8KEeUBkQ3HTmqrl9r1mOxo4fBmafSP-ZqHM-1Pc1V7s4WS-gRmHFhWZhFHhSMuFVdd_H3GSpixXLwdEeVMu4KzbrM997xMXoSrzWtLxTPXCPMx70m1Ea4N9a3EXPadRhr25Q";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_asset);
        TextView test = findViewById(R.id.test);
        Button btn = findViewById(R.id.testBtn);
        test.setText(GlobalVar.token);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiService.apiService.getAsset("5zI6XqkQVSfdgOrZ1MyWEf", "Bearer " + token)
                        .enqueue(new Callback<Asset>() {
                            @Override
                            public void onResponse(Call<Asset> call, Response<Asset> response) {
                                Log.d("API CALL", response.code()+"");
                                Asset asset = response.body();
                                test.setText(String.valueOf(asset.attributes.windSpeed.value));

                            }

                            @Override
                            public void onFailure(Call<Asset> call, Throwable t) {

                            }
                        });
            }
        });

//        apiService = APIClientAsset.getClient().create(ApiService.class);
//        btn.setOnClickListener(v -> {
//            Call<Asset> call = apiService.getAsset("5zI6XqkQVSfdgOrZ1MyWEf");
//            call.enqueue(new Callback<Asset>() {
//                @SuppressLint("SetTextI18n")
//                @Override
//                public void onResponse(Call<Asset> call, Response<Asset> response) {
//                    Log.d("API CALL", response.code()+"");
//                    //Log.d ("API CALL", response.toString());
//                    Asset asset = response.body();
//
//                    test.setText(String.valueOf(asset.attributes.rainFall.value));
//
//                    Log.d("API CALL", asset.type+"");
//
//                    //txttype.setText(asset.type);
//                }
//
//                @Override
//                public void onFailure(Call<Asset> call, Throwable t) {
//                    Log.d("API CALL", t.getMessage().toString());
//
//                    //t.printStackTrace();
//
//                }
//            });
//        });

    }
}