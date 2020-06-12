package br.com.wallet.control.web.model;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EasynvestProvider implements UserInvestmentsProvider {
	
	@Autowired
	private RestTemplate restTemplate;
	
	private final String EASYNVEST_API = "https://www.easynvest.com.br/api";
	
	private final String CLIENT_ID = "876dab2190464884bf9b092aa1407585";	

	@Override
	public InvestmentPosition getFinancialPositionFromUser() {
		Login login = (Login) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		log.info("FETCHING INVESTMENTS FROM USER {}", login.getEmail());
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
		headers.set("Authorization", "Basic " +  new String(Base64.getEncoder().encode((CLIENT_ID + ":").getBytes())));
		MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
		form.add("grant_type", "password");
		form.add("username", login.getCpf());
		form.add("password", new String(Base64.getDecoder().decode(login.getEasynvestPassword())));
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(form, headers);
		String url = UriComponentsBuilder
			.fromHttpUrl(EASYNVEST_API + "/auth/v2/security/token")
			.toUriString();
		String token = restTemplate.postForEntity(url, request, String.class).getBody();
		HttpHeaders headersHttp = new HttpHeaders();
		headersHttp.set("Authorization", "Bearer " +  token);
		HttpEntity<?> requestPosition = new HttpEntity<>(headersHttp);
		EasynvestPosition easynvestPosition = restTemplate.exchange(EASYNVEST_API + "/samwise/custody-position", HttpMethod.GET, requestPosition, EasynvestPosition.class).getBody();
		return easynvestPosition;
	}

}
