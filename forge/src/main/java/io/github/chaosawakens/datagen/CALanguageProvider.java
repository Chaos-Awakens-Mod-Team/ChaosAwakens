package io.github.chaosawakens.datagen;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Lists;
import io.github.chaosawakens.CAConstants;
import io.github.chaosawakens.api.block.standard.BlockPropertyWrapper;
import io.github.chaosawakens.api.damage_type.DamageTypeWrapper;
import io.github.chaosawakens.api.item.ItemPropertyWrapper;
import io.github.chaosawakens.common.registry.CABlocks;
import io.github.chaosawakens.common.registry.CACreativeModeTabs;
import io.github.chaosawakens.common.registry.CAItems;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.LanguageProvider;

import java.util.List;
import java.util.stream.Collectors;

public class CALanguageProvider extends LanguageProvider {
    private static final ObjectArrayList<String> DEFAULT_SEPARATORS = ObjectArrayList.of("Of", "And");
    private static final Object2ObjectOpenHashMap<String, String> MANUAL_TRANSLATIONS = new Object2ObjectOpenHashMap<String, String>();
    private static final LinkedHashMultimap<String, String> MANUAL_INSERTIONS = LinkedHashMultimap.create();

    public CALanguageProvider(PackOutput output) {
        super(output, CAConstants.MODID, "en_us");
    }

    @Override
    public String getName() {
        return CAConstants.MOD_NAME.concat(": Language Provider (en_us)");
    }

    public static void addManualTranslation(String localisedKey, String translationValue) {
        MANUAL_TRANSLATIONS.put(localisedKey, translationValue);
    }

    /**
     * Safely attempts to insert a {@link String} into another {@link String} at the specified index. Referenced from
     * {@code https://www.geeksforgeeks.org/insert-a-string-into-another-string-in-java/}.
     *
     * @param originalString The original {@link String} to modify.
     * @param stringToBeInserted The {@link String} to insert into the specified original {@link String}.
     * @param insertionIndex The index at which to insert the {@link String} to be inserted. Appends to the end of the original {@link String}
     *                       if the insertion index is larger than or equal to the length of the original {@link String}.
     *
     * @return A new {@link String} with the contents of the specified insertion {@link String} inserted at the specified index.
     */
    public static String insertStringAt(String originalString, String stringToBeInserted, int insertionIndex) {
        StringBuilder moddedString = new StringBuilder();

        if (insertionIndex >= originalString.length()) return originalString.concat(stringToBeInserted);
        if (insertionIndex <= 0) return stringToBeInserted.concat(originalString);

        for (int i = 0; i < originalString.length(); i++) {
            moddedString.append(originalString.charAt(i));

            if (i == insertionIndex) moddedString.append(stringToBeInserted);
        }

        return moddedString.toString();
    }

    protected String handleInput(String displayName) {
        ObjectArrayList<String> finalizedList = new ObjectArrayList<String>(1);

        MANUAL_INSERTIONS.asMap().forEach((curDisplayName, matchersAndInsertion) -> {
            if (!displayName.equalsIgnoreCase(curDisplayName)) return;

            ObjectArrayList<String> copiedMatchersAndInsertions = new ObjectArrayList<String>(matchersAndInsertion);
            ObjectArrayList<String> prunedMatchersAndInsertion = copiedMatchersAndInsertions.stream()
                    .filter(curString -> copiedMatchersAndInsertions.indexOf(curString) < 2)
                    .collect(Collectors.toCollection(ObjectArrayList::new));

            if (!curDisplayName.contains(prunedMatchersAndInsertion.get(0))) return;

            int insertionLocation = curDisplayName.indexOf(prunedMatchersAndInsertion.get(0)) + prunedMatchersAndInsertion.get(0).length() - 1;

            curDisplayName = insertStringAt(curDisplayName, prunedMatchersAndInsertion.get(1), insertionLocation);

            finalizedList.add(curDisplayName);
            CAConstants.LOGGER.debug("[Inserted Manual String]: " + displayName + " -> " + curDisplayName);
        });

        return finalizedList.isEmpty() ? displayName : finalizedList.get(0);
    }

    // https://stackoverflow.com/questions/1892765/how-to-capitalize-the-first-character-of-each-word-in-a-string
    protected static String formatString(String input, List<String> separators) { //TODO Refactor
        char[] charSet = input.toLowerCase().toCharArray();
        boolean found = false;

        for (int i = 0; i < charSet.length - 1; i++) {
            if (!found && Character.isLetter(charSet[i])) {
                charSet[i] = Character.toUpperCase(charSet[i]);
                found = true;
            } else if (Character.isWhitespace(charSet[i]) || charSet[i] == '.' || charSet[i] == '_') found = false;
        }

        String baseResult = String.valueOf(charSet);

        for (String lcw : DEFAULT_SEPARATORS) if (baseResult.contains(lcw)) baseResult = baseResult.replaceAll(lcw, lcw.toLowerCase());

        return baseResult;
    }

