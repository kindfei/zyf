package test.skype;

import com.skype.Chat;
import com.skype.ChatMessage;
import com.skype.ChatMessageAdapter;
import com.skype.Skype;
import com.skype.SkypeException;
import com.skype.User;

public class Test {
	
    public static void main(String[] args) throws Exception {
    	displayUsers("MFX");
    }
    
    public static void startListening() throws SkypeException {
    	
        Skype.setDeamon(false);
        
        Skype.addChatMessageListener(new ChatMessageAdapter() {
            public void chatMessageReceived(ChatMessage received) throws SkypeException {
//            	displayUsers(received);
//            	autoAnswering(received, "I love this game", "huaran78", null, "我踢不了长腿！", 1, 1);
//            	autoAnswering(received, "I love this game", "shangcm2006", "谁能把长腿踢了？", "我踢不了长腿！", 1, 1);
            	autoAnswering(received, "吃饭，睡觉，打鸡米，晴天大霹雳！", null, null, "喝酒", 20, 3);
            }
        });
    	
    }
    
    public static void sendTo(String title, String msg, int len, int cnt) throws SkypeException {
    	Chat[] chats = Skype.getAllActiveChats();
    	for (int i = 0; i < chats.length; i++) {
			System.out.println(chats[i].getWindowTitle());
			if (chats[i].getWindowTitle().equals(title)) {
				send(chats[i], msg,  len, cnt);
				break;
			}
		}
    }
    
    public static void displayUsers(String title) throws SkypeException {

    	Chat[] chats = Skype.getAllActiveChats();
    	for (int i = 0; i < chats.length; i++) {
			System.out.println(chats[i].getWindowTitle());
			if (chats[i].getWindowTitle().equals(title)) {
		    	
		    	System.out.println("title: " + chats[i].getWindowTitle());
		    	
		    	User[] users = chats[i].getAllActiveMembers();
		    	
		    	for (int j = 0; j < users.length; j++) {
					System.out.println(users[j].getFullName() + "=" + users[j].getId());
				}
		    	
				break;
			}
		}
    }
    
    private static void autoAnswering(ChatMessage received, String title, String id, String content
    		, String msg, int len, int cnt) throws SkypeException {
    	
    	Chat chat = received.getChat();
    	
    	if (!chat.getWindowTitle().equals(title)) {
    		System.out.println("ignore title: " + chat.getWindowTitle());
    		return;
    	}
    	
    	if (content != null && !received.getSender().getId().equals(id)) {
    		System.out.println("ignore id: " + received.getSender());
    		return;
    	}
    	
    	if (content != null && !received.getContent().equals(content)) {
    		System.out.println("ignore content: " + received.getContent());
    		return;
    	}

		send(chat, msg,  len, cnt);
		
    }
    
    private static void send(Chat chat, String msg, int len, int cnt) throws SkypeException {
    	for (int i = 1; i <= len * cnt; i++) {
    		StringBuffer sb = new StringBuffer();
    		int x = i % (len * 2);
    		int l = 0;
    		
    		if (x < len) {
        		l = x;
    		} else {
    			l = (len * 2) - x;
    		}
    		
    		for (int j = 0; j < l; j++) {
				sb.append(msg);
			}
    		
        	chat.send(sb.toString());
    	}
    	
    	System.out.println("Send OK!");
    }
}
