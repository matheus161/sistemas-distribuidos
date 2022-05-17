package tcp_thread;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor extends Thread {
	private static ServerSocket ss;
	private static Socket sock;
	private OutputStream os;
	private InputStream is;
	
	public Servidor(Socket sock) {
		this.sock = sock;
		try {
			is = sock.getInputStream();
			os = sock.getOutputStream();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void send(byte[] buf) throws InterruptedException {
		try {
			System.out.print("[ Enviando mensagem ............................... ");
			os.write(buf);
			//os.flush();
			System.out.println("[OK] ]");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void run() {
		try {
			OutputStream os = this.sock.getOutputStream();
			byte[] buf = new byte[20];
			
			System.out.print("[ Aguardando recebimento de mensagem ..................... ");
			is.read(buf); // Operação bloqueante (aguarando chegada de dados)
			System.out.println("[OK] ]");
			
			System.out.println(" Mensagem recebida: " + new String(buf));
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void main(String[] args) {
		try (ServerSocket ss = new ServerSocket(3_300, 5, InetAddress.getByName("127.0.0.1"))) {
			System.out.print("[ Aguardando pedidos de conexão .................. "); 
			sock = ss.accept(); // Operação bloqueante (aguardando pedido de conexão)
			System.out.println("[OK] ]");
			
			Thread server = new Servidor(sock);
			server.start();
			
			Servidor sv = new Servidor(sock);
			sv.send("Olá Cliente".getBytes());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
