package exercicio_03_old;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class EnvioMensagem extends Thread {
	// Aqui ele vai receber a parte que tecolhe a mensagem e envia
	private Socket sock;
	
	public EnvioMensagem (Socket sock) {
		this.sock = sock;
	}
	
	@Override
	public void run() {
		Scanner sc = new Scanner(System.in);
		Mensagem msg = new Mensagem();
		ObjectOutputStream oos = null;
		BufferedReader entradaTeclado = new BufferedReader(new InputStreamReader(System.in));
		
		try {			
			while(true) {
				System.out.println(" Digite uma mensagem para o servidor: ");
				msg.setMessage(sc.nextLine().getBytes());
				
				System.out.println(" Digite o destinatario da mensagem:"
						+ " 0. Para todos; 1. Cliente_2 e 2. Cliente_3: ");
				msg.setDestinatario(sc.nextInt());
				
				oos = new ObjectOutputStream(sock.getOutputStream());
				System.out.print("[ Enviando mensagem para o Servidor ............................ ");
				//oos.write(msg.getMessage()); // Envia a mensagem
				oos.writeObject(msg);
				//oos.close();
				System.out.println("[OK] ]");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
}
