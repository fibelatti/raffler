package com.fibelatti.raffler.models;

import com.fibelatti.raffler.db.Database;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Parcel
public class QuickDecision {
    Long id;
    String name;
    List<String> values;
    Boolean enabled;

    public QuickDecision() {
    }

    public QuickDecision(Long id, String name, ArrayList<String> values, Boolean enabled) {
        this.id = id;
        this.name = name;
        this.values = values;
        this.enabled = enabled;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setValues(ArrayList<String> values) {
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

    public void refresh() {
        QuickDecision qd = Database.quickDecisionDao.fetchQuickDecisionById(this.id);

        this.name = qd.getName();
        this.values = qd.getValues();
        this.enabled = qd.getEnabled();
    }
}
