package kim.uno.mock.extension

import kim.uno.mock.util.TLSSocketFactory
import okhttp3.OkHttpClient
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

fun OkHttpClient.Builder.enableTlsSocketFactory(): OkHttpClient.Builder {
    try {
        val trustManager = object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
        }
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, arrayOf(trustManager), SecureRandom())
        sslSocketFactory(TLSSocketFactory(sslContext.socketFactory), trustManager)
    } catch (e: Exception) {

    }
    return this
}
