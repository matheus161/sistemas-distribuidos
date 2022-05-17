package chat_exercicio_02;

import java.io.Serializable;

public class Message implements Serializable{
	private byte[] msg;
	
	public Message(byte[] msg) {
		super();
		this.msg = msg;
	}
	
	public Message() {
		
	}
	
	public void setMessage(byte[] msg) {
		this.msg = msg;
	}
	
	public byte[] getMessage() {
		return msg;
	}
}
