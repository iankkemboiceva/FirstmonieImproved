package firstmob.firstbank.com.firstagent.network;


import firstmob.firstbank.com.firstagent.Activity.BuildConfig;
import firstmob.firstbank.com.firstagent.constants.Constants;
import firstmob.firstbank.com.firstagent.security.TLSSocketFactory;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.CertificatePinner;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static firstmob.firstbank.com.firstagent.constants.Constants.AND_POINT;
import static firstmob.firstbank.com.firstagent.constants.Constants.NET_URL;

public class RetrofitInstance {

    private static Retrofit retrofit;


    private static final String BASE_URL = NET_URL;

    /**
     * Create an instance of Retrofit object
     * */

    public static Retrofit getRetrofitInstance() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        int KEY_TIMEOUT = 140;
        builder.readTimeout(KEY_TIMEOUT, TimeUnit.SECONDS).connectTimeout(KEY_TIMEOUT, TimeUnit.SECONDS).writeTimeout(KEY_TIMEOUT, TimeUnit.SECONDS);

        // Create a trust manager that does not validate certificate chains
        final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };


        // Create an ssl socket factory with our all-trusting manager

        SSLSocketFactory sslSocketFactory = null;
        try {

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            sslSocketFactory = new TLSSocketFactory();

        } catch (KeyManagementException ignored) {
            ignored.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //  final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();


        final String userid = "NA";
        final String baseimage = "NA";
        System.out.println("baseimage [" + baseimage + "]");
        int count = 0;

        final String appid = "NA";


        builder.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request request = null;
                String appidnew = appid;
                String useridnew = userid;


                if (appid == null) {
                    appidnew = "N";
                }
                if (userid == null) {
                    useridnew = "N";
                }
                if (!(baseimage.equals("N"))) {


                    request = original.newBuilder()
                            .header("man", useridnew)
                            .header("serial", appidnew)

                            .header("base64", baseimage)
                            .method(original.method(), original.body())
                            .build();
                } else {
                    request = original.newBuilder()
                            .header("man", useridnew)
                            .header("serial", appidnew)


                            .method(original.method(), original.body())
                            .build();
                }


                return chain.proceed(request);
            }
        });
        builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });


        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.networkInterceptors().add(httpLoggingInterceptor);
        builder.addInterceptor(httpLoggingInterceptor);

        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });

        builder.retryOnConnectionFailure(true);
        OkHttpClient okHttpClient = builder.build();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }

        return retrofit;

    }
}