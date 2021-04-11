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
	
	private double countGhostsAround(int new_x,int new_y, PacmanGame game) {
		double result=0;
		
		//on verifie pour chaque fantôme s'il est dans une des case autour de la position
		for(Agent ghost : game.get_agentsFantom()) {
			if(ghost.get_position().getX()==new_x){
				if((ghost.get_position().getY()==new_y+1)||(ghost.get_position().getY()==new_y-1)||(ghost.get_position().getY()==new_y)) {
					result++;
				}
			} else if((ghost.get_position().getX()==new_x+1) || (ghost.get_position().getX()==new_x-1)) {
				if(ghost.get_position().getY()==new_y) result++;
			} 
		}
		//System.out.println("si je vais à la position ["+new_x+","+new_y+"] je serai entouré de "+result+" fantômes");
		
		return result;
		
	}
	
	@Override
	public void learn(ArrayList<TrainExample> trainExamples) {
		// pas utilisé si pas de reseaux neuronaux
		
	}

	//reprise de l'algorithme disponible dans le AStarStrategy.java pour trouver la pacgomme/la capsule/le fantôme effrayé le plus proche
	private int nbCoupsProchainePacgomme(PacmanGame game,int new_x, int new_y) {
		
		
		ArrayList<Node> currentNodes = new ArrayList<Node>();
		
		Node nodeStart = new Node(null,null,new_x,new_y,0);
		
		currentNodes.add(nodeStart);
		
		ArrayList<Integer> idNodesAlreadyExplored = new ArrayList<Integer>();
		
		boolean notfound = true;
		
		int idNodeStart= nodeStart.id;
		
		while(currentNodes.size() > 0) {
			
			Collections.sort(currentNodes, new ComparatorCoutCroissant());
					
			Node node = currentNodes.get(0);	
			currentNodes.remove(0);
				
			for(int i =0; i < 4; i++) {
				
				AgentAction action = new AgentAction(i);
				
				int newx = node.x + action.get_vx();
				int newy = node.y + action.get_vy();
				
				if(!game.getMaze().isWall(newx, newy)) {
					

					//int heuristicdist = Math.abs(new_x - newx) + Math.abs(new_y - newy);
							
					Node newNode  = new Node(node, action, newx, newy, node.effectiveCost +1);
					
					
					boolean isAlreadyExplored = false;
					for(int j =0; j < idNodesAlreadyExplored.size(); j++) {
						
						if(idNodesAlreadyExplored.get(j) == newNode.id) {
							isAlreadyExplored = true;
							break;
						}
						
					}
					
					if(isAlreadyExplored == false) {
		
						currentNodes.add(newNode);		
						idNodesAlreadyExplored.add(newNode.id);
						if(game.isGhostsScarred()) {
							for(PositionAgent gPos :game.getPostionFantom()) {
								if(game.getMaze().isFood(newx, newy) || game.getMaze().isCapsule(newx, newy) || (gPos.getX()==newx && gPos.getY()==newy)) {
									//System.out.println("objectif en ["+newx+","+newy+"] avec un cout de "+newNode.effectiveCost);
									return newNode.effectiveCost;
								}
							}
						}else {
							if(game.getMaze().isFood(newx, newy) || game.getMaze().isCapsule(newx, newy)) {
								//System.out.println("objectif en ["+newx+","+newy+"] avec un cout de "+newNode.effectiveCost);
								return newNode.effectiveCost;
								
							}
						}
						
					}
						 

				}
								
			}

		
			
		}
		
		return 0;//cas où il n'y a plus d'objectifs accessibles
			
		
	}

	
	
	
	public int manhattan(PositionAgent s, PositionAgent o) {
		return Math.abs(s.getX() - o.getX()) + Math.abs(s.getY() - o.getY());
	}
	
	
	public class Node {
		
		public int x;
		public int y;
		
		//public int heuristicCost;
		public int effectiveCost;
		
		//public int globalCost;
		
		public int id;
		
		public Node parent;
		public AgentAction action;
		
		public Node(Node node, AgentAction action, int x,int y, int effectiveCost) {
			
			this.parent = node;
			this.action = action;
			this.x = x;
			this.y = y;
			this.effectiveCost = effectiveCost;
			//this.heuristicCost = heuristicCost;
			
			//this.globalCost = effectiveCost + heuristicCost;
			this.id = x*100+ y;
		}
		
	}
	
	public class ComparatorCoutCroissant implements Comparator<Node> {
		@Override
		public int compare(Node a, Node b) {
			return a.effectiveCost - b.effectiveCost;
		}
	}
	
	public void afficheFeatures(double[] features,AgentAction action,double value) {
		//affichage de la direction
		String result="direction ";
		switch(action.get_direction()) {
			case(0):result+="NORD ";break;
			case(1):result+="SUD ";break;
			case(2):result+="EST ";break;
			case(3):result+="OUEST ";break;
			default: result+="NONE ";break;
		}
		//affichage des features
		result+="mes features sont ["+features[0];
		for(int i=1; i<features.length;i++) {
			result+=","+features[i];
		}
		System.out.println(result+"] de valeur "+value);
	}

}