    protected static String formatString(String input) {
        return formatString(input, DEFAULT_SEPARATORS);
    }

    protected String getTranslatedRegistryName(String registryName, List<String> separators) {
        if (registryName.isBlank() || registryName.isEmpty() || registryName == null) return registryName;
        if (!registryName.contains(".")) return registryName;

        String regNameTemp = registryName;
        String formattedName = formatString(regNameTemp, separators);
        String displayName = formattedName.substring(formattedName.lastIndexOf(".") + 1).replaceAll("_", " ");

        return displayName;
    }

    protected String getTranslatedRegistryName(String registryName) {
        return getTranslatedRegistryName(registryName, DEFAULT_SEPARATORS);
    }

    protected void localizeGeneralRegistryName(String registryName, String translatedName, List<String> toTrim) {
        if (toTrim != null && !toTrim.isEmpty()) {
            for (int i = 0; i < toTrim.size(); i++) {
                String curTrim = toTrim.get(i);

                translatedName = translatedName.replaceFirst(curTrim, "");
            }
        }

        if (!MANUAL_TRANSLATIONS.containsKey(registryName)) add(registryName, handleInput(translatedName));
    }

    protected void localizeGeneralRegistryName(String registryName, List<String> separators, List<String> toTrim) {
        String translatedRegName = getTranslatedRegistryName(registryName, separators);

        if (toTrim != null && !toTrim.isEmpty()) {
            for (int i = 0; i < toTrim.size(); i++) {
                String curTrim = toTrim.get(i);

                translatedRegName = translatedRegName.replaceFirst(curTrim, "");
            }
        }

        if (!MANUAL_TRANSLATIONS.containsKey(registryName)) add(registryName, handleInput(translatedRegName));
    }

    protected void localizeGeneralRegistryName(String registryName, String translatedName) {
        localizeGeneralRegistryName(registryName, translatedName, List.of());
    }

    protected void localizeGeneralRegistryName(String registryName, List<String> toTrim) {
        localizeGeneralRegistryName(registryName, DEFAULT_SEPARATORS, toTrim);
    }

    protected void localizeGeneralRegistryName(String registryName) {
        localizeGeneralRegistryName(registryName, List.of());
    }

    protected void translateBlocks() {
        if (!BlockPropertyWrapper.getMappedBpws().isEmpty()) {
            BlockPropertyWrapper.getMappedBpws().forEach((blockRegNameEntry, curBwp) -> { //TODO Optimize and update logging :trol:
                if (!curBwp.getManuallyLocalizedBlockName().isBlank()) addManualTranslation(blockRegNameEntry.get().getDescriptionId(), curBwp.getManuallyLocalizedBlockName());
                else if (!curBwp.getDefinedSeparatorWords().isEmpty() && curBwp.getBlockTranslationFunc() == null && !curBwp.hasLiteralTranslation()) localizeGeneralRegistryName(blockRegNameEntry.get().getDescriptionId(), Lists.asList(DEFAULT_SEPARATORS.get(0), DEFAULT_SEPARATORS.get(1), curBwp.getDefinedSeparatorWords().toArray(String[]::new)), ObjectArrayList.of());
            });
        }

        CABlocks.getBlocks().forEach(blockRegEntry -> {
            Block blockEntry = blockRegEntry.get();
            String blockRegName = blockEntry.getDescriptionId();
            BlockPropertyWrapper mappedBpw = BlockPropertyWrapper.getMappedBpws().entrySet().stream().anyMatch(curEntry -> curEntry.getKey().get().getDescriptionId().equals(blockRegName)) ? BlockPropertyWrapper.getMappedBpws().entrySet().stream().filter(curEntry -> curEntry.getKey().get().getDescriptionId().equals(blockRegName)).findFirst().get().getValue() : null;
            String translatedBlockName = MANUAL_TRANSLATIONS.containsKey(blockRegName) ? MANUAL_TRANSLATIONS.get(blockRegName) : getTranslatedRegistryName(blockRegName);

            CAConstants.LOGGER.debug("[Currently Translating Block]: {} -> {}", blockRegName, translatedBlockName);

            // TODO Indent this monstrosity (too lazy to do it rn)
            if (blockRegName.endsWith("_block") && !MANUAL_TRANSLATIONS.containsKey(blockRegName) && (mappedBpw == null || (mappedBpw.hasLiteralTranslation() && mappedBpw.getBlockTranslationFunc() == null))) localizeGeneralRegistryName(blockRegName, "Block of " + getTranslatedRegistryName(blockRegName).substring(0, getTranslatedRegistryName(blockRegName).lastIndexOf(" Block")));
            else if (mappedBpw != null && mappedBpw.getBlockTranslationFunc() != null) {
                CAConstants.LOGGER.debug("[Currently Modifying Localized Block]: {} -> {}", translatedBlockName, mappedBpw.getBlockTranslationFunc().apply(translatedBlockName));

                add(blockEntry, mappedBpw.getBlockTranslationFunc().apply(translatedBlockName));
            } else localizeGeneralRegistryName(blockRegName);
        });
    }

