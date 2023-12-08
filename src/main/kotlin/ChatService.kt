object ChatService {
    private val chats = mutableListOf<Chat>()
    private var cid = 0

    private val messages = mutableListOf<Message>()
    private var mid = 0

    //    методе создание нового чата
    private fun addChat(senderId: Int, receiverId: Int): Chat {
        chats.add(Chat(id = ++cid, userIds = Pair(senderId, receiverId)))
        return chats.last()
    }

    //    метод удавления чата
    fun deleteChat(id: Int): Boolean {
        return chats.find { it.id == id && !it.isDeleted }?.let { chat ->
            chats[chats.indexOf(chat)] = chat.copy(isDeleted = true)
            messages.filter { it.cid == id }.forEach {
                deleteMessage(it.id)
            }
            true
        } ?: false
    }

    //    метод получения списка всех чатов
    fun getAllChats(): List<Chat> {
        return chats
    }

    //    метод получения списка неудаленных чатов
    fun getNotDeletedChats(): List<Chat> {
        return chats.filter { !it.isDeleted }
    }

    //    метод получения чатов с непрочитанными сообщениями
    fun getUnreadChatsCount(): Int {
        return chats.filter { getUnreadMessages(it.id).isNotEmpty() }.size
    }

    //    метод получения чата между двумя пользователями или его создания, если чат не существует
    private fun getOrCreateChat(senderId: Int, receiverId: Int): Chat {
        return chats.find {
            it.userIds == Pair(senderId, receiverId) || it.userIds == Pair(receiverId, senderId)
        } ?: addChat(senderId, receiverId)
    }


    //    метод добавления нового сообщения
    fun addMessage(message: Message): Message {
        val chat = getOrCreateChat(message.senderId, message.receiverId)
        messages.add(message.copy(id = ++mid, cid = chat.id))
        return messages.last()
    }

    //    метод удаления сообщения
    fun deleteMessage(id: Int): Boolean {
        return messages.find { it.id == id && !it.isDeleted }?.let { message ->
            messages[messages.indexOf(message)] = message.copy(isDeleted = true)
            true
        } ?: false
    }

    //    получение списка неудаленных и непрочитанных сообщения чата
    fun getUnreadMessages(cid: Int): List<Message> {
        return messages.filter { it.cid == cid && !it.isDeleted && !it.isRead }
    }

    //    получение последних сообщений из чата
    fun getLastMessages(cid: Int, count: Int): List<Message> {
        return messages.filter { !it.isDeleted && it.cid == cid }.takeLast(count)
    }

    //    получение списка сообщений из чата по id собеседника
    fun getMessagesFromUser(uid: Int, count: Int): List<Message> {
        val result = messages.filter { !it.isDeleted && it.senderId == uid }.takeLast(count)
        result.forEach { setMessageIsRead(it.id) }
        return result
    }

    fun setMessageIsRead(id: Int): Boolean {
        return messages.find { it.id == id && !it.isDeleted }?.let { findMessage ->
            messages[messages.indexOf(findMessage)] = findMessage.copy(isRead = true)
            true
        } ?: false
    }

    // метод изменения сообщения
    fun updateMessage(message: Message): Boolean {
        return messages.find { it.id == message.id && !it.isDeleted }?.let { findMessage ->
            messages[messages.indexOf(findMessage)] = message.copy()
            true
        } ?: false
    }

    //     метод очистки данных
    fun clear() {
        chats.clear()
        cid = 0

        messages.clear()
        mid = 0

    }
}