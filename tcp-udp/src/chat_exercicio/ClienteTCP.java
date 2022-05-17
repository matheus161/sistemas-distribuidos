package chat_exercicio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClienteTCP {
	public static void main(String[] args) throws InterruptedException {
		System.out.print("[ Conectando com o Servidor TCP ..................... ");
		try (Socket sock = new Socket("127.0.0.1", 3_300);){
			System.out.println("[OK] ]");
			
			Scanner sc = new Scanner(System.in);
			
			InputStream is = sock.getInputStream(); // canal de entrada de dados
			OutputStream os = sock.getOutputStream(); // canal de saida de dados
			
			String msg_send;
			byte[] buf_send; // Obtendo a resepresentação em bytes da mensagem
			byte[] buf_received = new byte[30]; // buffer de recepção
			String msg_received;
			
			while(true) {
				System.out.print(" Digite uma mensagem para o servidor: ");
				msg_send = sc.nextLine();
				buf_send = msg_send.getBytes();
				
				System.out.print("[ Enviando mensagem ............................... ");
				os.write(buf_send);
				System.out.println("[OK] ]");
				
				System.out.print("[ Aguardando resposta do servidor ..................... ");
				is.read(buf_received);
				System.out.println("[OK] ]");
				msg_received = new String(buf_received); // Mapeando vetor de bytes recebido para String
				
				System.out.println(" Mensagem recebida do servidor: " + msg_received);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
	}
}

