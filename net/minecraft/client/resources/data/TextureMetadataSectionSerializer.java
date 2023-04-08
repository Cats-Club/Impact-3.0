package net.minecraft.client.resources.data;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;

import net.minecraft.client.resources.data.BaseMetadataSectionSerializer;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.util.JsonUtils;

public class TextureMetadataSectionSerializer extends BaseMetadataSectionSerializer<net.minecraft.client.resources.data.TextureMetadataSection>
{
    public net.minecraft.client.resources.data.TextureMetadataSection deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException
    {
        JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
        boolean flag = JsonUtils.getBoolean(jsonobject, "blur", false);
        boolean flag1 = JsonUtils.getBoolean(jsonobject, "clamp", false);
        return new TextureMetadataSection(flag, flag1);
    }

    /**
     * The name of this section type as it appears in JSON.
     */
    public String getSectionName()
    {
        return "texture";
    }
}
