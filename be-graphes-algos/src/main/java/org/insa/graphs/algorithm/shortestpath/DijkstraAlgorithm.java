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

    protected void insert(BinaryHeap<Label> BinHeap, Node noeud, boolean isMarked, float cout, Arc pere, ShortestPathData data) {
    	Label l;
		l = new Label(noeud, isMarked, cout, pere);
		BinHeap.insert(l);
		Label.Labels[noeud.getId()] = l;
		notifyNodeReached(l.getSommet_courrant());
    }
    
    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData();
        ShortestPathSolution solution = null;
        
        // On recupere l'origine
        notifyOriginProcessed(data.getOrigin());
        
        Graph graphe = data.getGraph();
        BinaryHeap<Label> BinHeap = new BinaryHeap<Label>();
        final int nbNodes = graphe.size();
        int Nb_Marked_Nodes = 0;
        
        // On initialise
        Label.Labels = new Label[graphe.getNodes().size()];
        Node noeud = data.getOrigin();
        insert(BinHeap, noeud, true, (float)0.0, null, data);
        
        //iterations
        while (Nb_Marked_Nodes != nbNodes && !BinHeap.isEmpty()){
        	Label L_Origin = BinHeap.findMin();
        	BinHeap.deleteMin();
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
					insert(BinHeap, N_Destination, false, (float) (L_Origin.getCost() + data.getCost(a)), a, data);
				}
				else {
					if (Label.Labels[N_Destination.getId()].isMarked() == false) {
	        			if (Label.Labels[N_Destination.getId()].getCost() > (L_Origin.getCost() + data.getCost(a))) {
	        				BinHeap.remove(Label.Labels[N_Destination.getId()]);        					
	        				Label.Labels[N_Destination.getId()].setCost((float) (L_Origin.getCost() + data.getCost(a)));
	        				Label.Labels[N_Destination.getId()].setPere(a);
	        				BinHeap.insert(Label.Labels[N_Destination.getId()]);
	        			}
	        		}
				}
        		
        	}
        }
        
        //Si le cout est infini, le chemin est infaisable
        if (Label.Labels[data.getDestination().getId()] == null || !Label.Labels[data.getDestination().getId()].isMarked()) {
        	solution = new ShortestPathSolution(data, Status.INFEASIBLE);
        }
        else {
        	//La destination a ete trouvee et on le notifie
        	notifyDestinationReached(data.getDestination());
           
        	// creation du path a partir des predecesseurs
        	ArrayList<Arc> arcs = new ArrayList<>();
        	Arc arc = Label.Labels[data.getDestination().getId()].getPere();
        	while (arc != null) {
        		arcs.add(arc);
        		arc = Label.Labels[arc.getOrigin().getId()].getPere();
        	}
         
        	//inversion du path pour avoir la solution a l'endroit
        	Collections.reverse(arcs);
        	
        	// Solution finale
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
