package exercicio_03_old;

import java.io.Serializable;
import java.util.Arrays;

public class Mensagem implements Serializable{
	private byte[] msg;
	private int destinatario;
	
	public Mensagem(byte[] msg) {
		super();
		this.msg = msg;
	}
	
	public Mensagem() {
		
	}
	
	public void setDestinatario (int destinatario) {
		this.destinatario = destinatario;
	}
	
	public int getDestinatario () {
		return destinatario;
	}
	
	public void setMessage(byte[] msg) {
		this.msg = msg;
	}
	
	public byte[] getMessage() {
		return msg;
	}

	@Override
	public String toString() {
		return "Mensagem [msg=" + msg + "]";
	}
	
}

