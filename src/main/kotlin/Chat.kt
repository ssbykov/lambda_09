data class Chat (
    val id: Int,
    val userIds: Pair<Int, Int>,
    val isDeleted: Boolean = false
)


data class Message (
    val id: Int = 0,
    val cid: Int = 0,
    val receiverId: Int,
    val senderId: Int,
    val text: String,
    val isRead: Boolean = false,
    val isDeleted: Boolean = false
)