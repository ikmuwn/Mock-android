package kim.uno.mock.data.remote

import android.os.Build
import androidx.collection.ArrayMap
import kim.uno.mock.BuildConfig
import kim.uno.mock.data.remote.github.dto.GithubServerException
import kim.uno.mock.util.Retrofit
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class GithubRetrofit @Inject constructor() : Retrofit() {

    override val domain = DOMAIN
    override val enableTls = true
    override val headers: ArrayMap<String, String>
        get() = Companion.headers

    override fun proceed(chain: Interceptor.Chain, request: Request): Response {
        val response = super.proceed(chain, request)
        if (!response.isSuccessful) {
            throw errorBodyAs(GithubServerException::class.java, response.body)
                ?: IOException(response.message)
        }

        return response
    }

    companion object {

        val DOMAIN: String
            get() = "https://api.github.com"

        val headers: ArrayMap<String, String>
            get() = ArrayMap<String, String>().apply {
                put("x-app-ver", BuildConfig.VERSION_NAME)
                put("x-os-ver", Build.VERSION.RELEASE)
                put("x-device-model", Build.MODEL)
            }
    }

}
