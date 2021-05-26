package org.insa.graphs.model;

public class LabelAStar extends Label {

	private float cout_estime;
	
	public LabelAStar(Node noeud, boolean isMarked, float cout, Arc pere, float cout_estime) {
		super(noeud, isMarked, cout, pere);
		this.cout_estime = cout_estime;
	}
	
	public float getTotalCost() {
		return this.cout+this.cout_estime;
	}
	
}