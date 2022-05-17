package chat_exercicio;

/*
 * Utilizar conceito de threads no código
 * Preciso de 4 portas, não especifíco a do cliente
 * is.available - verifica se tem alguma informação
 * recebida e checa. Maior que 0 chegou alguns bytes
 * */

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class ClienteUDP {
	
	public static void main(String[] args) {
		DatagramSocket socket_received;
		DatagramPacket pack_send, pack_received;
		Scanner sc = new Scanner(System.in);
		byte[] msg_buf_send;
		String msg_send;
		int msg_size_send;
		int destination_port;
		byte[] buf_received = new byte[20];
		byte[] received_data;
		String received_msg;
		int origin_port;
		
		System.out.print("[ Alocando porta UDP  ................. ");
		// Crio um socket e escolho uma porta
		try (DatagramSocket socket = new DatagramSocket(10_000)) {
			System.out.println("[OK] ]");
			while(true) {
				// Enviando mensagem para o Servidor
				System.out.print("Digite uma mensagem para o Servidor: ");
				msg_send = sc.nextLine();			
				
				msg_buf_send = msg_send.getBytes();
				msg_size_send = msg_buf_send.length;
				InetAddress destination_address = InetAddress.getLocalHost(); // Quero mandar por localhost
				destination_port = 20_000; // Porta de destino
				
				System.out.print("[ Montando datagrama UDP .............. ");
				pack_send = new DatagramPacket(msg_buf_send, msg_size_send, destination_address, destination_port); // Crio o datagrama
				System.out.println("[OK] ]");
				
				System.out.print("[ Enviando datagrama UDP .............. ");
				socket.send(pack_send);				
				System.out.println("[OK] ]");
				
				// Recebendo mensagem do Servidor
				pack_received = new DatagramPacket(buf_received, buf_received.length); 
				socket_received = new DatagramSocket(30_000);
				
				System.out.print("[ Aguardando recebimento da resposta ..................");
				socket_received.receive(pack_received);
				System.out.println("[OK] ]");
				
				received_data = pack_received.getData();
				received_msg = new String(received_data);
				InetAddress origin_address = pack_received.getAddress();
				origin_port = pack_received.getPort();
				
				System.out.println(" Mensagem:              " + received_msg);
				System.out.println(" Endereco de origem:    " + origin_address.getHostAddress());
				System.out.println(" Porta de origem:       " + origin_port);
			}
			//System.out.println("[ Finalizando o programa .................. [OK] ]");
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
	}
}