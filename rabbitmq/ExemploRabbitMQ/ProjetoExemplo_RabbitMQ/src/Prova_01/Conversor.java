/*
 * @authors: Erick Santos Resende e Matheus Lima Pinheiro
 * */

package Prova_01;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP.BasicProperties;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Conversor {

	private static String QUEUE_NAME = "minha-fila";
	private static final String EXCHANGE_NAME = "mensagens";	

	public static void main(String[] argv) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("127.0.0.1"); 
		factory.setUsername("guest"); 
		factory.setPassword("guest"); 
		factory.setVirtualHost("/");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		//BufferedImage image = null;
		// Recebendo as imagens
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);

		System.out.println(" [*] Esperando recebimento de mensagens...");
		
		Consumer consumer = new DefaultConsumer(channel) {
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws IOException {
				
				System.out.println(" [x] Mensagem recebida: '" + body + "'");
				
				System.out.println(" [x] Convertendo para cinza ... ");
				
				/* Pegando a imagem que chegou pelo body e convertendo em BufferedImage */ 
				//BufferedImage image = ImageIO.read(new ByteArrayInputStream(body));
				
				ByteArrayInputStream bais = new ByteArrayInputStream(body);
				ObjectInputStream ois = new ObjectInputStream(bais);
				try {
					SerializableImage serializableImage = (SerializableImage) ois.readObject();
					BufferedImage image = ImageIO.read(new ByteArrayInputStream(serializableImage.getImage()));
					
					// Convertendo para cinza
					BufferedImage grayImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
					Graphics graphic = grayImage.getGraphics();
					graphic.drawImage(image, 0, 0, null);  
					graphic.dispose(); 
					
					/*	Convertendo novamente para bytes[] para que seja enviado para o servidor */ 
					//serializableImage = new SerializableImage(grayImage);
					
					SerializableImage serializableGrayImage = new SerializableImage(grayImage, serializableImage.getImageName());
					
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ObjectOutputStream ous = new ObjectOutputStream(baos);
					ous.writeObject(serializableGrayImage);
					
					byte [] body_gray = baos.toByteArray();
					
					// Enviando as mensagens
					channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
					channel.basicPublish(EXCHANGE_NAME, "", null, body_gray);
					System.out.println(" [x] Mensagem enviada: '" + serializableImage + "'");
				} catch (Exception e) {
					e.printStackTrace();
				}				
			}
		};
		channel.basicConsume(QUEUE_NAME, true, consumer);
	}
}
