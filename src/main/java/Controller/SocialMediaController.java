package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.List;
// Importing Object Mapper
import com.fasterxml.jackson.databind.ObjectMapper;

//Importing Json Processing Exception 
import com.fasterxml.jackson.core.JsonProcessingException;

// Importing essentional class model
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;


/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;
    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postAccountHandler);    
        app.post("/login", this::postAccountLogInHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::getMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("messages/{message_id}", this::updateMessageByIdHandler);
        app.get("accounts/{account_id}/messages", this::getAllMessagesByAccountIdHandler);
        return app;
    }

    /**
     * Handler to register new Account - End point for new account registration
     * 
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void postAccountHandler(Context context) throws JsonProcessingException {
        
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account registeredAccount = accountService.insertAccount(account);

        if (registeredAccount != null) {
            context.json(mapper.writeValueAsString(registeredAccount));
        }
        else {
            context.status(400);
        }
    }

    /**
     * Handler to log in Account - End point for log in
     * 
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void postAccountLogInHandler(Context context) throws JsonProcessingException {
        
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account accfromDatabase = accountService.loginAccount(account);
        if (accfromDatabase != null) {
            context.json(mapper.writeValueAsString(accfromDatabase)).status(200);
        }
        else {
            context.status(401);
        }
    }

    /**
     * Handler to get all message - End point to retrieve all messages
     * 
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void getMessagesHandler(Context context) throws JsonProcessingException {
        List<Message> messages = messageService.getAllMessages();
        context.json(messages);
    }

    /**
     * Handler to post new message - End point to create new messages
     * 
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void postMessageHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        Account accfromDatabase = accountService.getAccountById(message.getPosted_by());
        if (accfromDatabase != null) {
            Message newMessage = messageService.insertMessage(message);
            if (newMessage != null) {
                context.json(mapper.writeValueAsString(newMessage));
            }
            else {
                context.status(400);
            }
        }
        else {
            context.status(400);
        }

    }

    /**
     * Handler to post get message by message_id - End point to get message by message_id
     * 
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void getMessageByIdHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message messagefromDatabase = messageService.getMessageById(message_id);
        if (messagefromDatabase != null) {
            context.json(mapper.writeValueAsString(messagefromDatabase));
        }
        else {
            context.status(200);
        }    
    }

    /**
     * Handler to delete message by message_id - End point to delete message
     * 
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void deleteMessageByIdHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message messagefromDatabase = messageService.getMessageById(message_id);
        if (messagefromDatabase != null) {
            messageService.deleteMesssageById(message_id);
            context.json(mapper.writeValueAsString(messagefromDatabase));
        }
        else {
            context.status(200);
        }    
    }

    /**
     * Handler to update message using message id - End point to update messages
     * 
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void updateMessageByIdHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message messagefromDatabase = messageService.getMessageById(message_id);
        
        
        if ((message.getMessage_text().isEmpty()) || (message.getMessage_text().length() > 255) || (messagefromDatabase == null)) {
            context.status(400);
        }
        else {
                messageService.updateMessageById(message_id, message.getMessage_text());
                Message newUpdatedMessage = messageService.getMessageById(message_id);
                context.json(mapper.writeValueAsString(newUpdatedMessage));  
        }
        
    }

    /**
     * Handler to get all message by account_id (posted_id) - End point to get all message by account_id
     * 
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void getAllMessagesByAccountIdHandler(Context context) throws JsonProcessingException {
        int account_id = Integer.parseInt(context.pathParam("account_id"));
        List<Message> messages = messageService.getAllMessagesByAccountId(account_id);
        context.json(messages);
    }
}