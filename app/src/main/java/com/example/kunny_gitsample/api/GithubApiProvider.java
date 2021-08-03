package com.example.kunny_gitsample.api;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.kunny_gitsample.data.AuthTokenProvider;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class GithubApiProvider {

    public static AuthApi provideAuthApi() {
        return new Retrofit.Builder()
                .baseUrl("https://github.com/")
                .client(provideOkHttpClient(provideLoggingInterceptor(),null))
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AuthApi.class);

    }

    public static GithubApi provideGithubApi(@NonNull Context context){
        return new Retrofit.Builder()
                .baseUrl("http://api.github.com/")
                .client(provideOkHttpClient(provideLoggingInterceptor(),
                            provideAuthInterceptor(provideAuthTokenProvider(context))))
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GithubApi.class);
    }

    private static OkHttpClient provideOkHttpClient(
            @NonNull HttpLoggingInterceptor interceptor,
            @NonNull AuthInterceptor authInterceptor
            ){
        OkHttpClient.Builder b = new OkHttpClient.Builder();
        if (null != authInterceptor){
            b.addInterceptor(authInterceptor);
        }
        b.addInterceptor(interceptor);
        return b.build();
    }

    private static HttpLoggingInterceptor provideLoggingInterceptor(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }

    private static AuthInterceptor provideAuthInterceptor(@NonNull AuthTokenProvider provider){
        String token = provider.getToken();
        if (null == token){
            throw new IllegalStateException("authToken cannot be null");
        }
        return new AuthInterceptor(token);
    }

    private static AuthTokenProvider provideAuthTokenProvider(@NonNull Context context){
        return new AuthTokenProvider(context.getApplicationContext());
    }

    static class AuthInterceptor implements Interceptor {
        private final String token;

        public AuthInterceptor(String token) {
            this.token = token;
        }


        @NotNull
        @Override
        public Response intercept(@NotNull Chain chain) throws IOException {
            Request original = chain.request();

            Request.Builder b = original.newBuilder()
                    .addHeader("Authorization","token" + token);

            Request request = b.build();
            return chain.proceed(request);
        }
    }
}
