package com.classy.class_2021a_andb_4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<Shift> shifts = DataManager.generateMockData();

        writeToCsv("table_of_shifts", shifts);
    }

    private void writeToCsv(String fileName, ArrayList<Shift> shifts) {
        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + "MyFiles" + File.separator + "CSVs");
        boolean success = false;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }

        File file = new File(folder.getPath() + File.separator + fileName + ".csv");


        PrintWriter writer = null;

        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            // https://en.wikipedia.org/wiki/Byte_order_mark
            // https://stackoverflow.com/a/4192897/7147289
            os.write(239);
            os.write(187);
            os.write(191);

            writer = new PrintWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));

            String HEADER = "Key,Workplace,StartTime,EndTime,Salary,Comments";
            writer.print(HEADER);

            StringBuilder stringBuilder = new StringBuilder("");
            stringBuilder.setLength(0);
            for (Shift shift : shifts) {
                stringBuilder.append("\n");
                stringBuilder.append("\"" + shift.getKey() + "\"" + ",");
                stringBuilder.append("\"" + shift.getWorkplace() + "\"" + ",");
                stringBuilder.append("\"" + new SimpleDateFormat("dd/MM/yy HH:mm", Locale.US).format(shift.getStartTime()) + "\"" + ",");
                stringBuilder.append("\"" + new SimpleDateFormat("dd/MM/yy HH:mm", Locale.US).format(shift.getEndTime()) + "\"" + ",");
                stringBuilder.append("\"" + String.format(Locale.US, "%.03f", shift.getSalary()) + "\"" + ",");
                stringBuilder.append("\"" + shift.getComments() + "\"" + ",");
            }
            writer.print(stringBuilder.toString());

            writer.flush();
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        MyUtils.sendAttachedMail(this, file.getAbsolutePath(), "\nSent from our application.");

    }
}