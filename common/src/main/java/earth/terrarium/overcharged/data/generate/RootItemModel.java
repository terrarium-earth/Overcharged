package earth.terrarium.overcharged.data.generate;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RootItemModel extends DefaultItemModel {

    @SerializedName("overrides")
    public final List<Override> overrides;

    public RootItemModel(String parent, String textureName) {
        super(parent, textureName + "_uncharged");
        this.overrides = List.of(
                new Override(0.5f, textureName + "_charged"),
                new Override(1, textureName + "_empowered")
        );
    }

    public static class Override {

        @SerializedName("predicate")
        public final Predicate predicate;

        @SerializedName("model")
        public final String model;

        public Override(float predicate, String model) {
            this.predicate = new Predicate(predicate);
            this.model = "overcharged:item/" + model;
        }

        public static class Predicate {
            @SerializedName("overcharged:energy_state")
            public final float predicate;

            public Predicate(float predicate) {
                this.predicate = predicate;
            }
        }
    }
}
