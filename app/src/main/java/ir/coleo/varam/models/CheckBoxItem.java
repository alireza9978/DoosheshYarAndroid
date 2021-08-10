package ir.coleo.varam.models;

import android.content.Context;

import java.util.ArrayList;

/**
 * کلاس نگه دارنده اطلاعات یک اپشن ورودی در کل برنامه
 */
public class CheckBoxItem {

    private Integer name;
    private String nameString;
    private boolean check;
    private boolean active;
    private ArrayList<CheckBoxItem> disable;
    private CheckBoxItemListener listener = null;
    private long value = -1;

    public CheckBoxItem(Integer name, boolean check) {
        this.name = name;
        this.nameString = null;
        this.check = check;
        this.active = true;
        disable = new ArrayList<>();
    }

    public CheckBoxItem(Integer name) {
        this.name = name;
        this.nameString = null;
        this.check = false;
        this.active = true;
        disable = new ArrayList<>();
    }

    public CheckBoxItem(String nameString) {
        this.nameString = nameString;
        this.name = null;
        this.check = false;
        this.active = true;
        disable = new ArrayList<>();
    }

    public void add(CheckBoxItem item) {
        disable.add(item);
    }

    public void disableOther() {
        for (CheckBoxItem item : disable) {
            item.setActive(false);
            item.setCheck(false);
        }
    }

    public void enableOther() {
        for (CheckBoxItem item : disable) {
            item.setActive(true);
            item.setCheck(false);
        }
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getName(Context context) {
        if (name != null) {
            return context.getString(name);
        } else {
            return nameString;
        }
    }

    public Integer getName() {
        return name;
    }

    public void setName(Integer name) {
        this.name = name;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
        if (check && listener != null) {
            listener.run(value);
        }
    }

    public ArrayList<CheckBoxItem> getDisable() {
        return disable;
    }

    public void setDisable(ArrayList<CheckBoxItem> disable) {
        this.disable = disable;
    }

    public void setListener(CheckBoxItemListener listener) {
        this.listener = listener;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
