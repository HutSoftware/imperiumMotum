package org.hut.im;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

/*
 * Pega uma lista com os dispositivos pareados e tenta achar o lego nessa lista....
 */
public class TelaDeConexao extends Activity implements AdapterView.OnItemClickListener
{
	private static int DISCOVERY_REQUEST = 1;
	private BluetoothAdapter bluetooth;
	private ListView lst;
	private Button btnProcurar;
	private Set<BluetoothDevice> pairedDevices;
	static final UUID SERIAL_PORT_SERVICE_CLASS_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	
	
	ArrayList<String> lista;
    ArrayAdapter<String> aa;

	BroadcastReceiver bluetoohState = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent) 
		{
			
			
			String prevStateExtra = BluetoothAdapter.EXTRA_PREVIOUS_STATE;
			String stateExtra = BluetoothAdapter.EXTRA_STATE;
			int state = intent.getIntExtra(stateExtra, -1);
			int previousState = intent.getIntExtra(prevStateExtra, -1);
			
			String tt = "";
			switch(state)
			{
				case (BluetoothAdapter.STATE_TURNING_ON):{tt = "Bluetooth Ligando!";break;}
				case (BluetoothAdapter.STATE_ON):
				{
					tt = "Bluetooth Ligado!";
					unregisterReceiver(this);
					listarDispositivos();
					break;
					
				}
				case (BluetoothAdapter.STATE_TURNING_OFF):{tt = "Bluetooth Desligando!";break;}
				case (BluetoothAdapter.STATE_OFF):{tt = "Bluetooth Desligado!";break;}
				default : break;
			}
			Toast.makeText(TelaDeConexao.this, tt, Toast.LENGTH_LONG).show();
		}
		
	};

    BroadcastReceiver discoveryResult = new BroadcastReceiver()
	{

		@Override
		public void onReceive(Context context, Intent intent) 
		{		
			
			String remoteDeviceName = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);
    		
    		BluetoothDevice remoteDevice = null;
    		remoteDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
    		
    		Toast.makeText(getApplicationContext(), "Discovered: " + remoteDeviceName, Toast.LENGTH_LONG).show();
    		
    		//fa�a alguma coisa com o bluetoothdevice
    		if(remoteDeviceName != null)
    			if(!remoteDeviceName.equals("")) //&& !pairedDevices.contains(remoteDevice))
    			{
    				lista.add(remoteDeviceName);
    				lst.setAdapter(aa);
    				    				
    				//pairedDevices.add(remoteDevice);
        		}
		}		
	};
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.telaconexao);
        
        
        //startService(new Intent(this,ServerBluetooth.class));
       
        lst = (ListView)findViewById(R.id.lstDispositivosPareados);        
        lst.setOnItemClickListener(this);
        
    	lista = new ArrayList<String>();
        aa = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,lista);
        
        btnProcurar = (Button) findViewById(R.id.btnProcurar);
        btnProcurar.setOnClickListener(new Button.OnClickListener()
					{
						public void onClick(View v) 
						{
							registerReceiver(discoveryResult, new IntentFilter(BluetoothDevice.ACTION_FOUND));
				    		if(!bluetooth.isDiscovering())
				    			bluetooth.startDiscovery();							
								
				    		
						}
						
					});
        
                
        
        bluetooth = BluetoothAdapter.getDefaultAdapter();
        
        
        if (bluetooth == null) 
        {
        	Toast.makeText(this, "Não existe Bluetooth", Toast.LENGTH_LONG).show();
        }
        else //existe bluetooth
        {
        	if(!bluetooth.isEnabled())
        	{
        		String actionStateChanged = BluetoothAdapter.ACTION_STATE_CHANGED;
        		String enableBT = BluetoothAdapter.ACTION_REQUEST_ENABLE;
        		
        		registerReceiver(bluetoohState, new IntentFilter(actionStateChanged));
        		startActivityForResult(new Intent(enableBT),0);
        		
        		Toast.makeText(this, "N�o est� ativado", Toast.LENGTH_LONG).show();
        	}
        	else //bluetooth está ativado
        	{   
        		listarDispositivos();		        
        	}	        
        
        }
     
        
       
       
    }
    
    public void listarDispositivos()
    {
    	pairedDevices = bluetooth.getBondedDevices();
        
        
        if (pairedDevices.size() > 0) 
        {  
	        
	        
	         for (BluetoothDevice device : pairedDevices) 
	        	 lista.add(device.getName());
	        
	         lst.setAdapter(aa);
        }
    }
    
    @Override 
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
    	if(requestCode == DISCOVERY_REQUEST)
    	{
    		boolean isDiscoveable = resultCode > 0;
    		int discoverableDuration = resultCode;
    	}
    }

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

		 Applicacao app = (Applicacao)getApplication();
		 String dispClicado = (String)arg0.getItemAtPosition(arg2); 
		 AlertDialog.Builder ad = new AlertDialog.Builder(TelaDeConexao.this);
		 
		 ad.setTitle("TesteConexao");
		 ad.setMessage("Conexão com "+ dispClicado);
		 
		
		 if( app.getEntradaNXT()== null && app.getSaidaNXT() == null)
		 {
			 
			 for (BluetoothDevice device : pairedDevices) 
			 {
				 BluetoothSocket btSocketTemporary = null;
			     if(device.getName().equals(dispClicado))
			     {
			    	 
			    	 
			    	 try 
			    	 {
			    		 btSocketTemporary = device.createRfcommSocketToServiceRecord(SERIAL_PORT_SERVICE_CLASS_UUID);
			    		 btSocketTemporary.connect();
			    		 
			    		 
	            		 app.setSaidaNXT(btSocketTemporary.getOutputStream());
	            		 app.setEntradaNXT( btSocketTemporary.getInputStream());
	            		 if(app.getEntradaNXT()!= null && app.getSaidaNXT() !=  null)
	            		 {
	            			 
	            			 
	            			 ad = new AlertDialog.Builder(TelaDeConexao.this);
	            			 
	            			 ad.setTitle("Conexaõ feita com sucesso!");
	            			 ad.setMessage("Conexão com "+ device.getName()+" foi feita com sucesso!");
	            			 ad.setPositiveButton("OK!", new OnClickListener()
	            			 {
	
								public void onClick(DialogInterface arg0, int arg1) {
									Intent intent = new Intent(TelaDeConexao.this,MainActivity.class);
									startActivity(intent);
									
								}
	            			 
	            			 });
	            			 ad.show();
	            			 
	            		 }
			    	 } 
			    	 catch (IOException e) 
			    	 {
			    		 Toast.makeText(TelaDeConexao.this, "Erro ao conectar com o dispositivo "+device.getName(), Toast.LENGTH_LONG).show();
					 }			
			     }
			 }
			
		 }
	}
}
