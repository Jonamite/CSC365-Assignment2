package application;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
class Data implements Serializable {

    String Key;
    public int Frequency;
    //Frequency divide by total words of website
    public float termFrequency;

    // Constructor accepting word as parameter. It will accept word to the key and set the frequency as 1
    public Data(String key) {
        this.Key = key;
        this.Frequency = 1;
    }

    // Constructor accepting word, frequency and termfrequency as parameter
    public Data(String key, int frequency, float termFrequency) {
        this.Key = key;
        this.Frequency = frequency;
        this.termFrequency = termFrequency;
    }

    // To set the frequency
    public void SetFrequency(int f) {
        Frequency = f;
    }
}

@SuppressWarnings("serial")
class Page implements Serializable {

    private static int PAGE_SZ = 10;

    Map<String, Data> data = new HashMap<>();
    int local_depth = 0;

    public boolean full() {
        return data.size() >= PAGE_SZ;
    }

    public void put(String key, Data value) {
        data.put(key, value);
    }

    public Data get(String key) {
        return data.get(key);
    }

    public int get_local_high_bit() {
        return 1 << local_depth;
    }

}

@SuppressWarnings("serial")
class ExtendibleHashing implements Serializable{

    int global_depth = 0;
    Page[] directory = {new Page()};

    public Page get_page(String key) {
        int h = HashFunction(key);
        int index = h & ((1 << global_depth) - 1);
        return directory[index];
    }

    public void put(String key, Data value) {

        Page p = get_page(key);

        boolean full = p.full();
        p.put(key, value);

        if (full) {
            if (p.local_depth == global_depth) {

                Page[] new_directory = new Page[directory.length * 2];

                int j = 0;
                for (int i = 0; i < directory.length; i++) {
                    new_directory[j] = directory[i];
                    j += 1;
                }
                for (int i = 0; i < directory.length; i++) {
                    new_directory[j] = directory[i];
                    j += 1;
                }
                global_depth += 1;
                directory = new_directory;
            }

            Page p0 = new Page();
            Page p1 = new Page();
            p0.local_depth = p1.local_depth = p.local_depth + 1;
            int high_bit = p.get_local_high_bit();

            for (Map.Entry<String, Data> entry : p.data.entrySet()) {

                int h = HashFunction(entry.getKey());

                Page new_p = ((h & high_bit) != 0)?p1: p0;
                new_p.put(entry.getKey(), entry.getValue());
            }

            int i = HashFunction(key) & (high_bit - 1);
            while (i < directory.length) {

                directory[i] = ((i & high_bit) != 0)?p1: p0;

                i += high_bit;
            }
        }
    }

    public Data get(String key) {
        try {
            return get_page(key).get(key);
        }catch(Exception e) {
            return null;
        }
    }

    String WebsiteLink;
    int DistinctWords = 0;

    // TOTAL words
    int TotalWords = 0;

    // Function to add word. It accepts string as word
    void AddWord(String word) {
        // Convert it into lowercase
        word = word.toLowerCase();
        // Increase the total words
        TotalWords++;

        Data value = get(word);
        if (value == null) {

            put(word, new Data(word));

            // if word is not present then increase distinct word
            DistinctWords++;

        }else {
            value.Frequency += 1;
        }
    }

    // hash function
    int HashFunction(String s) {
        return s.hashCode();
    }

    // Set term frequency of each word
    void SetTermFrequency() {

        for (Page page: directory) {
            if (page != null) {

                for (Map.Entry<String, Data> entry : page.data.entrySet()) {
                    if (entry.getValue() != null) {
                        entry.getValue().termFrequency /= TotalWords;
                    }
                }
            }
        }
    }

    //Similarity Metric
    public String[] FindSimilarity(ExtendibleHashing Websites[]) {
        // make new array  and assign them 0
        float SimilarityCount[] = new float[Websites.length];
        for (int i = 0; i < Websites.length; i++) {
            SimilarityCount[i] = 0;
        }

        for (Page page: directory) {
            if (page != null) {

                for (Map.Entry<String, Data> entry : page.data.entrySet()) {

                    //check word in each website
                    for (int y = 0; y < Websites.length; y++) {
                        try {
                            // if word is found
                            int z = Websites[y].FindKey(entry.getKey());
                            // increase the similarity count of that websites links
                            SimilarityCount[y] = (SimilarityCount[y] + (entry.getValue().Frequency * z));
                        }catch(Exception e) {

                        }
                    }

                }
            }
        }

        int max = 0;
        for (int i = 1; i < SimilarityCount.length; i++){
            if (SimilarityCount[i] > SimilarityCount[max]){
                max = i;
            }
        }


        // Iterate each website data
        float SimilarityCountCategory[] = new float[Websites.length / 1];
        for (int i = 0; i < SimilarityCountCategory.length; i++) {
            SimilarityCountCategory[i] = 0;
        }
        int z = 0;
        for (int i = 0; i < Websites.length; i++) {
            if (i % 4 == 0 && i != 0) {

                z++;
            }
            //	System.out.println(z);

            SimilarityCountCategory[z] = SimilarityCountCategory[z] + SimilarityCount[i];
            System.out.println("Similarity count of website " + Websites[i].WebsiteLink + " is " + SimilarityCount[i]);
        }
        int maxSimilarityIdx = max;
        max = 0;
        for (int i = 1; i < SimilarityCountCategory.length; i++) {
            if (SimilarityCountCategory[i] > SimilarityCountCategory[max])
                max = i;
        }

        int similarWebsiteCluster = (int) Math.floor(Math.random() * (((max * 4) + 3) - (max * 4) + (max * 4)));

        int nClusters = 7;
        KMeans pageCluster = new KMeans();
        pageCluster.generateRecord(SimilarityCount, Websites.length);
        pageCluster.initiateClusterAndCentroid(nClusters);
        // pageCluster.printRecordInformation();
        int clusterCenterId = pageCluster.printClusterInformation(nClusters, maxSimilarityIdx);
        String result[] = new String[2];
        result[0] = Websites[clusterCenterId].WebsiteLink;
        result[1] = Websites[similarWebsiteCluster].WebsiteLink;

        //int SimilarWebsite=(max*4) + (int)(Math.random() * (((max*4)+3)) - (max*4) + 1);
        return result;
    }

    // find word and return its frequency
    public int FindKey(String word) {

        return get(word).Frequency;
    }

}

@SuppressWarnings("serial")
class GlobalHashTable implements Serializable{

    private ExtendibleHashing[] tables;
    public GlobalHashTable(String[] websites) {

        tables = new ExtendibleHashing[websites.length];

        for (int i = 0; i < websites.length; i++) {
            tables[i] = new ExtendibleHashing();
        }
    }

    public ExtendibleHashing getHashTableAt(int index) {
        return tables[index];
    }

    public ExtendibleHashing[] getHashTables() {
        return tables;
    }

    public void saveToFile(){
        try {

            FileOutputStream fileOut = new FileOutputStream("Data\\data.dat");
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(this);
            objectOut.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static GlobalHashTable loadHashTables() {

        GlobalHashTable table = null;

        try {
            File f = new File("Data\\data.dat");
            FileInputStream fi = new FileInputStream(f);
            ObjectInputStream oi = new ObjectInputStream(fi);

            table = (GlobalHashTable) oi.readObject();

            oi.close();
            fi.close();
        }catch (Exception ex){
            System.out.println("Warn: cannot load Data\\data.dat");
        }

        return table;
    }
}
