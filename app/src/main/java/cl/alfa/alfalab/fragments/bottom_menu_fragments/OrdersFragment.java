package cl.alfa.alfalab.fragments.bottom_menu_fragments;

import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import cl.alfa.alfalab.MainActivity;
import cl.alfa.alfalab.MainApplication;
import cl.alfa.alfalab.R;
import cl.alfa.alfalab.adapters.TabsAdapter;
import cl.alfa.alfalab.fragments.Delivered;
import cl.alfa.alfalab.fragments.Orders;
import cl.alfa.alfalab.interfaces.FabInterface;
import cl.alfa.alfalab.utils.SharedPreferences;

public class OrdersFragment extends Fragment {

    private ViewPager2 mViewPager;
    private final Context context = MainApplication.getContext();

    public static AppBarLayout appBarLayout;
    public View appBarLayoutShadow;

    private static FabInterface fabInterface;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.parent_fragment_layout, container, false);

        MainActivity.getToolbar().setElevation(0);

        final FragmentManager fm = getChildFragmentManager();
        final Lifecycle lifecycle = getViewLifecycleOwner().getLifecycle();
        final TabsAdapter adapter = new TabsAdapter(fm, lifecycle);
        final FloatingActionButton fab = view.findViewById(R.id.fab);
        final SharedPreferences mSharedPreferences = new SharedPreferences(context);
        appBarLayout = view.findViewById(R.id.app_bar_layout);
        appBarLayoutShadow = view.findViewById(R.id.divider);

        mViewPager = view.findViewById(R.id.vp_tabs);
        adapter.addFragment(new Orders(), getResources().getString(R.string.orders));
        adapter.addFragment(new Delivered(), getResources().getString(R.string.delivered));
        adapter.setContext(requireContext());
        mViewPager.setAdapter(adapter);

        fab.setOnClickListener(v -> {
            if (mViewPager.getCurrentItem() == 0) {
                fabInterface.onFabClicked();
            }
        });

        final StateListAnimator stateListAnimator = new StateListAnimator();
        stateListAnimator.addState(new int[0], ObjectAnimator.ofFloat(appBarLayout, "elevation", 2));
        appBarLayout.setStateListAnimator(stateListAnimator);

        final TabLayout mSlidingTabLayout = view.findViewById(R.id.stl_tabs);
        mSlidingTabLayout.setElevation(0);
        mSlidingTabLayout.setUnboundedRipple(false);
        final TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(mSlidingTabLayout, mViewPager, true, (tab, position) -> {
            tab.setText(adapter.getPageTitle(position));
            tab.setCustomView(adapter.getTabView(position));
        });

        if (mSharedPreferences.loadNightModeState()) {
            mSlidingTabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark)); //NIGHT MODE
        } else {
            mSlidingTabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight)); //DAY MODE
        }
        tabLayoutMediator.attach();

        mSlidingTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 1) {
                    fab.hide();
                } else {
                    fab.show();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        final Fragment childFragment = new Orders();
        final FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.child_fragment_container, childFragment).commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fabInterface = null;
    }

    public static void setListener(FabInterface fab) {
        fabInterface = fab;
    }

}
