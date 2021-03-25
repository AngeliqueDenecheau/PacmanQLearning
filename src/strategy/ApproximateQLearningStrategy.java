package strategy;

import java.util.ArrayList;
import agent.AgentAction;
import agent.MemoryAgent;
import agent.PositionAgent;

import motor.PacmanGame;
import neuralNetwork.TrainExample;



public class ApproximateQLearningStrategy extends QLearningStrategy {


	public ApproximateQLearningStrategy(double epsilon, double gamma, double alpha) {
		
		super(epsilon, gamma, alpha);
		
		// A completer
	}
	
	@Override
	public AgentAction jouer(PositionAgent position, PacmanGame game, PositionAgent objectif, MemoryAgent memoryAgent) {
		
		/// A completer

		return new AgentAction((int) Math.floor(Math.random() * 4));
	}
	
	

	public void update(PacmanGame game, PositionAgent newPos, double reward, boolean isFinalState, MemoryAgent memoryAgent ) {
		
		/// A completer

	}
	


	@Override
	public void learn(ArrayList<TrainExample> trainExamples) {
		// TODO Auto-generated method stub
		
	}



}
