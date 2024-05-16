package gptgenerator.services;

import java.awt.GridBagConstraints;
import java.awt.Insets;

/**
 * Verwaltet eine Instanz von GridBagConstraints, mit Hilfsmethoden 
 */
public class GbcFactory {
	private int x = 0;
	private int y = 0;

	public GbcBuilder nextRow() {
		x = 0;
		y++;
		GridBagConstraints result = new GridBagConstraints();
		result.gridx = x;
		result.gridy = y;
		return new GbcBuilder(result);
	}

	public GbcBuilder nextCol() {
		x++;
		GridBagConstraints result = new GridBagConstraints();
		result.gridx = x;
		result.gridy = y;
		return new GbcBuilder(result);
	}

	public GridBagConstraints getHorizontal(Insets insets) {
		nextCol();
		GridBagConstraints result = new GridBagConstraints();
		result.gridx = x;
		result.gridy = y;
		result.fill = GridBagConstraints.HORIZONTAL;
		result.weighty = 0.0;
		result.weightx = 0.5;
		result.insets = insets;
		return result;
	}

	public GridBagConstraints getBoth(Insets insets) {
		nextCol();
		GridBagConstraints result = new GridBagConstraints();
		result.gridx = x;
		result.gridy = y;
		result.fill = GridBagConstraints.BOTH;
		result.weighty = 0.5;
		result.weightx = 0.5;
		result.insets = insets;
		return result;
	}

	public GridBagConstraints getNone(Insets insets) {
		nextCol();
		GridBagConstraints result = new GridBagConstraints();
		result.gridx = x;
		result.gridy = y;
		result.fill = GridBagConstraints.NONE;
		result.weighty = 0.0;
		result.weightx = 0.0;
		result.insets = insets;
		return result;
	}
	
	public static class GbcBuilder {
		private GridBagConstraints constraints;
		
		public GbcBuilder(GridBagConstraints constraints) {
			this.constraints = constraints;
		}
		
		public GridBagConstraints get() {
			return constraints;
		}
		
		public GbcBuilder fillNone () {
			constraints.fill = GridBagConstraints.NONE;
			constraints.weighty = 0.0;
			constraints.weightx = 0.0;
			return this;
		}
		
		public GbcBuilder fillBoth () {
			constraints.fill = GridBagConstraints.BOTH;
			constraints.weighty = 0.5;
			constraints.weightx = 0.5;
			return this;
		}
		
		public GbcBuilder fillHorizontal() {
			constraints.fill = GridBagConstraints.HORIZONTAL;
			constraints.weighty = 0.0;
			constraints.weightx = 0.5;
			return this;
		}
		
		public GbcBuilder insets(Insets insets) {
			constraints.insets = insets;
			return this;
		}
		public GbcBuilder gridwidth(int width) {
			constraints.gridwidth = width;
			return this;
		}
		
		
	}

}
