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
public class StocksAllocationBySector {
	
	private String _id;
	
	private String sectorName;
	
	private float allocationPercentageGoal;
	
	@Transient
	private double allocationValueGoal;
	
	public double calculateAllocationValueGoal(double totalInvestedInStocks) {
		double calculated = totalInvestedInStocks * allocationPercentageGoal;
		setAllocationValueGoal(calculated);
		return calculated;
	}

}
