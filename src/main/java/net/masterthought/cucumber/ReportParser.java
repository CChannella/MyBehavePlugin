package net.masterthought.cucumber;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.masterthought.cucumber.json.Feature;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.MalformedJsonException;

public class ReportParser {

	private final Map<String, List<Feature>> jsonReportFiles;

	public ReportParser(List<String> jsonReportFiles) throws IOException {
		this.jsonReportFiles = parseJsonResults(jsonReportFiles);
	}

	public Map<String, List<Feature>> getFeatures() {
		return jsonReportFiles;
	}

	private Map<String, List<Feature>> parseJsonResults(List<String> jsonReportFiles) throws IOException {
		Map<String, List<Feature>> featureResults = new LinkedHashMap<String, List<Feature>>();
		for (String jsonFile : jsonReportFiles) {
			if (FileUtils.sizeOf(new File(jsonFile)) > 0) {
				try {
					Feature[] features = new Gson().fromJson(new FileReader(jsonFile), Feature[].class);
					//featureResults.put(jsonFile, Arrays.asList(features));
                    Type type = new TypeToken<ArrayList<Map<String, Object>>>() {}.getType();
                    ArrayList<Map<String, Object>> data = new Gson().fromJson(new FileReader(jsonFile), type);
                    //System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(data));
                    //Feature[] features = new Gson().fromJson(new FileReader(jsonFile), type);
                   // Feature[] features =(Feature[])new Gson().fromJson(new FileReader(jsonFile), type);

                    featureResults.put(jsonFile, Arrays.asList(features));

				} catch (JsonSyntaxException e) {
					System.out.println("[WARNING] File " + jsonFile + " is not a valid json report:  " + e.getMessage());
					if (e.getCause() instanceof MalformedJsonException) {
						// malformed json will be handled otherwise silently skip invalid cucumber json report
						throw e;
					}
				}
			}
		}

		return featureResults;
	}
}
