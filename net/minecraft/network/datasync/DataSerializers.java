package net.minecraft.network.datasync;

import com.google.common.base.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IntIdentityHashBiMap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Rotations;
import net.minecraft.util.text.ITextComponent;

public class DataSerializers
{
    private static final IntIdentityHashBiMap <net.minecraft.network.datasync.DataSerializer<? >> REGISTRY = new IntIdentityHashBiMap(16);
    public static final net.minecraft.network.datasync.DataSerializer<Byte> BYTE = new net.minecraft.network.datasync.DataSerializer<Byte>()
    {
        public void write(PacketBuffer buf, Byte value)
        {
            buf.writeByte(value.byteValue());
        }
        public Byte read(PacketBuffer buf)
        {
            return Byte.valueOf(buf.readByte());
        }
        public net.minecraft.network.datasync.DataParameter<Byte> createKey(int id)
        {
            return new net.minecraft.network.datasync.DataParameter(id, this);
        }
    };
    public static final net.minecraft.network.datasync.DataSerializer<Integer> VARINT = new net.minecraft.network.datasync.DataSerializer<Integer>()
    {
        public void write(PacketBuffer buf, Integer value)
        {
            buf.writeVarIntToBuffer(value.intValue());
        }
        public Integer read(PacketBuffer buf)
        {
            return Integer.valueOf(buf.readVarIntFromBuffer());
        }
        public net.minecraft.network.datasync.DataParameter<Integer> createKey(int id)
        {
            return new net.minecraft.network.datasync.DataParameter(id, this);
        }
    };
    public static final net.minecraft.network.datasync.DataSerializer<Float> FLOAT = new net.minecraft.network.datasync.DataSerializer<Float>()
    {
        public void write(PacketBuffer buf, Float value)
        {
            buf.writeFloat(value.floatValue());
        }
        public Float read(PacketBuffer buf)
        {
            return Float.valueOf(buf.readFloat());
        }
        public net.minecraft.network.datasync.DataParameter<Float> createKey(int id)
        {
            return new net.minecraft.network.datasync.DataParameter(id, this);
        }
    };
    public static final net.minecraft.network.datasync.DataSerializer<String> STRING = new net.minecraft.network.datasync.DataSerializer<String>()
    {
        public void write(PacketBuffer buf, String value)
        {
            buf.writeString(value);
        }
        public String read(PacketBuffer buf)
        {
            return buf.readStringFromBuffer(32767);
        }
        public net.minecraft.network.datasync.DataParameter<String> createKey(int id)
        {
            return new net.minecraft.network.datasync.DataParameter(id, this);
        }
    };
    public static final net.minecraft.network.datasync.DataSerializer<ITextComponent> TEXT_COMPONENT = new net.minecraft.network.datasync.DataSerializer<ITextComponent>()
    {
        public void write(PacketBuffer buf, ITextComponent value)
        {
            buf.writeTextComponent(value);
        }
        public ITextComponent read(PacketBuffer buf) throws java.io.IOException
        {
            return buf.readTextComponent();
        }
        public net.minecraft.network.datasync.DataParameter<ITextComponent> createKey(int id)
        {
            return new net.minecraft.network.datasync.DataParameter(id, this);
        }
    };
    public static final net.minecraft.network.datasync.DataSerializer<Optional<ItemStack>> OPTIONAL_ITEM_STACK = new net.minecraft.network.datasync.DataSerializer<Optional<ItemStack>>()
    {
        public void write(PacketBuffer buf, Optional<ItemStack> value)
        {
            buf.writeItemStackToBuffer((ItemStack)value.orNull());
        }
        public Optional<ItemStack> read(PacketBuffer buf) throws java.io.IOException
        {
            return Optional.<ItemStack>fromNullable(buf.readItemStackFromBuffer());
        }
        public net.minecraft.network.datasync.DataParameter<Optional<ItemStack>> createKey(int id)
        {
            return new net.minecraft.network.datasync.DataParameter(id, this);
        }
    };
    public static final net.minecraft.network.datasync.DataSerializer<Optional<IBlockState>> OPTIONAL_BLOCK_STATE = new net.minecraft.network.datasync.DataSerializer<Optional<IBlockState>>()
    {
        public void write(PacketBuffer buf, Optional<IBlockState> value)
        {
            if (value.isPresent())
            {
                buf.writeVarIntToBuffer(Block.getStateId((IBlockState)value.get()));
            }
            else
            {
                buf.writeVarIntToBuffer(0);
            }
        }
        public Optional<IBlockState> read(PacketBuffer buf)
        {
            int i = buf.readVarIntFromBuffer();
            return i == 0 ? Optional.<IBlockState>absent() : Optional.of(Block.getStateById(i));
        }
        public net.minecraft.network.datasync.DataParameter<Optional<IBlockState>> createKey(int id)
        {
            return new net.minecraft.network.datasync.DataParameter(id, this);
        }
    };
    public static final net.minecraft.network.datasync.DataSerializer<Boolean> BOOLEAN = new net.minecraft.network.datasync.DataSerializer<Boolean>()
    {
        public void write(PacketBuffer buf, Boolean value)
        {
            buf.writeBoolean(value.booleanValue());
        }
        public Boolean read(PacketBuffer buf)
        {
            return Boolean.valueOf(buf.readBoolean());
        }
        public net.minecraft.network.datasync.DataParameter<Boolean> createKey(int id)
        {
            return new net.minecraft.network.datasync.DataParameter(id, this);
        }
    };
    public static final net.minecraft.network.datasync.DataSerializer<Rotations> ROTATIONS = new net.minecraft.network.datasync.DataSerializer<Rotations>()
    {
        public void write(PacketBuffer buf, Rotations value)
        {
            buf.writeFloat(value.getX());
            buf.writeFloat(value.getY());
            buf.writeFloat(value.getZ());
        }
        public Rotations read(PacketBuffer buf)
        {
            return new Rotations(buf.readFloat(), buf.readFloat(), buf.readFloat());
        }
        public net.minecraft.network.datasync.DataParameter<Rotations> createKey(int id)
        {
            return new net.minecraft.network.datasync.DataParameter(id, this);
        }
    };
    public static final net.minecraft.network.datasync.DataSerializer<BlockPos> BLOCK_POS = new net.minecraft.network.datasync.DataSerializer<BlockPos>()
    {
        public void write(PacketBuffer buf, BlockPos value)
        {
            buf.writeBlockPos(value);
        }
        public BlockPos read(PacketBuffer buf)
        {
            return buf.readBlockPos();
        }
        public net.minecraft.network.datasync.DataParameter<BlockPos> createKey(int id)
        {
            return new net.minecraft.network.datasync.DataParameter(id, this);
        }
    };
    public static final net.minecraft.network.datasync.DataSerializer<Optional<BlockPos>> OPTIONAL_BLOCK_POS = new net.minecraft.network.datasync.DataSerializer<Optional<BlockPos>>()
    {
        public void write(PacketBuffer buf, Optional<BlockPos> value)
        {
            buf.writeBoolean(value.isPresent());

            if (value.isPresent())
            {
                buf.writeBlockPos((BlockPos)value.get());
            }
        }
        public Optional<BlockPos> read(PacketBuffer buf)
        {
            return !buf.readBoolean() ? Optional.<BlockPos>absent() : Optional.of(buf.readBlockPos());
        }
        public net.minecraft.network.datasync.DataParameter<Optional<BlockPos>> createKey(int id)
        {
            return new net.minecraft.network.datasync.DataParameter(id, this);
        }
    };
    public static final net.minecraft.network.datasync.DataSerializer<EnumFacing> FACING = new net.minecraft.network.datasync.DataSerializer<EnumFacing>()
    {
        public void write(PacketBuffer buf, EnumFacing value)
        {
            buf.writeEnumValue(value);
        }
        public EnumFacing read(PacketBuffer buf)
        {
            return (EnumFacing)buf.readEnumValue(EnumFacing.class);
        }
        public net.minecraft.network.datasync.DataParameter<EnumFacing> createKey(int id)
        {
            return new net.minecraft.network.datasync.DataParameter(id, this);
        }
    };
    public static final net.minecraft.network.datasync.DataSerializer<Optional<UUID>> OPTIONAL_UNIQUE_ID = new net.minecraft.network.datasync.DataSerializer<Optional<UUID>>()
    {
        public void write(PacketBuffer buf, Optional<UUID> value)
        {
            buf.writeBoolean(value.isPresent());

            if (value.isPresent())
            {
                buf.writeUuid((UUID)value.get());
            }
        }
        public Optional<UUID> read(PacketBuffer buf)
        {
            return !buf.readBoolean() ? Optional.<UUID>absent() : Optional.of(buf.readUuid());
        }
        public net.minecraft.network.datasync.DataParameter<Optional<UUID>> createKey(int id)
        {
            return new DataParameter(id, this);
        }
    };

    public static void registerSerializer(net.minecraft.network.datasync.DataSerializer<?> serializer)
    {
        REGISTRY.add(serializer);
    }

    @Nullable
    public static net.minecraft.network.datasync.DataSerializer<?> getSerializer(int id)
    {
        return (net.minecraft.network.datasync.DataSerializer)REGISTRY.get(id);
    }

    public static int getSerializerId(DataSerializer<?> serializer)
    {
        return REGISTRY.getId(serializer);
    }

    static
    {
        registerSerializer(BYTE);
        registerSerializer(VARINT);
        registerSerializer(FLOAT);
        registerSerializer(STRING);
        registerSerializer(TEXT_COMPONENT);
        registerSerializer(OPTIONAL_ITEM_STACK);
        registerSerializer(BOOLEAN);
        registerSerializer(ROTATIONS);
        registerSerializer(BLOCK_POS);
        registerSerializer(OPTIONAL_BLOCK_POS);
        registerSerializer(FACING);
        registerSerializer(OPTIONAL_UNIQUE_ID);
        registerSerializer(OPTIONAL_BLOCK_STATE);
    }
}
