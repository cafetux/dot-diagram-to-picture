package com.dot.renderder;

import com.dot.renderder.chart.FlowChart;
import com.dot.renderder.chart.Symbol;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DotDiagramRendererTest {

    private DotDiagramParser parser = new DotDiagramParser();
    private ImageRenderer renderer = new ImageRenderer();
    private Path resourceDirectory = Paths.get("src","test","resources","example");

    @Test
    public void should_parse_and_render_into_image() throws IOException {
        Path path = resourceDirectory.resolve("huge-dependencies.gv");
        FlowChart result = parser.parse(path);
        renderer.render(result,"target/result");
    }

}