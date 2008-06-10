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
//            	autoAnswering(received, "I love this game", "huaran78", null, "我踢不了长腿！");
            	autoAnswering(received, "I love this game", "shangcm2006", "谁能把长腿踢了？", "我踢不了长腿！");
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
    		System.out.println("ignore title: " + chat.getWindowTitle());
    		return;
    	}
    	
    	if (!received.getSender().getId().equals(id)) {
    		System.out.println("ignore id: " + received.getSender());
    		return;
    	}
    	
    	if (content != null && !received.getContent().equals(content)) {
    		System.out.println("ignore content: " + received.getContent());
    		return;
    	}
    	
    	chat.send(msg);
    	
    	System.out.println("OK!");
    }
}
