package kult.klient.systems.accounts;

import com.mojang.blaze3d.platform.TextureUtil;
import kult.klient.KultKlient;
import kult.klient.renderer.Texture;
import kult.klient.utils.misc.ISerializable;
import kult.klient.utils.misc.NbtException;
import kult.klient.utils.network.HTTP;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static kult.klient.KultKlient.mc;

public class AccountCache implements ISerializable<AccountCache> {
    private static Texture STEVE_HEAD;

    public String username = "";
    public String uuid = "";

    private Texture headTexture;

    public Texture getHeadTexture() {
        return headTexture != null ? headTexture : STEVE_HEAD;
    }

    public boolean shouldRotateHeadTexture() {
        return headTexture != null;
    }

    public boolean loadHead(String url) {
        try {
            BufferedImage skin = ImageIO.read(HTTP.get(url).sendInputStream());
            byte[] head = new byte[8 * 8 * 3];
            int[] pixel = new int[4];

            int i = 0;
            for (int x = 8; x < 16; x++) {
                for (int y = 8; y < 16; y++) {
                    skin.getData().getPixel(x, y, pixel);

                    for (int j = 0; j < 3; j++) {
                        head[i] = (byte) pixel[j];
                        i++;
                    }
                }
            }

            i = 0;
            for (int x = 40; x < 48; x++) {
                for (int y = 8; y < 16; y++) {
                    skin.getData().getPixel(x, y, pixel);

                    if (pixel[3] != 0) {
                        for (int j = 0; j < 3; j++) {
                            head[i] = (byte) pixel[j];
                            i++;
                        }
                    }
                    else i += 3;
                }
            }

            headTexture = new Texture(8, 8, head, Texture.Format.RGB, Texture.Filter.Nearest, Texture.Filter.Nearest);
            return true;
        } catch (IOException e) {
            KultKlient.LOG.error(KultKlient.logPrefix + "Failed to read skin url (" + url + ").");
            return false;
        }
    }

    @Override
    public NbtCompound toTag() {
        NbtCompound tag = new NbtCompound();

        tag.putString("username", username);
        tag.putString("uuid", uuid);

        return tag;
    }

    @Override
    public AccountCache fromTag(NbtCompound tag) {
        if (!tag.contains("username") || !tag.contains("uuid")) throw new NbtException();

        username = tag.getString("username");
        uuid = tag.getString("uuid");

        return this;
    }

    public static void loadSteveHead() {
        try {
            ByteBuffer data = TextureUtil.readResource(mc.getResourceManager().getResource(new Identifier("kultklient", "textures/steve.png")).getInputStream());
            data.rewind();

            try (MemoryStack stack = MemoryStack.stackPush()) {
                IntBuffer width = stack.mallocInt(1);
                IntBuffer height = stack.mallocInt(1);
                IntBuffer comp = stack.mallocInt(1);

                ByteBuffer image = STBImage.stbi_load_from_memory(data, width, height, comp, 3);

                STEVE_HEAD = new Texture();
                STEVE_HEAD.upload(width.get(0), height.get(0), image, Texture.Format.RGB, Texture.Filter.Nearest, Texture.Filter.Nearest, false);

                STBImage.stbi_image_free(image);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
