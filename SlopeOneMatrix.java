import java.util.*;
import java.util.Map.*;

/**
 * The class SlopeOneMatrix is a repository for matrices used in Slope One.
 * itemAVGDiffMatrix is the rating differences between each pair of items.
 *
 */
public class SlopeOneMatrix {
	private DataSource dataSRC;
	private HashMap<Integer, HashMap<Integer, Double>> itemAVGDiffMatrix;
	private HashMap<Integer, HashMap<Integer, Integer>> itemItemWeightMatrix;
	private boolean isWeighted;
	private Runtime runtime;
	private  int mb = 1024*1024;

	public SlopeOneMatrix(DataSource dataSRC, boolean isWeighted) {
		this.dataSRC = dataSRC;
		this.isWeighted = isWeighted;
		itemAVGDiffMatrix = new HashMap<Integer, HashMap<Integer, Double>>();
        //Getting the runtime reference from system
	   runtime  = Runtime.getRuntime();
		calcItemPairs();
	     //Print used memory
        System.out.println("Used Memory:"
            + (runtime.totalMemory() - runtime.freeMemory()) / mb);
	}

	private void calcItemPairs() {
		int weight = 0;
		HashMap<Integer, Integer> innerHashMapWeight = null;
		HashMap<Integer, Double> innerHashMapAVG = null;

		if (isWeighted) {
			itemItemWeightMatrix = new HashMap<Integer, HashMap<Integer, Integer>>();
		}

		Integer ratingI = -1, ratingJ = -1, userI = -1, userJ = -1;

		int dev = 0;
		int sum = 0;
		int countSim = 0;
		Double average = 0.0;

		System.out.println("Now running: Calculate Item-Item Average Diff");

		// for all items, i
		for (int i = 1; i <= dataSRC.getNumItems(); i++) {
			// for all other item, j
			for (int j = 1; j <= i; j++) {
				// for every user u expressing preference for both i and j
				for (Entry<Integer, Integer> entry : (dataSRC.getRatings())
						.get(j).entrySet()) {
					userJ = entry.getKey();
					ratingJ = entry.getValue();

					if (dataSRC.getRatings().get(i).containsKey(userJ)) {
						if (isWeighted) {
							weight++;
						}
						if (i != j) {
							userI = userJ;

							ratingI = dataSRC.getRatings().get(i).get(userI);

							dev = ratingJ - ratingI;
							sum += dev;
							countSim++;
						}
					}
				}

				if (i != j) {
					// add the difference in u’s preference for i and j to an
					// average
					average = ((double) sum / (double) countSim);

					innerHashMapAVG = itemAVGDiffMatrix.get(i);

					if (innerHashMapAVG == null) {
						innerHashMapAVG = new HashMap<Integer, Double>();
					}
				}

				if (isWeighted) {
					innerHashMapWeight = itemItemWeightMatrix.get(i);
					if (innerHashMapWeight == null) {
						innerHashMapWeight = new HashMap<Integer, Integer>();
						itemItemWeightMatrix.put(i, innerHashMapWeight);
					}
					innerHashMapWeight.put(j, weight);
					weight = 0;
				}

				if (i != j) {
					innerHashMapAVG.put(j, average);

					// Put the deviation average in a matrix for the items
					itemAVGDiffMatrix.put(i, innerHashMapAVG);

					countSim = 0;
					sum = 0;
				}
			}
		}
	}

	public double getItemPairAverageDiff(Integer i, Integer j) {
		HashMap<Integer, Double> outerHashMapI = itemAVGDiffMatrix.get(i);
		HashMap<Integer, Double> outerHashMapJ = itemAVGDiffMatrix.get(j);

		double avgDiff = 0.0;

		if (outerHashMapI != null && !outerHashMapI.isEmpty()
				&& outerHashMapI.containsKey(j)) {
			// If itemI < itemJ return the item else return the negation
			if (i < j) {
				avgDiff = -outerHashMapI.get(j);
			} else {
				avgDiff = outerHashMapI.get(j);
			}
		} else if (outerHashMapJ != null && !outerHashMapJ.isEmpty()
				&& outerHashMapJ.containsKey(i)) {
			if (i < j) {
				avgDiff = -outerHashMapJ.get(i);
			} else {
				avgDiff = outerHashMapJ.get(i);
			}
		}

		// If none of the cases applies above, the average difference is 0
		return avgDiff;
	}

	/*
	 * Returns the weight between items i and j
	 */
	public int getWeight(Integer i, Integer j) {
		HashMap<Integer, Integer> outerHashMap = itemItemWeightMatrix.get(i);

		int weight = 0;

		if (outerHashMap != null && !outerHashMap.isEmpty()
				&& outerHashMap.containsKey(j)) {
			weight = outerHashMap.get(j);

		} else {
			outerHashMap = itemItemWeightMatrix.get(j);
			if (outerHashMap != null && !outerHashMap.isEmpty()
					&& outerHashMap.containsKey(i)) {
				weight = outerHashMap.get(i);
			}
		}
		return weight;
	}
}
