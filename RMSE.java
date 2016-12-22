import java.util.ArrayList;

/**
 * RMSE (a.k.a. RMSD) evaluates an ArrayList of predicted doubles with an
 * ArrayList of the the actual value
 */
public class RMSE {

	public double evaluate(ArrayList<Double> actualVal,
			ArrayList<Double> predictedVal) {
		double rmse = 0.0;
		double diff;
		for (int i = 0; i < actualVal.size(); i++) {
			diff = actualVal.get(i) - predictedVal.get(i);
			rmse += diff * diff;
		}
		rmse = Math.sqrt(rmse / actualVal.size());
		return rmse;
	}

}
