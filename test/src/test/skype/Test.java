package test.skype;

import com.skype.Chat;
import com.skype.ChatMessage;
import com.skype.ChatMessageAdapter;
import com.skype.Skype;
import com.skype.SkypeException;
import com.skype.User;

public class Test {
	
    public static void main(String[] args) throws Exception {
    	sendTo("I love this game", "Í¦Æµ", 5, 4);
    }
    
    public static void startListening() throws SkypeException {
    	
        Skype.setDeamon(false);
        
        Skype.addChatMessageListener(new ChatMessageAdapter() {
            public void chatMessageReceived(ChatMessage received) throws SkypeException {
//            	displayUsers(received);
//            	autoAnswering(received, "I love this game", "huaran78", null, "ÎÒÌß²»ÁË³¤ÍÈ£¡", 1, 1);
//            	autoAnswering(received, "I love this game", "shangcm2006", "Ë­ÄÜ°Ñ³¤ÍÈÌßÁË£¿", "ÎÒÌß²»ÁË³¤ÍÈ£¡", 1, 1);
            	autoAnswering(received, "³Ô·¹£¬Ë¯¾õ£¬´ò¼¦Ã×£¬ÇçÌì´óÅùö¨£¡", null, null, "ºÈ¾Æ", 20, 3);
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
    
    private static void displayUsers(ChatMessage received) throws SkypeException {
    	Chat chat = received.getChat();
    	
    	System.out.println("title: " + chat.getWindowTitle());
    	
    	User[] users = chat.getAllActiveMembers();
    	
    	for (int i = 0; i < users.length; i++) {
			System.out.println(users[i].getFullName() + " - " + users[i].getId());
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
