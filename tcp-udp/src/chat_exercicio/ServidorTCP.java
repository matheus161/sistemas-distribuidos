package chat_exercicio;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ServidorTCP {
	public static void main(String[] args) {
		System.out.print("[ Iniciando Servidor TCP ................... ");
		try (ServerSocket ss = new ServerSocket(3_300, 5, InetAddress.getByName("127.0.0.1"))){
			System.out.println("[OK] ]"); // Abri conexão naquela porta
			
			Scanner sc = new Scanner(System.in);
			
			System.out.print("[ Aguardando pedidos de conexão .................. "); // atalho: sysout + ctrl + espace
			// Enquanto o cliente não conectar eu vou bloquear
			Socket sock = ss.accept(); // Operação bloqueante (aguardando pedido de conexão)
			System.out.println("[OK] ]");
			
			InputStream is = sock.getInputStream(); // canal de entrada de dados
			OutputStream os = sock.getOutputStream(); // canal de saída de dados
			String msg_received;
			byte[] buf_received = new byte[30]; // buffer de recepção
			String msg_send;
			byte[] buf_send; // buffer de envio
			
			while(true) {
				System.out.print("[ Aguardando recebimento de mensagem ..................... ");
				is.read(buf_received); // Operação bloqueante (aguarando chegada de dados)
				System.out.println("[OK] ]");
				
				msg_received = new String(buf_received); // Mapeando vetor de bytes recebido para String
				
				System.out.println(" Mensagem recebida do cliente: " + msg_received);
				
				System.out.print(" Digite uma mensagem para o cliente: ");
				msg_send = sc.nextLine();
				buf_send = msg_send.getBytes(); // preenchendo buffer de envio
				
				System.out.print("[ Enviando mensagem ............................... ");
				os.write(buf_send);
				System.out.println("[Ok] ]");
			}
				
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
	}

}