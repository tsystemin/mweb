package in.co.tsystem.mweb;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myAsyncTask tsk = new myAsyncTask();
        tsk.execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class RestService {

        public JSONObject doGet(String url) {
            JSONObject json = null;

            HttpClient httpclient = new DefaultHttpClient();
            // Prepare a request object
            HttpGet httpget = new HttpGet(url);
            // Accept JSON
            httpget.addHeader("accept", "application/json");
            // Execute the request
            HttpResponse response;

            try {
                response = httpclient.execute(httpget);
                // Get the response entity
                // Log.e("myApp", "Issue is here...!");
                HttpEntity entity = response.getEntity();
                // If response entity is not null
                if (entity != null) {
                    // get entity contents and convert it to string
                    InputStream instream = entity.getContent();
                    String result= convertStreamToString(instream);
                    // construct a JSON object with result
                    json=new JSONObject(result);
                    // Closing the input stream will trigger connection release
                    instream.close();
                }
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // Return the json
            return json;
        }

        private String convertStreamToString(InputStream is) {

            BufferedReader br = null;
            StringBuilder sb = new StringBuilder();

            String line;
            try {

                br = new BufferedReader(new InputStreamReader(is));
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return sb.toString();
        }
    }


    private class myAsyncTask extends AsyncTask<Void, Void, Void> {

        JSONObject jb;

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            TextView tv = (TextView)findViewById(R.id.text);
            tv.setText(jb.toString());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            //String url = "http://192.168.1.145/tst.php";
            String url = "http://10.0.0.17/opencart/?route=feed/web_api/products&category=27&key=key1";
            RestService re = new RestService();
            jb =  re.doGet(url);

            return null;

        }
    }
}
