package org.hut.im;
public class ComandosLCP {

	// Tipo da pacote que esta sendo enviado e se ha necessidade de resposta
    public static byte DIRECT_COMMAND_REPLY = 0x00;
    public static byte SYSTEM_COMMAND_REPLY = 0x01;
    public static byte REPLY_COMMAND = 0x02;
    public static byte DIRECT_COMMAND_NOREPLY = (byte)0x80; // Avoids ~100ms latency
    public static byte SYSTEM_COMMAND_NOREPLY = (byte)0x81; // Avoids ~100ms latency

    // Comandos Diretos
    public static final byte START_PROGRAM = 0x00;
    public static final byte STOP_PROGRAM = 0x01;
    public static final byte PLAY_SOUND_FILE = 0x02;
    public static final byte PLAY_TONE = 0x03;
    public static final byte SET_OUTPUT_STATE = 0x04;
    public static final byte SET_INPUT_MODE = 0x05;
    public static final byte GET_OUTPUT_STATE = 0x06;
    public static final byte GET_INPUT_VALUES = 0x07;
    public static final byte RESET_SCALED_INPUT_VALUE = 0x08;
    public static final byte MESSAGE_WRITE = 0x09;
    public static final byte RESET_MOTOR_POSITION = 0x0A;
    public static final byte GET_BATTERY_LEVEL = 0x0B;
    public static final byte STOP_SOUND_PLAYBACK = 0x0C;
    public static final byte KEEP_ALIVE = 0x0D;
    public static final byte LS_GET_STATUS = 0x0E;
    public static final byte LS_WRITE = 0x0F;
    public static final byte LS_READ = 0x10;
    public static final byte GET_CURRENT_PROGRAM_NAME = 0x11;
    public static final byte MESSAGE_READ = 0x13;

    // NXJ additions
    public static byte NXJ_DISCONNECT = 0x20;
    public static byte NXJ_DEFRAG = 0x21;

    // MINDdroidConnector additions
    public static final byte SAY_TEXT = 0x30;
    public static final byte VIBRATE_PHONE = 0x31;
    public static final byte ACTION_BUTTON = 0x32;


    // Codigos dos erros
    public static final byte MAILBOX_EMPTY = (byte)0x40;
    public static final byte FILE_NOT_FOUND = (byte)0x86;
    public static final byte INSUFFICIENT_MEMORY = (byte) 0xFB;
    public static final byte DIRECTORY_FULL = (byte) 0xFC;
    public static final byte UNDEFINED_ERROR = (byte) 0x8A;
    public static final byte NOT_IMPLEMENTED = (byte) 0xFD;




  private  static final byte PRA_FRENTE = -1;



    

    
   public static byte[] setAndar()
   {
	  return setAndar((byte)0xFF,(byte)100,true);
	   
   }
   public static byte[] setAndar(byte motor, byte velocidade,boolean praFrente)
   {
    	int power = velocidade;
    	if(praFrente)
    		 power *= PRA_FRENTE;

    	byte[] retorno = new byte[13];
    	retorno[0] = ComandosLCP.DIRECT_COMMAND_NOREPLY;
    	retorno[1] = ComandosLCP.SET_OUTPUT_STATE;
    	retorno[2] = motor;
    	retorno[3] = (byte)power;
    	retorno[4] = 0x01; // "Ligar" motor
    	retorno[5] = 0x01; // Permite controlar a velocidade atraves do power
    	retorno[6] = 0;
    	retorno[7] = 0x20; //Correndo

    	//Tempo (0 = infinito)
    	retorno[8] = 0;
    	retorno[9] = 0;
    	retorno[10] = 0;
    	retorno[11] = 0;
    	retorno[12] = 0;

    	return retorno;
    }
   
   public static byte[] setVirar(byte motor, boolean direita)
   {
    	int power = 100;
    	if(direita)
    		 power *= PRA_FRENTE;

    	byte[] retorno = new byte[13];
    	retorno[0] = ComandosLCP.DIRECT_COMMAND_NOREPLY;
    	retorno[1] = ComandosLCP.SET_OUTPUT_STATE;
    	retorno[2] = motor;
    	retorno[3] = (byte)power;
    	retorno[4] = 0x01; // "Ligar" motor
    	retorno[5] = 0x01; // Permite controlar a velocidade atraves do power
    	retorno[6] = 30;
    	retorno[7] = 0x20; //Correndo
    	int angle = 1;
    	//Tempo (0 = infinito)
    	retorno[8] =(byte) angle;
    	retorno[9] =0;
    	retorno[10] =0;
    	retorno[11] =0;
    	retorno[12] = 0;

    	return retorno;
    }
   
   
   public static byte[] setParar()
   {
	   return setParar((byte)0xFF);
   }
   public static byte[] setParar(byte motor)
   	{
   	 	byte[] retorno = new byte[13];
    	retorno[0] = ComandosLCP.DIRECT_COMMAND_NOREPLY;
    	retorno[1] = ComandosLCP.SET_OUTPUT_STATE;
    	retorno[2] = motor;
    	retorno[3] = 0;
    	retorno[4] = 0x02; // "break" motor
    	retorno[5] = 0x00; // Nao permite nenhum controle
    	retorno[6] = 0;
    	retorno[7] = 0x00; //Idle mode

    	//Tempo (0 = infinito)
    	retorno[8] = 0;
    	retorno[9] = 0;
    	retorno[10] = 0;
    	retorno[11] = 0;
    	retorno[12] = 0;

    	return retorno;
   	}




   	public static byte[] getSetAtivaLuz(byte porta,boolean ativa)
   	{

   		byte[] retorno = new byte[5];
   		retorno[0] = ComandosLCP.DIRECT_COMMAND_NOREPLY;
   		retorno[1] = ComandosLCP.SET_INPUT_MODE;
   		retorno[2] = porta;
   		if(ativa)
   			retorno[3] = 0x04;
   		else
   			retorno[3]= 0x07;
   		retorno[4] = (byte)0x00;

   		return retorno;
   	}




   	public static byte[] setGetBatteryLevel()
   	{
   		byte[] retorno = new byte[2];
   		retorno[0] = ComandosLCP.DIRECT_COMMAND_REPLY;
   		retorno[1] = 0x0B;
   		return retorno;
   	}


   	public static byte[] setGetFirmwareLevel()
   	{
   		byte[] retorno = new byte[2];
   		retorno[0] = ComandosLCP.DIRECT_COMMAND_REPLY;
   		retorno[1] =(byte) 0x88;
   		return retorno;
   	}

   	
   	public static byte[] getSensorInput(byte porta)
   	{
   		byte[] retorno = new byte[3];
   		retorno[0] = ComandosLCP.DIRECT_COMMAND_REPLY;
   		retorno[1] =(byte) 0x07;
   		retorno[2] = porta;
   		return retorno;
   	}

}







