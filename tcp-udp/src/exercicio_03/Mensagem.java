package exercicio_03;

import java.io.Serializable;

public class Mensagem implements Serializable {
	private byte[] msg;
	private int destinatario;

	public Mensagem(byte[] msg) {
		this.msg = msg;
	}

	public Mensagem() {

	}

	public void setDestinatario(int destinatario) {
		this.destinatario = destinatario;
	}

	public int getDestinatario() {
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
