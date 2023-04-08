package net.minecraft.network.play.server;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketEntityProperties implements Packet<INetHandlerPlayClient>
{
    private int entityId;
    private final List<Snapshot> snapshots = Lists.<Snapshot>newArrayList();

    public SPacketEntityProperties()
    {
    }

    public SPacketEntityProperties(int entityIdIn, Collection<IAttributeInstance> instances)
    {
        this.entityId = entityIdIn;

        for (IAttributeInstance iattributeinstance : instances)
        {
            this.snapshots.add(new Snapshot(iattributeinstance.getAttribute().getAttributeUnlocalizedName(), iattributeinstance.getBaseValue(), iattributeinstance.getModifiers()));
        }
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.entityId = buf.readVarIntFromBuffer();
        int i = buf.readInt();

        for (int j = 0; j < i; ++j)
        {
            String s = buf.readStringFromBuffer(64);
            double d0 = buf.readDouble();
            List<AttributeModifier> list = Lists.<AttributeModifier>newArrayList();
            int k = buf.readVarIntFromBuffer();

            for (int l = 0; l < k; ++l)
            {
                UUID uuid = buf.readUuid();
                list.add(new AttributeModifier(uuid, "Unknown synced attribute modifier", buf.readDouble(), buf.readByte()));
            }

            this.snapshots.add(new Snapshot(s, d0, list));
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeVarIntToBuffer(this.entityId);
        buf.writeInt(this.snapshots.size());

        for (Snapshot spacketentityproperties$snapshot : this.snapshots)
        {
            buf.writeString(spacketentityproperties$snapshot.getName());
            buf.writeDouble(spacketentityproperties$snapshot.getBaseValue());
            buf.writeVarIntToBuffer(spacketentityproperties$snapshot.getModifiers().size());

            for (AttributeModifier attributemodifier : spacketentityproperties$snapshot.getModifiers())
            {
                buf.writeUuid(attributemodifier.getID());
                buf.writeDouble(attributemodifier.getAmount());
                buf.writeByte(attributemodifier.getOperation());
            }
        }
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleEntityProperties(this);
    }

    public int getEntityId()
    {
        return this.entityId;
    }

    public List<Snapshot> getSnapshots()
    {
        return this.snapshots;
    }

    public class Snapshot
    {
        private final String name;
        private final double baseValue;
        private final Collection<AttributeModifier> modifiers;

        public Snapshot(String nameIn, double baseValueIn, Collection<AttributeModifier> modifiersIn)
        {
            this.name = nameIn;
            this.baseValue = baseValueIn;
            this.modifiers = modifiersIn;
        }

        public String getName()
        {
            return this.name;
        }

        public double getBaseValue()
        {
            return this.baseValue;
        }

        public Collection<AttributeModifier> getModifiers()
        {
            return this.modifiers;
        }
    }
}
