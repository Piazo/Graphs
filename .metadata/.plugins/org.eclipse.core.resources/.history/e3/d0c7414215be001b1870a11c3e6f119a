package org.insa.graphs.algorithm.shortestpath;



import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Label;

import java.util.ArrayList;
import java.util.Collections;

import org.insa.graphs.algorithm.AbstractSolution.Status;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    protected void insert(BinaryHeap<Label> BH, Node n, boolean isMarked, float cout, Arc pere, ShortestPathData data) {
    	Label l;
		l = new Label(n, isMarked, cout, pere);
		BH.insert(l);
		Label.Labels[n.getId()] = l;
		notifyNodeReached(l.getSommet_courrant());
    }
    
    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData();
        ShortestPathSolution solution = null;
        
     // On recupere l'origine
        notifyOriginProcessed(data.getOrigin());
        
        Graph graphe = data.getGraph();
        BinaryHeap<Label> BH = new BinaryHeap<Label>();
        final int nbNodes = graphe.size();
        int Nb_Marked_Nodes = 0;
        
        // initialization
        Label.Labels = new Label[graphe.getNodes().size()];
        Node n = data.getOrigin();
        insert(BH, n, true, (float)0.0, null, data);
        
        //iterations
        while (Nb_Marked_Nodes != nbNodes && !BH.isEmpty()){
        	Label L_Origin = BH.findMin();
        	BH.deleteMin();
        	L_Origin.setMarked(true);
        	Nb_Marked_Nodes++;
        	notifyNodeMarked(L_Origin.getSommet_courrant());
        	if (L_Origin.getSommet_courrant() == data.getDestination()) {
        		break;
        	}
        	for (Arc a : L_Origin.getSommet_courrant().getSuccessors()) {
        		// Small test to check allowed roads...
                if (!data.isAllowed(a)) {
                    continue;
                }
                
        		Node N_Destination = a.getDestination();
				if (Label.Labels[N_Destination.getId()] == null) {
					insert(BH, N_Destination, false, (float) (L_Origin.getCost() + data.getCost(a)), a, data);
				}
				else {
					if (Label.Labels[N_Destination.getId()].isMarked() == false) {
	        			if (Label.Labels[N_Destination.getId()].getCost() > (L_Origin.getCost() + data.getCost(a))) {
	        				BH.remove(Label.Labels[N_Destination.getId()]);        					
	        				Label.Labels[N_Destination.getId()].setCost((float) (L_Origin.getCost() + data.getCost(a)));
	        				Label.Labels[N_Destination.getId()].setPere(a);
	        				BH.insert(Label.Labels[N_Destination.getId()]);
	        			}
	        		}
				}
        		
        	}
        }
        
        //Destination cost is infinite, the solution is infeasible...
        if (Label.Labels[data.getDestination().getId()] == null || !Label.Labels[data.getDestination().getId()].isMarked()) {
        	solution = new ShortestPathSolution(data, Status.INFEASIBLE);
        }
        else {
        	// The destination has been found, notify the observers.
        	notifyDestinationReached(data.getDestination());
           
        	// Create the path from the array of predecessors...
        	ArrayList<Arc> arcs = new ArrayList<>();
        	Arc arc = Label.Labels[data.getDestination().getId()].getPere();
        	while (arc != null) {
        		arcs.add(arc);
        		arc = Label.Labels[arc.getOrigin().getId()].getPere();
        	}
         
        	// Reverse the path...
        	Collections.reverse(arcs);
        	
        	// Create the final solution.
        	if (arcs.size() == 0) {
        		solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(graphe, data.getOrigin()));
        	}
        	else {
        		solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(graphe, arcs));
        	}
        }
        
        
        return solution;
    }

}
