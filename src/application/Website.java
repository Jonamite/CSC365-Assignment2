package application;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.util.LinkedList;

class Website {
    String Websites[];
    GlobalHashTable globalHashtable;
    ExtendibleHashing UserWebsiteHashTable;
    int CurrentWebsite;
    int TotalWebsites;

    Website() throws IOException {
        //Load all the website urls from 'website.txt'
        LoadWebsiteLinks();

        String[] websiteNames = new String[TotalWebsites];
        for (int i = 0; i < TotalWebsites; i++) {
            websiteNames[i] = Websites[CurrentWebsite].replaceAll("[\\/:*?\"<>|]", "");
        }
        globalHashtable = new GlobalHashTable(websiteNames);

        CurrentWebsite = 0;

        // CREATE USER LINK HASH TABLE
        UserWebsiteHashTable = new ExtendibleHashing();

        File f = new File("Data\\data.dat");
        if(!f.isFile()) {

            for (int i = 0; i < TotalWebsites; i++) {

                System.out.println("LOADING Website " + i);

                try {
                    LoadWebsite();
                    globalHashtable.getHashTableAt(CurrentWebsite).SetTermFrequency();
                }catch(IOException ex){
                    System.out.println("Warn: can not load the website " + Websites[CurrentWebsite]);
                }
                CurrentWebsite++;
            }

            globalHashtable.saveToFile();

        }else {
            for (int i = 0; i < TotalWebsites; i++) {

                System.out.println("LOADING Website " + i);

                try {
                    LoadWebsite();
                    globalHashtable.getHashTableAt(CurrentWebsite).SetTermFrequency();
                }catch(IOException ex){
                    System.out.println("Warn: can not load the website " + Websites[CurrentWebsite]);
                }
                CurrentWebsite++;
            }

            globalHashtable.saveToFile();
            globalHashtable = GlobalHashTable.loadHashTables();
        }


    }

    public void LoadWebsiteLinks() throws FileNotFoundException {
        LinkedList<String> Websiteslinks = new LinkedList<String>();
        //Load websites from file
        TotalWebsites = 0;
        File myObj = new File("Websites.txt");
        String data;
        BufferedReader myReader = new BufferedReader(new FileReader(myObj));
        try {
            while ((data = myReader.readLine()) != null) {
                Websiteslinks.add(data);
                TotalWebsites++;
                //System.out.println(data);
            }
            myReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Websites = new String[TotalWebsites];

        for (int i1 = 0; i1 < Websiteslinks.size(); i1++) {
            Websites[i1] = Websiteslinks.get(i1);
        }
    }

    public void LoadWebsite() throws IOException {


        globalHashtable.getHashTableAt(CurrentWebsite).WebsiteLink = Websites[CurrentWebsite];

        Document doc = Jsoup.connect(Websites[CurrentWebsite]).get();
        String Words = doc.getAllElements().text();

        String[] arrOfStr = Words.split(" ");
        for (String a : arrOfStr) {

            globalHashtable.getHashTableAt(CurrentWebsite).AddWord(a);
        }

        arrOfStr = Words.split(",");
        for (String a : arrOfStr)
            globalHashtable.getHashTableAt(CurrentWebsite).AddWord(a);


        Words = doc.body().text();


        arrOfStr = Words.split(" ");
        for (String a : arrOfStr) {

            globalHashtable.getHashTableAt(CurrentWebsite).AddWord(a);
        }

        arrOfStr = Words.split(",");
        for (String a : arrOfStr)
            globalHashtable.getHashTableAt(CurrentWebsite).AddWord(a);

    }

    public String[] InputFromUser(String web) throws IOException {
        // SCRAP Website USING JSOUP
        String website = web.replaceAll("[\\/:*?\"<>|]", "");
        File f = new File("Data\\" + website);
        if (f.isFile()){
            BufferedReader reader = new BufferedReader(new FileReader(f));
            String strCurrentLine = reader.readLine();
            strCurrentLine = strCurrentLine.split(":")[1];
            strCurrentLine = strCurrentLine.trim();
            UserWebsiteHashTable.TotalWords = Integer.parseInt(strCurrentLine);

            strCurrentLine = reader.readLine();
            strCurrentLine = strCurrentLine.split(":")[1];
            strCurrentLine = strCurrentLine.trim();
            UserWebsiteHashTable.DistinctWords = Integer.parseInt(strCurrentLine);

            while((strCurrentLine = reader.readLine()) != null){
                strCurrentLine = strCurrentLine.split(":")[1];
                strCurrentLine = strCurrentLine.trim();
                String key = strCurrentLine;

                strCurrentLine = reader.readLine();
                strCurrentLine = strCurrentLine.split(":")[1];
                strCurrentLine = strCurrentLine.trim();
                Integer frequency = Integer.parseInt(strCurrentLine);

                strCurrentLine = reader.readLine();
                strCurrentLine = strCurrentLine.split(":")[1];
                strCurrentLine = strCurrentLine.trim();
                float termFrequency = Float.parseFloat(strCurrentLine);

                UserWebsiteHashTable.put(key, new Data(key, frequency, termFrequency));
                reader.readLine();
            }
            reader.close();
        }
        else {
            Document doc = Jsoup.connect(web).get();
            String Words = doc.getAllElements().text();
            String[] arrOfStr = Words.split(" ");
            for (String a : arrOfStr)
                UserWebsiteHashTable.AddWord(a);

            arrOfStr = Words.split(",");
            for (String a : arrOfStr)
                UserWebsiteHashTable.AddWord(a);


            Words = doc.body().text();
            arrOfStr = Words.split(" ");
            for (String a : arrOfStr)
                UserWebsiteHashTable.AddWord(a);

            arrOfStr = Words.split(",");
            for (String a : arrOfStr)
                UserWebsiteHashTable.AddWord(a);

            UserWebsiteHashTable.SetTermFrequency();
        }
        return UserWebsiteHashTable.FindSimilarity(globalHashtable.getHashTables());
    }
}