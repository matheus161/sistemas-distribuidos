package exercicio_03_old;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class TratarConexao extends Thread {
	private static ArrayList<Socket> LISTA_CONEXAO = new ArrayList<>();
	private Socket sock;
	
	public TratarConexao (Socket sock) {
		this.sock = sock;
		LISTA_CONEXAO.add(sock);
	}
	
	@Override
	public void run() {
		Scanner sc = new Scanner(System.in);
		ObjectInputStream ois = null;
		//Mensagem msg = new Mensagem();

		try {
			ois = new ObjectInputStream(sock.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
		while (true) {
			try {
				System.out.println("");
				System.out.print("[ Aguardando recebimento de mensagem ..................... ");
				Mensagem msg = (Mensagem) ois.readObject();// Operação bloqueante (aguarda a chegada dos dados) ERROR AQUI
				System.out.println("[OK] ]");
				
				System.out.println(" Mensagem recebida: " + new String(msg.getMessage()));
				
				if (msg.getDestinatario() == 0) {
					// Envia para todos os clientes
					for (Socket socket : LISTA_CONEXAO) {
						System.out.print("[ Enviando mensagem para todos os Clientes ............................ ");
						socket.getOutputStream().write(msg.getMessage());
						System.out.println("[OK] ]");						
					}
				} else if (msg.getDestinatario() == 1){
					System.out.print("[ Enviando mensagem para o Cliente 2 ............................ ");
					LISTA_CONEXAO.get(1).getOutputStream().write(msg.getMessage());
					System.out.println("[OK] ]");
				} else {
					System.out.print("[ Enviando mensagem para o Cliente 3 ............................ ");
					LISTA_CONEXAO.get(2).getOutputStream().write(msg.getMessage());
					System.out.println("[OK] ]");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