    protected void translateCreativeModeTabs() {
        CACreativeModeTabs.getCreativeModeTabs().forEach(tabRegEntry -> {
            CreativeModeTab tabEntry = tabRegEntry.get();
            String tabRegName = tabEntry.getDisplayName().toString();
            int endCharIndex = tabRegName.indexOf("',");
            tabRegName = tabRegName.substring(17, endCharIndex);

            CAConstants.LOGGER.debug("[Currently Translating Creative Mode Tab]: {} -> {}", tabRegName, getTranslatedRegistryName(tabRegName));

            MANUAL_INSERTIONS.putAll(getTranslatedRegistryName(tabRegName), ObjectArrayList.of("Chaos Awakens", ":")); // Oneshot (No fancy shenanigans for em anyway)

            localizeGeneralRegistryName(tabRegName);
        });
    }

    protected void translateDamageSources() { // Moreso grabbed from damage types but uhhh... same difference idk
        if (!DamageTypeWrapper.getMappedDtws().isEmpty()) {
            DamageTypeWrapper.getMappedDtws().forEach((damageTypeEntry, curDtw) -> {
                if (!curDtw.getCopiedMsgId().isBlank()) {
                    String curDmgTypeKey = "death.attack.".concat(curDtw.getCopiedMsgId());
                    String curDmgTypeDesc = curDtw.getLocalizedDeathMessageComponent() == null ? "" : curDtw.getLocalizedDeathMessageComponent().getString();

                    if (!curDmgTypeDesc.isBlank()) {
                        CAConstants.LOGGER.debug("[Currently Adding Damage Type Description]: {} -> {}", curDmgTypeKey, curDmgTypeDesc);
                        addManualTranslation(curDmgTypeKey, curDmgTypeDesc);
                    }
                }
            });
        }
    }

    protected void translateItems() {
        if (!ItemPropertyWrapper.getMappedIpws().isEmpty()) {
            ItemPropertyWrapper.getMappedIpws().forEach((itemRegNameEntry, curIwp) -> { //TODO Optimize and update logging :trol:
                if (!curIwp.getManuallyLocalizedItemName().isBlank()) addManualTranslation(itemRegNameEntry.get().getDescriptionId(), curIwp.getManuallyLocalizedItemName());
                else if (!curIwp.getDefinedSeparatorWords().isEmpty() && curIwp.getItemTranslationFunc() == null && !curIwp.hasLiteralTranslation()) localizeGeneralRegistryName(itemRegNameEntry.get().getDescriptionId(), Lists.asList(DEFAULT_SEPARATORS.get(0), DEFAULT_SEPARATORS.get(1), curIwp.getDefinedSeparatorWords().toArray(String[]::new)), ObjectArrayList.of());
            });
        }

        CAItems.getItems().forEach(itemRegEntry -> {
            Item itemEntry = itemRegEntry.get();
            String itemRegName = itemEntry.getDescriptionId();
            ItemPropertyWrapper mappedIpw = ItemPropertyWrapper.getMappedIpws().entrySet().stream().anyMatch(curEntry -> curEntry.getKey().get().getDescriptionId().equals(itemRegName)) ? ItemPropertyWrapper.getMappedIpws().entrySet().stream().filter(curEntry -> curEntry.getKey().get().getDescriptionId().equals(itemRegName)).findFirst().get().getValue() : null;
            String translatedItemName = MANUAL_TRANSLATIONS.containsKey(itemRegName) ? MANUAL_TRANSLATIONS.get(itemRegName) : getTranslatedRegistryName(itemRegName);

            CAConstants.LOGGER.debug("[Currently Translating Item]: {} -> {}", itemRegName, translatedItemName);

            if (mappedIpw != null && mappedIpw.getItemTranslationFunc() != null) {
                CAConstants.LOGGER.debug("[Currently Modifying Localized Item]: {} -> {}", translatedItemName, mappedIpw.getItemTranslationFunc().apply(translatedItemName));

                add(itemEntry, mappedIpw.getItemTranslationFunc().apply(translatedItemName));
            } else localizeGeneralRegistryName(itemRegName);
        });
    }

    protected void handleManualTranslations() {
        MANUAL_TRANSLATIONS.forEach((translationKey, translationValue) -> {
            if (!((translationKey == null || translationValue == null) || translationKey.isEmpty() || translationKey.isBlank() || translationValue.isEmpty() || translationValue.isBlank())) {
                add(translationKey, translationValue);
            }
        });
    }

    @Override
    protected void addTranslations() {
        translateBlocks();
        translateCreativeModeTabs();
        translateDamageSources();
        translateItems();

        handleManualTranslations();
    }
}
