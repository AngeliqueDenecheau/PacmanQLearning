package strategy;

import java.io.Serializable;
import java.util.ArrayList;

import agent.AgentAction;
import agent.PositionAgent;
import motor.PacmanGame;
import neuralNetwork.TrainExample;

public abstract class QLearningStrategy implements Strategy{

	protected double epsilon;
	protected double gamma;
	protected double alpha;
	
	private double baseEpsilon;
	
	public ArrayList<TrainExample> trainExamples  = new ArrayList<TrainExample>();
	
	public QLearningStrategy(double epsilon, double gamma, double alpha) {
		
		this.epsilon = epsilon;
		this.gamma = gamma;
		this.alpha = alpha;
		
		this.baseEpsilon = epsilon;
		
	}
	
	
	public AgentAction play(PacmanGame game, PositionAgent positionAgent, PositionAgent objectif) {
		
		return this.play(game);
	}
	
	
	
	
	
	public void modeTest() {
		this.epsilon = 0;
	}
	public void modeTrain() {
		this.epsilon = baseEpsilon;
	}
	
	public abstract AgentAction play(PacmanGame state);
	public abstract void learn(ArrayList<TrainExample> trainExamples);
	
	//public abstract void update(PacmanGame state, PacmanGame nextState, AgentAction action, double reward, boolean isFinalState);
	
	
}
