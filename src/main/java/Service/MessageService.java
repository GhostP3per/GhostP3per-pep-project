package Service;

import DAO.MessageDAO;
import Model.Message;
import java.util.List;

public class MessageService {
    public MessageDAO messageDAO;
    
    public MessageService() {
        messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message insertMessage (Message message) {
        if ((message.getMessage_text().length() <= 255) && (message.getMessage_text().length() > 0)) {
            return messageDAO.insertMessage(message);
        } 
        return null;
    }
    
}
