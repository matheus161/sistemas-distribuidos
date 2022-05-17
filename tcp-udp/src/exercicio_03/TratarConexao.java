package exercicio_03;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class TratarConexao extends Thread {
	private static ArrayList<Conexao> LISTA_CONEXAO = new ArrayList<>();
	private Conexao conexao;

	public TratarConexao(Socket sock) throws IOException {
		this.conexao = new Conexao(sock);
		LISTA_CONEXAO.add(this.conexao);
	}

	@Override
	public void run() {
		Scanner sc = new Scanner(System.in);
		// Mensagem msg = new Mensagem();


		while (true) {
			try {
				System.out.println("");
				System.out.print("[ Aguardando recebimento da mensagem ..................... ");
				Mensagem msg = (Mensagem) this.conexao.getOis().readObject();// Operação bloqueante (aguarda a chegada dos dados)
				System.out.println("[OK] ]");

				System.out.println(" Mensagem recebida: " + new String(msg.getMessage()));

				if (msg.getDestinatario() == 0) {
					// Envia para todos os clientes
					for (Conexao conexao : LISTA_CONEXAO) {
						System.out.print("[ Enviando mensagem para todos os Clientes ............................ ");
						conexao.getOos().writeObject(msg.getMessage());
						System.out.println("[OK] ]");
					}
				} else if (msg.getDestinatario() == 1) {
					System.out.print("[ Enviando mensagem para o Cliente 2 ............................ ");
					LISTA_CONEXAO.get(1).getOos().writeObject(msg.getMessage());
					System.out.println("[OK] ]");
				} else {
					System.out.print("[ Enviando mensagem para o Cliente 3 ............................ ");
					LISTA_CONEXAO.get(2).getOos().writeObject(msg.getMessage());
					System.out.println("[OK] ]");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
