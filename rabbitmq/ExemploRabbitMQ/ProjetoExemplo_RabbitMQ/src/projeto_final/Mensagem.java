/*
 * Alunos: Erick Santos Resende e Matheus Lima Pinheiro 
 * 
 * */

package projeto_final;

import java.io.Serializable;

public class Mensagem implements Serializable{
	private int x;
	private int y;
	private int velo;
	private int id;
	private String msg;
	private String motorista;
	
	public Mensagem(String motorista) {
		super();
		this.motorista = motorista;
	}
	
	public Mensagem() {
		
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getVelo() {
		return velo;
	}

	public void setVelo(int velo) {
		this.velo = velo;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setMensagem(String msg) {
		this.msg = msg;
	}
	
	public String getMensagem() {
		return msg;
	}
	
	public String getMotorista() {
		return motorista;
	}
	
	public String toString() {
		return this.getMensagem();
	}
}
