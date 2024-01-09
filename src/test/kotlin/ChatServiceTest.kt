import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class ChatServiceTest {

    private val service = ChatService

    @Before
    fun clearBeforeTest() {
        service.clear()
    }

    //    проверка метода добавления сообщения
    @Test
    fun addMessage() {
        val message1 = service.addMessage(Message(receiverId = 2, senderId = 1, text = "Сообщение 12"))
        assertEquals(1, message1.id) // проверяем, что сообщение добавлено по id

        val message2 = service.addMessage(Message(receiverId = 1, senderId = 2, text = "Сообщение 21"))
        assertEquals(message1.cid, message2.cid)// проверяем, что для нового сообщения не создан новый чат

        val message3 = service.addMessage(Message(receiverId = 3, senderId = 1, text = "Сообщение 31"))
        assertEquals(2, message3.cid)// проверяем, что для нового сообщения создан новый чат

    }

    // тест проверки метода получения количества чатов с непрочитанными сообщениями
    @Test
    fun getUnreadChatsCount() {
        service.addMessage(Message(receiverId = 2, senderId = 1, text = "Сообщение 12"))
        val message2 = service.addMessage(Message(receiverId = 1, senderId = 2, text = "Сообщение 21"))
        val message3 = service.addMessage(Message(receiverId = 3, senderId = 1, text = "Сообщение 31"))

        assertEquals(2, service.getUnreadChatsCount())

        service.setMessageIsRead(message3.id)
        service.deleteMessage(message2.id)
        assertEquals(1, service.getUnreadChatsCount())
    }


    //    тест для проверки метода получения неудаленных чатов
    @Test
    fun getNotDeletedChats() {
        service.addMessage(Message(receiverId = 2, senderId = 1, text = "Сообщение 12"))
        val message2 = service.addMessage(Message(receiverId = 1, senderId = 2, text = "Сообщение 21"))
        service.addMessage(Message(receiverId = 3, senderId = 1, text = "Сообщение 31"))

        service.deleteChat(message2.cid)

        assertEquals(1, service.getNotDeletedChats().size)
    }


    //    тест проверки успешного удаления чата и сообщений в нем
    @Test
    fun deleteChatIsTrue() {
        val message = service.addMessage(Message(receiverId = 1, senderId = 2, text = "Сообщение 1"))
        service.addMessage(Message(receiverId = 1, senderId = 2, text = "Сообщение 2"))
        service.addMessage(Message(receiverId = 2, senderId = 1, text = "Сообщение 3"))

        val result = service.deleteChat(message.cid)

        assertTrue(result)
        assertTrue(service.getUnreadMessages(message.cid).isEmpty())
    }

    //    тест проверки неуспешного удаления чата
    @Test
    fun deleteChatIsFalse() {
        val message = service.addMessage(Message(receiverId = 1, senderId = 2, text = "Сообщение 1"))
        val result = service.deleteChat(message.cid + 1)
        assertFalse(result)
    }

    //    тест для метода получения непрочитанных сообщений из чаата
    @Test
    fun getUnreadMessages() {
        val message1 = service.addMessage(Message(receiverId = 2, senderId = 1, text = "Сообщение 12"))
        val message2 = service.addMessage(Message(receiverId = 1, senderId = 2, text = "Сообщение 21"))
        val message3 = service.addMessage(Message(receiverId = 3, senderId = 1, text = "Сообщение 31"))

        service.deleteMessage(message1.id)
        service.setMessageIsRead(message3.id)
        val result = service.getUnreadMessages(message1.cid)
        assertEquals(listOf(message2), result)
    }

    //    тест проверки метода получения последних сообщений из чатов пользователя
    @Test
    fun getLastMessages() {
        val message1 = service.addMessage(Message(receiverId = 1, senderId = 2, text = "Сообщение 1"))
        service.addMessage(Message(receiverId = 1, senderId = 2, text = "Сообщение 2"))

        val message2 = service.addMessage(Message(receiverId = 1, senderId = 3, text = "Сообщение 3"))
        service.deleteMessage(message2.id)

        val result = service.getLastMessages(message1.receiverId)

        assertEquals(listOf("Сообщение 2", "нет сообщений"), result)
    }

    //    тест проверки метода получения всех сообщений из чата для текущего пользователя
    @Test
    fun getMessagesFromUser() {
        service.addMessage(Message(receiverId = 1, senderId = 2, text = "Сообщение 1"))
        val message2 = service.addMessage(Message(receiverId = 1, senderId = 2, text = "Сообщение 2"))
        val message3 = service.addMessage(Message(receiverId = 1, senderId = 2, text = "Сообщение 3"))

        val result1 = service.getMessagesFromUser(1, 2, 2)

        assertEquals(listOf(message3, message2), result1)
        val result2 = service.getMessagesFromUser(1, 2, 2)

        assertTrue(result2[0].isRead)
        assertTrue(result2[1].isRead)

    }

    // тест для успешного удаления сообщения
    @Test
    fun deleteMessageIsTrue() {
        val message = service.addMessage(Message(receiverId = 1, senderId = 2, text = "Сообщение 1"))
        service.addMessage(Message(receiverId = 1, senderId = 2, text = "Сообщение 2"))

        val result = service.deleteMessage(message.id)

        assertTrue(result)
    }

    // тест для неуспешного удаления сообщения
    @Test
    fun deleteMessageIsFalse() {
        val message = service.addMessage(Message(receiverId = 1, senderId = 2, text = "Сообщение 1"))

        val result = service.deleteMessage(message.id + 1)

        assertFalse(result)
    }

    //    тест для успешного изменения сообщения
    @Test
    fun updateMessageIsTrue() {
        val message = service.addMessage(Message(receiverId = 1, senderId = 2, text = "Сообщение 1"))

        val result = service.updateMessage(message.id, "Сообщение изменено")

        assertTrue(result)
    }

    //    тест для неуспешного изменения сообщения
    @Test
    fun updateMessageIsFalse() {
        val message = service.addMessage(Message(receiverId = 1, senderId = 2, text = "Сообщение 1"))

        val result = service.updateMessage(message.id + 1, "Сообщение изменено")

        assertFalse(result)
    }


}