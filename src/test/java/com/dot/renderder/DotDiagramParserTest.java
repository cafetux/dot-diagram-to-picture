package com.dot.renderder;

import com.dot.renderder.chart.FlowChart;
import com.dot.renderder.chart.LinkType;
import com.dot.renderder.chart.Symbol;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

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

        assertThat(result.neighborsOf(new Symbol("A"))).hasSize(1);
        assertThat(result.links().get(0).getType()).isEqualTo(LinkType.SIMPLE);

    }

    @Test
    void should_map_another_diagram() {
        Path path = resourceDirectory.resolve("five-dependency.gv");
        FlowChart result = parser.parse(path);
        assertThat(result.boxes()).hasSize(5);
        assertThat(result.boxes().get(0).getSymbol().get()).isEqualTo("A");
        assertThat(result.boxes().get(0).getLabel()).isEqualTo("web");

        assertThat(result.boxes().get(1).getSymbol().get()).isEqualTo("B");
        assertThat(result.boxes().get(1).getLabel()).isEqualTo("service-orders");

        assertThat(result.boxes().get(2).getSymbol().get()).isEqualTo("C");
        assertThat(result.boxes().get(2).getLabel()).isEqualTo("service-invoice");

        assertThat(result.boxes().get(3).getSymbol().get()).isEqualTo("D");
        assertThat(result.boxes().get(3).getLabel()).isEqualTo("common");

        assertThat(result.boxes().get(4).getSymbol().get()).isEqualTo("E");
        assertThat(result.boxes().get(4).getLabel()).isEqualTo("database");

        assertThat(result.neighborsOf(new Symbol("A"))).hasSize(3);
        assertThat(result.neighborsOf(new Symbol("B"))).hasSize(3);
        assertThat(result.neighborsOf(new Symbol("C"))).hasSize(4);
        assertThat(result.neighborsOf(new Symbol("D"))).hasSize(3);
        assertThat(result.neighborsOf(new Symbol("E"))).hasSize(2);

        assertThat(result.links().get(0).getType()).isEqualTo(LinkType.ARROW);
    }

    @Test
    void should_cannot_parse_file_without_graph(){
        Path path = resourceDirectory.resolve("wrong-format.gv");
        assertThrows(IllegalStateException.class, () -> parser.parse(path));
    }
}