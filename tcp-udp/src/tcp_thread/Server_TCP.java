package tcp_thread;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server_TCP extends Thread {
	private static ArrayList<BufferedWriter> clientes;
	private static ServerSocket ss;
	private String nome;
	private Socket sock;
	private InputStream is;
	private InputStreamReader inr;
	private BufferedReader bfr;
	
	public Server_TCP(Socket sock) {
		this.sock = sock;
		
		try {
			is = sock.getInputStream();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		try {
			OutputStream os = this.sock.getOutputStream();
			byte[] buf = new byte[20];
			
			System.out.print("[ Aguardando recebimento de mensagem ..................... ");
			is.read(buf); // Operação bloqueante (aguarando chegada de dados)
			System.out.println("[OK] ]");
			
			String msg = new String(buf); // Mapeando vetor de bytes recebido para String
			
			System.out.println(" Mensagem recebida: " + msg);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void main(String[] args) {
		try (ServerSocket ss = new ServerSocket(3_300, 5, InetAddress.getByName("127.0.0.1"))) {
			System.out.print("[ Aguardando pedidos de conexão .................. "); 
			Socket sock = ss.accept(); // Operação bloqueante (aguardando pedido de conexão)
			System.out.println("[OK] ]");
			
			Thread server = new Server_TCP(sock);
			server.start();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
