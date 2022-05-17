package udp;

/*
 * O protocolo UDP não garante a entrega, então se eu toda
 * apenas a classe ClienteUDP eu não saberei se a mensagem
 * realmente chegou no destino.
 **/

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ClienteUDP {
	
	public static void main(String[] args) {
		System.out.print("[ Alocando porta UDP  ................. ");
		// Crio um socket e escolho uma porta
		try (DatagramSocket socket = new DatagramSocket(10_000)) {
			System.out.println("[OK] ]");
			
			String msg = "Olá!!";
			
			// Mensagem de envio
			byte[] msg_buf = msg.getBytes();
			int msg_size = msg_buf.length;
			InetAddress destination_address = InetAddress.getLocalHost(); // Quero mandar por localhost
			int destination_port = 20_000; // Porta de destino
			
			System.out.print("[ Montando datagrama UDP .............. ");
			DatagramPacket pack = new DatagramPacket(msg_buf, msg_size, destination_address, destination_port); // Crio o datagram
			System.out.println("[OK] ]");
			
			System.out.print("[ Enviando datagrama UDP .............. ");
			socket.send(pack);
			System.out.println("[OK] ]");
			System.out.println("[ Finalizando o programa .................. [OK] ]");
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
	}
}
