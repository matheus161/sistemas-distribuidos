package exercicio_03;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class EnvioMensagem extends Thread {
	// Aqui ele vai receber a parte que recolhe a mensagem e envia
	private Socket sock;

	public EnvioMensagem(Socket sock) {
		this.sock = sock;
	}

	@Override
	public void run() {
		
		
		ObjectOutputStream oos = null;
//		BufferedReader entradaTeclado = new BufferedReader(new InputStreamReader(System.in));
		try {
			oos = new ObjectOutputStream(sock.getOutputStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			while (true) {
				System.out.println("");
				System.out.println(" Digite uma mensagem : ");
				Mensagem msg = new Mensagem();
				Scanner sc = new Scanner(System.in);
				msg.setMessage(sc.nextLine().getBytes());

				System.out.println(
						" Digite o destinatario da mensagem:" + " (0) Para todos; (1) Cliente_2 e (2) Cliente_3: ");
				msg.setDestinatario(sc.nextInt());

				
				System.out.print("[ Enviando mensagem para o Servidor ............................ ");
				oos.writeObject(msg); // Envia a mensagem
				System.out.println("[OK] ]");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
