package com.example.appdocbao.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.appdocbao.Adapter.RssAdapter;
import com.example.appdocbao.Model.Item;
import com.example.appdocbao.R;
import com.example.appdocbao.databinding.ActivityMainBinding;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Objects;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class MainActivity extends AppCompatActivity implements RssAdapter.IRSS {

    private ArrayList<Item> itemList=new ArrayList<>();
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        RssAdapter adapter = new RssAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this,LinearLayoutManager.VERTICAL,false);
        binding.recylerView.setLayoutManager(linearLayoutManager);
        binding.recylerView.setAdapter(adapter);
        onClickLink();
        loadData();
    }

    private void loadData() {
        String url_link = "https://vnexpress.net/rss/suc-khoe.rss";
        new ReadXML().execute(url_link);
        itemList = new ArrayList<>();
    }
    private String docURL(String theUrl){
        try    {

            URL url = new URL(theUrl);
            URLConnection urlConnection = url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            String fullStr = "";
            while ((line = bufferedReader.readLine()) != null){
                fullStr = fullStr.concat(line + "\n");
            }
            InputStream istream = url.openStream();
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(istream);
            document.getDocumentElement().normalize();
            NodeList nodeList = document.getElementsByTagName("item");
            for (int i=0;i<nodeList.getLength();i++){
                Node node = nodeList.item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) node;
                    Item items = new Item();
                    items.title = getTagValue("title",element);
                    items.description = getTagValue("description",element);
                    items.description = items.description.replaceAll("\\<.*?\\>", "");
                    itemList.add(new Item(items.title, items.description));

                }
            }
            bufferedReader.close();
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        catch(Exception e)    {
            e.printStackTrace();
        }
        return "";
    }
    private String getTagValue(String sTag, Element eElement) {
        NodeList nlList = eElement.getElementsByTagName(sTag).item(0)
                .getChildNodes();

        Node nValue = nlList.item(0);

        return nValue.getNodeValue();

    }

    @Override
    public int getCount() {
        if(itemList.size() == 0){
            return 0;
        }
        return itemList.size();
    }

    @Override
    public Item getData(int position) {
        return itemList.get(position);
    }

    @Override
    public void setOnClickData(int position) {
        Intent intent = new Intent(this, NewActivity.class);
        intent.putExtra("title",itemList.get(position).title);
        intent.putExtra("description",itemList.get(position).description);
        startActivity(intent);
    }

    @SuppressLint("StaticFieldLeak")
    private class ReadXML extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            return docURL(strings[0]);
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Objects.requireNonNull(binding.recylerView.getAdapter()).notifyDataSetChanged();
        }
    }

    private void onClickLink() {
        binding.txtTieuDe.setOnClickListener(view -> {
            Uri uri = Uri.parse("https://vnexpress.net/rss/suc-khoe.rss");
            startActivity(new Intent(Intent.ACTION_VIEW,uri));
        });
    }
}