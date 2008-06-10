package test.skype;

import com.skype.Chat;
import com.skype.ChatMessage;
import com.skype.ChatMessageAdapter;
import com.skype.Skype;
import com.skype.SkypeException;
import com.skype.User;

public class TestListener {
	
    public static void main(String[] args) throws Exception {
    	
        Skype.setDeamon(false);
        
        Skype.addChatMessageListener(new ChatMessageAdapter() {
            public void chatMessageReceived(ChatMessage received) throws SkypeException {
//            	displayUsers(received);
            	autoAnswering(received, "I love this game", "huaran78", null, "���߲��˳��ȣ�");
            }
        });
        
    }
    
    private static void displayUsers(ChatMessage received) throws SkypeException {
    	Chat chat = received.getChat();
    	
    	System.out.println("title: " + chat.getWindowTitle());
    	
    	User[] users = chat.getAllActiveMembers();
    	
    	for (int i = 0; i < users.length; i++) {
			System.out.println(users[i].getFullName() + " - " + users[i].getId());
		}
    	
    }
    
    private static void autoAnswering(ChatMessage received, String title, String id, String content, String msg) throws SkypeException {
    	
    	Chat chat = received.getChat();
    	
    	if (!chat.getWindowTitle().equals(title)) {
    		return;
    	}
    	
    	if (!received.getSender().getId().equals(id)) {
    		return;
    	}
    	
    	if (content != null && !received.getContent().equals(content)) {
    		return;
    	}
    	
    	chat.send(msg);
    	
    	System.out.println("OK!");
    }
    
}
