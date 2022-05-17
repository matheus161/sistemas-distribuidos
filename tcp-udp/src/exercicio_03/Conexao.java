package exercicio_03;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Conexao {
	private Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	
	public Conexao(Socket socket) throws IOException {
		this.socket = socket;
		this.oos = new ObjectOutputStream(this.socket.getOutputStream());
		this.ois = new ObjectInputStream(this.socket.getInputStream());
	}

	public Socket getSocket() {
		return this.socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public ObjectOutputStream getOos() {
		return this.oos;
	}

	public void setOos(ObjectOutputStream oos) {
		this.oos = oos;
	}

	public ObjectInputStream getOis() {
		return this.ois;
	}

	public void setOis(ObjectInputStream ois) {
		this.ois = ois;
	}
}
