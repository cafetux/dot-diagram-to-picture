package com.dot.renderder;

import com.dot.renderder.chart.ChartBox;
import com.dot.renderder.chart.FlowChart;
import com.dot.renderder.chart.LinkType;
import com.dot.renderder.chart.Symbol;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DotDiagramParser {

    private static final String nodeDeclarationRegexp = "^[\\s]*([A-Za-z0-9]{1,3})(\\[label=\\\"([a-zA-Z0-9-_]*)\\\"\\])?;?\n?$";
    private static final String dependanceDeclarationRegexp = "^[\\s\\t]*([A-Za-z0-9]{1,3})([ ->]*)([A-Za-z0-9]{1,3});?$";

    private Pattern nodeDeclarationPattern = Pattern.compile(nodeDeclarationRegexp);
    private Pattern linksDeclarationPattern = Pattern.compile(dependanceDeclarationRegexp);

    public FlowChart parse(Path file) {

        List<String> lines = read(file);
        if(lines.isEmpty() || !lines.get(0).contains("graph ")) {
            throw new IllegalStateException("invalid diagram file");
        }
        FlowChart flowChart = new FlowChart();
        for (String line : lines) {
            Matcher n = nodeDeclarationPattern.matcher(line);
            if(n.matches()) {
                String symbol = n.group(1);
                String label = n.group(3);
                flowChart.add(new ChartBox(symbol, label));
            }
            Matcher d = linksDeclarationPattern.matcher(line);
            if(d.matches()) {
                String first = d.group(1);
                String second = d.group(3);
                flowChart.addLink(new Symbol(first), new Symbol(second), LinkType.from(d.group(2)));
            }
        }
        return flowChart;
    }

    private List<String> read(Path file) {
        try {
            return Files.readAllLines(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
