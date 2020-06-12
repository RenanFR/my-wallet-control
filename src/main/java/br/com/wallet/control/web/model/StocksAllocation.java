package br.com.wallet.control.web.model;

import org.springframework.data.annotation.Transient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StocksAllocation {
	
	private String _id;
	
	private String ticker;
	
	private int numberOfStocks;
	
	@Transient
	private double currentPrice;
	
	@Transient
	private double allocationValue;
	
	private String sector;
	
	@Transient
	private double percentageScoreOfSectorGoal;
	
	@Transient
	private double differenceValueToSectorGoal;
	
	public double calculateAllocationValue() {
		double calculated = numberOfStocks * currentPrice;
		setAllocationValue(calculated);
		return calculated;
	}	
	
	public double calculatePercentageScoreOfSectorGoal(double allocationValueGoalBySector) {
		double calculated = allocationValue / allocationValueGoalBySector;
		setPercentageScoreOfSectorGoal(calculated);
		return calculated;
	}	
	
	public double calculateDifferenceValueToSectorGoal(double allocationValueGoalBySector) {
		double calculated = allocationValue - allocationValueGoalBySector;
		setDifferenceValueToSectorGoal(calculated);
		return calculated;
	}	
	
}
