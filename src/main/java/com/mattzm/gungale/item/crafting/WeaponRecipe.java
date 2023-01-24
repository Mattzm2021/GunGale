package com.mattzm.gungale.item.crafting;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;

public class WeaponRecipe extends ShapedRecipe {
    static int MAX_WIDTH = 5;
    static int MAX_HEIGHT = 3;

    public WeaponRecipe(ResourceLocation p_i48162_1_, String p_i48162_2_, int p_i48162_3_, int p_i48162_4_, NonNullList<Ingredient> p_i48162_5_, ItemStack p_i48162_6_) {
        super(p_i48162_1_, p_i48162_2_, p_i48162_3_, p_i48162_4_, p_i48162_5_, p_i48162_6_);
    }

    private static @NotNull Map<String, Ingredient> keyFromJson(@NotNull JsonObject p_192408_0_) {
        Map<String, Ingredient> map = Maps.newHashMap();

        for(Map.Entry<String, JsonElement> entry : p_192408_0_.entrySet()) {
            if (entry.getKey().length() != 1) {
                throw new JsonSyntaxException("Invalid key entry: '" + entry.getKey() + "' is an invalid symbol (must be 1 character only).");
            }

            if (" ".equals(entry.getKey())) {
                throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
            }

            map.put(entry.getKey(), Ingredient.fromJson(entry.getValue()));
        }

        map.put(" ", Ingredient.EMPTY);
        return map;
    }

    @VisibleForTesting
    static String @NotNull [] shrink(String @NotNull ... p_194134_0_) {
        int i = Integer.MAX_VALUE;
        int j = 0;
        int k = 0;
        int l = 0;

        for(int i1 = 0; i1 < p_194134_0_.length; ++i1) {
            String s = p_194134_0_[i1];
            i = Math.min(i, firstNonSpace(s));
            int j1 = lastNonSpace(s);
            j = Math.max(j, j1);
            if (j1 < 0) {
                if (k == i1) {
                    ++k;
                }

                ++l;
            } else {
                l = 0;
            }
        }

        if (p_194134_0_.length == l) {
            return new String[0];
        } else {
            String[] asString = new String[p_194134_0_.length - l - k];

            for(int k1 = 0; k1 < asString.length; ++k1) {
                asString[k1] = p_194134_0_[k1 + k].substring(i, j + 1);
            }

            return asString;
        }
    }

    private static int firstNonSpace(@NotNull String p_194135_0_) {
        int i;

        for (i = 0; i < p_194135_0_.length() && p_194135_0_.charAt(i) == ' ';) {
            ++i;
        }

        return i;
    }

    private static int lastNonSpace(@NotNull String p_194136_0_) {
        int i;

        for (i = p_194136_0_.length() - 1; i >= 0 && p_194136_0_.charAt(i) == ' ';) {
            --i;
        }

        return i;
    }

    private static String @NotNull [] patternFromJson(@NotNull JsonArray p_192407_0_) {
        String[] asString = new String[p_192407_0_.size()];
        if (asString.length > MAX_HEIGHT) {
            throw new JsonSyntaxException("Invalid pattern: too many rows, " + MAX_HEIGHT + " is maximum");
        } else if (asString.length == 0) {
            throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
        } else {
            for(int i = 0; i < asString.length; ++i) {
                String s = JSONUtils.convertToString(p_192407_0_.get(i), "pattern[" + i + "]");
                if (s.length() > MAX_WIDTH) {
                    throw new JsonSyntaxException("Invalid pattern: too many columns, " + MAX_WIDTH + " is maximum");
                }

                if (i > 0 && asString[0].length() != s.length()) {
                    throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
                }

                asString[i] = s;
            }

            return asString;
        }
    }

    private static @NotNull NonNullList<Ingredient> dissolvePattern(String @NotNull [] p_192402_0_, @NotNull Map<String, Ingredient> p_192402_1_, int p_192402_2_, int p_192402_3_) {
        NonNullList<Ingredient> nonNullList = NonNullList.withSize(p_192402_2_ * p_192402_3_, Ingredient.EMPTY);
        Set<String> set = Sets.newHashSet(p_192402_1_.keySet());
        set.remove(" ");

        for(int i = 0; i < p_192402_0_.length; ++i) {
            for(int j = 0; j < p_192402_0_[i].length(); ++j) {
                String s = p_192402_0_[i].substring(j, j + 1);
                Ingredient ingredient = p_192402_1_.get(s);
                if (ingredient == null) {
                    throw new JsonSyntaxException("Pattern references symbol '" + s + "' but it's not defined in the key");
                }

                set.remove(s);
                nonNullList.set(j + p_192402_2_ * i, ingredient);
            }
        }

        if (!set.isEmpty()) {
            throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + set);
        } else {
            return nonNullList;
        }
    }

    @Override
    public @NotNull IRecipeType<?> getType() {
        return ModRecipeTypes.WEAPON;
    }

    @Override
    public @NotNull IRecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.WEAPON_RECIPE;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<WeaponRecipe> {
        @Override
        public @NotNull WeaponRecipe fromJson(@NotNull ResourceLocation p_199425_1_, @NotNull JsonObject p_199425_2_) {
            String s = JSONUtils.getAsString(p_199425_2_, "group", "");
            Map<String, Ingredient> map = keyFromJson(JSONUtils.getAsJsonObject(p_199425_2_, "key"));
            String[] asString = shrink(patternFromJson(JSONUtils.getAsJsonArray(p_199425_2_, "pattern")));
            int i = asString[0].length();
            int j = asString.length;
            NonNullList<Ingredient> nonNullList = dissolvePattern(asString, map, i, j);
            ItemStack itemstack = itemFromJson(JSONUtils.getAsJsonObject(p_199425_2_, "result"));
            return new WeaponRecipe(p_199425_1_, s, i, j, nonNullList, itemstack);
        }

        @Nullable
        @Override
        public WeaponRecipe fromNetwork(@NotNull ResourceLocation location, @NotNull PacketBuffer buffer) {
            int i = buffer.readVarInt();
            int j = buffer.readVarInt();
            String s = buffer.readUtf(32767);
            NonNullList<Ingredient> nonNullList = NonNullList.withSize(i * j, Ingredient.EMPTY);
            nonNullList.replaceAll(ignored -> Ingredient.fromNetwork(buffer));
            ItemStack itemstack = buffer.readItem();
            return new WeaponRecipe(location, s, i, j, nonNullList, itemstack);
        }

        @Override
        public void toNetwork(@NotNull PacketBuffer buffer, @NotNull WeaponRecipe recipe) {
            buffer.writeVarInt(recipe.getWidth());
            buffer.writeVarInt(recipe.getHeight());
            buffer.writeUtf(recipe.getGroup());

            for (Ingredient ingredient : recipe.getIngredients()) {
                ingredient.toNetwork(buffer);
            }

            buffer.writeItem(recipe.getResultItem());
        }
    }
}
