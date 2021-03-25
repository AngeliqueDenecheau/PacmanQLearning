package main;

import controller.GameController;
import motor.PacmanGame;
import strategy.ApproximateQLearningStrategy;
import strategy.Strategy;
import view.View;

public class Main_normalMode {

	public static void main(String[] args) {
		
		boolean nightmareMode = false;
		
		
		double gamma = 0.95;
		double epsilon = 0.1;
		double alpha = 0.1;
		
		
		Strategy strat = new ApproximateQLearningStrategy(epsilon, gamma, alpha);
	
		String chemin_maze = "src/layout/originalClassic.lay";
		
		PacmanGame _motor = new PacmanGame(chemin_maze, 1000, (long) 100);
		_motor.initGameQLearning(strat, nightmareMode);
		
		GameController controller = GameController.getInstance(_motor);
		
		@SuppressWarnings("unused")
		View _view = View.getInstance(controller, _motor, false);

	}
	
	
}
