package com.example.n8154.app9_1;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    String area;
    TextView text;
    Theatre teatteri;
    ArrayList<Theatre> theatreArray = new ArrayList<>();
    ArrayList<String> movieList = new ArrayList<>();
    ListView simpleList;
    TextView text2;
    EditText date;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        text = (TextView) findViewById(R.id.textView);

        ArrayList<String> spinnerArray = new ArrayList<String>();
        ArrayList<String> nameArray = new ArrayList<String>();
        System.out.print("READ");
        readAreaXML(nameArray);
        System.out.print("READ DONE");

        spinner = (Spinner) findViewById(R.id.areaSpinner);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item, theatreArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getBaseContext(),"Leffateatteri: " + parent.getSelectedItem(),Toast.LENGTH_LONG).show();
                Theatre area = (Theatre) spinner.getSelectedItem();
                try {
                    text.setText(Integer.toString(area.getID()));
                } catch (Exception e) {
                    e.printStackTrace();
                } readMovieXML(area.getID(), "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }


    public void readAreaXML (ArrayList nameArray) {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            String urlString = "https://www.finnkino.fi/xml/TheatreAreas/";
            Document doc = builder.parse(urlString);
            doc.getDocumentElement().normalize();
            //System.out.println("Root element: " + doc.getDocumentElement().getNodeName());

            NodeList nList = doc.getDocumentElement().getElementsByTagName("TheatreArea");
            for (int i=0; i<nList.getLength(); i++) {
                Node node = nList.item(i);
                //System.out.println("Elementti on: " + node.getNodeName());
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    System.out.print("Nimi: ");
                    System.out.println(element.getElementsByTagName("Name").item(0).getTextContent());
                    nameArray.add(element.getElementsByTagName("Name").item(0).getTextContent());
                    teatteri = new Theatre(element.getElementsByTagName("Name").item(0).getTextContent(), Integer.parseInt(element.getElementsByTagName("ID").item(0).getTextContent()));
                    System.out.print(teatteri.getID());
                    theatreArray.add(teatteri);



                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } finally {
            System.out.println("#########DONE#########");
        }
    }

    public void readMovieXML (int ID, String date) {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            String urlString = "http://www.finnkino.fi/xml/Schedule/?area=" +ID + "&dt=" + date;
            Document doc = builder.parse(urlString);
            doc.getDocumentElement().normalize();
            movieList.clear();
            NodeList nList = doc.getDocumentElement().getElementsByTagName("Show");

            for (int i=0; i<nList.getLength(); i++) {
                Node node = nList.item(i);
                //System.out.println("Elementti on: " + node.getNodeName());
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    //System.out.print("Leffa: ");
                    //System.out.println(element.getElementsByTagName("Title").item(0).getTextContent());
                    movieList.add(element.getElementsByTagName("Title").item(0).getTextContent().toString());


                }
            }
            simpleList = (ListView) findViewById(R.id.simpleListView);
            System.out.println("#######-- Movies" + movieList);
            ArrayAdapter<String> movieAdapter = new ArrayAdapter<String>(this, R.layout.activity_listview, movieList);
            simpleList.setAdapter(movieAdapter);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } finally {
            System.out.println("#########DONE#########");
        }
    }

    public void setDate(int ID) {

    }

    public void buttonPress(View v) {
        Theatre area = (Theatre) spinner.getSelectedItem();
        date =  (EditText) findViewById(R.id.editText);
        readMovieXML(area.getID(), date.getText().toString());
    }
}
