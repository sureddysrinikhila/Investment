package com.dandi.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dandi.api.model.InvestmentOptions;
import com.dandi.api.model.InvestmentResponse;
import com.dandi.api.model.InvestmentSector;
import com.dandi.api.model.Prediction;

@RestController
@RequestMapping(value="/PredictionService")
public class PredictionController {

	@Autowired
	private UserRepository userRepository;
	
	@Value("${tomcat.path}")
	private String tomcatPath;
	
	@Value("${historic-data.path}")
	private String historicDataFolderPath;
	
	@Value("${static-pdf.url}")
	private String staticPDFURL;
	
	@Value("${anaconda-bootstrap-script.path}")
	private String anacondaBootStrapScript;
	
	@RequestMapping(method = RequestMethod.POST,value="/predict")
	public InvestmentResponse predict(@RequestBody InvestmentOptions predict){
	
		InvestmentResponse investmentResponse = new InvestmentResponse();
		
		List<Prediction> predictions = new ArrayList<>();
		
		String historicDataFileName;
		int timePeriod;
		BigDecimal investmentAmount;
		
		for(InvestmentSector sector:predict.getInvestmentSectors()) {
			
			historicDataFileName = historicDataFolderPath + sector.getSector() + ".csv";
			timePeriod = sector.getTimePeriod();
			investmentAmount = sector.getInvestmentAmount();
		
			try {
				String [] args = new String[5];
				
				args[0] = anacondaBootStrapScript;
				args[1] = historicDataFileName;
				args[2] = String.valueOf(timePeriod * 365);
				args[3] = tomcatPath + predict.getUserName() + "_" + sector.getSector() + ".pdf";
				args[4] = String.valueOf(investmentAmount);
				String fileName = predict.getUserName() + "_" + sector.getSector() + ".pdf";
				
				Process process = Runtime.getRuntime().exec(args); 
				
				int exitVal = process.waitFor();
				
				StringBuilder output = new StringBuilder();
				
				BufferedReader reader = new BufferedReader(
											(new InputStreamReader(
													process.getInputStream())));
				
				String line;
				
				while((line = reader.readLine()) != null)
					output.append(line + "\n");
				
				
				if(exitVal == 0) {
					Prediction prediction = new Prediction();
					prediction.setPdfURL(staticPDFURL + fileName);
					prediction.setSectorName(sector.getSector());
					predictions.add(prediction);
				}else {
				}
				
			}catch(IOException ex) {
				System.out.println(ex.getMessage());
			}catch(InterruptedException iex) {
				System.out.println(iex.getMessage());
			}

			investmentResponse.setPredictions(predictions);
			investmentResponse.setUserName(predict.getUserName());

		}
		
				
		return investmentResponse;
	}


}
