package chat_exercicio_02;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class TCP_Cliente {
	public static void main(String[] args) throws InterruptedException {
		Scanner sc = new Scanner(System.in);
		
		System.out.print("[ Conectando com o Servidor TCP ..................... ");
		try (Socket sock = new Socket("127.0.0.1", 3_300);){
			System.out.println("[OK] ]");
			
			ObjectOutputStream oos = new ObjectOutputStream(sock.getOutputStream());
			ObjectInputStream ois = new ObjectInputStream(sock.getInputStream());
			byte[] buf_receive;
			Message msg_send;
			
			while(true) {
				buf_receive = new byte[20];
				System.out.print("Digite uma mensagem para o Servidor: ");
				msg_send = new Message(sc.nextLine().getBytes()); // Pegando a mensagem e convertendo em Bytes
				
				
				if(new String(msg_send.getMessage()).equals("stop")) {
					System.out.println("[ Finalizando o programa ......................... ]");
					oos.close();
					ois.close();	
					sock.close();				
				}
				
				System.out.print("[ Enviando mensagem ............................... ");
				oos.write(msg_send.getMessage());
				oos.flush();
							
				System.out.println("[OK] ]");
				
				System.out.print("[ Aguardando resposta do Servidor ..................... ");
				//ois = new ObjectInputStream(sock.getInputStream());
				ois.read(buf_receive);
				
				System.out.println("[OK] ]");
				System.out.println(" Mensagem recebida: " + new String(buf_receive));
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
		//System.out.println("[ Finalizando o programa ......................... ]");
		
	}
}
