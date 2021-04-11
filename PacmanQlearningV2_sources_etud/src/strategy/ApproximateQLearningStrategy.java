package strategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import agent.Agent;
import agent.AgentAction;
import agent.PositionAgent;
import motor.Maze;
import motor.PacmanGame;
import neuralNetwork.TrainExample;

import java.util.Random;


public class ApproximateQLearningStrategy extends QLearningStrategy{

	
	double[] weights;
	int d;
	
	public ApproximateQLearningStrategy(double epsilon, double gamma, double alpha) {
		super(epsilon, gamma, alpha);
		
		this.d = 5;	
		weights = new double[d+1];
		Random r = new Random();
		
		for(int i = 0; i< weights.length; i++) {
			
			weights[i] = r.nextGaussian()/10;
		}
		
	}


	@Override
	public AgentAction play(PacmanGame state) {
		System.out.println("---------------new turn-----------------");
		ArrayList<AgentAction> legalActions = new ArrayList<AgentAction>();
		
		Maze maze = state.getMaze();
		
		AgentAction actionChoosen = new AgentAction(0);
		
		
		for(int i =0; i < 4; i++) {
			
			AgentAction action = new AgentAction(i);
			
			if(!maze.isWall(state.pacman.get_position().getX() + action.get_vx(),
					state.pacman.get_position().getY() + action.get_vy())) {
				
				legalActions.add(action);
			}
			
		}
		
		
		if(Math.random() < this.epsilon){
			
			actionChoosen = legalActions.get((int) Math.floor(Math.random() * legalActions.size()));
					
		} else {
			
			double maxQvalue = -9999;
			
			int trouve = 1;
			
			for(AgentAction action : legalActions) {
				
				double[] features = getFeatures(state, action);
				double qValue = perceptron(weights, features);
				afficheFeatures(features,action,qValue);
				
				if(qValue > maxQvalue) {
					
					maxQvalue = qValue;
					actionChoosen = action;
					trouve = 1;
					
					
					
				} else if(qValue == maxQvalue) {
					
					trouve += 1;
					
					if(Math.floor(trouve*Math.random())== 0) {
						maxQvalue = qValue;
						actionChoosen = action;
					}
					
				}
				
			}
			
		}
		
		
		return actionChoosen;
		
	}

	
	public double perceptron(double[] weights, double[] features) {
		
		double results = 0;
		
		for(int i =0; i < weights.length; i++) {
		
			results += weights[i]*features[i];
			
		}
		
		return results;
	}
	
	
	private double[] getFeatures(PacmanGame state, AgentAction action) {
		
		
		double[] features = new double[d+1];
		
		features[0] = 1;
		
		Maze maze = state.getMaze();
		
		int x = state.pacman._position.getX();
		int y = state.pacman._position.getY();
		
		int new_x = x + action.get_vx();
		int new_y = y + action.get_vy();
		
		/* 1 : {0,1} si mange pacgomme
		 * 2: {0,1} si mange capsule 
		 * 3 :{int} nb_fantômes autour de la case cible quand invulnérable
		 * 4: {int} nb_fantômes autour de la case cible quand vulnérable
		 * 5: {int} nb_coups avant prochain objectif
		 * 6: {int} nb_tours restants à l'invincibilité
		 * 
		 * nb_coups pour atteindre la prochaine pacgomme et nb_coups pour la prochaine capsule séparés ?
		 */
		
		if(maze.isFood(new_x, y + action.get_vy())) {
			features[1] = 1;
		}
		
		if(maze.isCapsule(new_x, new_y)) {
			features[2]=1;
		}
		
		if(state.getNbTourInvincible()>1) { // nb fantômes quand invincible
			features[3]=countGhostsAround(new_x,new_y,state);
		}else{ //nb fantômes quand vulnérable
			features[4]=countGhostsAround(new_x,new_y,state);
		}
		
		features[5]=nbCoupsProchainePacgomme(state,new_x,new_y);
		
		
		//features[6]=state.getNbTourInvincible();
		//System.out.println("je serai invincible encore "+features[6]+"tours");
		
		return features;
		
		
	}
	
	
	@Override
	public void update(PacmanGame state, PacmanGame nextState, AgentAction action, double reward, boolean isFinalState) {
		
		
		double targetQ;

		if(isFinalState) {
			
			targetQ = reward;
			
		} else {
			
			double maxQnext = getMaxQNext(nextState);
			
			targetQ = reward + this.gamma*maxQnext;
			
		}
		
		double[] features = getFeatures(state, action);	
		double qValue = perceptron(weights, features);
		
		
		for(int i =0; i < this.weights.length; i ++) {
			
			this.weights[i] = this.weights[i] - 2*this.alpha*features[i]*(qValue - targetQ);
			
		}
		
		
	}

	
	public double getMaxQNext(PacmanGame game) {
		
		PositionAgent nextPos = game.pacman._position;
		Maze maze = game.getMaze();
		
		double maxQvalue = -99999;
		
		for(int i =0; i < 4; i++) {
			
				
			AgentAction action = new AgentAction(i);
			if(!maze.isWall(nextPos.getX() + action.get_vx(),
					nextPos.getY() + action.get_vy())) {
				
				double[] features = getFeatures( game, action);
				double qValue = perceptron(weights, features);
				
				if(qValue > maxQvalue) {
					
					maxQvalue = qValue;
					
				}
		
			}
			
		}
		
		return maxQvalue;
		
	}
	
	@Override
	public void learn(ArrayList<TrainExample> trainExamples) {
		// pas utilisé si pas de reseaux neuronaux
		
	}

	

}
