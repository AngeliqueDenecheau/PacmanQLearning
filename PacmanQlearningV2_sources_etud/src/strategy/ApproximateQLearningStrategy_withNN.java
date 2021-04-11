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
import neuralNetwork.NeuralNetworkBasicJava;
import neuralNetwork.TrainExample;

import java.util.Random;


public class ApproximateQLearningStrategy_withNN extends QLearningStrategy {

	int nEpochs;
	int batchSize;
	NeuralNetWorkDL4J nn;
	int d;
	public ApproximateQLearningStrategy_withNN(double epsilon, double gamma, double alpha,  int nEpochs, int batchSize) {
		super(epsilon, gamma, alpha);
		this.nEpochs = nEpochs;
		this.batchSize = batchSize;
		this.d=6;
		
		this.nn=new NeuralNetWorkDL4J(alpha,0,d+1,1);
		
	}

	
	@Override
	public void update(PacmanGame state, PacmanGame nextState, AgentAction action, double reward,
			boolean isFinalState) {
		
		
		double[] targetQ=new double[1];

		if(isFinalState) {
			
			targetQ[0] = reward;
			
		} else {
			
			double maxQnext = getMaxQNext(nextState);
			targetQ[0] = reward + this.gamma*maxQnext;
			
		}
		
		double[] features = getFeatures(state, action);	
		this.trainExamples.add(new TrainExample(features,targetQ));


	}

	@Override
	public AgentAction play(PacmanGame state) {
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
				double qValue = this.nn.predict(features)[0];
				//afficheFeatures(features,action,qValue);
				
				if(qValue > maxQvalue) {
					
					maxQvalue = qValue;
					actionChoosen = action;
					trouve = 1;
					
					
					
				} else if(qValue == maxQvalue) {
					
					trouve += 1;
					
				//	if(Math.floor(trouve*Math.random())== 0) {
				//		maxQvalue = qValue;
				//		actionChoosen = action;
				//	}
					
				}
				
			}
			
		}
		
		
		return actionChoosen;
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
		 * 3 :{double} nb_fantômes autour de la case cible quand invulnérable
		 * 4: {double} nb_fantômes autour de la case cible quand vulnérable
		 * 5: {double} nb_coups avant prochain objectif
		 * 6: {double} nb_tours restants à l'invincibilité
		 * 7: {double} moyenne des distances entre fantômes et pacman
		 * 
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
		
		features[6]=state.getNbTourInvincible();
		
		//features[7]=distMoyennneFantomes(state,new_x,new_y);
		
		return features;
		
		
	}
	
	@Override
	public void learn(ArrayList<TrainExample> trainExamples) {
		nn.fit(trainExamples, nEpochs, batchSize,alpha);
		
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
				double qValue = this.nn.predict(features)[0];;
				
				if(qValue > maxQvalue) {
					
					maxQvalue = qValue;
					
				}
		
			}
			
		}
		
		return maxQvalue;
		
	}

	double distMoyennneFantomes(PacmanGame game, int newx, int newy) {
		double nb_fant=0;
		double total=0;
		for(PositionAgent gPos:game.getPostionFantom()) {
			nb_fant++;
			total+=manhattan(gPos,new PositionAgent(newx,newy,4));
		}	
		return(total/nb_fant);
	}
	
}
