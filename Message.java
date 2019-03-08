package Server;

import java.util.LinkedList;

/*
 * 信息生成
 */
public class Message {
	private final static String separator = "_!@&#!_!";  //信息分隔符
	private final static String escapeCharacter = "!!_@%#@%#_!!";
	public String autoSeparator;     //动态分隔符，用于其他情况
	public String autoEscapeCharacter;   //动态转义符，用于其他情况
	
	
	/*
	 * 测试用
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
	 * 生成信息字符串
	 */
	public static String generate(int identify, String source, String destination, String message) {  //identify标志了这是什么类型的信息
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
	 * 动态生成字符串方法
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
	 * 信息转义方法
	 */
	private static String escape(String message) {
		String mess = "";
		
		mess = message.replaceAll(separator, escapeCharacter);
		return mess;
	}
	
	
	/*
	 * 动态信息转义方法
	 */
	private String autoEscape(String message) {
		String mess = "";
		
		mess = message.replaceAll(autoSeparator, autoEscapeCharacter);
		return mess;
	}
	
	
	/*
	 * 逆转义方法
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
	 * 动态逆转义方法
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
	 * 接收到的字符串进行分割
	 */
	public static String [] analysis(String message) {
		String [] temp = message.split(separator);
		LinkedList<String> tempList = new LinkedList<String>();
		
		for(int i = 0; i < temp.length; i = i + 2) { //偶数时信息，奇数是判定列表
			String mess = temp[i];
			String list = temp[i + 1];
			
			if(list.equals(" ")) { //没有判定列表
				tempList.add(mess);
			} else {
				String [] list01 = list.split(" ");
				int [] list02 = new int [list01.length];
				for(int j = 0; j < list02.length; j++) { //将判定列表取出
					list02[j] = Integer.parseInt(list01[j]);
				}
				
				tempList.add(anti_escape(mess, list02));  //将还原后的信息加入临时链表
			}
		}
		
		String [] strs = new String [tempList.size()];
		for(int i = 0; i < strs.length; i++) {  //将链表转换成数组
			strs[i] = tempList.get(i);
		}
		
		return strs;
	}
	
	
	/*
	 * 动态接收到的字符串分割
	 */
	public String [] autoAnalysis(String message) {
		String [] temp = message.split(autoSeparator);
		LinkedList<String> tempList = new LinkedList<String>();
		
		for(int i = 0; i < temp.length; i = i + 2) { //偶数时信息，奇数是判定列表
			String mess = temp[i];
			String list = temp[i + 1];
			
			if(list.equals(" ")) { //没有判定列表
				tempList.add(mess);
			} else {
				String [] list01 = list.split(" ");
				int [] list02 = new int [list01.length];
				for(int j = 0; j < list02.length; j++) { //将判定列表取出
					list02[j] = Integer.parseInt(list01[j]);
				}
				
				tempList.add(auto_anti_escape(mess, list02));  //将还原后的信息加入临时链表
			}
		}
		
		String [] strs = new String [tempList.size()];
		for(int i = 0; i < strs.length; i++) {  //将链表转换成数组
			strs[i] = tempList.get(i);
		}
		
		return strs;
	}
	
	/*
	 * 用于生成字符串内被转义的子字符串
	 */
	static int [] indexGenerated(String beReplace, String mess) {
		int i = 0;   //mess游标
		int j = 0;  //临时数组的大小
		int [] list = null;
		
		while(true) {  //运用BF算法将匹配的字符串的首位置找出来
			i = mess.indexOf(beReplace, i);
			if(i == -1) {
				break;
			} else {
				int [] tempList = new int [++j];
				if(list == null) {   //第一个是特殊情况
					tempList[0] = i;
				} else { //进行列表扩展
					for(int k = 0; k < list.length; k++) {
						tempList[k] = list[k];
					}
					tempList[j - 1] = i;
				}
				list = tempList;
				
				i++;  //下一个位置
			}
		}
		
		return list;
	}
	
	/*
	 * 用于将生成的目录字符串化
	 */
	static String stringOf(int [] list) {
		String mess = " ";
		if(list == null) {/*证明没有冲突字符*/} else {
			for(int i : list) {
				mess = mess + String.valueOf(i) + " ";
			}
			mess = mess.trim();
		}
		return mess;
	}
}
