package com.dot.renderder.chart;

import java.util.Objects;

public class ChartBox {

    private final Symbol symbol;
    private final String label;

    public ChartBox(Symbol symbol, String label) {
        this.symbol = symbol;
        this.label = label;
    }
    public ChartBox(String symbol, String label) {
        this.symbol = new Symbol(symbol);
        this.label = label;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChartBox chartBox = (ChartBox) o;
        return Objects.equals(symbol, chartBox.symbol) && Objects.equals(label, chartBox.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, label);
    }

    @Override
    public String toString() {
        return "["+symbol+":"+label+"]";
    }

}
