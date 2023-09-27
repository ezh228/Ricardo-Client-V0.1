package ru.terrarXD.shit.settings;

/**
 * @author zTerrarxd_
 * @since 22:29 of 20.04.2023
 */
public class FloatSetting extends Setting{
    float min;
    float max;
    float val;
    float percent;
    public FloatSetting(String name, float min, float max, float val, float percent) {
        super(name);
        this.min = min;
        this.max = max;
        this.val = val;
        this.percent = percent;
    }

    public float getMin() {
        return min;
    }

    public float getMax() {
        return max;
    }

    public float getVal() {
        return val;
    }

    public float getPercent() {
        return percent;
    }

    public void setVal(float val) {
        this.val = val;
    }
}
