package com.guoxd.workframe.databinding;
import com.guoxd.workframe.R;
import com.guoxd.workframe.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivityDataBindingBindingImpl extends ActivityDataBindingBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.button, 7);
    }
    // views
    @NonNull
    private final androidx.constraintlayout.widget.ConstraintLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivityDataBindingBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 8, sIncludes, sViewsWithIds));
    }
    private ActivityDataBindingBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.TextView) bindings[5]
            , (android.widget.Button) bindings[7]
            , (androidx.appcompat.widget.AppCompatTextView) bindings[4]
            , (android.widget.TextView) bindings[6]
            , (androidx.appcompat.widget.AppCompatTextView) bindings[2]
            , (androidx.appcompat.widget.AppCompatTextView) bindings[3]
            , (androidx.appcompat.widget.AppCompatTextView) bindings[1]
            );
        this.arrayName.setTag(null);
        this.listName.setTag(null);
        this.mapName.setTag(null);
        this.mboundView0 = (androidx.constraintlayout.widget.ConstraintLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.peopleAge.setTag(null);
        this.peopleFave.setTag(null);
        this.peopleName.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x20L;
        }
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
        if (BR.view == variableId) {
            setView((android.view.View) variable);
        }
        else if (BR.maps == variableId) {
            setMaps((java.util.Map<java.lang.String,java.lang.String>) variable);
        }
        else if (BR.arrays == variableId) {
            setArrays((java.lang.String[]) variable);
        }
        else if (BR.lists == variableId) {
            setLists((java.util.List<java.lang.String>) variable);
        }
        else if (BR.person == variableId) {
            setPerson((com.guoxd.workframe.my_page.data_binding.PersonData) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setView(@Nullable android.view.View View) {
        this.mView = View;
    }
    public void setMaps(@Nullable java.util.Map<java.lang.String,java.lang.String> Maps) {
        this.mMaps = Maps;
        synchronized(this) {
            mDirtyFlags |= 0x2L;
        }
        notifyPropertyChanged(BR.maps);
        super.requestRebind();
    }
    public void setArrays(@Nullable java.lang.String[] Arrays) {
        this.mArrays = Arrays;
        synchronized(this) {
            mDirtyFlags |= 0x4L;
        }
        notifyPropertyChanged(BR.arrays);
        super.requestRebind();
    }
    public void setLists(@Nullable java.util.List<java.lang.String> Lists) {
        this.mLists = Lists;
        synchronized(this) {
            mDirtyFlags |= 0x8L;
        }
        notifyPropertyChanged(BR.lists);
        super.requestRebind();
    }
    public void setPerson(@Nullable com.guoxd.workframe.my_page.data_binding.PersonData Person) {
        this.mPerson = Person;
        synchronized(this) {
            mDirtyFlags |= 0x10L;
        }
        notifyPropertyChanged(BR.person);
        super.requestRebind();
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
        }
        return false;
    }

    @Override
    protected void executeBindings() {
        long dirtyFlags = 0;
        synchronized(this) {
            dirtyFlags = mDirtyFlags;
            mDirtyFlags = 0;
        }
        java.lang.String personFave = null;
        java.util.Map<java.lang.String,java.lang.String> maps = mMaps;
        java.lang.String lists0 = null;
        java.lang.String[] arrays = mArrays;
        java.lang.String mapsEmail = null;
        java.util.List<java.lang.String> lists = mLists;
        java.lang.String personAge = null;
        java.lang.String arrays0 = null;
        com.guoxd.workframe.my_page.data_binding.PersonData person = mPerson;
        java.lang.String personName = null;

        if ((dirtyFlags & 0x22L) != 0) {



                if (maps != null) {
                    // read maps["email"]
                    mapsEmail = maps.get("email");
                }
        }
        if ((dirtyFlags & 0x24L) != 0) {



                if (arrays != null) {
                    // read arrays[0]
                    arrays0 = getFromArray(arrays, 0);
                }
        }
        if ((dirtyFlags & 0x28L) != 0) {



                if (lists != null) {
                    // read lists[0]
                    lists0 = getFromList(lists, 0);
                }
        }
        if ((dirtyFlags & 0x30L) != 0) {



                if (person != null) {
                    // read person.fave
                    personFave = person.getFave();
                    // read person.age
                    personAge = person.getAge();
                    // read person.name
                    personName = person.getName();
                }
        }
        // batch finished
        if ((dirtyFlags & 0x24L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.arrayName, arrays0);
        }
        if ((dirtyFlags & 0x28L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.listName, lists0);
        }
        if ((dirtyFlags & 0x22L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.mapName, mapsEmail);
        }
        if ((dirtyFlags & 0x30L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.peopleAge, personAge);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.peopleFave, personFave);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.peopleName, personName);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): view
        flag 1 (0x2L): maps
        flag 2 (0x3L): arrays
        flag 3 (0x4L): lists
        flag 4 (0x5L): person
        flag 5 (0x6L): null
    flag mapping end*/
    //end
}