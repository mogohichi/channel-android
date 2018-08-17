package co.getchannel.channel.api;

import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.CertificateException;


import co.getchannel.channel.CHConfiguration;
import co.getchannel.channel.models.CHClient;
import co.getchannel.channel.ssl.NoSSLv3SocketFactory;
import co.getchannel.channel.ssl.SSLSocketFactoryExtended;
import co.getchannel.channel.ssl.Tls12SocketFactory;
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


    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)throws java.security.cert.CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)throws java.security.cert.CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            builder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {
                    String appKey =  CHConfiguration.getApplicationId();
                    Request original = chain.request();
                    Request request =  original.newBuilder()
                            .header("X-Channel-Client-ID", CHClient.currentClient().getClientID()==null?"":CHClient.currentClient().getClientID())
                            .header("X-Channel-Application-Key", appKey)
                            .header("content-type","application/json")
                            .method(original.method(), original.body())
                            .build();

                    return chain.proceed(request);
                }
            });

            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static OkHttpClient.Builder getUnsafeOkHttpClientBuilder() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public X509TrustManager provideX509TrustManager() {
        try {
            TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            factory.init((KeyStore) null);
            TrustManager[] trustManagers = factory.getTrustManagers();
            return (X509TrustManager) trustManagers[0];
        }
        catch (NoSuchAlgorithmException exception) {
            Log.e(getClass().getSimpleName(), "not trust manager available", exception);
        }
        catch (KeyStoreException exception) {
            Log.e(getClass().getSimpleName(), "not trust manager available", exception);
        }

        return null;
    }

    public static OkHttpClient.Builder enableTls12OnPreLollipop(OkHttpClient.Builder client) {
        if (Build.VERSION.SDK_INT >= 16 && Build.VERSION.SDK_INT < 22) {
            try {
                SSLContext sc = SSLContext.getInstance("TLSv1.2");
                sc.init(null, null, null);
                client.sslSocketFactory(new Tls12SocketFactory(sc.getSocketFactory()));

                ConnectionSpec cs = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                        .tlsVersions(TlsVersion.TLS_1_2)
                        .build();

                List<ConnectionSpec> specs = new ArrayList<>();
                specs.add(cs);
                specs.add(ConnectionSpec.COMPATIBLE_TLS);
                specs.add(ConnectionSpec.CLEARTEXT);

                client.connectionSpecs(specs);
            } catch (Exception exc) {
                Log.e("OkHttpTLSCompat", "Error while setting TLS 1.2", exc);
            }
        }

//        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N) {
//            client.connectionSpecs(Arrays.asList(
//                    ConnectionSpec.MODERN_TLS,
//                    ConnectionSpec.CLEARTEXT,
//                    new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
//                            .allEnabledTlsVersions()
//                            .allEnabledCipherSuites()
//                            .build()));
//        }
//
//        if(Build.VERSION.SDK_INT == 24){
//            try {
////                TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
////                factory.init((KeyStore) null);
////                TrustManager[] trustManagers = factory.getTrustManagers();
////                X509TrustManager tm = (X509TrustManager) trustManagers[0];
////
////                client.sslSocketFactory(new SSLSocketFactoryExtended(),tm);
//                return getUnsafeOkHttpClientBuilder();
//            } catch (Exception exc) {
//                Log.e("OkHttpTLSCompat", "Error while setting Protocols", exc);
//            }
//        }


        return client;
    }

    public static Retrofit getAPIWithApplication() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .followRedirects(true)
                .followSslRedirects(true)
                .retryOnConnectionFailure(true)
                .cache(null)
                .connectTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS);

        httpClient.addInterceptor(new Interceptor() {
                                      @Override
                                      public Response intercept(Interceptor.Chain chain) throws IOException {
                                          String appKey =  CHConfiguration.getApplicationId();
                                          Request original = chain.request();
                                          Request request =  original.newBuilder()
                                                    .header("X-Channel-Client-ID", CHClient.currentClient().getClientID()==null?"":CHClient.currentClient().getClientID())
                                                    .header("X-Channel-Application-Key", appKey)
                                                    .header("content-type","application/json")
                                                    .method(original.method(), original.body())
                                                    .build();

                                          return chain.proceed(request);
                                      }
                                  });

        OkHttpClient c = enableTls12OnPreLollipop(httpClient).build();//httpClient.build();//getUnsafeOkHttpClient();//
//        OkHttpClient c = new OkHttpClient();
//        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N) {
////            solution 1
////            httpClient.connectionSpecs(Arrays.asList(
////                    ConnectionSpec.MODERN_TLS,
////                    ConnectionSpec.CLEARTEXT,
////                    new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
////                            .allEnabledTlsVersions()
////                            .allEnabledCipherSuites()
////                            .build()));
////            c = httpClient.build();
//
////            solution 2
////            c =  getUnsafeOkHttpClientBuilder().build();
//
//
////          solution 3
////            try {
////                TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
////                factory.init((KeyStore) null);
////                TrustManager[] trustManagers = factory.getTrustManagers();
////                X509TrustManager tm = (X509TrustManager) trustManagers[0];
////
////                c = httpClient.sslSocketFactory(new SSLSocketFactoryExtended(),tm).build();
////            } catch (Exception exc) {
////                Log.e("OkHttpTLSCompat", "Error while setting Protocols", exc);
////            }
//
////            solution 4
////            c= getUnsafeOkHttpClient();
//
//            ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
//                    .tlsVersions(TlsVersion.TLS_1_2)
//                    .cipherSuites(
//                            CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
//                            CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
//                            CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256)
//                    .build();
//
//            httpClient.connectionSpecs(Arrays.asList(
//                    ConnectionSpec.MODERN_TLS,
//                    ConnectionSpec.CLEARTEXT,
//                    new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
//                            .allEnabledTlsVersions()
//                            .allEnabledCipherSuites()
//                            .build()));
//
//
//            c =  httpClient.connectionSpecs(Collections.singletonList(spec)).build();
//        }else{
//             c = enableTls12OnPreLollipop(httpClient).build();//httpClient.build();//getUnsafeOkHttpClient();//
//        }

         Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(c)
                .build();

        return retrofit;
    }
}
