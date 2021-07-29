package ru.bclib.api.dataexchange;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import ru.bclib.BCLib;

import java.nio.charset.StandardCharsets;

public class TestHandler extends DataHandler{
	public static DataHandlerDescriptor DESCRIPTOR = new DataHandlerDescriptor(new ResourceLocation(BCLib.MOD_ID, "__test"), TestHandler::new, true);
	
	public TestHandler() {
		super(DESCRIPTOR.IDENTIFIER, true);
	}
	
	@Override
	protected void deserializeFromIncomingData(FriendlyByteBuf buf, PacketSender responseSender, boolean fromClient) {
		int length = buf.readInt();
		CharSequence text = buf.readCharSequence(length, StandardCharsets.UTF_8);
		BCLib.LOGGER.info("PROCESSING INCOMING TEST-DATA fromClient="+fromClient+": "+text);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	protected void runOnClient(Minecraft client) {
		BCLib.LOGGER.info("RUNNING INCOMING TEST-DATA ON CLIENT");
	}
	
	@Override
	protected void serializeData(FriendlyByteBuf buf) {
		CharSequence text = "Welcome from BCLib";
		buf.writeInt(text.length());
		buf.writeCharSequence(text, StandardCharsets.UTF_8);
		BCLib.LOGGER.info("BUILDING OUTGOING TEST-DATA ON SERVER");
	}
}
