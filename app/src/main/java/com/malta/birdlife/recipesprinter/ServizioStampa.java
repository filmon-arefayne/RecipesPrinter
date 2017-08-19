package com.malta.birdlife.recipesprinter;

/**
 * Created by filmon on 05/09/2016.
 */

import android.content.Intent;
import android.print.PrintAttributes;
import android.print.PrinterCapabilitiesInfo;
import android.print.PrinterId;
import android.print.PrinterInfo;
import android.printservice.PrintDocument;
import android.printservice.PrintJob;
import android.printservice.PrintService;
import android.printservice.PrinterDiscoverySession;
import android.util.Log;


import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ServizioStampa extends PrintService {


    List<PrinterInfo> printers = new ArrayList<>();
    List<PrinterId> printersId = new ArrayList<>();
    final String TAG = "myprinter";
    @Override
    protected PrinterDiscoverySession onCreatePrinterDiscoverySession() {
        Log.d(TAG, "MyPrintService#onCreatePrinterDiscoverySession() called");

        return new PrinterDiscoverySession() {
            @Override
            public void onStartPrinterDiscovery(List<PrinterId> priorityList) {
                Log.d(TAG, "PrinterDiscoverySession#onStartPrinterDiscovery(priorityList: " + priorityList + ") called");

             /*   if (!priorityList.isEmpty()) {
                    return;
                }
*/

                final PrinterId printerId = generatePrinterId("thermal printer");
                final PrinterInfo.Builder builder = new PrinterInfo.Builder(printerId, "Bluetooth", PrinterInfo.STATUS_IDLE);
                PrinterCapabilitiesInfo.Builder capBuilder = new PrinterCapabilitiesInfo.Builder(printerId);
                capBuilder.addMediaSize(PrintAttributes.MediaSize.ISO_A4, true);
                capBuilder.addResolution(new PrintAttributes.Resolution("resolutionId", "default resolution", 600, 600), true);
                capBuilder.setColorModes(PrintAttributes.COLOR_MODE_COLOR | PrintAttributes.COLOR_MODE_MONOCHROME, PrintAttributes.COLOR_MODE_COLOR);
                builder.setCapabilities(capBuilder.build());
                printers.add(builder.build());
                printersId.add(printerId);
                addPrinters(printers);

            }

            @Override
            public void onStopPrinterDiscovery() {
                Log.d(TAG, "MyPrintService#onStopPrinterDiscovery() called");
                removePrinters(printersId);
            }

            @Override
            public void onValidatePrinters(List<PrinterId> printerIds) {
                Log.d(TAG, "MyPrintService#onValidatePrinters(printerIds: " + printerIds + ") called");
            }

            @Override
            public void onStartPrinterStateTracking(PrinterId printerId) {
                Log.d(TAG, "MyPrintService#onStartPrinterStateTracking(printerId: " + printerId + ") called");
            }

            @Override
            public void onStopPrinterStateTracking(PrinterId printerId) {
                Log.d(TAG, "MyPrintService#onStopPrinterStateTracking(printerId: " + printerId + ") called");
            }

            @Override
            public void onDestroy() {
                Log.d(TAG, "MyPrintService#onDestroy() called");
            }
        };
    }

    @Override
    protected void onPrintJobQueued(PrintJob printJob) {
        Log.d(TAG, "queued: " + printJob.getId().toString());

        printJob.start();

        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        final PrintDocument document = printJob.getDocument();



        FileInputStream fis = new FileInputStream((document.getData().getFileDescriptor()));
        StringBuffer pdfContent = new StringBuffer("");
        PdfReader reader;
        try {
            reader = new PdfReader(fis);

            for (int x = 1; x <= reader.getNumberOfPages(); x++)
            {
                pdfContent.append(PdfTextExtractor.getTextFromPage(reader, x));
            }
            reader.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, pdfContent.toString());
        i.putExtra("document",pdfContent.toString());
        printJob.complete();

        startActivity(i);
    }
    @Override
    protected void onRequestCancelPrintJob(PrintJob printJob) {
        Log.d(TAG, "canceled: " + printJob.getId().toString());

        printJob.cancel();
    }
}