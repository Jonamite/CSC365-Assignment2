package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class KMeans {

	List<Record> data = new ArrayList<Record>();
	List<Cluster> clusters = new ArrayList<Cluster>();
	Map<Cluster, List<Record>> clusterRecords = new HashMap<Cluster, List<Record>>();
	
	public void generateRecord(float[] similiarityCounts, int size) {
        for (int i = 0; i < size; i++){
            Record record = new Record(i + 1, similiarityCounts[i]);
            data.add(record);
        }
	}

	public void initiateClusterAndCentroid(int clusterNumber) {
		int counter = 1;
		Iterator<Record> iterator = data.iterator();
		Record record = null;
		
		while(iterator.hasNext()) {
			record = iterator.next();
			if(counter <= clusterNumber) {
				record.setClusterNumber(counter);
				initializeCluster(counter, record);
				counter++;
			}else {
				// System.out.println(record);
				// System.out.println("** Cluster Information **");
				// for(Cluster cluster : clusters) {
					// System.out.println(cluster);
				// }
				// System.out.println("*********************");
                double minDistance = Integer.MAX_VALUE;
                Cluster whichCluster = null;
                
                for(Cluster cluster : clusters) {
                	double distance = cluster.calculateDistance(record);
                	// System.out.println(distance);
                	if(minDistance > distance) {
                		minDistance = distance;
                		whichCluster = cluster;
                	}
                }
                
                record.setClusterNumber(whichCluster.getClusterNumber());
				whichCluster.updateCentroid(record);
				clusterRecords.get(whichCluster).add(record);
			}
			
			// System.out.println("** Cluster Information **");
			// for(Cluster cluster : clusters) {
			// 	System.out.println(cluster);
			// }
			// System.out.println("*********************");
		}

	}

	public void initializeCluster(int clusterNumber, Record record) {
		Cluster cluster = new Cluster(clusterNumber, record.getScore());
		clusters.add(cluster);
		List<Record> clusterRecord = new ArrayList<Record>();
		clusterRecord.add(record);
		clusterRecords.put(cluster, clusterRecord);
	}

	public void printRecordInformation() {
		   System.out.println("****** Each Record INFORMATION *********");
		   for(Record record : data) {
			   System.out.println(record);
		   }
	   }

	public int printClusterInformation(int nClusters, int similarWebsiteIdx) {
	   	// System.out.println("****** FINAL CLUSTER INFORMATION *********");
		int clusterRecordId = 0;
		for (Map.Entry<Cluster, List<Record>> entry : clusterRecords.entrySet())  {
			// System.out.println("Key = " + entry.getKey() + 
						// ", Value = " + entry.getValue()); 
			List<Record> entries = entry.getValue();
			float scoreCentroid = entry.getKey().getScoreCentroid();
			int closestIdx = 0;
			boolean clusterFound = false;
			for (int i = 0; i < entries.size(); i++){
				if (entries.get(i).getId() == similarWebsiteIdx){
					clusterFound = true;
					for (int j = 1; j < entries.size(); j++){
						if (Math.abs(entries.get(j).getScore() - scoreCentroid) < (Math.abs(entries.get(closestIdx).getScore() - scoreCentroid))){
							closestIdx = j;
						}
					}
				}
			}
			if (clusterFound){
				clusterRecordId = entries.get(closestIdx).getId();	
				break;
			}
		}
		return clusterRecordId;
    }
}