package it.pspgt.mongodb;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;

// Packages needed to interact with MongoDB and Stitch
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;

// Necessary component for working with MongoDB Mobile
import com.mongodb.stitch.android.services.mongodb.local.LocalMongoDbService;

import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView text,text1,text2,text3;
    MongoCollection<Document> localCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final StitchAppClient client =
                Stitch.initializeDefaultAppClient("aaaaa");
        final MongoClient mobileClient =
                client.getServiceClient(LocalMongoDbService.clientFactory);
        localCollection =
                mobileClient.getDatabase("my_db").getCollection("my_collection2");


        AsyncTask<Void,Void,Void> asyncTask= new AsyncTask() {
            @Override
            protected Boolean doInBackground(Object[] objects) {
                for (int i=0; i<40000; i++){
                    localCollection.insertOne(new Document("chiave",i));
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                Toast.makeText(getApplicationContext(),"Finish",Toast.LENGTH_SHORT).show();
                Log.i("Async","finished");
            }
        };


        asyncTask.execute();


         text = (TextView) findViewById(R.id.text);
         text1 = (TextView) findViewById(R.id.textView);
         text2 = (TextView) findViewById(R.id.textView2);
         text3 = (TextView) findViewById(R.id.textView3);
         Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Random rnd = new Random();
        Document doc = localCollection.find(new Document("chiave", rnd.nextInt(40000))).first();
        text.setText(doc.get("chiave").toString());
        doc = localCollection.find(new Document("chiave", rnd.nextInt(40000))).first();
        text1.setText(doc.get("chiave").toString());
        doc = localCollection.find(new Document("chiave", rnd.nextInt(40000))).first();
        text2.setText(doc.get("chiave").toString());
        text3.setText(Long.toString(localCollection.count()));
    }
}
