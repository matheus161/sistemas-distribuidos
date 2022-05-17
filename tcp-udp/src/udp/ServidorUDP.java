package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ServidorUDP {	
	public static void main(String [] args) throws IOException {
		System.out.print("[Alocando porta UDP               .............");
		try (DatagramSocket socket = new DatagramSocket(20_000);) {
			System.out.println("[OK] ]");
			
			byte[] buf = new byte[20];
			
			DatagramPacket pack = new DatagramPacket(buf, buf.length); 
			
			System.out.println("[ Aguardando recebimento da mensagem ..................");
			socket.receive(pack);
			System.out.println("[OK] ]");
			
			byte[] received_data = pack.getData();
			String received_msg = new String(received_data);
			InetAddress origin_address = pack.getAddress();
			int origin_port = pack.getPort();
			
			System.out.println(" Mensagem:              " + received_msg);
			System.out.println(" Endereco de origem:    " + origin_address.getHostAddress());
			System.out.println(" Porta de origem:       " + origin_port);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
