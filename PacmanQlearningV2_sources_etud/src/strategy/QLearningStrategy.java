package strategy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import agent.Agent;
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
	
	//reprise de l'algorithme disponible dans le AStarStrategy.java pour trouver la pacgomme/la capsule/le fantôme effrayé le plus proche
	protected int nbCoupsProchainePacgomme(PacmanGame game,int new_x, int new_y) {
		
		
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
		

		
	protected double countGhostsAround(int new_x,int new_y, PacmanGame game) {
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
