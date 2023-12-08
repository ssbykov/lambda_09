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
        val message1 = service.addMessage(Message(senderId = 1, receiverId = 2, text = "Сообщение 12"))
        assertEquals(1, message1.id) // проверяем, что сообщение добавлено по id

        val message2 = service.addMessage(Message(senderId = 2, receiverId = 1, text = "Сообщение 21"))
        assertEquals(message1.cid, message2.cid)// проверяем, что для нового сообщения не создан новый чат

        val message3 = service.addMessage(Message(senderId = 1, receiverId = 3, text = "Сообщение 31"))
        assertEquals(2, message3.cid)// проверяем, что для нового сообщения создан новый чат

    }
}