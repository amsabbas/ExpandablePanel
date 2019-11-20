# ExpandablePanel

![test image size](/device-2019-11-20-123210.png) ![test image size](/device-2019-11-20-123233.png)


# Download
Add it in your root build.gradle at the end of repositories:
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
}
```

Add the dependency
```
dependencies {
	        implementation 'com.github.amsabbas:ExpandablePanel:Tag'
}
```


## Usage
```
<com.ams.panel.ExpandablePanel xmlns:panel="http://schemas.android.com/apk/res-auto"
        android:id="@+id/infoPanel"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        panel:expanded_view="@+id/container"
        panel:less="@drawable/ic_expand_less"
        panel:more="@drawable/ic_expand_more"
        panel:main_view="@+id/btnExpand">

        <androidx.constraintlayout.widget.ConstraintLayout
            android:id="@+id/infoMainView"
            android:layout_width="match_parent"
            android:layout_height="wrap_content">

          //main view

        </androidx.constraintlayout.widget.ConstraintLayout>

        <androidx.constraintlayout.widget.ConstraintLayout
                android:id="@+id/container"
                android:layout_width="match_parent"
                android:paddingStart="16dp"
                android:paddingEnd="16dp"
                android:paddingTop="8dp"
                android:paddingBottom="8dp"
                panel:layout_constraintTop_toBottomOf="@id/infoMainView"
                android:layout_height="wrap_content">

          //expanded view

       </androidx.constraintlayout.widget.ConstraintLayout>
</com.ams.panel.ExpandablePanel>
```

## Listener
```
infoPanel.setOnExpandListener(object :OnExpandListener{
            override fun onExpandToggle(isExpand: Boolean, expandedView: View?, mainView: View?) {
            }
})
```
