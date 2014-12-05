package org.worldbank.seg_2g.worldbankapp;

import java.util.ArrayList;

import android.content.Context;

public class HTMLGenerator {

	public HTMLGenerator(Context context) {
		DataBank.initialise(context);
	}

	public String getNoDataCode() {
		return "<html><head></head><body><br><br><br><br><br><center><h1>Please select an attribute</h1></center></body></html>";
	}

	public String getLoadingCode() {
		return    "<html><body>\n"
				+ "<br><br><br>\n"
				+ "<center><h1>Fetching data</h1>"
				+ "<h1><span id=\"wait\">.</span></h1>"
				+ "<h1>Please wait</h1></center>\n"
				+ "\n" + "<script>\n"
				+ "var dots = window.setInterval( function() {\n"
				+ "    var wait = document.getElementById(\"wait\");\n"
				+ "    if ( wait.innerHTML.length > 3 ) \n"
				+ "        wait.innerHTML = \".\";\n" + "    else \n"
				+ "        wait.innerHTML += \".\";\n" + "    }, 1000);\n"
				+ "</script>\n" + "\n" + "</body></html>\n";
	}

	public String getHTMLCode(int queryCode, int year) {

		// TODO check for internet connection

		String html = "";

		html = getHTMLBeginning(Settings.Attributes[queryCode])
				+ getCountryValueBlock(queryCode, year) + getHTMLEnding();

		return html;
	}

	private String getHTMLBeginning(String attribute) {
		String s = "<html>\n"
				+ "  <head>\n"
				+ "    <script type=\"text/javascript\" src=\"https://www.google.com/jsapi\"></script>\n"
				+ "    <script type=\"text/javascript\">\n"
				+ "      google.load(\"visualization\", \"1\", {packages:[\"geochart\"]});\n"
				+ "      google.setOnLoadCallback(drawRegionsMap);\n"
				+ "\n"
				+ "      function drawRegionsMap() {\n"
				+ "\n"
				+ "        var data = google.visualization.arrayToDataTable([\n"
				+ "          ['Country', '" + attribute + "'],\n";
		return s;
	}

	private String getHTMLEnding() {
		String s = "        ]);\n"
				+ "\n"
				+ "        var options = {\n"
				+ "          colorAxis: {colors: ['green', 'red']},\n"
				+ "          datalessRegionColor: 'lightgray'\n"
				+ "};\n"
				+ "\n"
				+ "        var chart = new google.visualization.GeoChart(document.getElementById('regions_div'));\n"
				+ "\n"
				+ "        chart.draw(data, options);\n"
				+ "      }\n"
				+ "    </script>\n"
				+ "  </head>\n"
				+ "  <body>\n"
				+ "    <div id=\"regions_div\" style=\"width: 900px; height: 500px;\"></div>\n"
				+ "  </body>\n" + "</html>\n";

		return s;
	}

	private String getCountryValueBlock(int queryCode, int year) {

		String returnString = "";
		DataBank.fetchData(queryCode);
		ArrayList<Country> arrl = DataBank.getCountryArrayList();
		for (int i = 0; i < arrl.size(); i++) {
			Country country = arrl.get(i);
			double value = DataBank.getValuesFor(country, queryCode, year);
			returnString += getCountryAttributeLine(country, value);
		}
		return returnString;
	}

	private String getCountryAttributeLine(Country country, double value) {
		if (value > 0)
			return "          ['" + country.getName() + "', " + value + "],\n";
		else
			return "";
	}

}
