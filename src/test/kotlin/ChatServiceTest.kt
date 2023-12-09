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

//    тест проверки метода получения последних сообщений из чатов пользователя
    @Test
    fun getLastMessages(){
        val message1 = service.addMessage(Message(receiverId = 1, senderId = 2, text = "Сообщение 1"))
        service.addMessage(Message(receiverId = 1, senderId = 2, text = "Сообщение 2"))

        val message2 = service.addMessage(Message(receiverId = 1, senderId = 3, text = "Сообщение 3"))
        service.deleteMessage(message2.id)

        val result = service.getLastMessages(message1.receiverId)

        assertEquals(listOf("Сообщение 2", "нет сообщений"), result)
    }

//    тест проверки метода получения всех сообщений из чата для текущего пользователя
    @Test
    fun getMessagesFromUser(){
    val message1 = service.addMessage(Message(receiverId = 1, senderId = 2, text = "Сообщение 1"))
    val message2 = service.addMessage(Message(receiverId = 1, senderId = 2, text = "Сообщение 2"))
    val message3 = service.addMessage(Message(receiverId = 1, senderId = 2, text = "Сообщение 3"))

    val result1 = service.getMessagesFromUser(1, 2, 2)

    assertEquals(listOf(message2, message3), result1)
    val result2 = service.getMessagesFromUser(1, 2, 2)

    assertTrue(result2[0].isRead)
    assertTrue(result2[1].isRead)

    }


    fun createMessages(chatCount: Int, messageCount: Int) {
        (1..chatCount).forEach { chat ->
            (1..messageCount).forEach { message ->
                service.addMessage(
                    Message(
                        receiverId = chat,
                        senderId = chatCount + 1,
                        text = "Сообщение от id${chatCount + 1}, для id${chat}"
                    )
                )
            }
        }
    }
}