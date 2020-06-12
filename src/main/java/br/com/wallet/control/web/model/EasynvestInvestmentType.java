package br.com.wallet.control.web.model;

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
public class EasynvestInvestmentType {
	
	private Long id;
	
	private String description;
	
	private String color;

}
