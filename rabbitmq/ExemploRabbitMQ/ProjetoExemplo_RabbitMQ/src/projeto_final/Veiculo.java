/*
 * Alunos: Erick Santos Resende e Matheus Lima Pinheiro 
 * 
 * */

package projeto_final;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;
import java.util.Scanner;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP.BasicProperties;


public class Veiculo {
	private static String DIRECT_MSG = "";
	private static final String EXCHANGE_NAME = "redemotoristas";
	
	public static void main(String[] argv) throws Exception {
		Scanner sc = new Scanner(System.in);
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("127.0.0.1"); 
		factory.setUsername("guest"); 
		factory.setPassword("guest"); 
		factory.setVirtualHost("/");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		
		// Pegando o nome do motorista
		System.out.println("[x] Bem-vindo! Qual o nome do motorista? ");
		String motorista = sc.nextLine();
		
		
		// Crio a Thread e passo um emissor para esse receptor
		VeiculoThread veiculo = new VeiculoThread(channel, motorista);
		veiculo.start();
		
		// Declarando a fila de recepção de mensagens diretas de outros motoristas
		DIRECT_MSG = motorista;
		channel.queueDeclare(DIRECT_MSG, false, false, false, null);
		
		// Declarando fila de Fanout (recebendo mensagens da rede)
		channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
		String nomeFila = channel.queueDeclare().getQueue();
		channel.queueBind(nomeFila, EXCHANGE_NAME, "");
		
		// Consumindo a mensagem direta de qualquer outro motorista(as)
		Consumer consumer = new DefaultConsumer(channel) {
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws IOException {
				
				// Convertendo de byte para Object
				ByteArrayInputStream bais = new ByteArrayInputStream(body);
				ObjectInputStream ois = new ObjectInputStream(bais);
				Mensagem msg_received = null;
				try {
					// Lendo a mensagem que chegou da rede/motorista
					msg_received = (Mensagem) ois.readObject();
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
				}
				
				// Printando a mensagem que chegou				
				System.out.println(msg_received.toString());
			}
		};
		// Consumindo a mensagem da rede ou direta
		channel.basicConsume(DIRECT_MSG, true, consumer);
		channel.basicConsume(nomeFila, true, consumer);
	}
}
