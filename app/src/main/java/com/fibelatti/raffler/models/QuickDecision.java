package com.fibelatti.raffler.models;

import org.parceler.Parcel;

import java.util.Arrays;
import java.util.List;

@Parcel
public class QuickDecision {
    String key;
    String name;
    List<String> values;
    Boolean enabled;

    private QuickDecision() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getValues() {
        return values;
    }

    public Integer getValuesCount() {
        return values != null ? values.size() : 0;
    }

    public String getValuesString() {
        return android.text.TextUtils.join(",", values);
    }

    public String getValueAt(Integer index) {
        return values != null ? values.get(index) : "";
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public void setValues(String values) {
        this.values = Arrays.asList(values.split("\\s*,\\s*"));
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public static class Builder {
        final QuickDecision quickDecision;

        public Builder() {
            quickDecision = new QuickDecision();
        }

        public Builder setKey(String key) {
            quickDecision.setKey(key);
            return this;
        }

        public Builder setName(String name) {
            quickDecision.setName(name);
            return this;
        }

        public Builder setValues(List<String> values) {
            quickDecision.setValues(values);
            return this;
        }

        public Builder setEnabled(Boolean enabled) {
            quickDecision.setEnabled(enabled);
            return this;
        }

        public QuickDecision build() {
            return quickDecision;
        }
    }
}
