package net.minecraft.client.renderer;

import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.WorldVertexBufferUploader;

public class Tessellator
{
    private final VertexBuffer worldRenderer;
    private final WorldVertexBufferUploader vboUploader = new WorldVertexBufferUploader();

    /** The static instance of the Tessellator. */
    private static final Tessellator INSTANCE = new Tessellator(2097152);

    public static Tessellator getInstance()
    {
        return INSTANCE;
    }

    public Tessellator(int bufferSize)
    {
        this.worldRenderer = new VertexBuffer(bufferSize);
    }

    /**
     * Draws the data set up in this tessellator and resets the state to prepare for new drawing.
     */
    public void draw()
    {
        this.worldRenderer.finishDrawing();
        this.vboUploader.draw(this.worldRenderer);
    }

    public VertexBuffer getBuffer()
    {
        return this.worldRenderer;
    }
}
