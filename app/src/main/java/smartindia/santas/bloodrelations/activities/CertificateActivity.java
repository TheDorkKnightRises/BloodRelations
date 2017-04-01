package smartindia.santas.bloodrelations.activities;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.provider.DocumentsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import smartindia.santas.bloodrelations.R;

public class CertificateActivity extends AppCompatActivity {
    private WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificate);
        String html_value= "<html xmlns=\\\"http://www.w3.org/1999/xhtml\\\">" +
                "<head><meta http-equiv=\\\"Content-Type\\\" content=\\\"text/html; charset=iso-8859-1\\\">" +
                "<title>Certificate <style>\n" +
                "</style></title></head><body style=\\\"width:300px; color: #00000; \\\"><h1 style=\"text-align: center;\">CERTIFICATE OF APPRECIATION</h1>\n" +
                "<p style=\"text-align: center;\">\n" +
                "\n" +
                "Presented to\n" +
                "\n" +
                "Mr./Mrs./Miss " + "  Lorem Ipsum  " +
                "\n" +
                "for voluntary blood donation\n" +
                "\n" +
                "We appreciate and thank you for your contribution \n" +
                "towards this noble cause\n" +
                "</p>\n" +
                "<p style=\"text-align: right;\">\n" +
                "Blood Bank Director\n" +
                "</p></body></html>";

        webView = (WebView) findViewById(R.id.webview_certificate);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.loadData(html_value, "text/html", "UTF-8");
        createWebPrintJob(webView);

    }


    private void createWebPrintJob(WebView webView) {

        // Get a PrintManager instance
        PrintManager printManager = (PrintManager) this.getSystemService(Context.PRINT_SERVICE);

        // Get a print adapter instance
        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter();

        // Create a print job with name and adapter instance
        String jobName = getString(R.string.app_name) + " Document";
        PrintJob printJob = printManager.print(jobName, printAdapter,
                new PrintAttributes.Builder().build());

        // Save the job object for later status checking
        //mPrintJobs.add(printJob);
    }



}
