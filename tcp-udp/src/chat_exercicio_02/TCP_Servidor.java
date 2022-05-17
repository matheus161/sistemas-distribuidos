package chat_exercicio_02;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class TCP_Servidor {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		System.out.print("[ Iniciando Servidor TCP ................... ");
		try (ServerSocket ss = new ServerSocket(3_300, 5, InetAddress.getByName("127.0.0.1"))){
			System.out.println("[OK] ]"); // Abri conexão naquela porta
			
			System.out.print("[ Aguardando pedidos de conexão .................. "); // atalho: sysout + ctrl + espace
			// Enquanto o cliente não conectar eu vou bloquear
			Socket sock = ss.accept(); // Operação bloqueante (aguardando pedido de conexão)
			System.out.println("[OK] ]");
			
			ObjectInputStream ois = new ObjectInputStream(sock.getInputStream());
			ObjectOutputStream oos = new ObjectOutputStream(sock.getOutputStream());
			byte[] buf_receive; // buffer de recepção
			Message msg_send;
			
			while(true) {
				buf_receive = new byte[20]; 
				
				System.out.print("[ Aguardando resposta do Cliente ..................... "); 
				ois.read(buf_receive); // Operação bloqueante (aguardando chegada de dados)
				System.out.println("[OK] ]");
				
				System.out.println(" Mensagem recebida: " + new String(buf_receive));
				
				System.out.print("Digite uma mensagem para o Cliente: ");
				msg_send = new Message(sc.nextLine().getBytes()); // Pegando a mensagem e convertendo em Bytes
				
				System.out.print("[ Enviando mensagem para o Cliente ............................ ");
				oos.write(msg_send.getMessage());
				oos.flush();			
				System.out.println("[OK] ]");
				
				
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
	}
}
