package tcp_thread;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

import chat_exercicio_02.Message;

public class Cliente {
	private static Socket sock;
	private OutputStream os;
	private InputStream is;
	private int port;
	
	public void conectar(Socket sock) {
		this.sock = sock;
		try {
			is = sock.getInputStream();
			os = sock.getOutputStream();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void send(byte[] buf) throws InterruptedException {
		try {
			System.out.print("[ Enviando mensagem ............................... ");
			os.write(buf);
			os.flush();
			System.out.println("[OK] ]");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void receive() throws InterruptedException {
		byte [] buf = new byte[20];
		try {
			System.out.print("[ Aguardando resposta do servidor ..................... ");
			is.read(buf);
			System.out.println("[OK] ]");
			
			System.out.println(" Mensagem recebida do servidor: " + new String(buf));
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void listen() {
		byte [] buf = new byte[20];
		try {
			System.out.print("[ Aguardando resposta do servidor ..................... ");
			is.read(buf);
			System.out.println("[OK] ]");
			System.out.println(" Mensagem recebida do servidor: " + new String(buf));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void main(String [] args) throws InterruptedException {
		Cliente cl = new Cliente();
		System.out.print("[ Conectando com o Servidor TCP ..................... ");
		try {
			Socket sock = new Socket("127.0.0.1", 3_300);
			System.out.println("[OK] ]");
			
			cl.conectar(sock);
			String msg = "Olá, Servidor";
			cl.send(msg.getBytes());
			cl.receive();
			System.out.println("[ Finalizando o programa ......................... ]");
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		Socket sock = new Socket();
	}
}
