package me.zero.clarinet.util.misc;

import java.awt.Desktop;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FilenameUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import me.zero.clarinet.Impact;
import me.zero.clarinet.util.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;

public class MiscUtils {
	
	public static void uploadToImgur(File file) {
		ClientUtils.message(TextFormatting.GRAY + "Beginning upload to Imgur!");
		new Thread() {
			@Override
			public void run() {
				try {
					BufferedImage image = ImageIO.read(new File(file.getAbsolutePath()));
					ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
					ImageIO.write(image, "png", byteArray);
					byte[] fileByes = byteArray.toByteArray();
					String base64File = Base64.encodeBase64String(fileByes);
					String postData = URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(base64File, "UTF-8");
					
					URL imgurApi = new URL("https://api.imgur.com/3/image");
					HttpURLConnection connection = (HttpURLConnection) imgurApi.openConnection();
					connection.setDoOutput(true);
					connection.setDoInput(true);
					connection.setRequestMethod("POST");
					connection.setRequestProperty("Authorization", "Client-ID be12a30d5172bb7");
					connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
					connection.connect();
					
					OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
					outputStreamWriter.write(postData);
					outputStreamWriter.flush();
					outputStreamWriter.close();
					
					StringBuilder stringBuilder = new StringBuilder();
					BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
					String line;
					while ((line = rd.readLine()) != null) {
						stringBuilder.append(line).append(System.lineSeparator());
					}
					rd.close();
					
					Gson gson = new GsonBuilder().setPrettyPrinting().create();
					JsonObject json = (JsonObject) gson.fromJson(stringBuilder.toString(), JsonObject.class);
					String url = "http://imgur.com/" + json.get("data").getAsJsonObject().get("id").getAsString() + "";
					
					ClientUtils.message(TextFormatting.GRAY + "Uploaded Screenshot to Imgur! " + TextFormatting.BLUE + url);					
				} catch (IOException e) {
					ClientUtils.error("Upload to Imgur failed!");
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	public static ResourceLocation getImageFromURL(String url) {
		String mptHash = FilenameUtils.getBaseName(url);
		ResourceLocation rsc = new ResourceLocation("downloaded/" + mptHash);
		TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
		ITextureObject itextureobject = texturemanager.getTexture(rsc);
		IImageBuffer iimagebuffer = new IImageBuffer() {
			ImageBufferDownload ibd = new ImageBufferDownload();
			
			public BufferedImage parseUserSkin(BufferedImage image) {
				return image;
			}
			
			public void skinAvailable() {}
		};
		ThreadDownloadImageData threaddownloadimagedata1 = new ThreadDownloadImageData((File) null, url, (ResourceLocation) null, iimagebuffer);
		// threaddownloadimagedata1.pipeline = true;
		texturemanager.loadTexture(rsc, threaddownloadimagedata1);
		return rsc;
	}
	
	public static void copyToClipboard(String string) {
		StringSelection selection = new StringSelection(string);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(selection, selection);
	}
	
	public static String getClipboardContents() {
		try {
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			return (String) clipboard.getData(DataFlavor.stringFlavor);
		} catch (HeadlessException | UnsupportedFlavorException | IOException e) {
			return null;
		}
	}
	
	public static <T> boolean arrayContains(T[] array, T value) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] == value) {
				return true;
			}
		}
		return false;
	}
}
