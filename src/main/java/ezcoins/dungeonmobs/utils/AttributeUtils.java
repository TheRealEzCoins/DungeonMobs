package ezcoins.dungeonmobs.utils;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.LivingEntity;

public class AttributeUtils {

    public static void changeAttribute(LivingEntity entity, Attribute attribute, double newValue) {
        AttributeInstance attributeInstance = entity.getAttribute(attribute);
        if(attributeInstance != null) {
            attributeInstance.setBaseValue(newValue);
        }
    }
}
