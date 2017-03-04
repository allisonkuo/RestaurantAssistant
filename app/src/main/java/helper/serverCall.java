package helper;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by paul on 3/1/17.
 */

// ===== INSTRUCTIONS ON MAKING SERVER CALL =====
// 1. Copy the commented code below to wherever you want to make server call
// 2. modify line: results = task.execute... line, indicating which script and key-value pairs you want to pass into the backend PHP script
// 3. After server call, the return string will be saved into "result"

/*
String result = "";
serverCall task = new serverCall();
try
{
    // ONLY PART YOU HAVE TO CHANGE
    // template: result = task.execute("http://custom-env.hsqkmufkrn.us-west-1.elasticbeanstalk.com/scripts/SCRIPT","KEY1","VALUE1","KEY2", "VALUE2",...).get();
    result = task.execute("http://custom-env.hsqkmufkrn.us-west-1.elasticbeanstalk.com/scripts/test.php","input","*returnedValue*").get();

} catch (InterruptedException e) {
e.printStackTrace();
} catch (ExecutionException e) {
e.printStackTrace();
}
// server call's response is saved into result
Log.v("server response: ", result);
*/

    // IGNORE EVERYTHING BELOW HERE
public class serverCall extends AsyncTask<String , Void ,String> {
    String server_response;

    @Override
    protected String doInBackground(String... strings) {

        URL url;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL(strings[0]);

            String param = "";
            for(int i = 1; i < strings.length; i = i+2)
            {
                if(i != 1)
                {
                    param += '&';
                }
                param += strings[i];
                param += "=";
                param += strings[i+1];
            }

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");

            urlConnection.setFixedLengthStreamingMode(param.getBytes().length);
            urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
            out.print(param);
            out.close();

            server_response = readStream(urlConnection.getInputStream());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return server_response;

    }
/*        @Override
        protected String onPostExecute(String result) {

        }
*/
// Converting InputStream to String

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }
}
