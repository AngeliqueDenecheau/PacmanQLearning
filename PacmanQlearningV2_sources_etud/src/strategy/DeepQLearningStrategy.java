package strategy;

import java.util.ArrayList;
import java.util.List;

import agent.Agent;
import agent.AgentAction;

import agent.PositionAgent;
import agent.typeAgent;
import motor.Maze;
import motor.PacmanGame;
import neuralNetwork.NeuralNetWorkDL4J;

import neuralNetwork.TrainExample;

import java.util.Random;


public class DeepQLearningStrategy extends QLearningStrategy {


	int nEpochs;
	int batchSize;
	int range;
	
	
	public DeepQLearningStrategy(double epsilon, double gamma, double alpha, int range, int nEpochs, int batchSize) {
		
		
		super(epsilon, gamma, alpha);
		
		this.nEpochs = nEpochs;
		this.batchSize = batchSize;
		this.range = range;
		
	}
	
	
	@Override
	public void update(PacmanGame state, PacmanGame nextState, AgentAction action, double reward,
			boolean isFinalState) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public AgentAction play(PacmanGame state) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void learn(ArrayList<TrainExample> trainExamples) {
		// TODO Auto-generated method stub
		
	}
	

}
