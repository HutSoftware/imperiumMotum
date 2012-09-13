package org.hut.im;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	
	
	
	
	private Button btnCima, btnBaixo,btnParar;
	private RoboNXT robo= null;
	private int contGiros = 0;
	private boolean enviarComandoParar = false;
	
	

    
    @Override    
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //ISSO SÓ PERMITE ORIENTAÇÂO VERTICAL
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        
         
        btnCima = (Button)findViewById(R.id.botao1);
        btnBaixo = (Button)findViewById(R.id.botao2);
        btnParar = (Button)findViewById(R.id.btnParar);
        
        Applicacao app = (Applicacao)getApplication();
        robo = new RoboNXT(app.getEntradaNXT(),app.getSaidaNXT());
        
        gerenciadorSensor = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        
        btnCima.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
								
				robo.andarPraFrente();
				
			}
		});
        
        btnBaixo.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				robo.andarPraTras();
				
			}
		});
        
        btnParar.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				robo.parar();
				
			}
		});
        
       
        
        
        
    }

    @Override
	protected void onDestroy() {
    	robo.finalizarRobo();
	}

	SensorManager gerenciadorSensor;
 
    private final SensorEventListener sensorEventListener = new SensorEventListener() {
      public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION)
        {              
          
          float angulo = event.values[2] ;
          if(angulo < -30)
          {
        	  if(contGiros != 2)
        	  {
        		  robo.virar(1);
        		  contGiros++;
        		  enviarComandoParar = true;
        	  }
          }
          else
        	  if(angulo > 30)
        	  {
        		  if(contGiros != -2)
            	  {
            		  robo.virar(-1);
            		  contGiros--;
            		  enviarComandoParar = true;
            	  }
        	  }
        	  else //angulo menor
        	  {
        		  if(contGiros > 0)
       			  {
        				  robo.virar(-1);
                		  contGiros--;
       			  }
        		  else
        			  if(contGiros < 0)
           			  {
           				  robo.virar(1);
                   		  contGiros++;
           			  }
        			  
        	  }
        	  
        }
       
      }

      public void onAccuracyChanged(Sensor sensor, int accuracy) {}
   	};
   	
   	
   	@Override
   	protected void onResume() {
   	  super.onResume();

   	    Sensor sensorDeOrientacao = gerenciadorSensor.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		gerenciadorSensor.registerListener(sensorEventListener, 
										   sensorDeOrientacao, 
		                                   SensorManager.SENSOR_DELAY_NORMAL);
		
   	}

   	
   	@Override
   	protected void onStop() {
   		gerenciadorSensor.unregisterListener(sensorEventListener);
   	    super.onStop();
   	}
       	
       	
       	
       	
    
}