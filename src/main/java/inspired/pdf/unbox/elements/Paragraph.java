package inspired.pdf.unbox.elements;

import inspired.pdf.unbox.*;
import inspired.pdf.unbox.internal.SimpleFont;
import inspired.pdf.unbox.internal.TextWriter;

public class Paragraph implements PdfElement {

    private final static float HEIGHT_UNDEFINED = -1f;

    private final TextWriter textWriter;
    private final String text;

    private Align align = Align.LEFT;
    private VAlign vAlign = VAlign.TOP;
    private float height = HEIGHT_UNDEFINED;

    private Padding padding = Padding.of(4);
    private Margin margin = Margin.none();

    public Paragraph(String text) {
        this(text, SimpleFont.helvetica(8));
    }

    public Paragraph(String text, Font font) {
        this.textWriter = new TextWriter(font);
        this.text = text;
    }

    public Paragraph with(Margin margin) {
        this.margin = margin;
        return this;
    }

    public Paragraph with(Padding padding) {
        this.padding = padding;
        return this;
    }

    public Paragraph align(Align align) {
        this.align = align;
        return this;
    }

    public Paragraph align(VAlign vAlign) {
        this.vAlign = vAlign;
        return this;
    }

    public Paragraph align(Align align, VAlign vAlign) {
        this.align = align;
        this.vAlign = vAlign;
        return this;
    }

    public Paragraph withHeight(float height) {
        this.height = height;
        return this;
    }

    @Override
    public float render(LinearPDFWriter writer, Bounds viewPort)  {
        Bounds bounds = effectiveBounds(viewPort);
        float actualHeight = textWriter.write(writer.getContentStream(), bounds, text, align, vAlign);

        if (height > HEIGHT_UNDEFINED) {
            return height;
        } else if (VAlign.TOP == vAlign) {
            return actualHeight + padding.vertical();
        } else {
            return viewPort.height();
        }
    }

    @Override
    public Margin margin() {
        return margin;
    }

    @Override
    public float innerHeight(Bounds viewPort) {
        if (height > HEIGHT_UNDEFINED) {
            return height;
        } else if (VAlign.TOP == vAlign) {
            return textWriter.calculateHeight(text, viewPort) + padding.vertical();
        } else {
            return viewPort.height();
        }
    }

    private Bounds effectiveBounds(Bounds viewPort) {
        if (height > HEIGHT_UNDEFINED) {
            return viewPort.apply(margin).height(height).apply(padding);
        } else {
            return viewPort.apply(margin).apply(padding);
        }
    }

}
