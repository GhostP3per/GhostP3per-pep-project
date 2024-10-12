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
        app.delete("/messages/{message_id}", this::getMessageByIdHandler);
        app.patch("messages/{message_id}", this::updateMessageByIdHandler);
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
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
     * Handler to log in Account - End point for new account registration
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
    private void updateMessageByIdHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        String message_text = mapper.readValue(context.body(), String.class);
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        
        if ((message_text.length() < 0) && (message_text.length() > 255)) {
            context.status(400);
        }
        else {
            Message messagefromDatabase = messageService.getMessageById(message_id);
            if (messagefromDatabase != null) {
                messageService.updateMessageById(message_id, message_text);
                Message newUpdatedMessage = messageService.getMessageById(message_id);
                context.json(mapper.writeValueAsString(newUpdatedMessage));
            }
            else {
                context.status(400);
            }    
        }
        
    }

    
    




}