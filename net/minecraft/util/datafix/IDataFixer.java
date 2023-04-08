package net.minecraft.util.datafix;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixType;

public interface IDataFixer
{
    NBTTagCompound process(IFixType type, NBTTagCompound compound, int versionIn);
}
