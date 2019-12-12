package hr.dbab.chats;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabsAdapter extends FragmentPagerAdapter {

    // Constructor
    public TabsAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    // Method which returns the fragment associated with a specified position
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ChatsFragment chatsFragment = new ChatsFragment();
                return chatsFragment;
            case 1:
                ContactsFragment contactsFragment = new ContactsFragment();
                return contactsFragment;

            default:
                return null;

        }
    }

    // Method that returns the number of views available
    @Override
    public int getCount() {
        return 2;
    }

    // Method for obtaining a title string to describe the specified page
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Chats";

            case 1:
                return "Contacts";

            default:
                return null;
        }
    }
}
