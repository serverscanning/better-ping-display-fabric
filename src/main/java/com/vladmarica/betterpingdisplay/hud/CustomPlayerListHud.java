package com.vladmarica.betterpingdisplay.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import com.vladmarica.betterpingdisplay.BetterPingDisplayMod;
import com.vladmarica.betterpingdisplay.Config;
import com.vladmarica.betterpingdisplay.mixin.PlayerListHudInvoker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.math.MatrixStack;

public final class CustomPlayerListHud {
  private static final int PING_TEXT_RENDER_OFFSET = -13;
  private static final int PING_BARS_WIDTH = 11;
  private static final Config config = BetterPingDisplayMod.instance().getConfig();

  public static void renderPingDisplay(
          MinecraftClient client, PlayerListHud hud, DrawContext drawContext, int width, int x, int y, PlayerListEntry player) {
    TextRenderer textRenderer = client.textRenderer;

    String pingString = String.format(config.getTextFormatString(), player.getLatency());
    int pingStringWidth = textRenderer.getWidth(pingString);
    int pingTextColor = config.shouldAutoColorPingText() ? PingColors.getColor(player.getLatency()) : config.getTextColor();
    int textX = width + x - pingStringWidth + PING_TEXT_RENDER_OFFSET;

    if (!config.shouldRenderPingBars()) {
      textX += PING_BARS_WIDTH;
    }

    // Draw the ping text for the given player
    drawContext.drawText(textRenderer, pingString, textX, y, pingTextColor, false);

    if (config.shouldRenderPingBars()) {
      ((PlayerListHudInvoker) hud).invokeRenderLatencyIcon(drawContext, width, x, y, player);
    } else {
      // If we don't render ping bars, we need to reset the render system color so the rest
      // of the player list renders properly
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
  }
}
