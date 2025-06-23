package bamboo.inventory.mixin;

import java.util.stream.Stream;

import net.sourceforge.pinyin4j.PinyinHelper;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.search.SearchManager;

@Mixin(SearchManager.class)
public abstract class SearchManagerMixin {
    @Inject(method = "collectItemTooltips", at = @At("RETURN"), cancellable = true)
    private static void collectItemTooltips(CallbackInfoReturnable<Stream<String>> cir) {
        cir.setReturnValue(cir.getReturnValue().map(str -> {
            StringBuilder sb = new StringBuilder();
            for (char ch : str.toCharArray()) {
                String[] pinyin = PinyinHelper.toHanyuPinyinStringArray(ch);
                if (pinyin != null && pinyin.length > 0) {
                    sb.append(pinyin[0].replaceAll("\\d", ""));
                } else {
                    sb.append(ch);
                }
            }
            return sb.toString();
        }));
    }
}
