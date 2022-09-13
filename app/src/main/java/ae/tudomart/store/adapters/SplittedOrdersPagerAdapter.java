package ae.tudomart.store.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import ae.tudomart.store.ui.fragments.NewOrdersFragment;
import ae.tudomart.store.ui.fragments.PackingFragment;
import ae.tudomart.store.ui.fragments.ReadyToDispatchFragment;

public class SplittedOrdersPagerAdapter extends FragmentStateAdapter {
    public SplittedOrdersPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new NewOrdersFragment();
            case 1:
                return new PackingFragment();
            default:
                return new ReadyToDispatchFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
