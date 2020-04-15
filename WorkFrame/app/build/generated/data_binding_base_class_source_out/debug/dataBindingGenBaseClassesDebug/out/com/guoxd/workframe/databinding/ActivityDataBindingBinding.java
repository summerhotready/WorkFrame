package com.guoxd.workframe.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.guoxd.workframe.my_page.data_binding.PersonData;
import java.lang.String;
import java.util.List;
import java.util.Map;

public abstract class ActivityDataBindingBinding extends ViewDataBinding {
  @NonNull
  public final TextView arrayName;

  @NonNull
  public final Button button;

  @NonNull
  public final AppCompatTextView listName;

  @NonNull
  public final TextView mapName;

  @NonNull
  public final AppCompatTextView peopleAge;

  @NonNull
  public final AppCompatTextView peopleFave;

  @NonNull
  public final AppCompatTextView peopleName;

  @Bindable
  protected PersonData mPerson;

  @Bindable
  protected View mView;

  @Bindable
  protected List<String> mLists;

  @Bindable
  protected Map<String, String> mMaps;

  @Bindable
  protected String[] mArrays;

  protected ActivityDataBindingBinding(DataBindingComponent _bindingComponent, View _root,
      int _localFieldCount, TextView arrayName, Button button, AppCompatTextView listName,
      TextView mapName, AppCompatTextView peopleAge, AppCompatTextView peopleFave,
      AppCompatTextView peopleName) {
    super(_bindingComponent, _root, _localFieldCount);
    this.arrayName = arrayName;
    this.button = button;
    this.listName = listName;
    this.mapName = mapName;
    this.peopleAge = peopleAge;
    this.peopleFave = peopleFave;
    this.peopleName = peopleName;
  }

  public abstract void setPerson(@Nullable PersonData person);

  @Nullable
  public PersonData getPerson() {
    return mPerson;
  }

  public abstract void setView(@Nullable View view);

  @Nullable
  public View getView() {
    return mView;
  }

  public abstract void setLists(@Nullable List<String> lists);

  @Nullable
  public List<String> getLists() {
    return mLists;
  }

  public abstract void setMaps(@Nullable Map<String, String> maps);

  @Nullable
  public Map<String, String> getMaps() {
    return mMaps;
  }

  public abstract void setArrays(@Nullable String[] arrays);

  @Nullable
  public String[] getArrays() {
    return mArrays;
  }

  @NonNull
  public static ActivityDataBindingBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static ActivityDataBindingBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable DataBindingComponent component) {
    return DataBindingUtil.<ActivityDataBindingBinding>inflate(inflater, com.guoxd.workframe.R.layout.activity_data_binding, root, attachToRoot, component);
  }

  @NonNull
  public static ActivityDataBindingBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static ActivityDataBindingBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable DataBindingComponent component) {
    return DataBindingUtil.<ActivityDataBindingBinding>inflate(inflater, com.guoxd.workframe.R.layout.activity_data_binding, null, false, component);
  }

  public static ActivityDataBindingBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  public static ActivityDataBindingBinding bind(@NonNull View view,
      @Nullable DataBindingComponent component) {
    return (ActivityDataBindingBinding)bind(component, view, com.guoxd.workframe.R.layout.activity_data_binding);
  }
}
