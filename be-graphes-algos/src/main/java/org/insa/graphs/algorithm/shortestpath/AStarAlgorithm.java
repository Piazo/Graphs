package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.AbstractInputData.Mode;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.LabelAStar;
import org.insa.graphs.model.Label;
import org.insa.graphs.model.Node;

public class AStarAlgorithm extends DijkstraAlgorithm {

	@Override
	protected void insert(BinaryHeap<Label> BH, Node noeud, boolean isMarked, float cout, Arc pere, ShortestPathData data) {
    	Label label;
    	float cout_estime;
    	double max_speed = Math.max(data.getMaximumSpeed(), data.getGraph().getGraphInformation().getMaximumSpeed());
    	if (data.getMode() == Mode.LENGTH) {
    		cout_estime = (float)noeud.getPoint().distanceTo(data.getDestination().getPoint());
    	}
    	else {
    		cout_estime = (float)noeud.getPoint().distanceTo(data.getDestination().getPoint())/(float)max_speed;
    	}
		label = new LabelAStar(noeud, isMarked, cout, pere, cout_estime);
		BH.insert(label);
		Label.Labels[noeud.getId()] = label;
		notifyNodeReached(label.getSommet_courrant());
    }
    
    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }    
}
