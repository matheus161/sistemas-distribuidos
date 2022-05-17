/*
 * Alunos: Erick Santos Resende e Matheus Lima Pinheiro 
 * 
 * */

package projeto_final;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP.BasicProperties;

public class Central {
	private final static String QUEUE_NAME = "central";
	private static final String EXCHANGE_NAME = "redemotoristas";

	public static void main(String[] argv) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("127.0.0.1"); // Alterar
		factory.setUsername("guest"); // Alterar
		factory.setPassword("guest"); // Alterar
		factory.setVirtualHost("/");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		// Declarando fila de Direct (mensagens diretas pra central)
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		
		// Declarando fila de Fanout (recebendo mensagens da rede)
		channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
		String nomeFila = channel.queueDeclare().getQueue();
		channel.queueBind(nomeFila, EXCHANGE_NAME, "");
		
		// Crio a Thread e passo um channel
		CentralThread central = new CentralThread(channel);
		central.start();
	
		System.out.println(" [*] Esperando recebimento de mensagens...");
		
		// Recebendo mensagens direct ou da rede
		Consumer consumer = new DefaultConsumer(channel) {
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws IOException {
				
				// Convertendo de byte para Object
				ByteArrayInputStream bais = new ByteArrayInputStream(body);
				ObjectInputStream ois = new ObjectInputStream(bais);
				Mensagem msg = null;
				try {
					// Lendo a mensgaem que chegou em
					// Dando cast para para Mensagem
					msg = (Mensagem) ois.readObject();
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
				}
				
				// Printando a mensagem que chegou
				System.out.println( msg.toString());
			}
		};
		// Consumindo a mensagem da rede ou direta
		channel.basicConsume(QUEUE_NAME, true, consumer);
		channel.basicConsume(nomeFila, true, consumer);
	}
}
