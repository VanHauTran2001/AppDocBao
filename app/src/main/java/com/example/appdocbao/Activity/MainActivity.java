package com.example.appdocbao.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.content.Intent;
import android.net.Uri;

import android.os.AsyncTask;
import android.os.Bundle;

import android.view.View;

import com.example.appdocbao.Adapter.RssAdapter;
import com.example.appdocbao.HTTP.XMLROMPaser;
import com.example.appdocbao.Model.Item;
import com.example.appdocbao.R;
import com.example.appdocbao.databinding.ActivityMainBinding;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.XMLReader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class MainActivity extends AppCompatActivity {

    private List<Item> itemList;
    private RssAdapter adapter;
    private ActivityMainBinding binding;
    private String url_link = "https://vnexpress.net/rss/suc-khoe.rss";
    private Item items = new Item();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);

        khoiTaoDuLieu();
        onClickLink();
        loadData();
    }

    private void loadData() {
        new ReadXML().execute(url_link);
    }
    private String docURL(String theUrl){
        StringBuilder builder = new StringBuilder();
        try    {

            URL url = new URL(theUrl);
            URLConnection urlConnection = url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line = "";
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
                    items = new Item();
                    items.title = getTagValue("title",element);
                    items.description = getTagValue("description",element);
                    String noHTMLString = items.description.replaceAll("\\<.*?\\>", "");
                    items.description = noHTMLString;
                    itemList.add(new Item(items.title,items.description));
                }
            }
            adapter = new RssAdapter(MainActivity.this,itemList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext(),LinearLayoutManager.VERTICAL,false);
            binding.recylerView.setLayoutManager(linearLayoutManager);
            binding.recylerView.setAdapter(adapter);
            bufferedReader.close();
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        catch(Exception e)    {
            e.printStackTrace();
        }
        return builder.toString();
    }
    private String getTagValue(String sTag, Element eElement) {
        NodeList nlList = eElement.getElementsByTagName(sTag).item(0)
                .getChildNodes();

        Node nValue = (Node) nlList.item(0);

        return nValue.getNodeValue();

    }
    private class ReadXML extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            String kq = docURL(strings[0]);
            return kq;
        }

        @Override
        protected void onPostExecute(String s) {
//            XMLROMPaser parser = new XMLROMPaser();
//            Document doc = parser.getDocument(s);
//            NodeList nodeList = doc.getElementsByTagName("item");
////            NodeList nodeListDecription = doc.getElementsByTagName("link");
//            String title = "";
//            String description = "";
//            itemList = new ArrayList<>();
//            for(int i = 0 ; i < nodeList.getLength() ; i++){
//                String cdata = nodeListDecription.item(i+1).getTextContent();
//                Pattern p = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
//                Matcher matcher = p.matcher(cdata);
//                if(matcher.find()){
//                    description = matcher.group(1);
//                }
//                Element e = (Element) nodeList.item(i);
//                title = parser.getValue(e,"title");
//                description = parser.getValue(e,"link");
//                itemList.add(new Item(title,description));
//            }


        }
    }



    private void khoiTaoDuLieu() {
            itemList = new ArrayList<>();
    }

    private void onClickLink() {
        binding.txtTieuDe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://vnexpress.net/rss/suc-khoe.rss");
                startActivity(new Intent(Intent.ACTION_VIEW,uri));
            }
        });
    }
}