/**
 * A Slope One Recommender System. Takes input in constructor: DataSource (see
 * class DataSource), boolean to specify if weighted version is used and
 * SlopeOneMatrix to get the matrices that Slope One uses in the algorithm.
 * 
 */
public class SlopeOneRecommender {
	boolean isWeighted;
	DataSource dataSRC;
	SlopeOneMatrix soMatrix;

	public SlopeOneRecommender(DataSource dataSRC, boolean isWeighted,
			SlopeOneMatrix soMatrix) {
		this.isWeighted = isWeighted;
		this.dataSRC = dataSRC;
		this.soMatrix = soMatrix;

	}

	/*
	 * Predicts one item i for the user u using the Slope One algorithm.
	 */
	public double recommendOne(int u, int i) {
		double difference = 0.0, userRatingSum = 0.0, prediction = 0.0;
		int weight = 0, weightSum = 0, numRatings = 0;

		// For every item j that user u has rated
		for (int j = 1; j <= dataSRC.getNumItems(); j++) {
			if (dataSRC.getRatings().get(j).get(u) != null && i != j) {

				if (isWeighted) {
					// find the weight between j and i
					weight = soMatrix.getWeight(i, j);
					// find the average rating difference between j and i
					difference += soMatrix.getItemPairAverageDiff(j, i)
							* weight;
					// find the sum of ratings for j
					userRatingSum += dataSRC.getRatings().get(j).get(u)
							* weight;
					// calculate the weight sum
					weightSum += weight;

				} else {
					difference += soMatrix.getItemPairAverageDiff(j, i);
					userRatingSum += dataSRC.getRatings().get(j).get(u);
					// calculate the number of ratings u has rated
					numRatings++;
				}
			}

		}

		// calculate the prediction
		if (isWeighted) {
			prediction = (double) ((userRatingSum + difference) / weightSum);

		} else {
			prediction = (double) ((userRatingSum + difference) / numRatings);
		}

		return prediction;
	}

}
