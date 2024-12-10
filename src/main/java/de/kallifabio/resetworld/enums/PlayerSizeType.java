/**
 * Erstellt von Gamer_Kidd_LP | kallifabio
 * am 10.12.2024 um 02:29
 * Projektname: ResetWorld
 * Packagename: de.kallifabio.resetworld.enums
 */

package de.kallifabio.resetworld.enums;

public enum PlayerSizeType {

    SMALL(0.2f),
    NORMAL(1.0f),
    LARGE(2.0f);

    private final float scale;

    PlayerSizeType(float scale) {
        this.scale = scale;
    }

    public float getScale() {
        return scale;
    }
}
