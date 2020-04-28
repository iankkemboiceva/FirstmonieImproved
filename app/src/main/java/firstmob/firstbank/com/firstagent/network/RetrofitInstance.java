package firstmob.firstbank.com.firstagent.network;


import android.content.Context;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;


import firstmob.firstbank.com.firstagent.constants.Constants;
import firstmob.firstbank.com.firstagent.constants.SharedPrefConstants;
import firstmob.firstbank.com.firstagent.security.SecurityLayer;
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
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static firstmob.firstbank.com.firstagent.constants.Constants.AND_POINT;
import static firstmob.firstbank.com.firstagent.constants.Constants.MICRO_URL;
import static firstmob.firstbank.com.firstagent.constants.Constants.NET_URL;
import static firstmob.firstbank.com.firstagent.constants.Constants.SUPAGENT_API;
import static firstmob.firstbank.com.firstagent.constants.SharedPrefConstants.KEY_LATITUDE;
import static firstmob.firstbank.com.firstagent.constants.SharedPrefConstants.KEY_LONGIT;

public class RetrofitInstance {

    private static Retrofit retrofit;
    public  static  OkHttpClient okHttpClient = null;




    /**
     * Create an instance of Retrofit object
     */

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

                String lat = Prefs.getString(KEY_LATITUDE,"NA");
                String longt = Prefs.getString(KEY_LONGIT,"NA");
                SecurityLayer.Log("latitude is "+lat);
                SecurityLayer.Log("longitude is "+longt);



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
                    .baseUrl(NET_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }

        return retrofit;

    }


    public static Retrofit getRetrofitSupInstance() {
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

                String lat = Prefs.getString(KEY_LATITUDE,"NA");
                String longt = Prefs.getString(KEY_LONGIT,"NA");
                SecurityLayer.Log("latitude is "+lat);
                SecurityLayer.Log("longitude is "+longt);



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
                    .baseUrl(MICRO_URL+SUPAGENT_API)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }

        return retrofit;

    }
    public static Retrofit getClient() {

        try {
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

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager

            SSLSocketFactory sslSocketFactory = null;
            try {
                sslSocketFactory = new TLSSocketFactory();

            } catch (KeyManagementException ignored) {
                ignored.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            //  final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();




            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            final String token = Prefs.getString("TOKEN","NA");

            builder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request newRequest = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer " + token)
                            .build();
                    return chain.proceed(newRequest);
                }
            });

            builder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    Request request = null;
                    String adminid = Prefs.getString(SharedPrefConstants.KEY_USERID,"NA");



                    if(adminid == null){
                        adminid = "N";
                    }




                    request = original.newBuilder()
                            .header("userId", adminid)

                            .method(original.method(), original.body())
                            .build();


                    return chain.proceed(request);
                }
            });
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);


            okHttpClient = builder
                    .connectTimeout(240, TimeUnit.SECONDS)
                    .writeTimeout(240, TimeUnit.SECONDS)
                    .readTimeout(240, TimeUnit.SECONDS)
                    .build();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.MICRO_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(okHttpClient)

                .build();
        return  retrofit;
    }

    public static Retrofit getAgentCreditInstance() {

        try {
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

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager

            SSLSocketFactory sslSocketFactory = null;
            try {
                sslSocketFactory = new TLSSocketFactory();

            } catch (KeyManagementException ignored) {
                ignored.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            //  final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();




            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            final String token = Prefs.getString("TOKEN","NA");

            builder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request newRequest = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer " + token)
                            .build();
                    return chain.proceed(newRequest);
                }
            });

            builder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    Request request = null;
                    String adminid = Prefs.getString(SharedPrefConstants.KEY_USERID,"NA");



                    if(adminid == null){
                        adminid = "N";
                    }




                    request = original.newBuilder()
                            .header("userId", adminid)

                            .method(original.method(), original.body())
                            .build();


                    return chain.proceed(request);
                }
            });
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);


            okHttpClient = builder
                    .connectTimeout(240, TimeUnit.SECONDS)
                    .writeTimeout(240, TimeUnit.SECONDS)
                    .readTimeout(240, TimeUnit.SECONDS)
                    .build();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.AGENTCREDIT_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(okHttpClient)

                .build();
        return  retrofit;
    }
}