package exercicio_03_old;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {	
	public static void main(String[] args) throws ClassNotFoundException {
		System.out.print("[ Iniciando Servidor TCP .................. "); 
		try (ServerSocket ss = new ServerSocket(3_300, 5, InetAddress.getByName("127.0.0.1"))) {
			System.out.println("[OK] ]");
			
			// O servidor vai ficar rodando esperando os clientes se conectarem
			while (true) {
				System.out.print("[ Aguardando pedidos de conexão .................. "); 
				Socket sock = ss.accept(); // Operação bloqueante (aguardando pedido de conexão)
				System.out.println("[OK] ]");
				
				// Crio uma nova Thread Servidor para se conectar com aquele cliente
				TratarConexao tratarConexao = new TratarConexao(sock);
				tratarConexao.start();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}