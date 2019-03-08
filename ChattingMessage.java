package Server;


/* 
 * ������Ϣ����
 */
public class ChattingMessage extends Message {  
	private final static String separator = "!_&@#%_!";         //�ָ���
	private final static String escapeCharacter = "_!!@#@%##%!!_";
	
	
	/*
	 * ���������ַ���
	 */
	public static String generate(String source, String destination, String message, int identify) {  //identify��־������Ⱥ����Ϣ����˽����Ϣ�����ļ�
		String mess = null;

		source = escape(source);  //����Ϣ����ת��
		message = escape(message);
		destination = escape(destination);
		
		mess = identify + separator + source + separator + destination + separator + message;
		return mess;
	}
	
	
	/*
	 * ��Ϣת�巽��
	 */
	private static String escape(String message) {
		String mess = null;
		
		mess = message.replaceAll(separator, escapeCharacter);
		return mess;
	}
	
	
	/*
	 * ��ת�巽��
	 */
	private static String anti_escape(String message) {
		String mess = null;
		
		mess = message.replaceAll(escapeCharacter, separator);
		return mess;
	}
	
	
	/*
	 * ���յ����ַ������зָ�
	 */
	public static String [] analysis(String message) {
		String [] strs = null;
		
		strs = message.split(separator);
		for(int i = 0; i < strs.length; i++) { //������ת��
			strs[i] = anti_escape(strs[i]);
		}
		return strs;
	}
	
}
