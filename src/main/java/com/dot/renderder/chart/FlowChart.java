package com.dot.renderder.chart;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FlowChart {

    private List<ChartBox> boxes = new ArrayList<>();
    private List<BoxLink> links = new ArrayList<>();

    public List<ChartBox> boxes() {
        return new ArrayList<>(boxes);
    }

    public void add(ChartBox box) {
        ChartBox existingBox = get(box.getSymbol());
        if(existingBox != null) {
            this.boxes.remove(existingBox);
        }
        this.boxes.add(box);

    }

    public void addLink(Symbol fromSymbol, Symbol toSymbol, LinkType link) {
        this.links.add(new BoxLink(fromSymbol, toSymbol, link));
    }

    private ChartBox get(Symbol symbol) {
        return this.boxes.stream()
                .filter(x -> x.getSymbol().equals(symbol))
                .findFirst().orElseGet(() -> create(symbol));
    }

    private ChartBox create(Symbol symbol) {
        ChartBox chartBox = new ChartBox(symbol, null);
        this.boxes.add(chartBox);
        return chartBox;
    }

    public List<ChartBox> neighborsOf(Symbol symbol) {
        return this.links.stream()
                .filter(link -> isContaining(symbol, link))
                .map(link -> link.getFrom().equals(symbol) ? get(link.getTo()) : get(link.getFrom()))
                .collect(Collectors.toList());
    }

    private boolean isContaining(Symbol symbol, BoxLink link) {
        return link.getFrom().equals(symbol) || link.getTo().equals(symbol);
    }

    public List<BoxLink> links() {
        return new ArrayList<>(links);
    }
}
