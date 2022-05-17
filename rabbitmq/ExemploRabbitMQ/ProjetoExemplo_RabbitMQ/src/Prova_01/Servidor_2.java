/*
 * @authors: Erick Santos Resende e Matheus Lima Pinheiro
 * */

package Prova_01;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;

import javax.imageio.ImageIO;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP.BasicProperties;

public class Servidor_2 {
	private static final String EXCHANGE_NAME = "mensagens";
	private static SerializableImage serializableImage;
	private byte[] imageBuffer = null;
	
	public static void main(String[] argv) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("127.0.0.1"); 
		factory.setUsername("guest"); 
		factory.setPassword("guest"); 
		factory.setVirtualHost("/");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		
		channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
		String nomeFila = channel.queueDeclare().getQueue();
		channel.queueBind(nomeFila, EXCHANGE_NAME, "");

		System.out.println(" [*] Esperando recebimento de mensagens...");

		Consumer consumer = new DefaultConsumer(channel) {
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws IOException {
				
				// Convertendo de byte para Object
				ByteArrayInputStream bais = new ByteArrayInputStream(body);
				ObjectInputStream ois = new ObjectInputStream(bais);
				try {
					serializableImage = (SerializableImage) ois.readObject();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				System.out.println(" [x] Imagem recebida: '" + serializableImage.getImageName() + "'");
				
				// Pegando o diretório atual
				File file = new File("");
				String directoryName = file.getAbsoluteFile().toString();
				
				System.out.println(" [x] Salvando imagem no servidor ... ");
				
				// Salvando as imagens cinzas em uma pasta				
				FileOutputStream endImage = new FileOutputStream(directoryName + "\\imagens_server2\\" + new String(serializableImage.getImageName()) + ".jpg");
                
				BufferedImage image = ImageIO.read(new ByteArrayInputStream(serializableImage.getImage()));

                ImageIO.write(image, "jpg", endImage);
			}
		};
		channel.basicConsume(nomeFila, true, consumer);
	}
}
