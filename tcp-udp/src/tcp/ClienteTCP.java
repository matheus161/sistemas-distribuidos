package tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClienteTCP {
	public static void main(String[] args) throws InterruptedException {
		System.out.print("[ Conectando com o Servidor TCP ..................... ");
		try (Socket sock = new Socket("127.0.0.1", 3_300);){
			System.out.println("[OK] ]");
			
			InputStream is = sock.getInputStream(); // canal de entrada de dados
			OutputStream os = sock.getOutputStream(); // canal de saida de dados
			String msg = "Olá, DCOMP";
			byte[] buf = msg.getBytes(); // Obtendo a resepresentação em bytes da mensagem
			
			System.out.print("[ Enviando mensagem ............................... ");
			os.write(buf);
			
			System.out.println("[OK] ]");
			System.out.println("[ Finaliznado o programa ......................... ]");
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
	}
}
