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

    /**
     * Use the messageDAO to get all messages from the database.
     * 
     * @param - none.
     * 
     * @return a List of Message objects from the database
     */
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    /**
     * Use the messageDAO to insert a new message into the database.
     * 
     * @param message - a message object with all property of message to be inserted into the database.
     * 
     * @return - the newly inserted message object, with message_id that was automatically generated.
     */
    public Message insertMessage (Message message) {
        if ((message.getMessage_text().length() <= 255) && (message.getMessage_text().length() > 0)) {
            return messageDAO.insertMessage(message);
        } 
        return null;
    }

    /**
     * Use the messageDAO to retrieve a message using message_id from the database.
     * 
     * @param message_id - that id of the message we want to retrieve from the database.
     * 
     * @return the Message object with the given message_id if exist
     */
    public Message getMessageById(int message_id) {
        return messageDAO.getMesssageById(message_id);
    }

    /**
     * Use the messageDAO to delete a message using message_id from the database.
     * 
     * @param message_id - that id of the message we want to delete from the database.
     * 
     * @return none
     */
    public void deleteMesssageById(int message_id) {
        messageDAO.deleteMesssageById(message_id);
    }

    /**
     * Use the messageDAO to update a message using message_id from the database.
     * 
     * @param message_id - that id of the message we want to update from the database.
     * 
     * @return the newly updated Message object with the given message_id if exist
     */
    public Message updateMessageById(int message_id, String message_text) {
        messageDAO.updateMesssageById(message_id, message_text);
        return messageDAO.getMesssageById(message_id);
    }


    /**
     * Use the messageDAO to retrieve a message posted by a specific account from the database.
     * 
     * @param account_id - that id of the account that we want to retrieve all the messages from, from the database.
     * 
     * @return - a List of Message objects possted by an account matching the account_id if exist
     */
    public List<Message> getAllMessagesByAccountId(int account_id) {
        return messageDAO.getAllMessagesByAccountId(account_id);
    }
}
