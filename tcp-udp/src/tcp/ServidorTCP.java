package tcp;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorTCP {
	public static void main(String[] args) {
		System.out.print("[ Iniciando Servidor TCP ................... ");
		try (ServerSocket ss = new ServerSocket(3_300, 5, InetAddress.getByName("127.0.0.1"))){
			System.out.println("[OK] ]"); // Abri conexão naquela porta
			
			System.out.print("[ Aguardando pedidos de conexão .................. "); // atalho: sysout + ctrl + espace
			// Enquanto o cliente não conectar eu vou bloquear
			Socket sock = ss.accept(); // Operação bloqueante (aguardando pedido de conexão)
			System.out.println("[OK] ]");
			
			InputStream is = sock.getInputStream(); // canal de entrada de dados
			OutputStream os = sock.getOutputStream(); // canal de saída de dados
			byte[] buf = new byte[20]; // buffer de recepção
			
			System.out.print("[ Aguardando recebimento de mensagem ..................... ");
			is.read(buf); // Operação bloqueante (aguarando chegada de dados)
			System.out.println("[OK] ]");
			
			String msg = new String(buf); // Mapeando vetor de bytes recebido para String
			
			System.out.println(" Mensagem recebida: " + msg);
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
	}

}
