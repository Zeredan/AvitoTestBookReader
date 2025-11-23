package test.task.auth

data class AuthUser(
    val uid: String,
    val email: String?,
    val phoneNumber: String?,
    val displayName: String?,
    val photoUrl: String?,
)