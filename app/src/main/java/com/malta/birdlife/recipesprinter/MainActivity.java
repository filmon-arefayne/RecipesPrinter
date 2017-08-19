package com.malta.birdlife.recipesprinter;
/**
 * Created by filmon on 05/09/2016.
 */

/**
 * Google barcode API imports
 **/
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 *  MINI Termal Printer API
 */

import java.util.Set;
import android.content.Intent;
import com.zj.btsdk.BluetoothService;


import android.bluetooth.BluetoothAdapter;
import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.util.Log;

public class MainActivity extends Activity  {



    /*
        Attributi
     */
    BluetoothService btService = null;
    BluetoothDevice btDevice = null;

    Button btnPrint;
    Button btnSearch;



    private static final int REQUEST_ENABLE_BT = 2;

    private static final int REQUEST_CONNECT_DEVICE = 1;

    private int conn_flag = 0;

    private ConnectPaireDev mConnPaireDev = null;

 //   public String URLPAGE = "http:/birdlifemalta.org/";

    String pdfDocument = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Google barcode API button
         **/

     /* Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        TextView txtView = (TextView) findViewById(R.id.txtContent);
        ImageView myImageView = (ImageView) findViewById(R.id.imgview);
        Bitmap myBitmap = BitmapFactory.decodeResource(
                getApplicationContext().getResources(),
                R.drawable.puppy);
        myImageView.setImageBitmap(myBitmap);

        BarcodeDetector detector =
                new BarcodeDetector.Builder(getApplicationContext())
                        .setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE)
                        .build();
        if(!detector.isOperational()){
            txtView.setText("arrrggg!");
            return;
        }

        /** l'API di Google e' capace di leggere piu' barcodes alla volta
         *  devo gestire anche questo
         */
    /*  Frame frame = new Frame.Builder().setBitmap(myBitmap).build();
        SparseArray<Barcode> barcodes = detector.detect(frame);

        Barcode thisCode = barcodes.valueAt(0);

        txtView.setText(thisCode.rawValue);
    */

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            pdfDocument = extras.getString("document");
            //The key argument here mu st match that used in the other activity
          //  Log.i("BUFFER_DOC",bufferDoc);

        }

        btService = new BluetoothService(this,mHandler);


        // se il dispositivo non dispone di ricevitore bluetooth
        if( btService.isAvailable() == false ){

            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();

            finish();
            return;
        }
         // se il bluetooth non e' attivo
        else if( btService.isBTopen() == false)
        {

                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

                startActivityForResult(enableIntent, REQUEST_ENABLE_BT);

        }

    }

    @Override
    public void onStart() {

        super.onStart();

        try {

            btnPrint = (Button) this.findViewById(R.id.btnPrint);

            btnPrint.setOnClickListener(new ClickEvent());

            btnPrint.setEnabled(false);

            btnSearch = (Button) this.findViewById(R.id.btnSearch);

            btnSearch.setOnClickListener(new ClickEvent());

            ProgressBar loading = (ProgressBar) findViewById(R.id.progressBar);

            loading.setVisibility(View.INVISIBLE);


        } catch (Exception ex) {

            Log.e("MAIN_ACTIVITY",ex.getMessage());

        }

        if(pdfDocument==null) {
            //Toast.makeText(this, "Sono stati passati dei dati!!", Toast.LENGTH_LONG).show();
            Toast.makeText(this, "Go on Dolicloud facture", Toast.LENGTH_LONG).show();
            finish();
        }
        else {
            mConnPaireDev = new ConnectPaireDev();

            mConnPaireDev.start();
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        if (btService != null)
        {
            btService.stop();
        }
        btService = null;

    }

    class ClickEvent implements View.OnClickListener {

        public void onClick(View v) {

            if (v == btnSearch) {

                Intent serverIntent = new Intent(MainActivity.this, DeviceListActivity.class);

                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);

            } else if (v == btnPrint) {

                ProgressBar loading = (ProgressBar) findViewById(R.id.progressBar);

                loading.setVisibility(View.VISIBLE);

                /*String msg = "Cantami o Diva del pelide achille l'ira funesta";


                btService.sendMessage(msg,"GBK");
                */

            /*    // Unico comando
                byte[] cmd = new byte[3];
                cmd[0] = 0x1b; // ESC

                cmd[1] = 0x21; // !

                cmd[2] |= 0x10; // DLE (Double height mode)

                btService.write(cmd);

                btService.sendMessage("Congratulations!\n", "GBK");

               // cmd[2] &= 0xEF; //  ritorno a zero
                cmd[2] = 0x00;
                btService.write(cmd);

                byte[] cmd2 = new byte[4];

                cmd2[0] = 0x10;
                cmd2[1] = 0x14;
                cmd2[2] = 0x0;

                btService.sendMessage("Congratulations!\n", "GBK");

           //     btService.sendMessage(bufferDoc.toString(),"GBK");
                loading.setVisibility(View.INVISIBLE);

                /*

                String msg = "";

                String lang = getString(R.string.strLang);

                printImage();



                byte[] cmd = new byte[3];

                cmd[0] = 0x1b;

                cmd[1] = 0x21;

                if((lang.compareTo("en")) == 0){

                    cmd[2] |= 0x10;

                    btService.write(cmd);

                    btService.sendMessage("Congratulations!\n", "GBK");

                    cmd[2] &= 0xEF;

                    btService.write(cmd);

                    msg = "  You have sucessfully created communications between your device and our bluetooth printer.\n\n"

                            +"  Shenzhen Zijiang Electronics Co..Ltd is a high-tech enterprise which specializes" +

                            " in R&D,manufacturing,marketing of thermal printers and barcode scanners.\n\n"

                            +"  Please go to our website and see details about our company :\n" +"     http://birdlifemalta.org\n\n";



                    btService.sendMessage(msg,"GBK");
                */

                if (pdfDocument != null)
                {

                    byte[] sendData = null;
                    PrintPicEx pg = new PrintPicEx();
                    pg.initCanvas(384); // ParameterDescription： W:Value 384px for 58seriesprinter ，Value 576px for 80seriesprinter
                    pg.initPaint();
                    pg.drawImageResource(384/3,0,getApplicationContext().getResources(),R.drawable.logox);
                    sendData = pg.printDraw();
                    btService.write(sendData);
                 //   Log.d("Printer:",""+sendData.length);

                    String array[] = pdfDocument.split("\n");

                    byte[] cmd = new byte[3];

                    cmd[0] = 0x1b;

                    cmd[1] = 0x21;

                    cmd[2] = 0x10;

                    btService.write(cmd);

                    btService.sendMessage(array[0]+"\n", "GBK"); // title
                    cmd[2] &= 0xEF;
                  //  cmd[2] = 0x00;
                    btService.write(cmd);
                    for(int i = 1;i < array.length;i++)
                    {
                        if(i == 5)
                        {
                            btService.sendMessage("  \n", "GBK");
                        }
                        if(array[i].contains("Label Qty/Price Total"))
                        {
                            do {
                                i++;
                            }
                            while(array[i].contains("Total")!=true);
                        }
                        btService.sendMessage(array[i], "GBK");
                    }
                    btService.sendMessage("  \n", "GBK");
                 //   btService.sendMessage("  \n", "GBK");
                }

                loading.setVisibility(View.INVISIBLE);
                }

            }

        }



    private final  Handler mHandler = new Handler()
    {

        @Override
        public void handleMessage(Message msg) {

            String TAG_B = "BLUETOOTH";

            switch (msg.what)
            {

                case BluetoothService.MESSAGE_STATE_CHANGE:
                {
                    switch (msg.arg1)
                    {

                        case BluetoothService.STATE_CONNECTED:
                        {
                            Toast.makeText(getApplicationContext(), "Connect successful",

                                    Toast.LENGTH_SHORT).show();

                            btnPrint.setEnabled(true);

                            conn_flag = 1;

                            break;
                        }
                        case BluetoothService.STATE_CONNECTING: {
                            Log.d(TAG_B, "Connecting.....");

                            break;
                        }
                        case BluetoothService.STATE_LISTEN: {
                            Log.d(TAG_B, "Listening...");

                            break;
                        }
                        case BluetoothService.STATE_NONE: {


                            Log.d(TAG_B, "State none...");

                            break;
                        }
                    }

                    break;
                }
                case BluetoothService.MESSAGE_CONNECTION_LOST:
                {
                    Toast.makeText(getApplicationContext(), "Device connection was lost",

                            Toast.LENGTH_SHORT).show();

                    btnPrint.setEnabled(false);

                    break;
                }
                case BluetoothService.MESSAGE_UNABLE_CONNECT:
                {
                    Toast.makeText(getApplicationContext(), "Unable to connect device",

                            Toast.LENGTH_SHORT).show();

                    conn_flag = -1;

                    break;
                }
            }

        }



    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode)
        {

            case REQUEST_ENABLE_BT:
            {
                if (resultCode == Activity.RESULT_OK)
                {

                  //  Toast.makeText(this, "Bluetooth open successful", Toast.LENGTH_LONG).show();

                }
                else
                {
                    Toast.makeText(this, "Bluetooth is required for this app", Toast.LENGTH_LONG).show();
                    /* ########################################### */
                    finish();
                    // gestire eccezioni lo faccio dopo
                    /*
                    #################################
                    ##############################
                    ###########################
                    #########################
                    ######################
                    ###################

                     */
                }

                break;
            }
            case  REQUEST_CONNECT_DEVICE:
            {
                if (resultCode == Activity.RESULT_OK)
                {

                    String address = data.getExtras()

                            .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);

                    btDevice = btService.getDevByMac(address);


                    btService.connect(btDevice);

                }
                else
                {
                    Toast.makeText(this, "Bluetooth is required for this app", Toast.LENGTH_LONG).show();
                }

                break;
            }
        }

    }



    public class ConnectPaireDev extends Thread {

        public void run(){

            while(true)
            {
                if(btService != null)
                {

                    if (btService.isBTopen() == true)
                    {
                        break;
                    }
                }

            }

            Set<BluetoothDevice> pairedDevices = btService.getPairedDev();

            if (pairedDevices.size() > 0)
            {

                for (BluetoothDevice device : pairedDevices)
                {

                    if(conn_flag == 1) // se connesso
                    {

                        conn_flag = 0; // resetto

                        break;

                    }

                    while(true)
                    {
                        if (conn_flag == -1 || conn_flag == 0) // se impossibile connettersi
                        {
                            break;
                        }
                    }
                    btService.connect(device);

                    conn_flag = 2;

                }

            }

        }

    }


}
