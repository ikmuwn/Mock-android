package kim.uno.mock.data.remote.github.dto

import com.google.gson.annotations.SerializedName
import java.io.IOException

data class GithubServerException(
    @SerializedName("message")
    override val message: String?,
    @SerializedName("documentation_url")
    val documentationUrl: String?
) : IOException(message)
