package exercicio_03;

import java.io.ObjectInputStream;
import java.net.Socket;

//Alterei um import que tinha para Mensagem na package exercicio_02

public class Cliente {
	public static void main(String[] args) throws InterruptedException {
		ObjectInputStream ois = null;
		Mensagem msg = new Mensagem();
		String mensagemServidor;

		System.out.print("[ Conectando com o Servidor TCP ..................... ");
		try (Socket sock = new Socket("127.0.0.1", 3_300);) {
//			Cliente cliente = new Cliente();
//			cliente.conectar(sock);
			System.out.println("[OK] ]");

			ois = new ObjectInputStream(sock.getInputStream());
			
			EnvioMensagem cliente_thread = new EnvioMensagem(sock);
			cliente_thread.start();

			while (true) {
				System.out.print("[ Aguardando resposta do Servidor ..................... ");
				mensagemServidor = new String( (byte[]) ois.readObject());
				System.out.println("[OK] ]");

				System.out.println(" Mensagem recebida do servidor: " + mensagemServidor);
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		System.out.println("[ Finalizando o programa ......................... ]");
	}
}