package Server;

import java.util.LinkedList;

/*
 * ��Ϣ����
 */
public class Message {
	private final static String separator = "_!@&#!_!";  //��Ϣ�ָ���
	private final static String escapeCharacter = "!!_@%#@%#_!!";
	public String autoSeparator;     //��̬�ָ����������������
	public String autoEscapeCharacter;   //��̬ת����������������
	
	
	/*
	 * ������
	 */
	public static void main(String [] args) {
		Message m = new Message();
		m.autoSeparator = "@@@";
		m.autoEscapeCharacter = "###";
		String mess = m.autoGenerate("1", "source@@@###", "destination");
		String [] strs = m.autoAnalysis(mess);
		for(String str : strs) {
			System.out.println(str);
		}
	}
	
	
	/*
	 * ������Ϣ�ַ���
	 */
	public static String generate(int identify, String source, String destination, String message) {  //identify��־������ʲô���͵���Ϣ
		String mess = "";
		int [] tempList = null;
		tempList = indexGenerated(separator, source);
		source = escape(source);
		source = source + separator + stringOf(tempList);
		tempList = indexGenerated(separator, destination);
		destination = escape(destination);
		destination = destination + separator + stringOf(tempList);
		tempList = indexGenerated(separator, message);
		message = escape(message);
		message = message + separator + stringOf(tempList);
		
		mess = identify + separator + " " + separator + source + separator + destination + separator + message;
		return mess;
	}
	
	/*
	 * ��̬�����ַ�������
	 */
	public String autoGenerate(String ...message) {
		String mess = "";
		int [] tempList = null;
		String [] strs = new String[message.length];
		
		for(int i = 0; i < strs.length; i++) { 
			tempList = indexGenerated(autoSeparator, message[i]);
			strs[i] = autoEscape(message[i]) + autoSeparator + stringOf(tempList);
			if(i < strs.length - 1) {
				mess = mess + strs[i] + autoSeparator;
			} else {
				mess = mess + strs[i];
			}
		}
		
		return mess;
	}
	
	
	/*
	 * ��Ϣת�巽��
	 */
	private static String escape(String message) {
		String mess = "";
		
		mess = message.replaceAll(separator, escapeCharacter);
		return mess;
	}
	
	
	/*
	 * ��̬��Ϣת�巽��
	 */
	private String autoEscape(String message) {
		String mess = "";
		
		mess = message.replaceAll(autoSeparator, autoEscapeCharacter);
		return mess;
	}
	
	
	/*
	 * ��ת�巽��
	 */
	public static String anti_escape(String message, int list[]) {
		StringBuffer messBuff = new StringBuffer(message);
		int i = 0;
		int j = 0;
		while(true) {
			i = messBuff.indexOf(escapeCharacter, i);
			if(i == -1) {
				break;
			} else {
				try {
					if(i == list[j]) {
							messBuff.replace(i, i + escapeCharacter.length(), separator);
							j++;
						}
					} catch (Exception e) {}
				i++;
			}
		}
		return messBuff.toString();
	}
	
	
	/*
	 * ��̬��ת�巽��
	 */
	private String auto_anti_escape(String message, int list[]) {
		StringBuffer messBuff = new StringBuffer(message);
		int i = 0;
		int j = 0;
		while(true) {
			i = messBuff.indexOf(autoEscapeCharacter, i);
			if(i == -1) {
				break;
			} else {
				try {
					if(i == list[j]) {
							messBuff.replace(i, i + autoEscapeCharacter.length(), autoSeparator);
							j++;
						}
					} catch (Exception e) {}
				i++;
			}
		}
		return messBuff.toString();
	}
	
	
	/*
	 * ���յ����ַ������зָ�
	 */
	public static String [] analysis(String message) {
		String [] temp = message.split(separator);
		LinkedList<String> tempList = new LinkedList<String>();
		
		for(int i = 0; i < temp.length; i = i + 2) { //ż��ʱ��Ϣ���������ж��б�
			String mess = temp[i];
			String list = temp[i + 1];
			
			if(list.equals(" ")) { //û���ж��б�
				tempList.add(mess);
			} else {
				String [] list01 = list.split(" ");
				int [] list02 = new int [list01.length];
				for(int j = 0; j < list02.length; j++) { //���ж��б�ȡ��
					list02[j] = Integer.parseInt(list01[j]);
				}
				
				tempList.add(anti_escape(mess, list02));  //����ԭ�����Ϣ������ʱ����
			}
		}
		
		String [] strs = new String [tempList.size()];
		for(int i = 0; i < strs.length; i++) {  //������ת��������
			strs[i] = tempList.get(i);
		}
		
		return strs;
	}
	
	
	/*
	 * ��̬���յ����ַ����ָ�
	 */
	public String [] autoAnalysis(String message) {
		String [] temp = message.split(autoSeparator);
		LinkedList<String> tempList = new LinkedList<String>();
		
		for(int i = 0; i < temp.length; i = i + 2) { //ż��ʱ��Ϣ���������ж��б�
			String mess = temp[i];
			String list = temp[i + 1];
			
			if(list.equals(" ")) { //û���ж��б�
				tempList.add(mess);
			} else {
				String [] list01 = list.split(" ");
				int [] list02 = new int [list01.length];
				for(int j = 0; j < list02.length; j++) { //���ж��б�ȡ��
					list02[j] = Integer.parseInt(list01[j]);
				}
				
				tempList.add(auto_anti_escape(mess, list02));  //����ԭ�����Ϣ������ʱ����
			}
		}
		
		String [] strs = new String [tempList.size()];
		for(int i = 0; i < strs.length; i++) {  //������ת��������
			strs[i] = tempList.get(i);
		}
		
		return strs;
	}
	
	/*
	 * ���������ַ����ڱ�ת������ַ���
	 */
	static int [] indexGenerated(String beReplace, String mess) {
		int i = 0;   //mess�α�
		int j = 0;  //��ʱ����Ĵ�С
		int [] list = null;
		
		while(true) {  //����BF�㷨��ƥ����ַ�������λ���ҳ���
			i = mess.indexOf(beReplace, i);
			if(i == -1) {
				break;
			} else {
				int [] tempList = new int [++j];
				if(list == null) {   //��һ�����������
					tempList[0] = i;
				} else { //�����б���չ
					for(int k = 0; k < list.length; k++) {
						tempList[k] = list[k];
					}
					tempList[j - 1] = i;
				}
				list = tempList;
				
				i++;  //��һ��λ��
			}
		}
		
		return list;
	}
	
	/*
	 * ���ڽ����ɵ�Ŀ¼�ַ�����
	 */
	static String stringOf(int [] list) {
		String mess = " ";
		if(list == null) {/*֤��û�г�ͻ�ַ�*/} else {
			for(int i : list) {
				mess = mess + String.valueOf(i) + " ";
			}
			mess = mess.trim();
		}
		return mess;
	}
}
