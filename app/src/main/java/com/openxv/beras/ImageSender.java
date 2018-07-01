package com.openxv.beras;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import me.echodev.resizer.Resizer;

public class ImageSender extends AsyncTask<String, Void, String> {
    private final static String API_URL = "https://beras-server.herokuapp.com/predict/";
    private final static String MSG_SUCCESS = "Tipe Beras: ";
    private final static String MSG_FAILURE = "Gagal menentukan tipe beras";
    private final static String MSG_ERROR = "Terjadi kesalahan pada server";
    private final static String boundary = "*****";
    private final static String crlf = "\r\n";
    private final static String twoHyphens = "--";

    private Context context;
    private TextView resultText;

    public ImageSender(Context context, TextView resultText) {
        this.context = context;
        this.resultText = resultText;
    }

    private String sendImage(String path) {
        String result = null;
        HttpURLConnection connection = null;
        BufferedReader response = null;

        try {
            URL uploadUrl = new URL(API_URL);
            connection = (HttpURLConnection) uploadUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            File file = new File(path);
            String fileName = file.getName();

            File resizedImage = new Resizer(context)
                    .setTargetLength(500)
                    .setQuality(80)
                    .setOutputFormat("JPEG")
                    .setSourceImage(file)
                    .getResizedFile();

            byte[] bytes = new byte[(int) file.length()];
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(resizedImage));
            buf.read(bytes, 0, bytes.length);
            buf.close();

            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            DataOutputStream request = new DataOutputStream(connection.getOutputStream());

            request.writeBytes(twoHyphens + boundary + crlf);
            request.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\"" +
                    fileName + "\"" + crlf);
            request.writeBytes(crlf);
            request.write(bytes);
            request.writeBytes(crlf);
            request.writeBytes(twoHyphens + boundary + twoHyphens + crlf);
            request.flush();
            request.close();

            response = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder buffer = new StringBuilder();
            String line;
            while ((line = response.readLine()) != null) {
                buffer.append(line).append("\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            result = buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        try {
            JSONObject jsonResult = new JSONObject(s);
            if (jsonResult.getBoolean("success")) {
                String kelas = jsonResult.getString("class");
                resultText.setText(String.format("%s%s", MSG_SUCCESS, kelas));
            } else {
                String msg = jsonResult.getString("message");
                resultText.setText(msg);
            }
        } catch (Exception e) {
            resultText.setText(MSG_ERROR);
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        return sendImage(strings[0]);
    }

}
