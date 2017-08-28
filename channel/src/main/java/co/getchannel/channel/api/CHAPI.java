package co.getchannel.channel.api;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import co.getchannel.channel.CHConfiguration;
import co.getchannel.channel.ssl.NoSSLv3SocketFactory;
import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.TlsVersion;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;

import static co.getchannel.channel.helpers.CHConstants.BASE_URL;

/**
 * Created by Admin on 8/16/2017.
 */
public class CHAPI {

//    public static OkClient createClient(int readTimeout, TimeUnit readTimeoutUnit, int connectTimeout, TimeUnit connectTimeoutUnit)
//    {
//        final OkHttpClient okHttpClient = new OkHttpClient();
//        okHttpClient.setReadTimeout(readTimeout, readTimeoutUnit);
//        okHttpClient.setConnectTimeout(connectTimeout, connectTimeoutUnit);
//
//        try {
//            URL url = new URL(ApiIntentService.getHostAddress());
//            SSLSocketFactory NoSSLv3Factory = new NoSSLv3SocketFactory(url);
//            okHttpClient.setSslSocketFactory(NoSSLv3Factory);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return new OkClient(okHttpClient);
//
//    }



    public static Retrofit getAPIWithApplication() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

//        try {
//            URL url_g = new URL(BASE_URL);
//            try {
//                httpClient.socketFactory( new NoSSLv3SocketFactory(url_g));
//            } catch(java.io.IOException e) {
//                //Do something with the exception.
//            }
//        } catch(MalformedURLException e) {
//            //Do something with the exception.
//        }


//        ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
//                .tlsVersions(TlsVersion.TLS_1_2)
//                .cipherSuites(
//                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
//                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
//                        CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256)
//                .build();
//        OkHttpClient c = new OkHttpClient.Builder()
//                .connectionSpecs(Collections.singletonList(spec))
//                .build();



        httpClient.addInterceptor(new Interceptor() {
                                      @Override
                                      public Response intercept(Interceptor.Chain chain) throws IOException {
                                          String appKey =  CHConfiguration.getApplicationId();
                                          Request original = chain.request();
                                          Request request = original.newBuilder()
                                                  .header("X-Channel-Application-Key", appKey)
                                                  .method(original.method(), original.body())
                                                  .build();

                                          return chain.proceed(request);
                                      }
                                  });

         OkHttpClient c = httpClient.build();

         Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(c)
                .build();

        return retrofit;
    }
}
