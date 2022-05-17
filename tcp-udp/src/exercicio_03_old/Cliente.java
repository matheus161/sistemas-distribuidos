package exercicio_03_old;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

import chat_exercicio_02.Message;

public class Cliente {
	private static Socket sock;
	
	public void conectar(Socket sock) {
		this.sock = sock;
	}
	
	public static void main(String [] args) throws InterruptedException {
		ObjectInputStream ois = null;
		Mensagem msg = new Mensagem();
		
		System.out.print("[ Conectando com o Servidor TCP ..................... ");
		try (Socket sock = new Socket("127.0.0.1", 3_300);) {	
			Cliente cliente = new Cliente();
			cliente.conectar(sock);
			System.out.println("[OK] ]");
			
			EnvioMensagem cliente_thread = new EnvioMensagem(sock);		
			cliente_thread.start();
			
			ois = new ObjectInputStream(sock.getInputStream());
			
			while(true) {
				System.out.print("[ Aguardando resposta do servidor ..................... ");
				msg = (Mensagem) ois.readObject();
				System.out.println("[OK] ]");
				
				System.out.println(" Mensagem recebida do servidor: " + msg);
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		System.out.println("[ Finalizando o programa ......................... ]");
	}
}