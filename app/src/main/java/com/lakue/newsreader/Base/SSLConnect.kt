package com.lakue.newsreader.Base

import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import java.security.cert.CertificateException
import java.security.cert.X509Certificate

import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class SSLConnect {
    // always verify the host - dont check for certificate
    internal val DO_NOT_VERIFY: HostnameVerifier = HostnameVerifier { hostname, session -> true }

    /**
     * Trust every server - don't check for any certificate
     */
    private fun trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
                return arrayOf()
            }

            @Throws(CertificateException::class)
            override fun checkClientTrusted(
                chain: Array<X509Certificate>,
                authType: String
            ) {
            }

            @Throws(CertificateException::class)
            override fun checkServerTrusted(
                chain: Array<X509Certificate>,
                authType: String
            ) {
            }
        })

        // Install the all-trusting trust manager
        try {
            val sc = SSLContext.getInstance("TLS")
            sc.init(null, trustAllCerts, java.security.SecureRandom())
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.socketFactory)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun postHttps(url: String, connTimeout: Int, readTimeout: Int): HttpsURLConnection? {
        trustAllHosts()

        var https: HttpsURLConnection? = null
        try {
            https = URL(url).openConnection() as HttpsURLConnection
            https.hostnameVerifier = DO_NOT_VERIFY
            https.connectTimeout = connTimeout
            https.readTimeout = readTimeout
        } catch (e: MalformedURLException) {
            e.printStackTrace()
            return null
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        return https
    }
}