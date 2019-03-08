package Server;


/* 
 * 聊天信息生成
 */
public class ChattingMessage extends Message {  
	private final static String separator = "!_&@#%_!";         //分隔符
	private final static String escapeCharacter = "_!!@#@%##%!!_";
	
	
	/*
	 * 生成聊天字符串
	 */
	public static String generate(String source, String destination, String message, int identify) {  //identify标志了这是群聊信息还是私聊信息或是文件
		String mess = null;

		source = escape(source);  //对消息进行转义
		message = escape(message);
		destination = escape(destination);
		
		mess = identify + separator + source + separator + destination + separator + message;
		return mess;
	}
	
	
	/*
	 * 信息转义方法
	 */
	private static String escape(String message) {
		String mess = null;
		
		mess = message.replaceAll(separator, escapeCharacter);
		return mess;
	}
	
	
	/*
	 * 逆转义方法
	 */
	private static String anti_escape(String message) {
		String mess = null;
		
		mess = message.replaceAll(escapeCharacter, separator);
		return mess;
	}
	
	
	/*
	 * 接收到的字符串进行分割
	 */
	public static String [] analysis(String message) {
		String [] strs = null;
		
		strs = message.split(separator);
		for(int i = 0; i < strs.length; i++) { //进行逆转义
			strs[i] = anti_escape(strs[i]);
		}
		return strs;
	}
	
}
