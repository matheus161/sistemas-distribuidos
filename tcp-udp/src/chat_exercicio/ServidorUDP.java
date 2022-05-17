package chat_exercicio;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class ServidorUDP {
	
	public static void main(String [] args) throws IOException {
		DatagramPacket pack_received, pack_send;
		byte[] buf_received = new byte[20];
		Scanner sc = new Scanner(System.in);
		String msg_send;
		byte[] msg_buf_send;
		int msg_send_size;
		int destination_port;
		
		System.out.print("[Alocando porta UDP               .............");
		try (DatagramSocket socket_received = new DatagramSocket(20_000);) {
			System.out.println("[OK] ]");
			while(true) {
				// Recebendo mensagem do Cliente
				pack_received = new DatagramPacket(buf_received, buf_received.length); 
				
				System.out.print("[ Aguardando recebimento da mensagem ..................");
				socket_received.receive(pack_received);
				System.out.println("[OK] ]");
				
				byte[] received_data = pack_received.getData();
				String received_msg = new String(received_data);
				InetAddress origin_address = pack_received.getAddress();
				int origin_port = pack_received.getPort();
				
				System.out.println(" Mensagem:              " + received_msg);
				System.out.println(" Endereco de origem:    " + origin_address.getHostAddress());
				System.out.println(" Porta de origem:       " + origin_port);
				
				// Enviando resposta para o Cliente -- CRIAR OUTRO DATAGRAMA PARA ENVIAR A MENSAGEM
				DatagramSocket socket_send = new DatagramSocket(15_000);
				System.out.print(" Digite uma resposta para o Cliente: ");
				msg_send = sc.nextLine();
				msg_buf_send = msg_send.getBytes();
				msg_send_size = msg_buf_send.length;
				
				InetAddress destination_address = InetAddress.getLocalHost(); // Quero mandar por localhost
				destination_port = 30_000; // Porta de destino
				
				System.out.print("[ Montando datagrama UDP .............. ");
				pack_send = new DatagramPacket(msg_buf_send, msg_send_size, destination_address, destination_port); // Crio o datagrama
				System.out.println("[OK] ]");
				
				System.out.print("[ Enviando datagrama UDP .............. ");
				socket_send.send(pack_send);
				System.out.println("[OK] ]");
			}	
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
	}
}