package com.example.uit_project;

import java.io.IOException;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClientLogin {
    private static Retrofit retrofit = null;
    private static String token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJoREkwZ2hyVlJvaE5zVy1wSXpZeDBpT2lHMzNlWjJxV21sRk4wWGE1dWkwIn0.eyJleHAiOjE3MDIwOTQ2MzksImlhdCI6MTcwMjAwODIzOSwianRpIjoiOTA3NmVmNDAtZTU5NS00ZGMwLTkxMGItYjhhYWUwNzFjMzYyIiwiaXNzIjoiaHR0cHM6Ly91aW90Lml4eGMuZGV2L2F1dGgvcmVhbG1zL21hc3RlciIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiI0YzM5NmNiYS0yOWZmLTRhYzEtYWVhYS1kYmFiNmM3Y2E4MmUiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJvcGVucmVtb3RlIiwic2Vzc2lvbl9zdGF0ZSI6IjdhMmE1MjM3LTAxYTQtNGJiMS05M2I1LTM2NGFiOWE5NjI0MSIsImFjciI6IjEiLCJhbGxvd2VkLW9yaWdpbnMiOlsiaHR0cHM6Ly91aW90Lml4eGMuZGV2Il0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJkZWZhdWx0LXJvbGVzLW1hc3RlciIsIm9mZmxpbmVfYWNjZXNzIiwidW1hX2F1dGhvcml6YXRpb24iXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6InByb2ZpbGUgZW1haWwiLCJzaWQiOiI3YTJhNTIzNy0wMWE0LTRiYjEtOTNiNS0zNjRhYjlhOTYyNDEiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsInByZWZlcnJlZF91c2VybmFtZSI6ImxpbmgyMzEiLCJlbWFpbCI6ImxhbXBodW9uZzE2MDgyMDAzQGdtYWlsLmNvbSJ9.HMU8YYO8LuMkhB5zf_fxkDJw60cR1otDU64CGtHbKMJ35RDIR9BlJL7iTsF4mJnrEl7diaD7_k2TNbgyIoZ0BjRwxRzGcDcBXMxNdJ_bEawCIj1IK6DWghLOKFjl3g2rqiRhsdAESYIggHChYZ8Ua22iWyB47kd1q-rJj_rkC1P7iQi-7wMhodkTMT-nafq2V4e6FMLvhgsouzrMoXTSgC_0mwQOgbbMAJ7FGenJ931ue7TJ066KPvdOOr4C9z7M_DR-w7EAFuuWFzsv2l0O2y2VwoZyYKdfYJO9HR6pCOXRdnDSFacKressGtMDy7xhn-7U4duvXw6cigagxnMhkg";
    //private static String token = " eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJoREkwZ2hyVlJvaE5zVy1wSXpZeDBpT2lHMzNlWjJxV21sRk4wWGE1dWkwIn0.eyJleHAiOjE3MDE1ODAzMjEsImlhdCI6MTcwMTQ5MzkyMSwiYXV0aF90aW1lIjoxNzAxNDkzOTIxLCJqdGkiOiI3MTViMTRmNi1kZTZmLTRlMTEtOTFmNS1hNWMxZGJmMzZhZDMiLCJpc3MiOiJodHRwczovL3Vpb3QuaXh4Yy5kZXYvYXV0aC9yZWFsbXMvbWFzdGVyIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6IjRlM2E0NDk2LTJmMTktNDgxMy1iZjAwLTA5NDA3ZDFlZThjYiIsInR5cCI6IkJlYXJlciIsImF6cCI6Im9wZW5yZW1vdGUiLCJzZXNzaW9uX3N0YXRlIjoiMzM0ZWRiOTItNmZhNi00ODY3LWExOGQtOTNhMzc0NDFmM2U5IiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwczovL3Vpb3QuaXh4Yy5kZXYiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbImRlZmF1bHQtcm9sZXMtbWFzdGVyIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7Im9wZW5yZW1vdGUiOnsicm9sZXMiOlsicmVhZDptYXAiLCJyZWFkOnJ1bGVzIiwicmVhZDppbnNpZ2h0cyIsInJlYWQ6YXNzZXRzIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6InByb2ZpbGUgZW1haWwiLCJzaWQiOiIzMzRlZGI5Mi02ZmE2LTQ4NjctYTE4ZC05M2EzNzQ0MWYzZTkiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsIm5hbWUiOiJGaXJzdCBOYW1lIExhc3QgbmFtZSIsInByZWZlcnJlZF91c2VybmFtZSI6InVzZXIiLCJnaXZlbl9uYW1lIjoiRmlyc3QgTmFtZSIsImZhbWlseV9uYW1lIjoiTGFzdCBuYW1lIiwiZW1haWwiOiJ1c2VyQGl4eGMuZGV2In0.XOIvMFTOnLJLT25yUaBfjK4ETpJ67YA-7DIs8h-bItOzJeCYGG2a32i_kuBtiZ-os3UbDAfsTOVE3rSpXjEK4daZ_mo_xrftc8NCkj6p1FbMlVAlUvX4wOyTOuabHv_zUNypRUK3Kcwo8TBWkvsF791wA74b8KgOdo-7I1d66niSvZpmz25HPpOJtMoXB5YPzLAz_at1IxG2uI8w9MH5ODEq1QrHTImAcPRy8rXipCkSk60sbANCEbTNkmN04qnea0NxnYm9I1tMOd3mNHdmLbNWhIuutfbshC3YCWKZ8pbE2IQSEza993tK61kh3XiDVQvs3zvoMN2i6U9ZNzz-YQ";
    //private static String token = " eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJoREkwZ2hyVlJvaE5zVy1wSXpZeDBpT2lHMzNlWjJxV21sRk4wWGE1dWkwIn0.eyJleHAiOjE3MDE1Nzk5MTksImlhdCI6MTcwMTQ5MzUxOSwianRpIjoiYmEwZDcxZTMtYjYwNy00ODQ1LWEyODgtNTAzNTIzYjcwZTI5IiwiaXNzIjoiaHR0cHM6Ly91aW90Lml4eGMuZGV2L2F1dGgvcmVhbG1zL21hc3RlciIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiI0ZTNhNDQ5Ni0yZjE5LTQ4MTMtYmYwMC0wOTQwN2QxZWU4Y2IiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJvcGVucmVtb3RlIiwic2Vzc2lvbl9zdGF0ZSI6IjFlMTI0NGU5LWU2MjktNDI3NC04YzgyLWMyYjg3M2MyMWIwNiIsImFjciI6IjEiLCJhbGxvd2VkLW9yaWdpbnMiOlsiaHR0cHM6Ly91aW90Lml4eGMuZGV2Il0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJkZWZhdWx0LXJvbGVzLW1hc3RlciIsIm9mZmxpbmVfYWNjZXNzIiwidW1hX2F1dGhvcml6YXRpb24iXX0sInJlc291cmNlX2FjY2VzcyI6eyJvcGVucmVtb3RlIjp7InJvbGVzIjpbInJlYWQ6bWFwIiwicmVhZDpydWxlcyIsInJlYWQ6aW5zaWdodHMiLCJyZWFkOmFzc2V0cyJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJwcm9maWxlIGVtYWlsIiwic2lkIjoiMWUxMjQ0ZTktZTYyOS00Mjc0LThjODItYzJiODczYzIxYjA2IiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJuYW1lIjoiRmlyc3QgTmFtZSBMYXN0IG5hbWUiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJ1c2VyIiwiZ2l2ZW5fbmFtZSI6IkZpcnN0IE5hbWUiLCJmYW1pbHlfbmFtZSI6Ikxhc3QgbmFtZSIsImVtYWlsIjoidXNlckBpeHhjLmRldiJ9.Gf3MIbcC9bLDn2W8B669f0zdKTQexuCfMj2-1euLmRnVZnvO8uSC9kT465dtCnPOm1BqINe-f8iUHavmfQUKVxMQXVw8T6wRfcpTI-mliGPUcrCpxCNZat5Ol7ZiYmvnxXA624Al9j9zeDkAegZNqwfL0hyX6BPrQQ-Tiy76Et2jwwtb9R_Ww2PL7p6agfV9EyuQfRpC3F3SwwRFhtSLDdMDu0dCXD_CV9ihsrgoytpqHb1PcyWOACSIQlHdhtlXPhSofwZRpRmbtlaHu02U5aXGAjA0uQRastg-adMwdNPN6KghIdKCkamO-W8f5cT3nJooXcx0rZzxSX0QP1du1A";
    //private static String token = "eyJhbGciOiJIUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJjYjIzZWEzMC1mOTViLTQxYTYtYjVjOC01ZDIyNjMyNGEyMjMifQ.eyJleHAiOjE3MDIwNTU1MDUsImlhdCI6MTcwMTk2OTEwNSwianRpIjoiMTkyMDc3NTMtMzRlMy00Y2MyLTkwNTItNjI3MjA1M2IyMzcwIiwiaXNzIjoiaHR0cHM6Ly91aW90Lml4eGMuZGV2L2F1dGgvcmVhbG1zL21hc3RlciIsImF1ZCI6Imh0dHBzOi8vdWlvdC5peHhjLmRldi9hdXRoL3JlYWxtcy9tYXN0ZXIiLCJzdWIiOiI0YzM5NmNiYS0yOWZmLTRhYzEtYWVhYS1kYmFiNmM3Y2E4MmUiLCJ0eXAiOiJSZWZyZXNoIiwiYXpwIjoib3BlbnJlbW90ZSIsInNlc3Npb25fc3RhdGUiOiI1MmYxZjk1Yi05ZDVmLTQ4ZWYtYmI3Yi03MTFmMjM5ZjUxYWMiLCJzY29wZSI6InByb2ZpbGUgZW1haWwiLCJzaWQiOiI1MmYxZjk1Yi05ZDVmLTQ4ZWYtYmI3Yi03MTFmMjM5ZjUxYWMifQ.GviusckkP1Eenr9-q8JuucDOwSGTcMY0FYNFxrKV-IU";
    //private static String token = " eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJoREkwZ2hyVlJvaE5zVy1wSXpZeDBpT2lHMzNlWjJxV21sRk4wWGE1dWkwIn0.eyJleHAiOjE2OTkxMjE5MjIsImlhdCI6MTY5OTAzODAyOCwiYXV0aF90aW1lIjoxNjk5MDM1NTIyLCJqdGkiOiJjYWY0OGU4ZS1jMmZhLTQ2ZGEtYWY1Yy1iMDBhZGY3Y2MxMDkiLCJpc3MiOiJodHRwczovL3Vpb3QuaXh4Yy5kZXYvYXV0aC9yZWFsbXMvbWFzdGVyIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6IjRlM2E0NDk2LTJmMTktNDgxMy1iZjAwLTA5NDA3ZDFlZThjYiIsInR5cCI6IkJlYXJlciIsImF6cCI6Im9wZW5yZW1vdGUiLCJzZXNzaW9uX3N0YXRlIjoiMjQyNzYyMDItZjhiMy00M2M1LWExNjYtN2E0MmRjYjRiNTNmIiwiYWNyIjoiMCIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwczovL3Vpb3QuaXh4Yy5kZXYiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbImRlZmF1bHQtcm9sZXMtbWFzdGVyIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7Im9wZW5yZW1vdGUiOnsicm9sZXMiOlsicmVhZDptYXAiLCJyZWFkOnJ1bGVzIiwicmVhZDppbnNpZ2h0cyIsInJlYWQ6YXNzZXRzIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6Im9wZW5pZCBwcm9maWxlIGVtYWlsIiwic2lkIjoiMjQyNzYyMDItZjhiMy00M2M1LWExNjYtN2E0MmRjYjRiNTNmIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJuYW1lIjoiVXNlciBUZXN0IiwicHJlZmVycmVkX3VzZXJuYW1lIjoidXNlciIsImdpdmVuX25hbWUiOiJVc2VyIiwiZmFtaWx5X25hbWUiOiJUZXN0IiwiZW1haWwiOiJ1c2VyQGl4eGMuZGV2In0.XE0NVd3Dfog0BDpGXhsNqYkpC_7-8VIaRK8hXIU58dx6zIN8aEleu2oXWd1uISmawkskA68gAemKxDxdGAwFxL9w3zljj7TejZDQ6g_t0R0U6-d9GTTJjYn4FwnXe8eYWGxBIreQpOwddl7B_9RuNWiNcmTZ9f6FLmIL0jcdOuL2U8XZQOEqztTALT95tzriHwPKoI2CW2tf6A4WcDR0dfikHGsINKtoDMY3_DIEHcTJr3vGOpxcnyM6ExgicyOnAP3hsr2zJuK0fYJlqOCr64Fw75UpwBtVx9zNm4tQ3gmstUoBdU5wxg8FDCH6f0ikcno9RQK8P5Pof9b1lGB9PA";
    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            //Log
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);

            //Bear token
            builder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request newRequest  = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer " + token)
                            .build();
                    return chain.proceed(newRequest);
                }
            });


            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    static Retrofit getClient() {

        OkHttpClient client = getUnsafeOkHttpClient();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://uiot.ixxc.dev/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }
}