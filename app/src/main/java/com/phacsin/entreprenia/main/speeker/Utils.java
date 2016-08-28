package com.phacsin.entreprenia.main.speeker;

import com.phacsin.entreprenia.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bineesh P Babu on 20-08-2016.
 */
public class Utils {
    public static final List<Friend> friends = new ArrayList<>();

    static {
        friends.add(new Friend(R.drawable.dp1, "ANASTASIA", R.color.blue, "Sport", "Literature", "Music", "Art", "Technology"));
        friends.add(new Friend(R.drawable.dp2, "IRENE", R.color.red, "Travelling", "Flights", "Books", "Painting", "Design"));
        friends.add(new Friend(R.drawable.dp1, "KATE", R.color.green, "Sales", "Pets", "Skiing", "Hairstyles", "Сoffee"));
        friends.add(new Friend(R.drawable.dp2, "PAUL", R.color.violet, "Android", "Development", "Design", "Wearables", "Pets"));
        friends.add(new Friend(R.drawable.dp1, "DARIA", R.color.blue, "Design", "Fitness", "Healthcare", "UI/UX", "Chatting"));
        friends.add(new Friend(R.drawable.dp2, "KIRILL", R.color.red, "Development", "Android", "Healthcare", "Sport", "Rock Music"));
        friends.add(new Friend(R.drawable.dp1, "JULIA", R.color.green, "Cinema", "Music", "Tatoo", "Animals", "Management"));
        friends.add(new Friend(R.drawable.dp2, "YALANTIS", R.color.violet, "Android", "IOS", "Application", "Development", "Company"));
    }
}
