import java.util.ArrayList;

/**
 * Predicts all users and all movies of a certain DataSource. Evaluates the
 * predictions with RMSE.
 */
public class Main {
	public static void main(String[] args) {
		DataSource dataSRC = new DataSource();
		SlopeOneMatrix avgDiff = new SlopeOneMatrix(dataSRC, false);
		SlopeOneRecommender slopeOne = new SlopeOneRecommender(dataSRC, false, avgDiff);
		RMSE rmse = new RMSE();
		double prediction = 0.0;
		double rating = 0.0;
		ArrayList<Double> predictions = new ArrayList<Double>();
		ArrayList<Double> ratings = new ArrayList<Double>();

		// Iterate all users
		for (int userID : dataSRC.getUsers()) {

			// Iterate all movies
			for (int i = 1; i <= dataSRC.getNumItems(); i++) {

				// Get a prediction
				prediction = slopeOne.recommendOne(userID, i);
				// Get the actual value
				rating = dataSRC.getRating(userID, i);

				// Rating and Prediction is NaN if rating does not exist
				// or if a user only has rated one movie
				if (!Double.isNaN(rating) && !Double.isNaN(prediction)) {
					ratings.add(rating);
					predictions.add(prediction);
				}
			}
		}
		System.out.println();
		System.out.println("RMSE: " + rmse.evaluate(ratings, predictions));
	}

}
