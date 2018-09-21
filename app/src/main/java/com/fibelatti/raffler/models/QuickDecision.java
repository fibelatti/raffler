package com.fibelatti.raffler.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;
import java.util.List;

public class QuickDecision
        implements Parcelable {
    private String key;
    private String name;
    private List<String> values;

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

    public static final Parcelable.Creator<QuickDecision> CREATOR = new Parcelable.Creator<QuickDecision>() {
        public QuickDecision createFromParcel(Parcel in) {
            return new QuickDecision.Builder()
                    .setKey((String) in.readValue(String.class.getClassLoader()))
                    .setName((String) in.readValue(String.class.getClassLoader()))
                    .setValues((List<String>) in.readValue(String.class.getClassLoader()))
                    .build();
        }

        public QuickDecision[] newArray(int size) {
            return new QuickDecision[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(getKey());
        dest.writeValue(getName());
        dest.writeValue(getValues());
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

        public QuickDecision build() {
            return quickDecision;
        }
    }
}
