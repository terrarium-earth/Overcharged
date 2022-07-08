package earth.terrarium.overcharged.data.generate;

import com.google.gson.annotations.SerializedName;

public class DefaultItemModel {
    @SerializedName("parent")
    public final String parent;

    @SerializedName("textures")
    public final TextureLayer layer;

    public DefaultItemModel(String parent, String textureName) {
        this.parent = parent;
        this.layer = new TextureLayer(textureName);
    }

    public class TextureLayer {
        @SerializedName("layer0")
        public final String layer0;

        public TextureLayer(String layer0) {
            this.layer0 = "overcharged:item/" + layer0;
        }
    }
}
