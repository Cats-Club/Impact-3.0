package net.minecraft.client.resources;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;

public interface IResourceManager
{
    Set<String> getResourceDomains();

    net.minecraft.client.resources.IResource getResource(ResourceLocation location) throws IOException;

    List<IResource> getAllResources(ResourceLocation location) throws IOException;
}
