package com.dot.renderder;

import com.dot.renderder.chart.*;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DotDiagramParserTest {

    private DotDiagramParser parser = new DotDiagramParser();
    private Path resourceDirectory = Paths.get("src","test","resources","example");

    @Test
    void should_parse_alone_box(){
        Path path = resourceDirectory.resolve("one-box.gv");
        FlowChart result = parser.parse(path);
        assertThat(result.boxes()).hasSize(1);
        assertThat(result.boxes().get(0).getSymbol().get()).isEqualTo("A");
        assertThat(result.boxes().get(0).getLabel()).isEqualTo("service-orders");
    }

    @Test
    void should_parse_small_diagram(){
        Path path = resourceDirectory.resolve("very-simple-diagram.gv");
        FlowChart result = parser.parse(path);
        assertThat(result.boxes()).hasSize(2);
        assertThat(result.boxes().get(0).getSymbol().get()).isEqualTo("A");
        assertThat(result.boxes().get(0).getLabel()).isEqualTo("service-orders");

        assertThat(result.boxes().get(1).getSymbol().get()).isEqualTo("B");
        assertThat(result.boxes().get(1).getLabel()).isNull();

        assertThat(linksOf(result,"A")).hasSize(1);
        assertThat(result.links().get(0)).isEqualTo(boxLink("A", "B", LinkType.SIMPLE));

    }

    private BoxLink boxLink(String symbol1, String symbol2, LinkType linkType) {
        return new BoxLink(new Symbol(symbol1), new Symbol(symbol2), linkType);
    }

    private ChartBox box(String symmbol, String label) {
        return new ChartBox(new Symbol(symmbol), label);
    }
    @Test
    void should_map_another_diagram() {
        Path path = resourceDirectory.resolve("five-dependency.gv");
        FlowChart result = parser.parse(path);
        assertThat(result.boxes()).hasSize(5);

        assertThat(result.boxes()).contains(
                box("A", "web"),
                box("B", "service-orders"),
                box("C", "service-invoice"),
                box("D", "common"),
                box("E", "database")
                );

        assertThat(result.links()).hasSize(8);

        assertThat(linksOf(result,"A")).hasSize(3);
        assertThat(linksOf(result,"B")).hasSize(3);
        assertThat(linksOf(result,"C")).hasSize(4);
        assertThat(linksOf(result,"D")).hasSize(3);
        assertThat(linksOf(result,"E")).hasSize(2);

        assertThat(result.links().get(0).getType()).isEqualTo(LinkType.ARROW);
    }

    private List<BoxLink> linksOf(FlowChart result, String id) {
        Symbol symbol = new Symbol(id);
        return result.links().stream()
                .filter(x-> contains(symbol, x))
                .collect(Collectors.toList());
    }

    private boolean contains(Symbol symbol, BoxLink x) {
        return x.getFrom().equals(symbol) || x.getTo().equals(symbol);
    }

    @Test
    void should_cannot_parse_file_without_graph(){
        Path path = resourceDirectory.resolve("wrong-format.gv");
        assertThrows(IllegalStateException.class, () -> parser.parse(path));
    }
}