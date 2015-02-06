package foo;


import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 无视Https证书是否正确的Java Http Client
 * 
 * @author fragiler
 * @since 2014.5
 */
public class HttpsUtil {

	
	private static final Logger logger = LoggerFactory.getLogger(HttpsUtil.class);
    /**
     * 忽视证书HostName
     */
    private static HostnameVerifier ignoreHostnameVerifier = new HostnameVerifier() {
        public boolean verify(String s, SSLSession sslsession) {
        	logger.debug("WARNING: Hostname is not matched for cert.");
            return true;
        }
    };
    
     /**
     * Ignore Certification
     */
    private static TrustManager ignoreCertificationTrustManger = new X509TrustManager() {

        private X509Certificate[] certificates;

        @Override
        public void checkClientTrusted(X509Certificate certificates[],
                String authType) throws CertificateException {
            if (this.certificates == null) {
                this.certificates = certificates;
                logger.debug("init at checkClientTrusted");
            }
        }


        @Override
        public void checkServerTrusted(X509Certificate[] ax509certificate,
                String s) throws CertificateException {
            if (this.certificates == null) {
                this.certificates = ax509certificate;
//                System.out.println("init at checkServerTrusted");
                logger.debug("init at checkServerTrusted");
            }
//            for (int c = 0; c < certificates.length; c++) {
//                X509Certificate cert = certificates[c];
//                System.out.println(" Server certificate " + (c + 1) + ":");
//                System.out.println("  Subject DN: " + cert.getSubjectDN());
//                System.out.println("  Signature Algorithm: "
//                        + cert.getSigAlgName());
//                System.out.println("  Valid from: " + cert.getNotBefore());
//                System.out.println("  Valid until: " + cert.getNotAfter());
//                System.out.println("  Issuer: " + cert.getIssuerDN());
//            }
        }


        @Override
        public X509Certificate[] getAcceptedIssuers() {
            // TODO Auto-generated method stub
            return null;
        }
    };


    public static String getMethod(String urlString) {

    	logger.debug("检查堡垒机连接状态：["+(new SimpleDateFormat("yyyy-MM-dd HH:mm:ssss").format(Calendar.getInstance().getTime()))+"]");
        ByteArrayOutputStream buffer = new ByteArrayOutputStream(512);
        try {
            URL url = new URL(urlString);
            /*
             * use ignore host name verifier
             */
            HttpsURLConnection.setDefaultHostnameVerifier(ignoreHostnameVerifier);
            
            HttpsURLConnection	connection = (HttpsURLConnection) url.openConnection();

            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            // Prepare SSL Context
            TrustManager[] tm = { ignoreCertificationTrustManger };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());


            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            connection.setSSLSocketFactory(ssf);
            InputStream reader = null;
            try{
            	reader = connection.getInputStream();
            }catch(SocketTimeoutException ste){
//            	System.out.println("链接超时");
            	logger.debug("链接超时");
            	if(false!=SingletonFlag.getInstance()){
//            		System.out.println("原状态是连接，现为断开");
            		logger.info("原状态是连接，现为断开");
            		SingletonFlag.setFlag(false);
            		SingletonFlag.getFlagBean().notifyFlagEvent();
            	}
            	return "";
            }
            byte[] bytes = new byte[512];
            int length = reader.read(bytes);


            do {
                buffer.write(bytes, 0, length);
                length = reader.read(bytes);
            } while (length > 0);


            // result.setResponseData(bytes);
//            System.out.println(buffer.toString());
            reader.close();
            
            connection.disconnect();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }
        String repString= new String (buffer.toByteArray());
        return repString;
    }


/*    public static void main(String[] args) {
        String urlString = "https://116.228.208.250/index.php";
        String output = new String(HttpsUtil.getMethod(urlString));
        if(output.length()>10)
        	System.out.println(true);
//        System.out.println(output);
    }*/
}