package kim.uno.mock.data.remote.github.dto


import com.google.gson.annotations.SerializedName

data class GithubRepositoryPermissions(
    @SerializedName("admin")
    val admin: Boolean,
    @SerializedName("maintain")
    val maintain: Boolean,
    @SerializedName("pull")
    val pull: Boolean,
    @SerializedName("push")
    val push: Boolean,
    @SerializedName("triage")
    val triage: Boolean
)
