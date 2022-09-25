package com.dot.renderder.chart;

import java.util.Objects;

public class BoxLink {

    private final Symbol from;
    private final Symbol to;
    private final LinkType type;

    public BoxLink(Symbol from, Symbol to, LinkType type) {
        this.from = from;
        this.to = to;
        this.type = type;
    }

    public Symbol getFrom() {
        return from;
    }

    public Symbol getTo() {
        return to;
    }

    public LinkType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoxLink boxLink = (BoxLink) o;
        return Objects.equals(from, boxLink.from) && Objects.equals(to, boxLink.to) && type == boxLink.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, type);
    }

    @Override
    public String toString() {
        return "BoxLink{" +
                "symbolFrom='" + from + '\'' +
                ", symbolTo='" + to + '\'' +
                ", type=" + type +
                '}';
    }
}
