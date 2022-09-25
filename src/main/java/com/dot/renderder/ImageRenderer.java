package com.dot.renderder;

import com.dot.renderder.chart.*;
import com.mindfusion.diagramming.*;
import com.mindfusion.diagramming.Shape;
import com.mindfusion.drawing.*;

import javax.imageio.*;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static java.lang.Math.max;
import static javax.print.attribute.ResolutionSyntax.DPI;

public class ImageRenderer {


    private static final double INCH_2_CM = 5.08;
    public static final SolidBrush SOLID_BRUSH = new SolidBrush(Color.WHITE);
    public static final Font ARIAL_BOLD_4 = new Font("Arial", Font.BOLD, 4);

    public void render(FlowChart toRender, String filename) throws IOException {

        Diagram diagram = new Diagram();
        diagram.setBounds(new Rectangle2D.Float(0, 0, 1000, 600));
        diagram.setMeasureUnit(GraphicsUnit.Millimeter);
        diagram.setBackBrush(new SolidBrush(Color.white));

        for (ChartBox box : toRender.boxes()) {
            ShapeNode startNode = createNode(diagram, box);
            diagram.add(startNode);
        }

        for (BoxLink link : toRender.links()) {
            createLink(diagram, link);
        }

        arrangeDiagram(diagram);

        diagram.resizeToFitItems(5);

        save(diagram, filename);
    }

    /**
     * organize nodes and improve lisibility
     */
    private void arrangeDiagram(Diagram diagram) {
        LayeredLayout layout = new LayeredLayout();
        layout.setAnchoring(Anchoring.Reassign);
        layout.setEnforceLinkFlow(true);
        layout.setStraightenLongLinks(true);
        layout.setNodeDistance(10);
        layout.setLayerDistance(15);
        layout.arrange(diagram);
    }

    private ShapeNode createNode(Diagram diagram, ChartBox box) {
        double size = max(20, (labelSize(box) / 5) * 20);
        ShapeNode startNode = diagram.getFactory().createShapeNode(0, 0, size, 10);
        startNode.setBrush(SOLID_BRUSH);
        startNode.setFont(ARIAL_BOLD_4);
        if (box.getLabel() != null) {
            startNode.setText(box.getLabel());
        } else {
            startNode.setText(box.getSymbol().get());
        }
        startNode.setId(box.getSymbol().get());
        startNode.setShape(Shape.fromId("Ellipse"));
        startNode.setPen(new Pen(Color.black, 1));
        return startNode;
    }

    private void createLink(Diagram diagram, BoxLink link) {
        DiagramLink diagramLink = diagram.getFactory().createDiagramLink(getNode(diagram, link.getFrom()), getNode(diagram, link.getTo()));
        diagramLink.setBrush(new SolidBrush(Color.BLACK));
        diagramLink.setShadowBrush(Brushes.Transparent);
        diagramLink.setIntermediateShapeSize(50);
        if (link.getType() == LinkType.SIMPLE) {
            diagramLink.setHeadShapeSize(0);
        }
    }

    private double labelSize(ChartBox box) {
        return Optional.ofNullable(box)
                .map(ChartBox::getLabel)
                .map(String::length)
                .orElse(box.getSymbol().get().length());
    }

    private DiagramNode getNode(Diagram diagram, Symbol nodeId) {
        return diagram.getNodes().stream().filter(node -> node.getId().equals(nodeId.get())).findAny().get();
    }

    private void save(Diagram diagram, String filename) {

        File output = new File(filename + ".png");

        try (ImageOutputStream stream = ImageIO.createImageOutputStream(output)) {
            ImageWriter writer = ImageIO.getImageWritersByFormatName("png").next();
            ImageWriteParam writeParam = writer.getDefaultWriteParam();
            ImageTypeSpecifier typeSpecifier = ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_INT_RGB);
            IIOMetadata metadata = writer.getDefaultImageMetadata(typeSpecifier, writeParam);

            setDPI(metadata);

            writer.setOutput(stream);
            writer.write(metadata, new IIOImage(diagram.createImage(), null, metadata), writeParam);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setDPI(IIOMetadata metadata) throws IIOInvalidTreeException {

        // for PMG, it's dots per millimeter
        double dotsPerMilli = 1.0 * DPI / 10 / INCH_2_CM;

        IIOMetadataNode horiz = new IIOMetadataNode("HorizontalPixelSize");
        horiz.setAttribute("value", Double.toString(dotsPerMilli));

        IIOMetadataNode vert = new IIOMetadataNode("VerticalPixelSize");
        vert.setAttribute("value", Double.toString(dotsPerMilli));

        IIOMetadataNode dim = new IIOMetadataNode("Dimension");
        dim.appendChild(horiz);
        dim.appendChild(vert);

        IIOMetadataNode root = new IIOMetadataNode("javax_imageio_1.0");
        root.appendChild(dim);

        metadata.mergeTree("javax_imageio_1.0", root);
    }

}
