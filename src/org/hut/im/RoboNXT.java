package org.hut.im;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class RoboNXT {

	private InputStream entradaNXT = null;
	private OutputStream saidaNXT = null;

	public RoboNXT(InputStream entrada,OutputStream saida)
	{
		this.entradaNXT = entrada;
		this.saidaNXT = saida;


	}

	public void andarPraFrente()
	{
		this.andarPraFrente((byte)100);
	}
	public void andarPraFrente(byte velocidade)
	{
		this.andar(velocidade, true);
	}



	public void andarPraTras()
	{
		this.andarPraTras((byte)100);

	}
	public void andarPraTras(byte velocidade)
	{
		andar( velocidade,false);
	}


	public void parar()
	{
		 parar((byte)0xFF);
	}
	public void parar(byte motor)
	{

			byte[] vetor = ComandosLCP.setParar(motor);


			try {
				saidaNXT.write(vetor.length);
				saidaNXT.write(vetor.length>>8);
				saidaNXT.write(vetor);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	private void andar(byte velocidade,boolean praFrente)
	{
		this.andar((byte)0, velocidade, praFrente);
		this.andar((byte)1, velocidade, praFrente);
	}
	private void andar(byte motor, byte velocidade,boolean praFrente)
	{
		byte[] vetor = ComandosLCP.setAndar((byte)motor,velocidade,praFrente);

		try {
			saidaNXT.write(vetor.length);
			saidaNXT.write(vetor.length>>8);
			saidaNXT.write(vetor);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	public void virar(int direcao)
	{
		/*
		 * direcao
		 * == 0  -- reto
		 * > 0   -- direita
		 * < 0   -- esquerda
		 */
		/*		this.parar();

		if(direcao < 0 )
			this.andar((byte)2, (byte)100, praFrente);
			
		
		else
			if(direcao > 0 )
				this.andar((byte)1, (byte)100, praFrente);
			else
				if(praFrente)
					this.andarPraFrente();
					else
						this.andarPraTras();*/
		byte[] vetor =null;

		if(direcao > 0 )
			vetor = ComandosLCP.setVirar((byte)2, true);
		else
			vetor = ComandosLCP.setVirar((byte)2, false);
			
			
			
		try {
			saidaNXT.write(vetor.length);
			saidaNXT.write(vetor.length>>8);
			saidaNXT.write(vetor);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.parar((byte)2);
		
		

			

	}


	
	public void finalizarRobo()
	{
		try {
			this.saidaNXT.close();
			this.entradaNXT.close();

			entradaNXT = null;
			saidaNXT   = null;
		} catch (IOException e) {

			e.printStackTrace();
		}
	}



}
